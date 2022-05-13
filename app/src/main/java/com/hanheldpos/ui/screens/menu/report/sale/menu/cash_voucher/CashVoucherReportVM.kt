package com.hanheldpos.ui.screens.menu.report.sale.menu.cash_voucher

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.report.CashVoucherReport
import com.hanheldpos.model.menu.report.ReportItem
import com.hanheldpos.model.menu.report.ReportItemDetail
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.PriceUtils

class CashVoucherReportVM : BaseUiViewModel<CashVoucherReportUV>() {
    val isShowDetail = MutableLiveData(false)

    fun getCashVoucherSummary(cashVouchers : List<CashVoucherReport>?) : List<Any> {
        var total = 0.0
        val list = cashVouchers?.map { cashVoucher ->
            total += cashVoucher.PaymentAmount ?: 0.0
            ReportItem(
                PriceUtils.formatStringPrice(cashVoucher.PaymentAmount ?: 0.0),
                cashVoucher.Title
            )
        }
        return mutableListOf<Any>(
            total,
            list ?: mutableListOf<ReportItem>()
        )
    }

    fun getCashVoucherDetail(context : Context, cashVouchers : List<CashVoucherReport>?) : List<ReportItemDetail> {
        var totalQty = 0
        var totalAmount = 0.0
        val rows = mutableListOf<ReportItemDetail>()
        cashVouchers?.map { cashVoucher ->
            totalQty += cashVoucher.Quantity?.toInt() ?: 0
            totalAmount += cashVoucher.PaymentAmount ?: 0.0
            ReportItemDetail(
                cashVoucher.Title,
                cashVoucher.Quantity?.toInt()?.toString() ?: "",
                PriceUtils.formatStringPrice(cashVoucher.PaymentAmount?:0.0)
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