package com.hanheldpos.ui.screens.menu.report.sale.menu.payment_summary

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.report.OrderPayment
import com.hanheldpos.model.menu.report.ReportItem
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.PriceUtils

class PaymentReportVM : BaseUiViewModel<PaymentReportUV>() {
    val isShowDetail = MutableLiveData(false)

    fun getPaymentSummary(context: Context, payments: List<OrderPayment>?): MutableList<Any> {
        val items = mutableListOf<ReportItem>()
        var total = 0.0
        payments?.forEach { payment ->
            total += payment.PaymentAmount - payment.RefundAmount

            items.add(
                ReportItem(
                    payment.PaymentMethod,
                    PriceUtils.formatStringPrice(payment.PaymentAmount - payment.RefundAmount)
                )
            )
        }
        return mutableListOf(total,items)
    }


    fun getPaymentHeaders(context: Context): Collection<String> {
        return mutableListOf(
            context.getString(R.string.name),
            context.getString(R.string.qty),
            context.getString(R.string.amount),
            context.getString(R.string.r_qty),
            context.getString(R.string.r_amount),
            context.getString(R.string.total),
        )
    }

    fun getPaymentRows(context: Context, payments: List<OrderPayment>?): Collection<Collection<String>> {
        var totalQty = 0
        var totalAmount = 0.0
        var totalRQty = 0
        var totalRAmount = 0.0
        var total = 0.0
        val rows = mutableListOf<Collection<String>>()

        payments?.forEach { payment ->
            totalQty += payment.Quantity
            totalAmount += payment.PaymentAmount
            totalRQty += payment.QuantityRefund
            totalRAmount += payment.RefundAmount
            total += payment.PaymentAmount - payment.RefundAmount

            rows.add(
                mutableListOf(
                    payment.PaymentMethod,
                    payment.Quantity.toString(),
                    PriceUtils.formatStringPrice(payment.PaymentAmount),
                    payment.QuantityRefund.toString(),
                    PriceUtils.formatStringPrice(payment.RefundAmount),
                    PriceUtils.formatStringPrice(payment.PaymentAmount - payment.RefundAmount)
                )
            )
        }

        rows.add(
            mutableListOf(
                context.getString(R.string.total),
                totalQty.toString(),
                PriceUtils.formatStringPrice(totalAmount),
                totalRQty.toString(),
                PriceUtils.formatStringPrice(totalRAmount),
                PriceUtils.formatStringPrice(total)
            )
        )
        return rows
    }
}