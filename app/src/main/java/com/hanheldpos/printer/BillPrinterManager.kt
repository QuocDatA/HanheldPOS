package com.hanheldpos.printer

import android.content.Context
import com.hanheldpos.data.api.pojo.report.ReportSalesResp
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.order.OrderModel
import com.hanheldpos.model.report.ReportFilterModel
import com.hanheldpos.printer.layouts.LayoutType
import com.hanheldpos.printer.printer_devices.Printer
import com.hanheldpos.printer.printer_setup.PrinterTypes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class BillPrinterManager private constructor() {

// region Singleton

    companion object {
        @Volatile
        private lateinit var instance: BillPrinterManager


        // region variables

        private val printers = Collections.synchronizedMap(mutableMapOf<Printer, Job?>())
        private lateinit var applicationContext: Context
        // endregion

        fun init(
            context: Context,
            onConnectionFailed: ((exception: PrinterException) -> Unit)? = null,
            onConnectionSuccess: ((printConfig: Printer) -> Unit)? = null
        ): BillPrinterManager {

            instance = BillPrinterManager().apply {
                applicationContext = context
                // Cancel all Job Check Connection
                printers.values.forEach {
                    it?.cancel()
                }
                printers.clear()
            }
            if (instance.isConnected()) return instance

            instance.apply {
                (DataHelper
                    .hardwareSettingLocalStorage
                    ?.printerList
                    ?: emptyList())
                    .forEach {
                        val job = Job()
                        CoroutineScope(Dispatchers.IO + job).launch {
                            try {
                                val printer = Printer.getInstance(it)

                                if (printer.isConnected()) {
                                    printers[printer] = job
                                    onConnectionSuccess?.invoke(printer)
                                } else {
                                    onConnectionFailed?.invoke(
                                        PrinterException(
                                            printer,
                                            printer.toString()
                                        )
                                    )
                                }
                            } catch (ex: Exception) {
                                onConnectionFailed?.invoke(
                                    PrinterException(
                                        null,
                                        ex.message.toString(),
                                    )
                                )
                            }

                        }
                    }
            }
            do {
                val isDone =
                    printers.values.map { it?.isCompleted ?: true || it?.isCancelled ?: true }
            } while (isDone.none { !it })
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
                    printers.keys.find { limitedPrinter == it.printingSpecification.id }
                if (connectedPrinter != null) {
                    finalChosenPrinters.add(connectedPrinter)
                }
            }
        } else {
            finalChosenPrinters.addAll(printers.keys)
        }

        finalChosenPrinters.forEach {
            it.printBill(order, isReprint)
        }
        return this
    }

    fun printReport(
        layoutType: LayoutType.Report,
        report: ReportSalesResp?,
        filterOptions: ReportFilterModel?,
        printerTypes: PrinterTypes,
    ): BillPrinterManager {
        printers.keys
            .firstOrNull { it.printingSpecification.printerTypeId == printerTypes.value }
            ?.printReport(layoutType, report, filterOptions)

        return this
    }


    fun openCashDrawer(): BillPrinterManager {
        printers.keys
            .firstOrNull { it.printingSpecification.isConnectCashDrawer == true }
            ?.openCashDrawer()
        return this
    }

    fun isConnected(): Boolean {
        return printers.isNotEmpty() && printers.keys.all { it.isConnected() }
    }

    fun printers() = mutableListOf<Printer>().apply {
        addAll(printers.keys)
    }

// endregion
}