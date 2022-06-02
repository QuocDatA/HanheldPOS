package com.example.pos2.printer.layouts.report

import com.example.pos2.printer.layouts.BaseLayoutPrinter
import com.example.pos2.printer_setup.PrintOptions
import com.example.pos2.printer_setup.printer_manager.BasePrinterManager
import com.example.pos2.utils.time.DateTimeHelper
import com.example.pos2.wagu.Block
import com.example.pos2.wagu.WaguUtils


abstract class BaseLayoutReport(
    printer: BasePrinterManager,
    printOptions: PrintOptions,
    private val startDate: String,
    private val endDate: String
) :
    BaseLayoutPrinter(printer, printOptions) {

    protected fun printDate() {
        printer.drawText(
            WaguUtils.columnListDataBlock(
                charPerLineNormal,
                mutableListOf(
                    mutableListOf(
                        "[ ${
                            DateTimeHelper.utcTimeToDateStr(
                                startDate,
                                DateTimeHelper.Format.EEEE_dd_MMM_yyyy,
                                DateTimeHelper.Format.DD_MM_YYYY
                            )
                        } - ${
                            DateTimeHelper.utcTimeToDateStr(
                                endDate,
                                DateTimeHelper.Format.EEEE_dd_MMM_yyyy,
                                DateTimeHelper.Format.DD_MM_YYYY
                            )
                        } ]"
                    )
                ),
                aligns = mutableListOf(Block.DATA_CENTER)
            ),
        )
    }
}