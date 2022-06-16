package com.hanheldpos.ui.screens.menu.report.sale.menu.services

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.report.FeeReport
import com.hanheldpos.model.menu.report.ReportItem
import com.hanheldpos.model.menu.report.ReportItemDetail
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.PriceUtils

class ServicesReportVM : BaseUiViewModel<ServicesReportUV>() {
    val isShowDetail = MutableLiveData(false)

    fun getServicesSummary(services: List<FeeReport>?): List<Any> {
        var total = 0.0
        val list = services?.map { service ->
            total += service.Amount ?: 0.0
            ReportItem(
                PriceUtils.formatStringPrice(service.Amount ?: 0.0),
                service.FeeName
            )
        }
        return mutableListOf<Any>(
            total,
            list ?: mutableListOf<ReportItem>()
        )
    }

    fun getServicesDetail(context : Context, services: List<FeeReport>?): List<ReportItemDetail> {
        var totalQty : Long = 0
        var totalAmount = 0.0
        val rows = mutableListOf<ReportItemDetail>()
        services?.map { services ->
            totalQty += services.Quantity ?: 0
            totalAmount += services.Amount ?: 0.0
            ReportItemDetail(
                services.FeeName,
                services.Quantity?.toInt(),
                services.Amount?: 0.0
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