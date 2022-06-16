package com.hanheldpos.printer

import android.content.Context
import android.os.StrictMode
import android.util.Log
import com.hanheldpos.printer.interfaces.BluetoothPrinterManager
import com.hanheldpos.printer.interfaces.LanPrinterManager
import com.hanheldpos.printer.interfaces.UrovoPrinterManager
import com.hanheldpos.printer.printer_setup.PrintConnectionType
import com.hanheldpos.printer.printer_setup.PrintOptions
import com.hanheldpos.printer.printer_setup.device_info.DeviceType
import com.hanheldpos.printer.printer_setup.printer_manager.BasePrinterManager
import com.hanheldpos.data.api.pojo.report.ReportSalesResp
import com.hanheldpos.model.order.OrderModel
import com.hanheldpos.model.report.SaleReportFilter
import com.hanheldpos.printer.layouts.LayoutType
import com.hanheldpos.printer.layouts.order.CashierLayout
import com.hanheldpos.printer.layouts.order.KitchenLayout
import com.hanheldpos.printer.layouts.report.InventoryLayout
import com.hanheldpos.printer.layouts.report.OverviewLayout
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
        report: ReportSalesResp?,
        filterOptions: SaleReportFilter?,
    ) {
        printer?.performPrinterAction {
            when (layoutType) {
                LayoutType.Report.Inventory -> {
                    InventoryLayout(printer!!, printOptions, report, filterOptions)
                }
                LayoutType.Report.Overview -> {
                    OverviewLayout(printer!!, printOptions, report, filterOptions)
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