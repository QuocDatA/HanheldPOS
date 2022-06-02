package com.hanheldpos.printer

import android.content.Context
import android.os.StrictMode
import android.util.Log
import com.handheld.printer.PrintConstants
import com.handheld.printer.interfaces.BluetoothPrinterManager
import com.handheld.printer.interfaces.LanPrinterManager
import com.handheld.printer.interfaces.UrovoPrinterManager
import com.handheld.printer.printer_setup.PrintConnectionType
import com.handheld.printer.printer_setup.PrintOptions
import com.handheld.printer.printer_setup.device_info.DeviceType
import com.handheld.printer.printer_setup.printer_manager.BasePrinterManager
import com.hanheldpos.model.order.OrderModel
import com.hanheldpos.printer.layouts.LayoutType
import com.hanheldpos.printer.layouts.order.CashierLayout
import com.hanheldpos.printer.layouts.order.KitchenLayout
import com.hanheldpos.printer.layouts.report.InventoryLayout
import java.lang.Exception

class BillPrinterManager private constructor() {

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

                    when (printingOptions.deviceType) {
                        is DeviceType.SDK -> {
                            when ((printingOptions.deviceType as DeviceType.SDK).type) {
                                DeviceType.SDK.Types.UROVO -> {
                                    printer = UrovoPrinterManager()
                                    printer?.connect()
                                }
                                else -> {}
                            }
                        }
                        is DeviceType.NO_SDK -> {
                            printer = when (printingOptions.connectionType) {
                                PrintConnectionType.LAN -> {
                                    val policy = StrictMode
                                        .ThreadPolicy
                                        .Builder()
                                        .permitAll()
                                        .build()

                                    StrictMode.setThreadPolicy(policy)

                                    LanPrinterManager(
                                        ipAddress = printOptions.lanConfig.ipAddress,
                                        port = printOptions.lanConfig.port,
                                        timeout = printOptions.lanConfig.timeOut,
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
                    }

                    if (!instance.isConnected()) {
                        throw PrinterException("Printer is not connected")
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

// region printer methods

    fun printBill(
        order: OrderModel,
        layoutType: LayoutType.Order,
        isReprint: Boolean,
    ): BillPrinterManager {

        printer?.performPrinterAction {

            when (layoutType) {
                LayoutType.Order.Cashier -> CashierLayout(
                    order, printer!!, printOptions, isReprint
                )
                LayoutType.Order.Kitchen -> KitchenLayout(
                    order, printer!!, printOptions, isReprint
                )
            }.print()
        }

        return this
    }

    fun printReport(
        layoutType: LayoutType.Report,
        startDate: String,
        endDate: String,
    ) {
        printer?.performPrinterAction {
            when (layoutType) {
                LayoutType.Report.Inventory -> {
                    InventoryLayout(printer!!, printOptions, startDate, endDate, emptyMap())
                }
                LayoutType.Report.Overview -> {
                    InventoryLayout(printer!!, printOptions, startDate, endDate, emptyMap())
                }
            }.print()
        }
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

// endregion
}