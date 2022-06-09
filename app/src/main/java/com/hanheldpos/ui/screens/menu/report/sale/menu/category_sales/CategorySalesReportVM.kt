package com.hanheldpos.ui.screens.menu.report.sale.menu.category_sales

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.report.CategoryReport
import com.hanheldpos.model.menu.report.ReportItem
import com.hanheldpos.model.menu.report.ReportItemDetail
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.PriceUtils

class CategorySalesReportVM : BaseUiViewModel<CategorySalesReportUV>() {
    val isShowDetail = MutableLiveData(false)

    fun getCategorySalesSummary(category: List<CategoryReport>?): List<Any> {
        var total = 0.0
        val list = category?.map { i ->
            total += i.SubTotal ?: 0.0
            i.AllCategory.map { item ->
                ReportItem(
                    PriceUtils.formatStringPrice(item.SubTotal ?: 0.0),
                    item.CateName
                )
            }
        }?.flatten()
        return mutableListOf<Any>(
            total,
            list ?: mutableListOf<ReportItem>()
        )
    }

    fun getCategorySalesDetail(
        context: Context,
        category: List<CategoryReport>?
    ): List<ReportItemDetail> {
        var totalQty: Long = 0
        var totalAmount = 0.0
        val rows = mutableListOf<ReportItemDetail>()
        category?.map { i ->
            totalQty += i.Quantity.toLong()
            totalAmount += i.SubTotal
            i.AllCategory.map { item ->
                val items = mutableListOf<ReportItemDetail>()
                items.add(
                    ReportItemDetail(
                        item.CateName,
                        item.Quantity,
                        i.SubTotal
                    )
                )
                items.addAll(item.Product.map { pro ->
                    ReportItemDetail(
                        "       ${pro.ProductName}",
                        pro.Quantity,
                        pro.SubTotal,
                        isGray = true
                    )
                })
                items
            }.flatten()
        }?.flatten()?.toMutableList()?.let {
            rows.addAll(it)
        }
        rows.add(
            ReportItemDetail(
                context.getString(R.string.total),
                totalQty.toInt(),
                totalAmount,
                isBold = true
            )
        )
        return rows
    }
}