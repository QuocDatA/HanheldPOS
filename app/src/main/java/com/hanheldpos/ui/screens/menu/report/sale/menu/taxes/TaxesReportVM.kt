package com.hanheldpos.ui.screens.menu.report.sale.menu.taxes

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.report.CompReport
import com.hanheldpos.data.api.pojo.report.FeeReport
import com.hanheldpos.model.menu.report.ReportItem
import com.hanheldpos.model.menu.report.ReportItemDetail
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.PriceUtils

class TaxesReportVM : BaseUiViewModel<TaxesReportUV>() {
    val isShowDetail = MutableLiveData(false)

    fun getTaxesSummary(taxes: List<FeeReport>?): List<Any> {
        var total = 0.0
        val list = taxes?.map { tax ->
            total += tax.Amount ?: 0.0
            ReportItem(
                PriceUtils.formatStringPrice(tax.Amount ?: 0.0),
                tax.FeeName
            )
        }
        return mutableListOf<Any>(
            total,
            list ?: mutableListOf<ReportItem>()
        )
    }

    fun getTaxesDetail(context : Context, taxes: List<FeeReport>?): List<ReportItemDetail> {
        var totalQty : Long = 0
        var totalAmount = 0.0
        val rows = mutableListOf<ReportItemDetail>()
        taxes?.map { tax ->
            totalQty += tax.Quantity ?: 0
            totalAmount += tax.Amount ?: 0.0
            ReportItemDetail(
                tax.FeeName,
                tax.Quantity.toString(),
                PriceUtils.formatStringPrice(tax.Amount?: 0.0)
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