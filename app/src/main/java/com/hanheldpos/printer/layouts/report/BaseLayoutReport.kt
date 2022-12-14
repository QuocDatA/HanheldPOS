package com.hanheldpos.printer.layouts.report

import com.hanheldpos.data.api.pojo.report.ReportSalesResp
import com.hanheldpos.model.report.ReportFilterModel
import com.hanheldpos.printer.layouts.BaseLayoutPrinter
import com.hanheldpos.printer.printer_setup.PrintConfig
import com.hanheldpos.printer.printer_setup.printer_manager.BasePrinterManager
import com.hanheldpos.printer.wagu.Block
import com.hanheldpos.printer.wagu.WaguUtils
import com.hanheldpos.utils.DateTimeUtils
import java.util.*


abstract class BaseLayoutReport(
    printer: BasePrinterManager,
    printConfig: PrintConfig,
    private val title: String,
    protected val reportSalesModel: ReportSalesResp?,
    protected val filterOptionReportSale: ReportFilterModel?,
) :
    BaseLayoutPrinter(printer, printConfig) {

    private val startDate: String = DateTimeUtils.strToStr(
        filterOptionReportSale?.startDay,
        DateTimeUtils.Format.YYYY_MM_DD,
        DateTimeUtils.Format.EEEE_dd_MMM_yyyy
    )
    private val endDate: String = DateTimeUtils.strToStr(
        filterOptionReportSale?.endDay,
        DateTimeUtils.Format.YYYY_MM_DD,
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
        device.drawText(
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
        device.drawText(
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
        device.drawText(
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
        device.drawText(
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
        device.drawText(
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
        device.drawText(
            WaguUtils.columnListDataBlock(
                charPerLineNormal,
                mutableListOf(
                    mutableListOf(
                        "Bill Time",
                        if (filterOptionReportSale?.startHour?.isEmpty() == true && filterOptionReportSale.endHour?.isEmpty() == true)
                            "All Day"
                        else
                            "${filterOptionReportSale?.startHour ?: ""} - ${filterOptionReportSale?.endHour ?: ""}"
                    )
                ),
                aligns = mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT),
            )
        )
    }
}