package com.hanheldpos.ui.screens.menu.orders.synced

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentSyncedOrdersBinding
import com.hanheldpos.extension.setOnClickDebounce
import com.hanheldpos.model.menu.orders.SyncedOrdersFilterData
import com.hanheldpos.model.order.OrderSummaryPrimary
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.main.adapter.SubSpinnerAdapter
import com.hanheldpos.ui.screens.menu.order_detail.OrderDetailViewFragment
import com.hanheldpos.ui.screens.menu.orders.adapter.FilterOptionAdapter
import com.hanheldpos.ui.screens.menu.orders.adapter.OrdersMenuGroupAdapter
import com.hanheldpos.ui.screens.menu.orders.synced.filter.FilterSyncedOrdersFragment
import com.hanheldpos.utils.DateTimeUtils
import com.utils.helper.SystemHelper
import com.xiaofeng.flowlayoutmanager.Alignment
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager
import java.util.*


class SyncedOrdersFragment : BaseFragment<FragmentSyncedOrdersBinding, SyncedOrdersVM>(),
    SyncedOrdersUV {
    private lateinit var ordersMenuGroupAdapter: OrdersMenuGroupAdapter
    private lateinit var filterOptionAdapter: FilterOptionAdapter
    override fun layoutRes(): Int {
        return R.layout.fragment_synced_orders
    }

    override fun viewModelClass(): Class<SyncedOrdersVM> {
        return SyncedOrdersVM::class.java
    }

    override fun initViewModel(viewModel: SyncedOrdersVM) {
        viewModel.run {
            init(this@SyncedOrdersFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {
        filterOptionAdapter = FilterOptionAdapter(object : BaseItemClickListener<String> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onItemClick(adapterPosition: Int, item: String) {
                filterOptionAdapter.submitList(
                    filterOptionAdapter.currentList.toMutableList().apply { remove(item) })
                filterOptionAdapter.notifyDataSetChanged()
            }

        })
        binding.listFilterOption.apply {
            adapter = filterOptionAdapter
            layoutManager = FlowLayoutManager().apply {
                isAutoMeasureEnabled = true
                setAlignment(Alignment.LEFT)
            }
        }
        ordersMenuGroupAdapter = OrdersMenuGroupAdapter(listener = object :
            BaseItemClickListener<OrderSummaryPrimary> {
            override fun onItemClick(adapterPosition: Int, item: OrderSummaryPrimary) {
                item._id?.let {
                    navigator.goToWithCustomAnimation(OrderDetailViewFragment(it))
                }
            }

        })
        binding.listOrdersGroup.apply {
            adapter = ordersMenuGroupAdapter
        }
    }

    override fun initData() {

    }

    override fun initAction() {
        var isFirstLoad = true
        viewModel.filterData.observe(this) {
            if (isFirstLoad) {
                isFirstLoad = false
                return@observe
            }
            viewModel.getOrderFilter(it)
        }

        binding.btnSearch.setOnClickDebounce {
            viewModel.filterData.postValue(
                SyncedOrdersFilterData(
                    startDay = DateTimeUtils.curDate,
                    endDay = DateTimeUtils.curDate,
                    isAllDay = true,
                    startTime = null,
                    endTime = null,
                    diningOption = null,
                )
            )
        }

        binding.discountAutomaticInput.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                binding.btnSearch.callOnClick()
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view?.windowToken, 0)
            }
            true
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onLoadOrderFilter(orders: List<OrderSummaryPrimary>?) {
        ordersMenuGroupAdapter.submitList(viewModel.groupSyncOrders(orders ?: mutableListOf()))
        ordersMenuGroupAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onShowFilter() {
        navigator.goTo(FilterSyncedOrdersFragment(viewModel.filterData.value, update = { data ->
            viewModel.orderCodeSearch.postValue(null)
            viewModel.filterData.postValue(data)

            filterOptionAdapter.submitList(
                mutableListOf(
                    "${
                        DateTimeUtils.dateToString(
                            data.startDay,
                            DateTimeUtils.Format.dd_MMM_YYYY
                        )
                    } From ${data.startTime} To ${data.endTime}",
                    data.diningOption?.Title ?: context?.getString(R.string.all_dining_options)
                )
            )
            filterOptionAdapter.notifyDataSetChanged()
        }))
    }

}