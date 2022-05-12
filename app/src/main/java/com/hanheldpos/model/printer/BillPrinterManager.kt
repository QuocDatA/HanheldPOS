package com.hanheldpos.model.printer

import android.app.Activity
import android.content.Context
import android.os.StrictMode
import android.util.Log
import androidx.core.content.ContextCompat
import com.handheld.printer.PrintConstants
import com.handheld.printer.interfaces.BluetoothPrinterManager
import com.handheld.printer.interfaces.LanPrinterManager
import com.handheld.printer.interfaces.UrovoPrinterManager
import com.handheld.printer.printer_manager.BasePrinterManager
import com.hanheldpos.model.order.OrderReq
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

        fun charsPerLineText(): Int = when (deviceType) {
            DeviceType.POS -> 48
            DeviceType.HANDHELD -> 32
            DeviceType.UROVO -> 32
        }

        fun charsPerLineHeader(): Int = when (deviceType) {
            DeviceType.POS -> 24
            DeviceType.HANDHELD -> 32
            DeviceType.UROVO -> 32
        }

        // Number of characters per left column
        fun leftColumnWidth(): Int = when (deviceType) {
            DeviceType.HANDHELD -> 4
            DeviceType.POS -> 6
            DeviceType.UROVO -> 4
        }

        // Number of characters per center column
        fun centerColumnWidth(): Int = charsPerLineText() - leftColumnWidth() - rightColumnWidth()

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
        )

        fun setUpLan(lanConfig: LanConfig): PrintOptions {
            this.lanConfig = lanConfig
            return this
        }
    }

    // endregion

    // region Singleton

    companion object {
        @Volatile
        private lateinit var instance: BillPrinterManager


        // region variables

        private lateinit var printer: BasePrinterManager
        private lateinit var printOptions: PrintOptions
        private lateinit var applicationContext: Context
        // endregion

        fun init(context: Context, printingOptions: PrintOptions): BillPrinterManager {
            if (this::instance.isInitialized) return instance

            instance = BillPrinterManager()

            instance.apply {

                applicationContext = context
                printOptions = printingOptions


                if (printOptions.deviceInfo.deviceType == PrinterDeviceInfo.DeviceType.UROVO) {
                    printer = UrovoPrinterManager()
                    printer.connect()
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
                                charsPerLine = printOptions.deviceInfo.charsPerLineText()
                            )
                        }
                        PrintConnectionType.BLUETOOTH -> {
                            BluetoothPrinterManager(
                                context = context,
                                printerDPI = printOptions.deviceInfo.printerDPI(),
                                printerPaperWidth = printOptions.deviceInfo.paperWidth(),
                                charsPerLine = printOptions.deviceInfo.charsPerLineText(),
                            )
                        }
                    }
                }
            }

            if (!instance.isConnected()) {
                throw PrinterException("Unable to connect to printer")
            }

            Log.d(PrintConstants.TAG, "Printer initialized.")
            return instance
        }

        fun get(): BillPrinterManager {
            return if (this::instance.isInitialized && instance.isConnected())
                instance
            else {
                init(applicationContext, printOptions)
            }
        }
    }

    // endregion


    fun print(context: Context, order: OrderReq): BillPrinterManager {
        printer.performPrinterAction {
            if (printOptions.printMethod == PrintMethod.TEXT) {
                printBill(context, order)
            } else if (printOptions.printMethod == PrintMethod.IMAGE) {
                printImageBill(context, order)
            }

        }
        return this
    }

    fun openCashDrawer(): BillPrinterManager {
        printer.performPrinterAction {
            printer.openCashDrawer()
        }

        return this
    }

    fun disconnect() {
        printer.disconnect()
    }

    fun isConnected(): Boolean {
        return printer.isConnected()
    }


    // region print bill text

    private fun printBill(
        context: Context,
        order: OrderReq
    ) {
        LayoutBillPrinter(context, order, printer, printOptions).print()
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