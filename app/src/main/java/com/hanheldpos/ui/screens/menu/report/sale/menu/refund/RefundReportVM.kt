package com.hanheldpos.ui.screens.menu.report.sale.menu.refund

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.report.CompReport
import com.hanheldpos.data.api.pojo.report.RefundReport
import com.hanheldpos.model.menu.report.ReportItem
import com.hanheldpos.model.menu.report.ReportItemDetail
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.PriceUtils

class RefundReportVM : BaseUiViewModel<RefundReportUV>() {
    val isShowDetail = MutableLiveData(false)

    fun getRefundSummary(refunds: List<RefundReport>?): List<Any> {
        var total = 0.0
        val list = refunds?.map { refund ->
            total += refund.RefundAmount ?: 0.0
            ReportItem(
                PriceUtils.formatStringPrice(refund.RefundAmount ?: 0.0),
                refund.RefundTypeName
            )
        }
        return mutableListOf<Any>(
            total,
            list ?: mutableListOf<ReportItem>()
        )
    }

    fun getRefundDetail(context : Context, refunds: List<RefundReport>?): List<ReportItemDetail> {
        var totalQty : Long = 0
        var totalAmount = 0.0
        val rows = mutableListOf<ReportItemDetail>()
        refunds?.map { refund ->
            totalQty += refund.Quantity ?: 0
            totalAmount += refund.RefundAmount ?: 0.0
            ReportItemDetail(
                refund.RefundTypeName,
                refund.Quantity?.toInt(),
                refund.RefundAmount?: 0.0
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