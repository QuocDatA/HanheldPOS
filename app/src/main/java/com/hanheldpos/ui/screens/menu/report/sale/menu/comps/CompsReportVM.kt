package com.hanheldpos.ui.screens.menu.report.sale.menu.comps

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.report.CompReport
import com.hanheldpos.data.api.pojo.report.DinningOptionReport
import com.hanheldpos.model.menu.report.ReportItem
import com.hanheldpos.model.menu.report.ReportItemDetail
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.PriceUtils

class CompsReportVM : BaseUiViewModel<CompsReportUV>() {
    val isShowDetail = MutableLiveData(false)

    fun getCompsSummary(comps: List<CompReport>?): List<Any> {
        var total = 0.0
        val list = comps?.map { comp ->
            total += comp.Amount ?: 0.0
            ReportItem(
                PriceUtils.formatStringPrice(comp.Amount ?: 0.0),
                comp.Name
            )
        }
        return mutableListOf<Any>(
            total,
            list ?: mutableListOf<ReportItem>()
        )
    }

    fun getCompsDetail(context : Context, comps: List<CompReport>?): List<ReportItemDetail> {
        var totalQty : Long = 0
        var totalAmount = 0.0
        val rows = mutableListOf<ReportItemDetail>()
        comps?.map { comp ->
            totalQty += comp.Quantity ?: 0
            totalAmount += comp.Amount ?: 0.0
            ReportItemDetail(
                comp.Name,
                comp.Quantity.toString(),
                PriceUtils.formatStringPrice(comp.Amount?: 0.0)
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