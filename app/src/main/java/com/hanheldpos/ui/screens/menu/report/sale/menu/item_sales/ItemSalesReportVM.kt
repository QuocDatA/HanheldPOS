package com.hanheldpos.ui.screens.menu.report.sale.menu.item_sales

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.report.CashVoucherReport
import com.hanheldpos.data.api.pojo.report.ItemSale
import com.hanheldpos.model.menu.report.ReportItem
import com.hanheldpos.model.menu.report.ReportItemDetail
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.PriceUtils

class ItemSalesReportVM : BaseUiViewModel<ItemSalesReportUV>() {
    val isShowDetail = MutableLiveData(false)

    fun getItemSalesSummary(itemSales : List<ItemSale>?) : List<Any> {
        var total = 0.0
        val list = itemSales?.map { itemSale ->
            total += itemSale.TotalSales ?: 0.0
            ReportItem(
                PriceUtils.formatStringPrice(itemSale.TotalSales ?: 0.0),
                itemSale.ProductName
            )
        }
        return mutableListOf<Any>(
            total,
            list ?: mutableListOf<ReportItem>()
        )
    }

    fun getItemSalesDetail(context : Context, itemSales : List<ItemSale>?) : List<ReportItemDetail> {
        var totalQty = 0
        var totalAmount = 0.0
        val rows = mutableListOf<ReportItemDetail>()
        itemSales?.map { itemSale ->
            totalQty += itemSale.Quantity ?: 0
            totalAmount += itemSale.TotalSales ?: 0.0
            ReportItemDetail(
                itemSale.ProductName,
                itemSale.Quantity.toString() ?: "",
                PriceUtils.formatStringPrice(itemSale.TotalSales)
            )
        }?.toMutableList()?.let {
            rows.addAll(it)
        }
        rows.add(
            ReportItemDetail(
            context.getString(R.string.total),
            totalQty.toString(),
            PriceUtils.formatStringPrice(totalAmount),
            isBold = true
        )
        )
        return rows
    }
}