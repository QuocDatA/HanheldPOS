package com.hanheldpos.ui.screens.menu.orders.synced.filter

import android.annotation.SuppressLint
import android.view.View
import android.widget.AdapterView
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.order.settings.DiningOption
import com.hanheldpos.databinding.FragmentFilterSyncedOrdersBinding
import com.hanheldpos.extension.addItemDecorationWithoutLastDivider
import com.hanheldpos.extension.setOnClickDebounce
import com.hanheldpos.model.menu.orders.SyncedOrdersFilterData
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.home.DropDownItem
import com.hanheldpos.ui.screens.main.adapter.SubSpinnerAdapter
import com.hanheldpos.ui.screens.menu.orders.synced.filter.adapter.DiningOptionFilterAdapter
import com.hanheldpos.utils.DateTimeUtils


class FilterSyncedOrdersFragment(
    private val filterData: SyncedOrdersFilterData?,
    private val update: (filter: SyncedOrdersFilterData) -> Unit
) : BaseFragment<FragmentFilterSyncedOrdersBinding, FilterSyncedOrdersVM>(), FilterSyncedOrdersUV {
    private lateinit var startSpinnerAdapter: SubSpinnerAdapter
    private lateinit var endSpinnerAdapter: SubSpinnerAdapter
    private lateinit var diningOptionFilterAdapter: DiningOptionFilterAdapter
    override fun layoutRes(): Int {
        return R.layout.fragment_filter_synced_orders
    }

    override fun viewModelClass(): Class<FilterSyncedOrdersVM> {
        return FilterSyncedOrdersVM::class.java
    }

    override fun initViewModel(viewModel: FilterSyncedOrdersVM) {
        viewModel.run {
            init(this@FilterSyncedOrdersFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {
        startSpinnerAdapter = SubSpinnerAdapter(requireContext())
        binding.spinnerStart.adapter = startSpinnerAdapter
        endSpinnerAdapter = SubSpinnerAdapter(requireContext())
        binding.spinnerEnd.adapter = endSpinnerAdapter

        diningOptionFilterAdapter =
            DiningOptionFilterAdapter(object : BaseItemClickListener<DiningOption> {
                override fun onItemClick(adapterPosition: Int, item: DiningOption) {
                    viewModel.diningOption.postValue(item)
                }
            })
        binding.diningOptionList.adapter =diningOptionFilterAdapter
        binding.diningOptionList.addItemDecorationWithoutLastDivider()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initData() {
        viewModel.initFilterData(filterData)

        binding.btnIsAllDay.isChecked = filterData?.isAllDay ?: false

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
                list.find { item -> item.name == filterData?.startTime }?.position ?: 0
            )

            binding.spinnerEnd.setSelection(
                list.find { item -> item.name == filterData?.endTime }?.position ?: 0
            )
        }
        viewModel.getDiningOptionList(requireContext()).let {
            diningOptionFilterAdapter.submitList(it.toMutableList())
            diningOptionFilterAdapter.notifyDataSetChanged()
        }

    }

    override fun initAction() {

        binding.btnApply.setOnClickDebounce {
            setupFilterData()
        }

        binding.calendarView.setOnDateChangedListener { _, date, _ ->
            viewModel.startDay = date
            viewModel.endDay = date
            binding.btnIsAllDay.isChecked = true
        }
        binding.calendarView.setOnRangeSelectedListener { _,dates ->
            if (dates.isNotEmpty()){
                viewModel.startDay = dates.first()
                viewModel.endDay = dates.last()
            }

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

    private fun setupFilterData() {
        update.invoke(
            SyncedOrdersFilterData(
                startDay = (DateTimeUtils.strToDate(
                    "${viewModel.startDay.year}-${viewModel.startDay.month}-${viewModel.startDay.day}T${viewModel.startTime.value ?: "00:00"}:00",
                    DateTimeUtils.Format.FULL_DATE_UTC_NOT_MILI
                )),
                endDay = (DateTimeUtils.strToDate(
                    "${viewModel.endDay.year}-${viewModel.endDay.month}-${viewModel.endDay.day}T${viewModel.endTime.value ?: "00:00"}:00",
                    DateTimeUtils.Format.FULL_DATE_UTC_NOT_MILI
                )),
                isAllDay = viewModel.isAllDay.value ?: false,
                startTime = viewModel.startTime.value,
                endTime = viewModel.endTime.value,
                diningOption = viewModel.diningOption.value
            )
        )
        onFragmentBackPressed()
    }

}