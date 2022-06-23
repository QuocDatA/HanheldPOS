package com.hanheldpos.ui.screens.menu.report.sale.customize

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.model.report.SaleReportFilter
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.screens.home.DropDownItem
import com.hanheldpos.ui.screens.main.adapter.SubSpinnerAdapter
import com.hanheldpos.utils.DateTimeUtils
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.*

class CustomizeReportVM : BaseUiViewModel<CustomizeReportUV>() {

    lateinit var startDay: CalendarDay
    lateinit var endDay: CalendarDay
    val startTime  = MutableLiveData<String>()
    val endTime  = MutableLiveData<String>()
    var isCurrentDrawer = MutableLiveData(true)
    var isAllDay = MutableLiveData(false)
    var isAllDevice = MutableLiveData(false)
    var isThisDevice = MutableLiveData(true)

    fun initCustomReportData(saleReportCustomData: SaleReportFilter) {
        val startDayInInt = splitDate(saleReportCustomData.startDay!!)
        val endDayInInt = splitDate(saleReportCustomData.endDay!!)
        this.isCurrentDrawer.postValue(saleReportCustomData.isCurrentDrawer)
        this.isAllDay.postValue(saleReportCustomData.isAllDay)
        this.isAllDevice.postValue(saleReportCustomData.isAllDevice)
        this.isThisDevice.postValue(!saleReportCustomData.isAllDevice)
        this.startDay = CalendarDay.from(startDayInInt[2], startDayInInt[1], startDayInInt[0])
        this.endDay = CalendarDay.from(endDayInInt[2], endDayInInt[1], endDayInInt[0])
    }

    private fun splitDate(day: Date): MutableList<Int> {
        val dayStr = DateTimeUtils.dateToString(day, DateTimeUtils.Format.DD_MM_YYYY)
        val dayStrSplit = dayStr.split("/")
        val dateInInt: MutableList<Int> = mutableListOf()
        for (element in dayStrSplit) {
            dateInInt.add(element.toInt())
        }
        return dateInInt
    }

    fun getTimeDayList(): List<DropDownItem> {
        return  (0..23).map {
            val timeString = "${it.toString().padStart(2,'0')}:00"
            val time = DateTimeUtils.strToDate(timeString,DateTimeUtils.Format.HH_mm)
            DropDownItem(name = timeString, position = it, realItem = time)
        }
    }

    fun onDrawerCheckChange() {
        isCurrentDrawer.postValue(!isCurrentDrawer.value!!)
    }

    fun onAllDaySwitch() {
        isAllDay.postValue(!isAllDay.value!!)
    }

    fun onDeviceCheckChange() {
        isAllDevice.postValue(!isAllDevice.value!!)
        isThisDevice.postValue(!isThisDevice.value!!)
    }

    fun backPress() {
        uiCallback?.getBack()
    }
}