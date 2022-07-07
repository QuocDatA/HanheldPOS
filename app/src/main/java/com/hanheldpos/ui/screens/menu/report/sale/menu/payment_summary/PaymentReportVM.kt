package com.hanheldpos.ui.screens.menu.report.sale.menu.payment_summary

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.report.OrderPayment
import com.hanheldpos.model.menu.report.ReportItem
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.widgets.TableLayoutFixedHeader
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
        return mutableListOf(total, items)
    }


    fun getPaymentHeaders(context: Context): List<TableLayoutFixedHeader.Title> {
        return mutableListOf(
            TableLayoutFixedHeader.Title(context.getString(R.string.name)),
            TableLayoutFixedHeader.Title(context.getString(R.string.qty)),
            TableLayoutFixedHeader.Title(context.getString(R.string.amount)),
            TableLayoutFixedHeader.Title(context.getString(R.string.r_qty)),
            TableLayoutFixedHeader.Title(context.getString(R.string.r_amount)),
            TableLayoutFixedHeader.Title(context.getString(R.string.total)),
        )
    }

    fun getPaymentRows(
        context: Context,
        payments: List<OrderPayment>?
    ): List<TableLayoutFixedHeader.Row> {
        var totalQty = 0
        var totalAmount = 0.0
        var totalRQty = 0
        var totalRAmount = 0.0
        var total = 0.0
        val rows = mutableListOf<TableLayoutFixedHeader.Row>()

        payments?.forEach { payment ->
            totalQty += payment.Quantity
            totalAmount += payment.PaymentAmount
            totalRQty += payment.QuantityRefund
            totalRAmount += payment.RefundAmount
            total += payment.PaymentAmount - payment.RefundAmount

            rows.add(
                TableLayoutFixedHeader.Row(
                    mutableListOf(
                        TableLayoutFixedHeader.Title(payment.PaymentMethod),
                        TableLayoutFixedHeader.Title(payment.Quantity.toString()),
                        TableLayoutFixedHeader.Title(
                            PriceUtils.formatStringPrice(payment.PaymentAmount)
                        ),
                        TableLayoutFixedHeader.Title(payment.QuantityRefund.toString()),
                        TableLayoutFixedHeader.Title(PriceUtils.formatStringPrice(payment.RefundAmount)),
                        TableLayoutFixedHeader.Title(PriceUtils.formatStringPrice(payment.PaymentAmount - payment.RefundAmount))
                    )
                )

            )
        }

        rows.add(
            TableLayoutFixedHeader.Row(
                mutableListOf(
                    TableLayoutFixedHeader.Title(context.getString(R.string.total)),
                    TableLayoutFixedHeader.Title(totalQty.toString()),
                    TableLayoutFixedHeader.Title(PriceUtils.formatStringPrice(totalAmount)),
                    TableLayoutFixedHeader.Title(totalRQty.toString()),
                    TableLayoutFixedHeader.Title(PriceUtils.formatStringPrice(totalRAmount)),
                    TableLayoutFixedHeader.Title(PriceUtils.formatStringPrice(total))
                )
            )
        )
        return rows
    }
}