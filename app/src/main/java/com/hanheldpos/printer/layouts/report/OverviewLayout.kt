package com.hanheldpos.printer.layouts.report

import com.hanheldpos.printer.printer_setup.printer_manager.BasePrinterManager
import com.hanheldpos.printer.wagu.Block
import com.hanheldpos.printer.wagu.WaguUtils
import com.hanheldpos.data.api.pojo.report.ReportSalesResp
import com.hanheldpos.model.menu.report.ReportHelper
import com.hanheldpos.model.menu.report.SaleOptionPage
import com.hanheldpos.model.report.ReportFilterModel
import com.hanheldpos.printer.printer_setup.PrintConfig
import com.hanheldpos.printer.wagu.WrapType
import com.hanheldpos.utils.PriceUtils


class OverviewLayout(
    printer: BasePrinterManager,
    printConfig: PrintConfig,
    reportSalesModel: ReportSalesResp?,
    filterOptionReportSale: ReportFilterModel?
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
        reportSalesModel?.Device ?: return
        device.drawText(
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

        reportSalesModel.Device.forEach {
            device.drawText(
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

        device.drawText(
            WaguUtils.columnListDataBlock(
                charPerLineNormal,
                list = mutableListOf(
                    mutableListOf(
                        "Total",
                        PriceUtils.formatStringPrice(reportSalesModel.Device.sumOf { device ->
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
        device.drawText(
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

        val saleSummaries = reportSalesModel?.SalesSummary ?: return

        device.drawText(
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
                        saleSummaries.sumOf { it.OrderQuantity }.toString(),
                        PriceUtils.formatStringPrice(
                            saleSummaries.sumOf { it.OrderAmount },
                            limitLength = true
                        ),
                    ),
                    mutableListOf(
                        "Cover",
                        saleSummaries.sumOf { it.Cover }.toInt().toString(),
                        "0"
                    ),
                    mutableListOf(
                        "Service Charge",
                        "",
                        PriceUtils.formatStringPrice(
                            saleSummaries.sumOf { it.ServiceCharge },
                            limitLength = true
                        )
                    ),
                    mutableListOf(
                        "Discounts & Comp",
                        "",
                        PriceUtils.formatStringPrice(
                            saleSummaries.sumOf { it.OrderDiscountComp },
                            limitLength = true
                        )
                    ),
                    mutableListOf(
                        "Refund",
                        saleSummaries.sumOf { it.QuantityRefund.toInt() }.toString(),
                        PriceUtils.formatStringPrice(
                            saleSummaries.sumOf { it.OrderRefund },
                            limitLength = true
                        )
                    ),
                    mutableListOf(
                        "Net Sales",
                        "",
                        PriceUtils.formatStringPrice(
                            saleSummaries.sumOf { it.NetSales },
                            limitLength = true
                        )
                    ),
                    mutableListOf(
                        "Taxes",
                        "",
                        PriceUtils.formatStringPrice(
                            saleSummaries.sumOf { it.TaxFee },
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
        device.drawText(
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
        reportSalesModel?.Cash ?: return
        printSectionHeader("*** Cash Drawer ***")

        device.drawText(
            WaguUtils.columnListDataBlock(
                charPerLineNormal,
                mutableListOf(
                    mutableListOf(
                        "Starting Cash",
                        PriceUtils.formatStringPrice(
                            reportSalesModel.Cash.sumOf { it.StartingCash ?: 0.0 } ?: 0.0,
                        )
                    ),
                    mutableListOf(
                        "Paid In",
                        PriceUtils.formatStringPrice(
                            reportSalesModel.Cash.sumOf { it.PaidIn ?: 0.0 } ?: 0.0,
                        )
                    ),
                    mutableListOf(
                        "Paid Out",
                        PriceUtils.formatStringPrice(
                            reportSalesModel.Cash.sumOf { it.PaidOut ?: 0.0 } ?: 0.0,
                        )
                    ),
                    mutableListOf(
                        "Cash Sales",
                        PriceUtils.formatStringPrice(
                            reportSalesModel.Cash.sumOf { it.CashSales ?: 0.0 } ?: 0.0,
                        )
                    ),
                    mutableListOf(
                        "Cash Refunds",
                        PriceUtils.formatStringPrice(
                            reportSalesModel.Cash.sumOf { it.CashRefunds ?: 0.0 } ?: 0.0,
                        )
                    ),
                    mutableListOf(
                        "Expected In Drawer",
                        PriceUtils.formatStringPrice(
                            reportSalesModel?.Cash.sumOf { it.ExpectedInDrawer ?: 0.0 } ?: 0.0,
                        )
                    ),
                    mutableListOf(
                        "Actual In Drawer",
                        PriceUtils.formatStringPrice(
                            reportSalesModel.Cash.sumOf { it.ActualInDrawer ?: 0.0 } ?: 0.0,
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
        device.drawText(
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


        val reportItems = ReportHelper.feeDetailReport(reportSalesModel ?: return, type)
        if (reportItems.isEmpty()) return
        printSectionHeader(header)

        val rows = reportItems.map {
            mutableListOf(
                it.name ?: "",
                (it.qty ?: 0).toString(),
                PriceUtils.formatStringPrice(it.amount, true)
            )
        }


        device.drawText(
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
                    threeColumnsAlignRightSize,
                wrapType = WrapType.SOFT_WRAP,
            ),
            size = BasePrinterManager.FontSize.Small
        )

        printDivider()

        device.drawText(
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
            true,
        )
    }

    private fun printDiningOptionDetail() {
        printThreeColumnReport(
            "*** Dining Options Detail ***",
            SaleOptionPage.DiningOptions,
            true,
        )
    }

    private fun printCashVoucherDetail() {
        printThreeColumnReport(
            "*** Cash Vouchers Detail ***",
            SaleOptionPage.CashVoucher,
            true,
        )
    }

    private fun printCompDetail() {
        printThreeColumnReport(
            "*** Comp Detail ***",
            SaleOptionPage.Comps,
            true
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
        val aligns = mutableListOf(
            Block.DATA_TOP_LEFT,
            Block.DATA_CENTER,
            Block.DATA_TOP_RIGHT,
            Block.DATA_TOP_RIGHT
        )

        val columnSize = mutableListOf(charPerLineNormal - 4 - 9 - 7, 4, 9, 7)


        val reportList =
            ReportHelper.feeDetailReport(
                reportSalesModel ?: return,
                type = SaleOptionPage.CategorySales
            )

        if (reportList.isEmpty()) return
        printSectionHeader("*** Category Sales ***")
        feedLines(3)

        device.drawText(
            WaguUtils.columnListDataBlock(
                charPerLineNormal,
                mutableListOf(
                    mutableListOf(
                        "Name",
                        "Qty",
                        "Amount",
                        "Rate"
                    )
                ),
                aligns = aligns,
                columnSize = columnSize
            ),
            size = BasePrinterManager.FontSize.Small,
        )


        reportList.forEach {
            device.drawText(
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

            device.drawText(
                WaguUtils.columnListDataBlock(
                    charPerLineNormal,
                    mutableListOf(
                        *productList.toTypedArray()
                    ),
                    aligns = aligns,
                    columnSize = columnSize,
                    wrapType = WrapType.SOFT_WRAP,
                )
            )

            printDivider()

            device.drawText(
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
                    aligns = aligns,
                    columnSize = columnSize,
                ),
            )

            feedLines(2)
        }
        printDivider()
        device.drawText(
            WaguUtils.columnListDataBlock(
                charPerLineNormal,
                mutableListOf(
                    mutableListOf(
                        "Category Total",
                        PriceUtils.formatStringPrice(reportList.sumOf { it.amount }
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
        val orderGrandTotal =
            reportSalesModel?.OrderInfo?.sumOf { it.OrderGrandTotal ?: 0.0 } ?: 0.0
        val orderNumber = reportSalesModel?.OrderInfo?.sumOf {
            it.OrderNumber ?: 0
        } ?: 1
        device.drawText(
            WaguUtils.columnListDataBlock(
                charPerLineNormal,
                mutableListOf(
                    mutableListOf(
                        "Bill Count  (${orderNumber})",
                        PriceUtils.formatStringPrice(orderGrandTotal)
                    ),
                    mutableListOf(
                        "Bill Average",
                        PriceUtils.formatStringPrice(orderGrandTotal / orderNumber)
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