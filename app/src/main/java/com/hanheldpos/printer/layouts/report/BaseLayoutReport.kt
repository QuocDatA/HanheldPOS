package com.hanheldpos.printer.layouts.report

import com.handheld.printer.printer_setup.PrintOptions
import com.handheld.printer.printer_setup.printer_manager.BasePrinterManager
import com.handheld.printer.wagu.Block
import com.handheld.printer.wagu.WaguUtils
import com.hanheldpos.printer.layouts.BaseLayoutPrinter
import com.hanheldpos.utils.DateTimeUtils


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
                            DateTimeUtils.strToStr(
                                startDate,
                                DateTimeUtils.Format.EEEE_dd_MMM_yyyy,
                                DateTimeUtils.Format.DD_MM_YYYY
                            )
                        } - ${
                            DateTimeUtils.strToStr(
                                endDate,
                                DateTimeUtils.Format.EEEE_dd_MMM_yyyy,
                                DateTimeUtils.Format.DD_MM_YYYY
                            )
                        } ]"
                    )
                ),
                aligns = mutableListOf(Block.DATA_CENTER)
            ),
        )
    }
}