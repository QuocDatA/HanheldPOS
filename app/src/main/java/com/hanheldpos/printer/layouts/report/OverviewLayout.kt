package com.hanheldpos.printer.layouts.report

import com.hanheldpos.printer.printer_setup.printer_manager.BasePrinterManager
import com.hanheldpos.printer.wagu.Block
import com.hanheldpos.printer.wagu.WaguUtils
import com.hanheldpos.data.api.pojo.report.ReportSalesResp
import com.hanheldpos.model.menu.report.ReportHelper
import com.hanheldpos.model.menu.report.SaleOptionPage
import com.hanheldpos.model.report.SaleReportFilter
import com.hanheldpos.printer.printer_setup.PrintConfig
import com.hanheldpos.utils.PriceUtils


class OverviewLayout(
    printer: BasePrinterManager,
    printConfig: PrintConfig,
    reportSalesModel: ReportSalesResp?,
    filterOptionReportSale: SaleReportFilter?
) : BaseLayoutReport(
    printer,
    printConfig,
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

        reportSalesModel?.Device?.forEach {
            printer.drawText(
                WaguUtils.columnListDataBlock(
                    charPerLineNormal,
                    list = mutableListOf(
                        mutableListOf(
                            it.DeviceName,
                            it.Quantity.toString(),
                            PriceUtils.formatStringPrice(it.Total),
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
                        PriceUtils.formatStringPrice(reportSalesModel?.Device?.sumOf { device ->
                            device.Total
                        } ?: 0.0)
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

        val saleSummaries = reportSalesModel?.SalesSummary

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
                        (saleSummaries?.sumOf { it.OrderQuantity ?: 0 } ?: 0).toString(),
                        PriceUtils.formatStringPrice(
                            saleSummaries?.sumOf { it.OrderAmount } ?: 0.0,
                            limitLength = true
                        ),
                    ),
                    mutableListOf(
                        "Cover",
                        (saleSummaries?.sumOf { it.Cover } ?: 0.0).toInt().toString(),
                        "0"
                    ),
                    mutableListOf(
                        "Service Charge",
                        "",
                        PriceUtils.formatStringPrice(
                            saleSummaries?.sumOf { it.ServiceCharge } ?: 0.0,
                            limitLength = true
                        )
                    ),
                    mutableListOf(
                        "Discounts & Comp",
                        "",
                        PriceUtils.formatStringPrice(
                            saleSummaries?.sumOf { it.OrderDiscountComp } ?: 0.0,
                            limitLength = true
                        )
                    ),
                    mutableListOf(
                        "Refund",
                        (saleSummaries?.sumOf { it.QuantityRefund } ?: 0).toString(),
                        PriceUtils.formatStringPrice(
                            saleSummaries?.sumOf { it.OrderRefund ?: 0.0 } ?: 0.0,
                            limitLength = true
                        )
                    ),
                    mutableListOf(
                        "Net Sales",
                        "",
                        PriceUtils.formatStringPrice(
                            saleSummaries?.sumOf { it.NetSales ?: 0.0 } ?: 0.0,
                            limitLength = true
                        )
                    ),
                    mutableListOf(
                        "Taxes",
                        "",
                        PriceUtils.formatStringPrice(
                            saleSummaries?.sumOf { it.TaxFee ?: 0.0 } ?: 0.0,
                            limitLength = true
                        )
                    ),
                    mutableListOf(
                        "Service Fee",
                        "",
                        PriceUtils.formatStringPrice(
                            saleSummaries?.sumOf { it.ServiceFee ?: 0.0 } ?: 0.0,
                            limitLength = true
                        )
                    ),
                    mutableListOf(
                        "Surcharge Fee",
                        "",
                        PriceUtils.formatStringPrice(
                            saleSummaries?.sumOf { it.SurchargeFee ?: 0.0 } ?: 0.0,
                            limitLength = true
                        )
                    ),
                    mutableListOf(
                        "Total Sales",
                        "",
                        PriceUtils.formatStringPrice(
                            saleSummaries?.sumOf { it.TotalSales ?: 0.0 } ?: 0.0,
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
                        PriceUtils.formatStringPrice(saleSummaries?.sumOf { it.BillCount ?: 0.0 }
                            ?: 0.0)
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
                        PriceUtils.formatStringPrice(
                            reportSalesModel?.Cash?.sumOf { it.StartingCash ?: 0.0 } ?: 0.0,
                        )
                    ),
                    mutableListOf(
                        "Paid In",
                        PriceUtils.formatStringPrice(
                            reportSalesModel?.Cash?.sumOf { it.PaidIn ?: 0.0 } ?: 0.0,
                        )
                    ),
                    mutableListOf(
                        "Paid Out",
                        PriceUtils.formatStringPrice(
                            reportSalesModel?.Cash?.sumOf { it.PaidOut ?: 0.0 } ?: 0.0,
                        )
                    ),
                    mutableListOf(
                        "Cash Sales",
                        PriceUtils.formatStringPrice(
                            reportSalesModel?.Cash?.sumOf { it.CashSales ?: 0.0 } ?: 0.0,
                        )
                    ),
                    mutableListOf(
                        "Cash Refunds",
                        PriceUtils.formatStringPrice(
                            reportSalesModel?.Cash?.sumOf { it.CashRefunds ?: 0.0 } ?: 0.0,
                        )
                    ),
                    mutableListOf(
                        "Expected In Drawer",
                        PriceUtils.formatStringPrice(
                            reportSalesModel?.Cash?.sumOf { it.ExpectedInDrawer ?: 0.0 } ?: 0.0,
                        )
                    ),
                    mutableListOf(
                        "Actual In Drawer",
                        PriceUtils.formatStringPrice(
                            reportSalesModel?.Cash?.sumOf { it.ActualInDrawer ?: 0.0 } ?: 0.0,
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
                        PriceUtils.formatStringPrice(reportSalesModel?.Cash?.sumOf {
                            it.Difference ?: 0.0
                        } ?: 0.0)
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
        type: SaleOptionPage,
        isCenterAlign: Boolean,
    ) {
        printSectionHeader(header)

        val reportItems = ReportHelper.feeDetailReport(reportSalesModel ?: return, type)

        val rows = reportItems.map {
            mutableListOf(
                it.name ?: "",
                (it.qty ?: 0).toString(),
                PriceUtils.formatStringPrice(it.amount, true)
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
                        PriceUtils.formatStringPrice(reportItems.sumOf { it.amount ?: 0.0 })
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
            SaleOptionPage.PaymentSummary,
            false,
        )
    }

    private fun printDiningOptionDetail() {
        printThreeColumnReport(
            "*** Dining Options Detail ***",
            SaleOptionPage.DiningOptions,
            false,
        )
    }

    private fun printCashVoucherDetail() {
        printThreeColumnReport(
            "*** Cash Vouchers Detail ***",
            SaleOptionPage.CashVoucher,
            false,
        )
    }

    private fun printCompDetail() {
        printThreeColumnReport(
            "*** Comp Detail ***",
            SaleOptionPage.Comps,
            false
        )
    }

    private fun printDiscountDetail() {
        printThreeColumnReport(
            "*** Discount Detail ***",
            SaleOptionPage.Discounts,
            true
        )
    }

    private fun printCategorySales() {
        printSectionHeader("*** Category Sales ***")
        feedLines(3)
        val reportList =
            ReportHelper.feeDetailReport(
                reportSalesModel ?: return,
                type = SaleOptionPage.CategorySales
            )

        reportList.forEach {
            printer.drawText(
                WaguUtils.columnListDataBlock(
                    charPerLineNormal,
                    mutableListOf(
                        mutableListOf(
                            it.name ?: ""
                        )
                    ),
                    aligns = mutableListOf(Block.DATA_MIDDLE_LEFT)
                ),
                size = BasePrinterManager.FontSize.Small,
            )

            val productList = it.list?.map { productReport ->
                mutableListOf(
                    productReport.name ?: "",
                    (productReport.qty ?: 0).toString(),
                    PriceUtils.formatStringPrice(productReport.amount, limitLength = true),
                    productReport.subValue.toString()
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
                            (it.qty ?: 0).toString(),
                            PriceUtils.formatStringPrice(it.amount, limitLength = true),
                            it.subValue.toString(),
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
                        PriceUtils.formatStringPrice(reportSalesModel.Category.sumOf {
                            it.Total ?: 0.0
                        }
                            ?: 0.0)
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
                        "Bill Count  (${reportSalesModel?.SalesSummary?.sumOf { it.QuantityBillCount.toInt() ?: 0 } ?: 0})",
                        PriceUtils.formatStringPrice(reportSalesModel?.SalesSummary?.sumOf {
                            it.BillCount ?: 0.0
                        } ?: 0.0)
                    ),
                    mutableListOf(
                        "Bill Average",
                        PriceUtils.formatStringPrice(reportSalesModel?.SalesSummary?.sumOf {
                            it.BillAverage ?: 0.0
                        } ?: 0.0)
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