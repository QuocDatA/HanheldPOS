package com.hanheldpos.ui.screens.menu.option.report.sale.customize

import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentCustomizeReportBinding
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.utils.time.DateTimeHelper
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.*

class CustomizeReportFragment(private val listener: CustomizeReportCallBack) :
    BaseFragment<FragmentCustomizeReportBinding, CustomizeReportVM>(), CustomizeReportUV {
    override fun layoutRes() = R.layout.fragment_customize_report
    override fun viewModelClass(): Class<CustomizeReportVM> {
        return CustomizeReportVM::class.java
    }

    override fun initViewModel(viewModel: CustomizeReportVM) {
        viewModel.run {
            init(this@CustomizeReportFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {
        binding.btnDone.setOnClickListener {
            setUpReturn()
        }

        binding.calendarView.setOnDateChangedListener { _, _, _ ->
            binding.currentDrawerCheckbox.isChecked = false
            binding.btnIsAllDay.isChecked = true
        }
        binding.calendarView.setOnRangeSelectedListener { _, _ ->
            binding.currentDrawerCheckbox.isChecked = false
            binding.btnIsAllDay.isChecked = true
        }
    }

    private fun setUpReturn() {
        navigator.goOneBack()
        var startDay = CalendarDay.today()
        var endDay = startDay

        binding.calendarView.let {
            if (it.selectedDate != null) {
                endDay = it.selectedDate!!
                startDay = if (it.selectedDates.size > 1) it.selectedDates[0] else endDay
            }
        }
        listener.onComplete(
            startDay = (DateTimeHelper.strToDate(
                "${startDay.year}-${startDay.month}-${startDay.day}T00:00:00",
                DateTimeHelper.Format.FULL_DATE_UTC_NOT_MILI
            )),
            endDay = (DateTimeHelper.strToDate(
                "${endDay.year}-${endDay.month}-${endDay.day}T00:00:00",
                DateTimeHelper.Format.FULL_DATE_UTC_NOT_MILI
            )),
            isAllDay = viewModel.isAllDay.value!!,
            isAllDevice = viewModel.isAllDevice.value!!,
            isCurrentDrawer = viewModel.isCurrentDrawer.value!!,
            startTime = "",
            endTime = ""
        )
    }

    override fun initData() {
        binding.calendarView.selectedDate = CalendarDay.today()
    }

    override fun initAction() {

    }

    override fun getBack() {
        onFragmentBackPressed()
        listener.onCancel()
    }

    interface CustomizeReportCallBack {
        fun onComplete(
            startDay: Date?,
            endDay: Date?,
            isAllDay: Boolean,
            startTime: String,
            endTime: String,
            isAllDevice: Boolean,
            isCurrentDrawer: Boolean
        )

        fun onCancel()
    }
}