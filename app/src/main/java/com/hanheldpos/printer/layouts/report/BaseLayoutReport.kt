package com.hanheldpos.printer.layouts.report

import com.hanheldpos.printer.printer_setup.PrintOptions
import com.hanheldpos.printer.printer_setup.printer_manager.BasePrinterManager
import com.hanheldpos.printer.wagu.Block
import com.hanheldpos.printer.wagu.WaguUtils
import com.hanheldpos.data.api.pojo.report.ReportSalesResp
import com.hanheldpos.model.report.SaleReportFilter
import com.hanheldpos.printer.layouts.BaseLayoutPrinter
import com.hanheldpos.utils.DateTimeUtils
import java.util.*


abstract class BaseLayoutReport(
    printer: BasePrinterManager,
    printOptions: PrintOptions,
    private val title: String,
    protected val reportSalesModel: ReportSalesResp?,
    protected val filterOptionReportSale: SaleReportFilter?,
) :
    BaseLayoutPrinter(printer, printOptions) {

    private val startDate: String = DateTimeUtils.dateToString(
        filterOptionReportSale?.startDay,
        DateTimeUtils.Format.EEEE_dd_MMM_yyyy
    )
    private val endDate: String = DateTimeUtils.dateToString(
        filterOptionReportSale?.endDay,
        DateTimeUtils.Format.EEEE_dd_MMM_yyyy
    )

    protected fun printHeader() {
        printLogo()
        feedLines(1)
        printTitle()
        printDate()
        feedLines(1)

        printEmployee()
        printStaff()

        printDivider()
        printBillTime()
        printDivider()
    }

    private fun printLogo() {
        printer.drawText(
            WaguUtils.columnListDataBlock(
                charPerLineNormal,
                mutableListOf(
                    mutableListOf(
                        "<logo>"
                    )
                ),
                columnSize = mutableListOf(6),
                aligns = mutableListOf(Block.DATA_MIDDLE_LEFT)
            )
        )
    }

    private fun printTitle() {
        printer.drawText(
            WaguUtils.columnListDataBlock(
                charPerLineLarge,
                mutableListOf(
                    mutableListOf(
                        title,
                    )
                ),
                aligns = mutableListOf(Block.DATA_CENTER)
            ),
            bold = true,
            BasePrinterManager.FontSize.Large
        )
    }

    private fun printDate() {
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
                            DateTimeUtils.formatDate(
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

    private fun printEmployee() {
        printer.drawText(
            WaguUtils.columnListDataBlock(
                charPerLineNormal,
                mutableListOf(
                    mutableListOf(
                        "Shop: ${reportSalesModel?.Report?.firstOrNull()?.LocationName ?: ""}"
                    )
                ),
                aligns = mutableListOf(Block.DATA_MIDDLE_LEFT)
            ),
            size = BasePrinterManager.FontSize.Small
        )
    }

    private fun printStaff() {
        printer.drawText(
            WaguUtils.columnListDataBlock(
                charPerLineNormal,
                mutableListOf(
                    mutableListOf(
                        "Staff: ${reportSalesModel?.Report?.firstOrNull()?.Employee ?: ""}",
                        DateTimeUtils.dateToString(Date(), DateTimeUtils.Format.DD_MM_YYYY_HH_MM)
                    )
                ),
                aligns = mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT)
            )
        )
    }

    private fun printBillTime() {
        printer.drawText(
            WaguUtils.columnListDataBlock(
                charPerLineNormal,
                mutableListOf(
                    mutableListOf(
                        "Bill Time",
                        if (filterOptionReportSale?.isAllDay == true)
                            "All Day"
                        else
                            "${filterOptionReportSale?.startTime ?: ""} - ${filterOptionReportSale?.endTime ?: ""}"
                    )
                ),
                aligns = mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT),
            )
        )
    }
}