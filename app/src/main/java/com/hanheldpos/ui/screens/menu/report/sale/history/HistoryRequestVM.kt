package com.hanheldpos.ui.screens.menu.report.sale.history

import android.content.Context
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.report.OrderPayment
import com.hanheldpos.model.report.ReportModel
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.DateTimeUtils

class HistoryRequestVM : BaseUiViewModel<HistoryRequestUV>() {
    fun onFragmentBackPressed() {
        uiCallback?.onFragmentBackPressed()
    }

    fun getRequestHeaders(context: Context): MutableList<String> {
        return mutableListOf(
            context.getString(R.string.request_date),
            context.getString(R.string.start_day),
            context.getString(R.string.end_day),
            context.getString(R.string.device),
            context.getString(R.string.status),
        )
    }

    fun getRequestData(
        context: Context,
        reports: List<ReportModel>?
    ): Collection<Collection<String>> {
        return reports?.map {
            mutableListOf(
                DateTimeUtils.strToStr(
                    it.requestDate,
                    DateTimeUtils.Format.FULL_DATE_UTC_TIMEZONE,
                    DateTimeUtils.Format.REPORT_TIME
                ),
                DateTimeUtils.strToStr(
                    it.startDay,
                    DateTimeUtils.Format.YYYY_MM_DD,
                    DateTimeUtils.Format.DD_MM_YYYY
                ),
                DateTimeUtils.strToStr(
                    it.endDay,
                    DateTimeUtils.Format.YYYY_MM_DD,
                    DateTimeUtils.Format.DD_MM_YYYY
                ),
                if (it.isAllDevice == true) context.getString(R.string.all_devices) else context.getString(
                    R.string.current_device
                ),
                when (it.statusId?.toInt()) {
                    0 -> context.getString(R.string.in_process)
                    1 -> context.getString(R.string.done)
                    else -> context.getString(R.string.failed)
                }

            )
        } ?: emptyList()
    }
}