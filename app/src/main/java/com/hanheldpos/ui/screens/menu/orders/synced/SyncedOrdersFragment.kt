package com.hanheldpos.ui.screens.menu.orders.synced

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentSyncedOrdersBinding
import com.hanheldpos.model.order.OrderSummaryPrimary
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.orders.adapter.OrdersMenuGroupAdapter


class SyncedOrdersFragment : BaseFragment<FragmentSyncedOrdersBinding, SyncedOrdersVM>(),
    SyncedOrdersUV {
    private lateinit var ordersMenuGroupAdapter: OrdersMenuGroupAdapter
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
        ordersMenuGroupAdapter = OrdersMenuGroupAdapter(listener = object :
            BaseItemClickListener<OrderSummaryPrimary> {
            override fun onItemClick(adapterPosition: Int, item: OrderSummaryPrimary) {

            }

        })
        binding.listOrdersGroup.apply {
            adapter = ordersMenuGroupAdapter
        }
    }

    override fun initData() {

    }

    override fun initAction() {

    }

}