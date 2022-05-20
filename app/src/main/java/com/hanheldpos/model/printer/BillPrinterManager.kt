package com.hanheldpos.model.printer

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.StrictMode
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.handheld.printer.PrintConstants
import com.handheld.printer.interfaces.BluetoothPrinterManager
import com.handheld.printer.interfaces.LanPrinterManager
import com.handheld.printer.interfaces.UrovoPrinterManager
import com.handheld.printer.printer_manager.BasePrinterManager
import com.hanheldpos.model.order.Order
import com.hanheldpos.model.order.OrderReq
import com.hanheldpos.model.printer.layouts.BaseLayoutPrinter
import com.hanheldpos.model.printer.layouts.BaseLayoutPrinterFactory
import java.lang.Exception
import java.net.ConnectException
import java.util.concurrent.TimeoutException

class BillPrinterManager private constructor() {

    // region classes

    /**
     * Whether should print by image or text
     * */
    enum class PrintMethod {
        TEXT,
        IMAGE
    }

    enum class PrintConnectionType {
        LAN,
        BLUETOOTH,
    }

    class PrinterDeviceInfo(val deviceType: DeviceType) {
        enum class DeviceType {
            HANDHELD,
            POS,
            UROVO // SDK
        }

        fun printerDPI(): Int = 203

        fun paperWidth(): Float = when (deviceType) {
            DeviceType.POS -> 72f
            DeviceType.HANDHELD -> 48f
            DeviceType.UROVO -> 383f
        }

        fun charsPerLineNormal(): Int = when (deviceType) {
            DeviceType.POS -> 48
            DeviceType.HANDHELD -> 32
            DeviceType.UROVO -> 32
        }

        fun charsPerLineLarge(): Int = when (deviceType) {
            DeviceType.POS -> 24
            DeviceType.HANDHELD -> 16
            DeviceType.UROVO -> 22
        }

        // Number of characters per left column
        fun leftColumnWidth(): Int = when (deviceType) {
            DeviceType.HANDHELD -> 4
            DeviceType.POS -> 6
            DeviceType.UROVO -> 4
        }

        // Number of characters per center column
        fun centerColumnWidth(): Int =
            charsPerLineNormal() - leftColumnWidth() - rightColumnWidth()

        // Number of characters per right column
        fun rightColumnWidth(): Int = when (deviceType) {
            DeviceType.HANDHELD -> 9
            DeviceType.POS -> 9
            DeviceType.UROVO -> 9
        }

        fun linesFeed(numLines: Int): Int = when (deviceType) {
            DeviceType.HANDHELD, DeviceType.POS -> 1 * numLines
            DeviceType.UROVO -> 60 * numLines
        }
    }

    class PrintOptions(
        val printMethod: PrintMethod = PrintMethod.TEXT,
        val connectionType: PrintConnectionType,
        val deviceType: PrinterDeviceInfo.DeviceType,
    ) {
        val deviceInfo = PrinterDeviceInfo(deviceType)
        var lanConfig: LanConfig? = null

        class LanConfig(
            var port: Int? = null,
            var ipAddress: String? = null,
            var timeOut: Int? = 30000
        ) {
            override fun toString(): String {
                return "Port: $port, ipAddress: $ipAddress"
            }
        }

        fun setUpLan(lanConfig: LanConfig): PrintOptions {
            this.lanConfig = lanConfig
            return this
        }

        override fun toString(): String {
            return "Print method: ${printMethod.name}, Connection Type: ${connectionType.name}, Device Type: ${deviceInfo.deviceType.name}, Lan Configuration: ${lanConfig ?: "Not Config"}"
        }
    }

    // endregion

    // region Singleton

    companion object {
        @Volatile
        private lateinit var instance: BillPrinterManager


        // region variables

        private var printer: BasePrinterManager? = null
        private lateinit var printOptions: PrintOptions
        private lateinit var applicationContext: Context
        // endregion

        fun init(
            context: Context,
            printingOptions: PrintOptions,
            onConnectionFailed: (exception: PrinterException) -> Unit
        ): BillPrinterManager {
            try {
                if (!this::instance.isInitialized) instance = BillPrinterManager().apply {
                    applicationContext = context
                    printOptions = printingOptions
                }
                if (instance.isConnected()) return instance

                instance.apply {
                    if (printOptions.deviceInfo.deviceType == PrinterDeviceInfo.DeviceType.UROVO) {
                        printer = UrovoPrinterManager()
                        printer?.connect()
                    } else {
                        printer = when (printOptions.connectionType) {
                            PrintConnectionType.LAN -> {
                                val policy = StrictMode
                                    .ThreadPolicy
                                    .Builder()
                                    .permitAll()
                                    .build()

                                StrictMode.setThreadPolicy(policy)

                                LanPrinterManager(
                                    ipAddress = printOptions.lanConfig?.ipAddress ?: "",
                                    port = printOptions.lanConfig?.port ?: 0,
                                    timeout = printOptions.lanConfig?.timeOut,
                                    printerDPI = printOptions.deviceInfo.printerDPI(),
                                    printerPaperWidth = printOptions.deviceInfo.paperWidth(),
                                    charsPerLine = printOptions.deviceInfo.charsPerLineNormal()
                                )
                            }
                            PrintConnectionType.BLUETOOTH -> {
                                BluetoothPrinterManager(
                                    context = context,
                                    printerDPI = printOptions.deviceInfo.printerDPI(),
                                    printerPaperWidth = printOptions.deviceInfo.paperWidth(),
                                    charsPerLine = printOptions.deviceInfo.charsPerLineNormal(),
                                )
                            }
                        }
                    }

                    if (!instance.isConnected()) {
                        throw PrinterException("")
                    }

                    Log.d(PrintConstants.TAG, "Printer initialized.")
                }

            } catch (e: Exception) {
                onConnectionFailed(PrinterException("Unable to connect to printer with config: $printingOptions"))
            }
            return instance
        }

        fun get(onConnectionFailed: (exception: PrinterException) -> Unit): BillPrinterManager {
            return if (this::instance.isInitialized && instance.isConnected())
                instance
            else {
                init(applicationContext, printOptions, onConnectionFailed)
            }
        }
    }

    // endregion


    fun print(
        context: Context,
        order: OrderReq,
        layoutType: BaseLayoutPrinter.LayoutType
    ): BillPrinterManager {
        printer?.performPrinterAction {
            if (printOptions.printMethod == PrintMethod.TEXT) {
                printBill(context, order, layoutType)
            } else if (printOptions.printMethod == PrintMethod.IMAGE) {
                printImageBill(context, order)
            }
        }
        return this
    }

    fun openCashDrawer(): BillPrinterManager {
        printer?.performPrinterAction {
            printer?.openCashDrawer()
        }

        return this
    }

    fun isConnected(): Boolean {
        var isConnected = false
        printer?.performPrinterAction {
            isConnected = printer?.isConnected() ?: false
        }

        return isConnected
    }


    // region print bill text

    private fun printBill(
        context: Context,
        order: OrderReq,
        layoutType: BaseLayoutPrinter.LayoutType
    ) {
        printer ?: return
        BaseLayoutPrinterFactory.getLayout(
            context,
            order,
            printer!!,
            printOptions,
            layoutType,
        ).print()
    }

    // endregion

    // region print image

    private fun printImageBill(
        context: Context,
        order: OrderReq
    ) {

    }

    // endregion

}