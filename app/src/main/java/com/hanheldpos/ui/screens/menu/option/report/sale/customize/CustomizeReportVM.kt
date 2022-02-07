package com.hanheldpos.ui.screens.menu.option.report.sale.customize

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.model.report.SaleReportCustomData
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.time.DateTimeHelper
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.*

class CustomizeReportVM : BaseUiViewModel<CustomizeReportUV>() {

    lateinit var startDay: CalendarDay
    lateinit var endDay: CalendarDay
    var isCurrentDrawer = MutableLiveData<Boolean>(true);
    var isAllDay = MutableLiveData<Boolean>(false);
    var isAllDevice = MutableLiveData<Boolean>(false)
    var isThisDevice = MutableLiveData<Boolean>(true)

    fun initCustomReportData(saleReportCustomData: SaleReportCustomData) {
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
        val dayStr = DateTimeHelper.dateToString(day, DateTimeHelper.Format.DD_MM_YYYY)
        val dayStrSplit = dayStr.split("/")
        val dateInInt: MutableList<Int> = mutableListOf()
        for (element in dayStrSplit) {
            dateInInt.add(element.toInt())
        }
        return dateInInt
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