package com.example.pos2.printer.layouts.report

import com.example.pos2.model.report.sales.FilterOptionReportSale
import com.example.pos2.model.report.sales.ReportItem
import com.example.pos2.printer_setup.PrintOptions
import com.example.pos2.printer_setup.printer_manager.BasePrinterManager
import com.example.pos2.repo.report.AllCategory
import com.example.pos2.repo.report.ReportSalesModel
import com.example.pos2.utils.StringHelper
import com.example.pos2.view.left.report.ReportHelper
import com.example.pos2.view.left.report.sales.ReportType
import com.example.pos2.wagu.Block
import kotlin.streams.toList

class OverviewLayout(
    printer: BasePrinterManager,
    printOptions: PrintOptions,
    reportSalesModel: ReportSalesModel?,
    filterOptionReportSale: FilterOptionReportSale?
) : BaseLayoutReport(
    printer,
    printOptions,
    title = "Sales Report",
    reportSalesModel,
    filterOptionReportSale
) {

    private val threeColumnsAlignRightSize = mutableListOf<Int>(charPerLineNormal - 6 - 11, 6, 11)
    private val threeColumnAlignRightAlignment =
        mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT, Block.DATA_MIDDLE_RIGHT)

    override fun print() {
        printHeader()

        printDevicesReport()
        feedLines(5)

        printOverviews()
        feedLines(3)

        printCashDrawer()
        feedLines(3)

        printPaymentSummary()
        feedLines(3)

        printDiningOptionDetail()
        feedLines(3)

        printCashVoucherDetail()
        feedLines(3)

        printCompDetail()
        feedLines(3)

        printDiscountDetail()
        feedLines(3)

        feedLines(2)
        printCategorySales()

        printBillTotal()
        cutPaper()
    }

    private fun printDevicesReport() {

        printer.drawText(
            WaguUtils.columnListDataBlock(
                charPerLineNormal,
                list = mutableListOf(
                    mutableListOf(
                        "Device Name",
                        "Qty",
                        "Amount"
                    ),
                ),
                aligns = mutableListOf(
                    Block.DATA_MIDDLE_LEFT,
                    Block.DATA_CENTER,
                    Block.DATA_MIDDLE_RIGHT
                ),
            ), size = BasePrinterManager.FontSize.Small
        )

        reportSalesModel?.device?.forEach {
            printer.drawText(
                WaguUtils.columnListDataBlock(
                    charPerLineNormal,
                    list = mutableListOf(
                        mutableListOf(
                            it.deviceName ?: "",
                            (it.quantity ?: 0).toString(),
                            StringHelper.toPrice(it.total),
                        )
                    ),
                    aligns = mutableListOf(
                        Block.DATA_MIDDLE_LEFT,
                        Block.DATA_CENTER,
                        Block.DATA_MIDDLE_RIGHT
                    ),
                ),
                size = BasePrinterManager.FontSize.Small
            )
        }

        printDivider()

        printer.drawText(
            WaguUtils.columnListDataBlock(
                charPerLineNormal,
                list = mutableListOf(
                    mutableListOf(
                        "Total",
                        StringHelper.toPrice(reportSalesModel?.device?.sumOf { device ->
                            device.total ?: 0.0
                        })
                    )
                ),
                aligns = mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT),
                columnSize = mutableListOf(6, charPerLineNormal - 6)
            ), size = BasePrinterManager.FontSize.Small
        )
    }

    private fun printSectionHeader(sectionHeader: String) {
        printer.drawText(
            WaguUtils.columnListDataBlock(
                charPerLineNormal,
                mutableListOf(
                    mutableListOf(
                        sectionHeader
                    )
                ),
                aligns = mutableListOf(Block.DATA_CENTER)
            ),
            size = BasePrinterManager.FontSize.Small,
        )
        feedLines(1)
    }

    private fun printOverviews() {
        printSectionHeader("*** Over Views ***")

        val saleSummaries = reportSalesModel?.salesSummary

        printer.drawText(
            WaguUtils.columnListDataBlock(
                charPerLineNormal,
                mutableListOf(
                    mutableListOf(
                        "Name",
                        "Qty",
                        "Amount"
                    ),
                    mutableListOf(
                        "Order",
                        (saleSummaries?.sumOf { it.orderQuantity ?: 0 } ?: 0).toString(),
                        StringHelper.toPrice(
                            saleSummaries?.sumOf { it.orderAmount ?: 0.0 },
                            limitLength = true
                        ),
                    ),
                    mutableListOf(
                        "Cover",
                        (saleSummaries?.sumOf { it.cover ?: 0 } ?: 0).toString(),
                        "0"
                    ),
                    mutableListOf(
                        "Service Charge",
                        "",
                        StringHelper.toPrice(
                            saleSummaries?.sumOf { it.serviceCharge ?: 0.0 },
                            limitLength = true
                        )
                    ),
                    mutableListOf(
                        "Discounts & Comp",
                        "",
                        StringHelper.toPrice(
                            saleSummaries?.sumOf { it.orderDiscountComp ?: 0.0 },
                            limitLength = true
                        )
                    ),
                    mutableListOf(
                        "Refund",
                        (saleSummaries?.sumOf { it.quantityRefund ?: 0 } ?: 0).toString(),
                        StringHelper.toPrice(
                            saleSummaries?.sumOf { it.orderRefund ?: 0.0 },
                            limitLength = true
                        )
                    ),
                    mutableListOf(
                        "Net Sales",
                        "",
                        StringHelper.toPrice(
                            saleSummaries?.sumOf { it.netSales ?: 0.0 },
                            limitLength = true
                        )
                    ),
                    mutableListOf(
                        "Taxes",
                        "",
                        StringHelper.toPrice(
                            saleSummaries?.sumOf { it.taxFee ?: 0.0 },
                            limitLength = true
                        )
                    ),
                    mutableListOf(
                        "Service Fee",
                        "",
                        StringHelper.toPrice(
                            saleSummaries?.sumOf { it.serviceFee ?: 0.0 },
                            limitLength = true
                        )
                    ),
                    mutableListOf(
                        "Service Fee",
                        "",
                        StringHelper.toPrice(
                            saleSummaries?.sumOf { it.surchargeFee ?: 0.0 },
                            limitLength = true
                        )
                    ),
                    mutableListOf(
                        "Total Sales",
                        "",
                        StringHelper.toPrice(
                            saleSummaries?.sumOf { it.totalSales ?: 0.0 },
                            limitLength = true
                        )
                    ),
                ),
                aligns = threeColumnAlignRightAlignment,
                columnSize = threeColumnsAlignRightSize
            ), size = BasePrinterManager.FontSize.Small
        )

        printDivider()
        printer.drawText(
            WaguUtils.columnListDataBlock(
                charPerLineNormal, mutableListOf(
                    mutableListOf(
                        "Total",
                        StringHelper.toPrice(saleSummaries?.sumOf { it.billCount ?: 0.0 })
                    )
                ),
                aligns = mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT),
                columnSize = mutableListOf(6, charPerLineNormal - 6)
            ), size = BasePrinterManager.FontSize.Small
        )
    }

    private fun printCashDrawer() {

        printSectionHeader("*** Cash Drawer ***")

        printer.drawText(
            WaguUtils.columnListDataBlock(
                charPerLineNormal,
                mutableListOf(
                    mutableListOf(
                        "Starting Cash",
                        StringHelper.toPrice(
                            reportSalesModel?.Cash?.sumOf { it.startingCash ?: 0.0 },
                        )
                    ),
                    mutableListOf(
                        "Paid In",
                        StringHelper.toPrice(
                            reportSalesModel?.Cash?.sumOf { it.paidIn ?: 0.0 },
                        )
                    ),
                    mutableListOf(
                        "Paid Out",
                        StringHelper.toPrice(
                            reportSalesModel?.Cash?.sumOf { it.paidOut ?: 0.0 },
                        )
                    ),
                    mutableListOf(
                        "Cash Sales",
                        StringHelper.toPrice(
                            reportSalesModel?.Cash?.sumOf { it.cashSales ?: 0.0 },
                        )
                    ),
                    mutableListOf(
                        "Cash Refunds",
                        StringHelper.toPrice(
                            reportSalesModel?.Cash?.sumOf { it.cashRefunds ?: 0.0 },
                        )
                    ),
                    mutableListOf(
                        "Expected In Drawer",
                        StringHelper.toPrice(
                            reportSalesModel?.Cash?.sumOf { it.expectedInDrawer ?: 0.0 },
                        )
                    ),
                    mutableListOf(
                        "Actual In Drawer",
                        StringHelper.toPrice(
                            reportSalesModel?.Cash?.sumOf { it.actualInDrawer ?: 0.0 },
                        )
                    ),
                ),
                aligns = mutableListOf(
                    Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT
                ),
                columnSize = mutableListOf(20, charPerLineNormal - 20)
            ), size = BasePrinterManager.FontSize.Small
        )

        printDivider()
        printer.drawText(
            WaguUtils.columnListDataBlock(
                charPerLineNormal, mutableListOf(
                    mutableListOf(
                        "Differences",
                        StringHelper.toPrice(reportSalesModel?.Cash?.sumOf { it.difference ?: 0.0 })
                    )
                ),
                aligns = mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT),
                columnSize = mutableListOf(12, charPerLineNormal - 12)
            ), size = BasePrinterManager.FontSize.Small
        )
    }

    // If column size is null, this will be align center
    private fun printThreeColumnReport(
        header: String,
        type: ReportType,
        isCenterAlign: Boolean,
    ) {
        printSectionHeader(header)

        val reportItems = ReportHelper.feeDetailReport(reportSalesModel ?: return, type)

        val rows = reportItems.map {
            mutableListOf(
                it.title ?: "",
                (it.quantity ?: 0).toString(),
                StringHelper.toPrice(it.value ?: 0.0, limitLength = false)
            )
        }


        printer.drawText(
            WaguUtils.columnListDataBlock(
                charPerLineNormal,
                mutableListOf(
                    mutableListOf(
                        "Name",
                        "Qty",
                        "Amount"
                    ),
                    *rows.toTypedArray()
                ),
                aligns = if (isCenterAlign) mutableListOf(
                    Block.DATA_MIDDLE_LEFT,
                    Block.DATA_CENTER,
                    Block.DATA_MIDDLE_RIGHT
                ) else
                    threeColumnAlignRightAlignment,
                columnSize =
                if (isCenterAlign)
                    null
                else
                    threeColumnsAlignRightSize
            ),
            size = BasePrinterManager.FontSize.Small
        )

        printDivider()

        printer.drawText(
            WaguUtils.columnListDataBlock(
                charPerLineNormal, mutableListOf(
                    mutableListOf(
                        "Total",
                        StringHelper.toPrice(reportItems.sumOf { it.value ?: 0.0 })
                    )
                ),
                aligns = mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT),
                columnSize = mutableListOf(6, charPerLineNormal - 6)
            ), size = BasePrinterManager.FontSize.Small
        )
    }

    private fun printPaymentSummary() {
        printThreeColumnReport(
            "*** Payment Summary ***",
            ReportType.PAYMENT,
            false,
        )
    }

    private fun printDiningOptionDetail() {
        printThreeColumnReport(
            "*** Dining Options Detail ***",
            ReportType.DINING_OPTION,
            false,
        )
    }

    private fun printCashVoucherDetail() {
        printThreeColumnReport(
            "*** Cash Vouchers Detail ***",
            ReportType.CASH_VOUCHERS,
            false,
        )
    }

    private fun printCompDetail() {
        printThreeColumnReport(
            "*** Comp Detail ***",
            ReportType.COMP,
            false
        )
    }

    private fun printDiscountDetail() {
        printThreeColumnReport(
            "*** Discount Detail ***",
            ReportType.DISCOUNTS,
            true
        )
    }

    private fun printCategorySales() {
        printSectionHeader("*** Category Sales ***")
        feedLines(3)
        val reportList =
            ReportHelper.feeDetailReport(reportSalesModel ?: return, type = ReportType.CATEGORY)

        reportList.forEach {
            printer.drawText(
                WaguUtils.columnListDataBlock(
                    charPerLineNormal,
                    mutableListOf(
                        mutableListOf(
                            it.title ?: ""
                        )
                    ),
                    aligns = mutableListOf(Block.DATA_MIDDLE_LEFT)
                ),
                size = BasePrinterManager.FontSize.Small,
            )

            val productList = it.list?.map { productReport ->
                mutableListOf(
                    productReport.title ?: "",
                    (productReport.quantity ?: 0).toString(),
                    StringHelper.toPrice(productReport.value, limitLength = true),
                    productReport.subValue
                )
            } ?: emptyList()

            printer.drawText(
                WaguUtils.columnListDataBlock(
                    charPerLineNormal,
                    mutableListOf(
                        *productList.toTypedArray()
                    ),
                    aligns = mutableListOf(
                        Block.DATA_MIDDLE_LEFT,
                        Block.DATA_MIDDLE_RIGHT,
                        Block.DATA_MIDDLE_RIGHT,
                        Block.DATA_MIDDLE_RIGHT
                    ),
                    columnSize = mutableListOf(charPerLineNormal - 5 - 11 - 7, 5, 11, 7)
                )
            )

            printDivider()

            printer.drawText(
                WaguUtils.columnListDataBlock(
                    charPerLineNormal,
                    mutableListOf(
                        mutableListOf(
                            "",
                            (it.quantity ?: 0).toString(),
                            StringHelper.toPrice(it.value, limitLength = true),
                            it.subValue,
                        )
                    ),
                    aligns = mutableListOf(
                        Block.DATA_MIDDLE_LEFT,
                        Block.DATA_MIDDLE_RIGHT,
                        Block.DATA_MIDDLE_RIGHT,
                        Block.DATA_MIDDLE_RIGHT
                    ),
                    columnSize = mutableListOf(charPerLineNormal - 5 - 11 - 7, 5, 11, 7)
                ),
            )

            feedLines(2)
        }
        printDivider()
        printer.drawText(
            WaguUtils.columnListDataBlock(
                charPerLineNormal,
                mutableListOf(
                    mutableListOf(
                        "Category Total",
                        StringHelper.toPrice(reportSalesModel.category?.sumOf { it.total ?: 0.0 })
                    )
                ),
                aligns = mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT),
                columnSize = mutableListOf(14, charPerLineNormal - 14)
            ),
            size = BasePrinterManager.FontSize.Small
        )
        printDivider()
    }


    private fun printBillTotal() {
        printer.drawText(
            WaguUtils.columnListDataBlock(
                charPerLineNormal,
                mutableListOf(
                    mutableListOf(
                        "Bill Count  (${reportSalesModel?.salesSummary?.sumOf { it.quantityBillCount ?: 0 }})",
                        StringHelper.toPrice(reportSalesModel?.salesSummary?.sumOf {
                            it.billCount ?: 0.0
                        })
                    ),
                    mutableListOf(
                        "Bill Average",
                        StringHelper.toPrice(reportSalesModel?.salesSummary?.sumOf {
                            it.billAverage ?: 0.0
                        })
                    )
                ),
                aligns = mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT),
                columnSize = mutableListOf(20, charPerLineNormal - 20)
            ), size = BasePrinterManager.FontSize.Small
        )
        printDivider()
        feedLines(2)
    }

}