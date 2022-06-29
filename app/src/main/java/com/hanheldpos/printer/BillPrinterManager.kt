package com.hanheldpos.printer

import android.content.Context
import android.os.StrictMode
import android.util.Log
import com.hanheldpos.printer.printer_setup.device_info.DeviceType
import com.hanheldpos.printer.printer_setup.printer_manager.BasePrinterManager
import com.hanheldpos.data.api.pojo.report.ReportSalesResp
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.order.OrderModel
import com.hanheldpos.model.report.SaleReportFilter
import com.hanheldpos.printer.layouts.LayoutType
import com.hanheldpos.printer.layouts.order.CashierLayout
import com.hanheldpos.printer.layouts.order.KitchenLayout
import com.hanheldpos.printer.layouts.report.InventoryLayout
import com.hanheldpos.printer.layouts.report.OverviewLayout
import com.hanheldpos.printer.printer_devices.Printer
import com.hanheldpos.printer.printer_setup.PrintConfig
import com.hanheldpos.printer.printer_setup.PrinterTypes
import java.lang.Exception

class BillPrinterManager private constructor() {

// region Singleton

    companion object {
        @Volatile
        private lateinit var instance: BillPrinterManager


        // region variables

        private val printers = mutableListOf<Printer>()
        private lateinit var applicationContext: Context
        // endregion

        fun init(
            context: Context,
            onConnectionFailed: ((exception: PrinterException) -> Unit)? = null,
            onConnectionSuccess: ((printConfig: PrintConfig) -> Unit)? = null
        ): BillPrinterManager {
            try {
                instance = BillPrinterManager().apply {
                    applicationContext = context
                    printers.clear()
                }
                if (instance.isConnected()) return instance

                instance.apply {
                    (DataHelper
                        .hardwareSettingLocalStorage
                        ?.printerList
                        ?: emptyList())
                        .forEach {
                            val printer = Printer.getInstance(it)
                            if (printer.isConnected()) {
                                printers.add(printer)
                                onConnectionSuccess?.invoke(printer.printConfig)
                            } else {
                                onConnectionFailed?.invoke(PrinterException(printer.printConfig.toString()))
                            }
                        }
                }

            } catch (e: Exception) {
                onConnectionFailed?.invoke(PrinterException(e.message.toString()))
            }
            return instance
        }

        fun get(onConnectionFailed: ((exception: PrinterException) -> Unit)? = null): BillPrinterManager {
            return if (this::instance.isInitialized && instance.isConnected())
                instance
            else {
                init(applicationContext, onConnectionFailed)
            }
        }
    }

// endregion

// region printer methods

    fun printBill(
        order: OrderModel,
        isReprint: Boolean,
        limitToThesePrinterId: List<String?>? = null
    ): BillPrinterManager {

        val finalChosenPrinters = mutableListOf<Printer>()

        if (limitToThesePrinterId != null) {
            limitToThesePrinterId.forEach { limitedPrinter ->
                val connectedPrinter =
                    printers.find { limitedPrinter == it.printingSpecification.id }
                if (connectedPrinter != null) {
                    finalChosenPrinters.add(connectedPrinter)
                }
            }
        } else {
            finalChosenPrinters.addAll(printers)
        }

        finalChosenPrinters.forEach {
            it.printBill(order, isReprint)
        }
        return this
    }

    fun printReport(
        layoutType: LayoutType.Report,
        report: ReportSalesResp?,
        filterOptions: SaleReportFilter?,
        printerTypes: PrinterTypes,
    ): BillPrinterManager {
        printers.firstOrNull { it.printingSpecification.printerTypeId == printerTypes.value }
            ?.printReport(layoutType, report, filterOptions)

        return this
    }


    fun openCashDrawer(): BillPrinterManager {
        printers
            .firstOrNull { it.printingSpecification.isConnectCashDrawer == true }
            ?.openCashDrawer()
        return this
    }

    fun isConnected(): Boolean {
        return printers.isNotEmpty() && printers.all { it.isConnected() }
    }

    fun printers() = mutableListOf<Printer>().apply {
        addAll(printers)
    }

// endregion
}