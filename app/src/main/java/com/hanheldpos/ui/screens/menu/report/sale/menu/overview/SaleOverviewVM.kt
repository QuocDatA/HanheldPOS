package com.hanheldpos.ui.screens.menu.report.sale.menu.overview

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.data.api.pojo.report.SalesSummary
import com.hanheldpos.model.menu.report.ReportItem
import com.hanheldpos.model.menu.report.ReportItemDetail
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.PriceUtils

class SaleOverviewVM : BaseUiViewModel<SaleOverviewUV>() {
    val isShowDetail = MutableLiveData(false)
    fun getReportItems(salesSummary: SalesSummary?): List<ReportItem> {
        salesSummary ?: return emptyList()
        return mutableListOf(
            ReportItem(
                title = PriceUtils.formatStringPrice(salesSummary.GrossSale),
                sub = "Gross Sales"
            ),
            ReportItem(
                title = PriceUtils.formatStringPrice(salesSummary.NetSales),
                sub = "Net Sales"
            ),
            ReportItem(
                title = PriceUtils.formatStringPrice(salesSummary.TotalSales),
                sub = "Total Sales"
            ),
            ReportItem(
                title = PriceUtils.formatStringPrice(salesSummary.QuantityBillCount),
                sub = "Bill Count"
            ),
            ReportItem(
                title = PriceUtils.formatStringPrice(salesSummary.OrderDiscount),
                sub = "Discount"
            ),
            ReportItem(
                title = PriceUtils.formatStringPrice(salesSummary.OrderRefund),
                sub = "Refund"
            ),
        )
    }
    fun getReportItemsDetail(salesSummary: SalesSummary?) : List<ReportItemDetail> {
        salesSummary?: return emptyList()
        return mutableListOf(
            ReportItemDetail(
                name = "Order",
                qty = salesSummary.QuantityBillCount.toInt().toString(),
                amount = PriceUtils.formatStringPrice(salesSummary.OrderAmount)
            ),
            ReportItemDetail(
                name = "Cover",
                qty = salesSummary.Cover.toInt().toString(),
                amount = "0"
            ),
            ReportItemDetail(
                name = "Service Charge",
                qty = "",
                amount = PriceUtils.formatStringPrice(salesSummary.ServiceCharge)
            ),
            ReportItemDetail(
                name = "Discount & Comp",
                qty = "",
                amount = PriceUtils.formatStringPrice(salesSummary.OrderDiscountComp)
            ),
            ReportItemDetail(
                name = "Refund",
                qty = salesSummary.QuantityRefund.toInt().toString(),
                amount = PriceUtils.formatStringPrice(salesSummary.OrderRefund)
            ),
            ReportItemDetail(
                name = "Net Sales",
                qty = "",
                amount = PriceUtils.formatStringPrice(salesSummary.NetSales),
                isBold = true
            ),
            ReportItemDetail(
                name = "Taxes",
                qty = "",
                amount = PriceUtils.formatStringPrice(salesSummary.TaxFee),
            ),
            ReportItemDetail(
                name = "Service Fee",
                qty = "",
                amount = PriceUtils.formatStringPrice(salesSummary.ServiceFee),
            ),
            ReportItemDetail(
                name = "Surcharge",
                qty = "",
                amount = PriceUtils.formatStringPrice(salesSummary.SurchargeFee),
            ),
            ReportItemDetail(
                name = "Total Sales",
                qty = "",
                amount = PriceUtils.formatStringPrice(salesSummary.TotalSales),
                isBold = true
            ),
            ReportItemDetail(
                name = "Bill Count",
                qty = salesSummary.QuantityBillCount.toInt().toString(),
                amount = PriceUtils.formatStringPrice(salesSummary.BillCount),
                isBold = true
            ),
            ReportItemDetail(
                name = "Bill Average (Gross)",
                qty = "",
                amount = PriceUtils.formatStringPrice(salesSummary.BillAverageGrossSale),
            ),
            ReportItemDetail(
                name = "Bill Average (Net)",
                qty = "",
                amount = PriceUtils.formatStringPrice(salesSummary.BillAverageNetSale),
            ),
        )
    }
}