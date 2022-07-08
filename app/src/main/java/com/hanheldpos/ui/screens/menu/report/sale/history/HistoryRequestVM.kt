package com.hanheldpos.ui.screens.menu.report.sale.history

import android.content.Context
import android.graphics.Color
import com.hanheldpos.R
import com.hanheldpos.model.report.ReportFilterModel
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.widgets.TableLayoutFixedHeader
import com.hanheldpos.utils.DateTimeUtils

class HistoryRequestVM : BaseUiViewModel<HistoryRequestUV>() {
    fun onFragmentBackPressed() {
        uiCallback?.onFragmentBackPressed()
    }

    fun getRequestHeaders(context: Context): MutableList<TableLayoutFixedHeader.Title> {
        return mutableListOf(
            TableLayoutFixedHeader.Title(context.getString(R.string.request_date)),
            TableLayoutFixedHeader.Title(context.getString(R.string.start_day)),
            TableLayoutFixedHeader.Title(context.getString(R.string.end_day)),
            TableLayoutFixedHeader.Title(context.getString(R.string.device)),
            TableLayoutFixedHeader.Title(context.getString(R.string.status)),
        )
    }

    fun getRequestData(
        context: Context,
        reports: List<ReportFilterModel>?
    ): List<TableLayoutFixedHeader.Row> {
        return reports?.map {
            TableLayoutFixedHeader.Row(
                mutableListOf(
                    TableLayoutFixedHeader.Title(
                        DateTimeUtils.strToStr(
                            it.requestDate,
                            DateTimeUtils.Format.FULL_DATE_UTC_TIMEZONE,
                            DateTimeUtils.Format.REPORT_TIME,
                        ),
                    ),
                    TableLayoutFixedHeader.Title(
                        DateTimeUtils.strToStr(
                            it.startDay,
                            DateTimeUtils.Format.YYYY_MM_DD,
                            DateTimeUtils.Format.DD_MM_YYYY,
                        ),
                    ),
                    TableLayoutFixedHeader.Title(
                        DateTimeUtils.strToStr(
                            it.endDay,
                            DateTimeUtils.Format.YYYY_MM_DD,
                            DateTimeUtils.Format.DD_MM_YYYY,
                        ),
                    ),
                    TableLayoutFixedHeader.Title(
                        if (it.isAllDevice == true) context.getString(R.string.all_devices) else context.getString(
                            R.string.current_device,
                        ),
                    ),
                    when (it.statusId?.toInt()) {
                        0 -> TableLayoutFixedHeader.Title(
                            context.getString(R.string.in_process),
                            Color.parseColor("#F8DC28"),
                        )
                        1 -> TableLayoutFixedHeader.Title(
                            context.getString(R.string.done),
                            Color.parseColor("#12C339"),
                        )
                        else -> TableLayoutFixedHeader.Title(
                            context.getString(R.string.failed),
                            Color.parseColor("#D80028"),
                        )
                    },

                    ),
                it,
            )

        }?.sortedByDescending {
            DateTimeUtils.strToDate(
                (it.value as ReportFilterModel).requestDate,
                DateTimeUtils.Format.FULL_DATE_UTC_TIMEZONE
            )
        } ?: emptyList()
    }
}