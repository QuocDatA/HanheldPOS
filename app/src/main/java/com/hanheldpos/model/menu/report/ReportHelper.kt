package com.hanheldpos.model.menu.report

import com.hanheldpos.data.api.pojo.report.ReportSalesResp

object ReportHelper {

    fun feeDetailReport(
        reportSalesModel: ReportSalesResp,
        type: SaleOptionPage
    ): List<ReportItemDetail> {
        return when (type) {
            SaleOptionPage.PaymentSummary -> {
                reportSalesModel.OrderPayment.map {
                    ReportItemDetail(
                        name = it.PaymentMethod,
                        qty = it.Quantity.toInt(),
                        amount = it.PaymentAmount
                    )
                }
            }
            SaleOptionPage.DiningOptions -> {
                reportSalesModel.DinningOption.map {
                    ReportItemDetail(
                        name = it.DinningOptionName,
                        qty = it.Quantity.toInt(),
                        amount = it.Total,
                    )
                }
            }
            SaleOptionPage.Discounts -> {
                reportSalesModel.DiscountOrder.map {
                    ReportItemDetail(
                        name = it.DiscountName,
                        qty = it.Quantity.toInt(),
                        amount = it.DiscountAmount ?: 0.0
                    )
                }
            }
            SaleOptionPage.CashVoucher -> {
                reportSalesModel.CashVoucher.map {
                    ReportItemDetail(
                        name = it.Title,
                        qty = it.Quantity?.toInt(),
                        amount = it.PaymentAmount ?: 0.0
                    )
                }
            }
            SaleOptionPage.CategorySales -> {
                reportSalesModel.Category.firstOrNull()?.AllCategory?.map {
                    ReportItemDetail(
                        name = it.CateName,
                        qty = it.Quantity.toInt(),
                        amount = it.SubTotal,
                        list = it.Product.map { product ->
                            ReportItemDetail(
                                name = product.ProductName,
                                qty = product.Quantity.toInt(),
                                amount = product.SubTotal,
                                subValue = "%.2f".format(product.PercentLineTotal) + "%"
                            )
                        },
                        subValue = "%.2f".format(it.PercentTotalCategory) + "%"
                    )
                } ?: mutableListOf()
            }
            SaleOptionPage.ItemSales -> {
                reportSalesModel.Item.map {
                    ReportItemDetail(
                        name = it.ProductName,
                        qty = it.Quantity.toInt(),
                        amount = it.SubTotal,
                    )
                }
            }
            SaleOptionPage.Comps -> {
                reportSalesModel.Comp.map {
                    ReportItemDetail(
                        name = it.Name,
                        qty = it.Quantity?.toInt(),
                        amount = it.Amount ?: 0.0
                    )
                }
            }
            SaleOptionPage.Refund -> {
                reportSalesModel.Refund.map {
                    ReportItemDetail(
                        name = it.RefundTypeName,
                        qty = it.Quantity?.toInt(),
                        amount = it.RefundAmount ?: 0.0,
                    )
                }
            }
            SaleOptionPage.Taxes -> {
                reportSalesModel.ListTaxFee.map {
                    ReportItemDetail(
                        name = it.FeeName,
                        qty = it.Quantity?.toInt(),
                        amount = it.Amount ?: 0.0,
                    )
                }
            }
            SaleOptionPage.Service -> {
                reportSalesModel.ListServiceFee.map {
                    ReportItemDetail(
                        name = it.FeeName,
                        qty = it.Quantity?.toInt(),
                        amount = it.Amount ?: 0.0,
                    )
                }
            }
            SaleOptionPage.Surcharge -> {
                reportSalesModel.ListSurchargeFee.map {
                    ReportItemDetail(
                        name = it.FeeName,
                        qty = it.Quantity?.toInt(),
                        amount = it.Amount ?: 0.0
                    )
                }
            }
            else -> {
                listOf()
            }
        }
    }
}