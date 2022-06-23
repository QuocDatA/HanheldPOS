package com.hanheldpos.ui.screens.menu.report.sale.customize

import android.view.View
import android.widget.AdapterView
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentCustomizeReportBinding
import com.hanheldpos.extension.setOnClickDebounce
import com.hanheldpos.model.report.SaleReportFilter
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.home.DropDownItem
import com.hanheldpos.ui.screens.main.adapter.SubSpinnerAdapter
import com.hanheldpos.utils.DateTimeUtils
import kotlinx.coroutines.CoroutineScope

class CustomizeReportFragment(
    private val saleReportCustomData: SaleReportFilter,
    private val listener: CustomizeReportCallBack
) :
    BaseFragment<FragmentCustomizeReportBinding, CustomizeReportVM>(), CustomizeReportUV {

    private lateinit var startSpinnerAdapter: SubSpinnerAdapter
    private lateinit var endSpinnerAdapter: SubSpinnerAdapter

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
        startSpinnerAdapter = SubSpinnerAdapter(requireContext())
        binding.spinnerStart.adapter = startSpinnerAdapter
        endSpinnerAdapter = SubSpinnerAdapter(requireContext())
        binding.spinnerEnd.adapter = endSpinnerAdapter
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
            saleReportCustomData = SaleReportFilter(
                startDay = (DateTimeUtils.strToDate(
                    "${viewModel.startDay.year}-${viewModel.startDay.month}-${viewModel.startDay.day}T${viewModel.startTime.value ?: "00:00"}:00",
                    DateTimeUtils.Format.FULL_DATE_UTC_NOT_MILI
                )),
                endDay = (DateTimeUtils.strToDate(
                    "${viewModel.endDay.year}-${viewModel.endDay.month}-${viewModel.endDay.day}T${viewModel.endTime.value ?: "00:00"}:00",
                    DateTimeUtils.Format.FULL_DATE_UTC_NOT_MILI
                )),
                isAllDay = viewModel.isAllDay.value!!,
                isAllDevice = viewModel.isAllDevice.value!!,
                isCurrentDrawer = viewModel.isCurrentDrawer.value!!,
                startTime = viewModel.startTime.value,
                endTime = viewModel.endTime.value
            )
        )
    }

    override fun initData() {

        viewModel.initCustomReportData(
            saleReportCustomData
        )

        binding.currentDrawerCheckbox.isChecked = saleReportCustomData.isCurrentDrawer

        binding.btnIsAllDay.isChecked = saleReportCustomData.isAllDay

        if (viewModel.startDay.isBefore(viewModel.endDay)) {
            binding.calendarView.selectRange(
                viewModel.startDay, viewModel.endDay
            )
        } else {
            binding.calendarView.selectedDate = viewModel.startDay
        }


        viewModel.getTimeDayList().let { list ->
            startSpinnerAdapter.submitList(list)
            endSpinnerAdapter.submitList(list)
            viewModel.isAllDay.observe(this) {
                if (!it) return@observe
                try {
                    binding.spinnerStart.setSelection(0)
                    binding.spinnerEnd.setSelection(0)
                } catch (e: Exception) {
                }

            }
            binding.spinnerStart.setSelection(
                list.find { item -> item.name == saleReportCustomData.startTime }?.position ?: 0
            )

            binding.spinnerEnd.setSelection(
                list.find { item -> item.name == saleReportCustomData.endTime }?.position ?: 0
            )
        }
    }

    override fun initAction() {
        binding.btnDone.setOnClickDebounce {
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

        binding.spinnerStart.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    id: Long
                ) {
                    val item = parent?.getItemAtPosition(position) as DropDownItem
                    viewModel.startTime.postValue(item.name)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }
        binding.spinnerEnd.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    id: Long
                ) {
                    val item = parent?.getItemAtPosition(position) as DropDownItem
                    viewModel.endTime.postValue(item.name)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }
    }

    override fun getBack() {
        onFragmentBackPressed()
    }

    interface CustomizeReportCallBack {
        fun onComplete(
            saleReportCustomData: SaleReportFilter
        )

    }
}