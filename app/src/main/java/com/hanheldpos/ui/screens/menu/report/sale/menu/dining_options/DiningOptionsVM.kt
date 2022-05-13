package com.hanheldpos.ui.screens.menu.report.sale.menu.dining_options

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.report.DinningOptionReport
import com.hanheldpos.model.menu.report.ReportItem
import com.hanheldpos.model.menu.report.ReportItemDetail
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.PriceUtils

class DiningOptionsVM : BaseUiViewModel<DiningOptionsUV>() {
    val isShowDetail = MutableLiveData(false)

    fun getDiningOptionsSummary(dinningOptions: List<DinningOptionReport>?): List<Any> {
        var total = 0.0
        val list = dinningOptions?.map { dinningOptionReport ->
            total += dinningOptionReport.Total
            ReportItem(
                PriceUtils.formatStringPrice(dinningOptionReport.Total),
                dinningOptionReport.DinningOptionName
            )
        }
        return mutableListOf<Any>(
            total,
            list ?: mutableListOf<ReportItem>()
        )
    }

    fun getDinningOptionsDetail(context : Context, dinningOptions: List<DinningOptionReport>?): List<ReportItemDetail> {
        var totalQty = 0
        var totalAmount = 0.0
        val rows = mutableListOf<ReportItemDetail>()
        dinningOptions?.map { dinningOptionReport ->
            totalQty += dinningOptionReport.Quantity
            totalAmount += dinningOptionReport.Total
            ReportItemDetail(
                dinningOptionReport.DinningOptionName,
                dinningOptionReport.Quantity.toString(),
                PriceUtils.formatStringPrice(dinningOptionReport.Total)
            )
        }?.toMutableList()?.let {
            rows.addAll(it)
        }
        rows.add(ReportItemDetail(
            context.getString(R.string.total),
            totalQty.toString(),
            PriceUtils.formatStringPrice(totalAmount),
            isBold = true
        ))
        return rows
    }
}