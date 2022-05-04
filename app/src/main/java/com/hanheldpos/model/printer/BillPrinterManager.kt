package com.hanheldpos.model.printer

import android.content.Context
import android.os.StrictMode
import android.util.Log
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

    class PrinterDeviceInfo(private val deviceType: DeviceType) {
        enum class DeviceType {
            HANDHELD,
            POS,
            UROVO
        }

        fun printerDPI(): Int = when (deviceType) {
            DeviceType.POS -> 203
            DeviceType.HANDHELD -> 203
            DeviceType.UROVO -> 203
        }
        fun paperWidth(): Float {
            return when (deviceType) {
                DeviceType.POS -> 72f
                DeviceType.HANDHELD -> 48f
                DeviceType.UROVO -> 383f
            }
        }

        fun charsPerLineText(): Int {
            return when (deviceType) {
                DeviceType.POS -> 48
                DeviceType.HANDHELD -> 32
                DeviceType.UROVO -> 32
            }
        }

        fun charsPerLineHeader(): Int {
            return when (deviceType) {
                DeviceType.POS -> 48
                DeviceType.HANDHELD -> 16
                DeviceType.UROVO -> 22
            }
        }

        fun lineFeed() : Int {
            return when (deviceType) {
                DeviceType.POS -> 1
                DeviceType.HANDHELD -> 1
                DeviceType.UROVO -> 60
            }
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
    }

    class PrintOptions(
        val printMethod: PrintMethod = PrintMethod.TEXT,
        val connectionType: PrintConnectionType,
        deviceType: PrinterDeviceInfo.DeviceType,
    ) {
        val deviceInfo = PrinterDeviceInfo(deviceType)
        var lanConfig: LanConfig? = null
        var useSDK: Boolean = false

        class LanConfig(
            var port: Int? = null,
            var ipAddress: String? = null,
            var timeOut: Int? = 30000
        )

        fun setUpLan(lanConfig: LanConfig): PrintOptions {
            this.lanConfig = lanConfig
            return this
        }

        fun setUrovo(useSDK : Boolean) : PrintOptions {
            this.useSDK = useSDK
            return this
        }
    }

    // endregion

    // region Singleton

    companion object {
        @Volatile
        private lateinit var instance: BillPrinterManager

        fun init(context: Context, printOptions: PrintOptions): BillPrinterManager {
            this.instance = BillPrinterManager().apply {
                if (printOptions.useSDK)
                    printer = UrovoPrinterManager()

                else
                    when (printOptions.connectionType) {
                        PrintConnectionType.LAN -> {

                            val policy = StrictMode
                                .ThreadPolicy
                                .Builder()
                                .permitAll()
                                .build()

                            StrictMode.setThreadPolicy(policy)

                            printer = LanPrinterManager(
                                ipAddress = printOptions.lanConfig?.ipAddress ?: "",
                                port = printOptions.lanConfig?.port ?: 0,
                                timeout = printOptions.lanConfig?.timeOut,
                                printerDPI = printOptions.deviceInfo.printerDPI(),
                                printerPaperWidth = printOptions.deviceInfo.paperWidth(),
                                charsPerLine = printOptions.deviceInfo.charsPerLineText()
                            )
                        }
                        PrintConnectionType.BLUETOOTH -> {
                            printer = BluetoothPrinterManager(
                                context,
                                printerDPI = printOptions.deviceInfo.printerDPI(),
                                printerPaperWidth = printOptions.deviceInfo.paperWidth(),
                                charsPerLine = printOptions.deviceInfo.charsPerLineText()
                            )
                        }
                    }
                this.printOptions = printOptions
                if (!this::printer.isInitialized) {
                    throw ConnectException("Unable to initialize printer")
                }
                printer.connect()

            }
            Log.d("Check Initial", this::instance.isInitialized.toString())
            return this.instance
        }

        fun get() = this.instance
    }

    // endregion

    // region variables

    private lateinit var printer: BasePrinterManager
    private lateinit var printOptions: PrintOptions

    // endregion

    // region print

    fun print(context: Context, order: OrderReq) {
        if (!this::printer.isInitialized) {
            throw TimeoutException("Printer have not initialized")
        }

        if (printOptions.printMethod == PrintMethod.TEXT) {
            printBill(context, order)
        } else if (printOptions.printMethod == PrintMethod.IMAGE) {
            printImageBill(context, order)
        }
    }

    // endregion

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
        /*val drawable = ContextCompat.getDrawable(context as Activity, R.drawable.bitmap)
        val wrappedDrawable: Drawable = DrawableCompat.wrap(drawable!!)
        DrawableCompat.setTint(wrappedDrawable, Color.BLACK)
        printer.drawBitmap(
            DrawableHelper.drawableToBitmap(wrappedDrawable),
            BasePrinterManager.BitmapAlign.Center
        )*/
    }

    // endregion

    fun isConnected() : Boolean {
        return true
    }

    fun disconnect() {
        printer.disconnect()
    }
}