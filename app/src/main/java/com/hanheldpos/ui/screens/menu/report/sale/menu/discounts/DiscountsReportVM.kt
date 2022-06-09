package com.hanheldpos.ui.screens.menu.report.sale.menu.discounts

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.report.CashVoucherReport
import com.hanheldpos.data.api.pojo.report.DiscountOrderReport
import com.hanheldpos.model.menu.report.ReportItem
import com.hanheldpos.model.menu.report.ReportItemDetail
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.PriceUtils

class DiscountsReportVM : BaseUiViewModel<DiscountsReportUV>() {
    val isShowDetail = MutableLiveData(false)

    fun getDiscountsSummary(discounts : List<DiscountOrderReport>?) : List<Any> {
        var total = 0.0
        val list = discounts?.map { discount ->
            total += discount.DiscountAmount ?: 0.0
            ReportItem(
                PriceUtils.formatStringPrice(discount.DiscountAmount ?: 0.0),
                discount.DiscountName
            )
        }
        return mutableListOf<Any>(
            total,
            list ?: mutableListOf<ReportItem>()
        )
    }

    fun getDiscountsDetail(context : Context, discounts : List<DiscountOrderReport>?) : List<ReportItemDetail> {
        var totalQty = 0
        var totalAmount = 0.0
        val rows = mutableListOf<ReportItemDetail>()
        discounts?.map { discount ->
            totalQty += discount.Quantity.toInt() ?: 0
            totalAmount += discount.DiscountAmount ?: 0.0
            ReportItemDetail(
                discount.DiscountName,
                discount.Quantity.toInt(),
                discount.DiscountAmount?:0.0
            )
        }?.toMutableList()?.let {
            rows.addAll(it)
        }
        rows.add(
            ReportItemDetail(
            context.getString(R.string.total),
            totalQty,
            totalAmount,
            isBold = true
        )
        )
        return rows
    }
}