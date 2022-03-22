package com.hanheldpos.ui.screens.menu.option.report.sale.customize

import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentCustomizeReportBinding
import com.hanheldpos.model.report.SaleReportCustomData
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.utils.time.DateTimeUtils
import java.util.*

class CustomizeReportFragment(
    private val saleReportCustomData: SaleReportCustomData,
    private val listener: CustomizeReportCallBack
) :
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
        viewModel.initCustomReportData(
            saleReportCustomData
        )

        binding.btnDone.setOnClickListener {
            setUpReturn()
        }

        binding.currentDrawerCheckbox.isChecked = saleReportCustomData.isCurrentDrawer
        binding.btnIsAllDay.isChecked = saleReportCustomData.isAllDay

        if (viewModel.startDay.isBefore(viewModel.endDay)) {
            binding.calendarView.selectRange(
                viewModel.startDay, viewModel.endDay
            )
        } else {
            binding.calendarView.selectedDate = viewModel.startDay
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

        binding.calendarView.let {
            if (it.selectedDate != null) {
                viewModel.endDay = it.selectedDate!!
                viewModel.startDay =
                    if (it.selectedDates.size > 1) it.selectedDates[0] else viewModel.endDay
            }
        }
        listener.onComplete(
            saleReportCustomData = SaleReportCustomData(
                startDay = (DateTimeUtils.strToDate(
                    "${viewModel.startDay.year}-${viewModel.startDay.month}-${viewModel.startDay.day}T00:00:00",
                    DateTimeUtils.Format.FULL_DATE_UTC_NOT_MILI
                )),
                endDay = (DateTimeUtils.strToDate(
                    "${viewModel.endDay.year}-${viewModel.endDay.month}-${viewModel.endDay.day}T00:00:00",
                    DateTimeUtils.Format.FULL_DATE_UTC_NOT_MILI
                )),
                isAllDay = viewModel.isAllDay.value!!,
                isAllDevice = viewModel.isAllDevice.value!!,
                isCurrentDrawer = viewModel.isCurrentDrawer.value!!,
                startTime = "",
                endTime = ""
            )
        )
    }

    private fun convertDateToLocalDate(dateStr: String) {

    }

    override fun initData() {
    }

    override fun initAction() {

    }

    override fun getBack() {
        onFragmentBackPressed()
    }

    interface CustomizeReportCallBack {
        fun onComplete(
            saleReportCustomData: SaleReportCustomData
        )

    }
}