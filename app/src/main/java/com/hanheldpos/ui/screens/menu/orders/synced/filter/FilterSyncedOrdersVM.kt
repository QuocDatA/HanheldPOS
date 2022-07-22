package com.hanheldpos.ui.screens.menu.orders.synced.filter

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.order.settings.DiningOption
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.menu.orders.SyncedOrdersFilterData
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.screens.home.DropDownItem
import com.hanheldpos.utils.DateTimeUtils
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.*

class FilterSyncedOrdersVM : BaseUiViewModel<FilterSyncedOrdersUV>() {

    lateinit var startDay: CalendarDay
    lateinit var endDay: CalendarDay
    val isAllDay = MutableLiveData(false)
    val startTime  = MutableLiveData<String>()
    val endTime  = MutableLiveData<String>()
    val diningOption = MutableLiveData<DiningOption>()

    fun initFilterData(filter: SyncedOrdersFilterData?) {
        val startDayInInt = splitDate(filter?.startDay)
        val endDayInInt = splitDate(filter?.endDay)
        this.isAllDay.postValue(filter?.isAllDay)
        this.startDay = CalendarDay.from(startDayInInt[2], startDayInInt[1], startDayInInt[0])
        this.endDay = CalendarDay.from(endDayInInt[2], endDayInInt[1], endDayInInt[0])
        this.startTime.postValue(filter?.startTime)
        this.endTime.postValue(filter?.endTime)
        this.diningOption.postValue(filter?.diningOption)
    }

    private fun splitDate(day: Date? = DateTimeUtils.curDate): MutableList<Int> {
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

    fun getDiningOptionList(context : Context) : List<DiningOption> {
        val listOption = mutableListOf<DiningOption>()
        listOption.add(DiningOption(Title = context.getString(R.string.all_dining_options)))
        return listOption.apply { addAll(DataHelper.orderSettingLocalStorage?.ListDiningOptions?: mutableListOf()) }
    }

    fun onAllDaySwitch() {
        isAllDay.postValue(!(isAllDay.value ?: false))
    }

    fun onFragmentBackPressed() {
        uiCallback?.onFragmentBackPressed()
    }
}