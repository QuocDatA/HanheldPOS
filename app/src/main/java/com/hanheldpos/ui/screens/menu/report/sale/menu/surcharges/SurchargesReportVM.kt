package com.hanheldpos.ui.screens.menu.report.sale.menu.surcharges

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.report.CompReport
import com.hanheldpos.data.api.pojo.report.FeeReport
import com.hanheldpos.model.menu.report.ReportItem
import com.hanheldpos.model.menu.report.ReportItemDetail
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.PriceUtils

class SurchargesReportVM : BaseUiViewModel<SurchargesReportUV>() {
    val isShowDetail = MutableLiveData(false)

    fun getSurchargesSummary(surcharges : List<FeeReport>?): List<Any> {
        var total = 0.0
        val list = surcharges?.map { surcharge ->
            total += surcharge.Amount ?: 0.0
            ReportItem(
                PriceUtils.formatStringPrice(surcharge.Amount ?: 0.0),
                surcharge.FeeName
            )
        }
        return mutableListOf<Any>(
            total,
            list ?: mutableListOf<ReportItem>()
        )
    }

    fun getSurchargesDetail(context : Context, surcharges : List<FeeReport>?): List<ReportItemDetail> {
        var totalQty : Long = 0
        var totalAmount = 0.0
        val rows = mutableListOf<ReportItemDetail>()
        surcharges?.map { surcharge ->
            totalQty += surcharge.Quantity ?: 0
            totalAmount += surcharge.Amount ?: 0.0
            ReportItemDetail(
                surcharge.FeeName,
                surcharge.Quantity?.toInt(),
                surcharge.Amount?: 0.0
            )
        }?.toMutableList()?.let {
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