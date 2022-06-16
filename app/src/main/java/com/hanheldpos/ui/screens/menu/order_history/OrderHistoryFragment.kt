package com.hanheldpos.ui.screens.menu.order_history

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentOrderHistoryBinding
import com.hanheldpos.model.menu.orders.OrderMenuGroupItem
import com.hanheldpos.model.order.OrderSummaryPrimary
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.order_detail.OrderDetailViewFragment
import com.hanheldpos.ui.screens.menu.orders.adapter.OrdersMenuGroupAdapter


class OrderHistoryFragment : BaseFragment<FragmentOrderHistoryBinding,OrderHistoryVM>() , OrderHistoryUV {
    private lateinit var ordersMenuGroupAdapter : OrdersMenuGroupAdapter
    override fun layoutRes(): Int {
        return R.layout.fragment_order_history
    }

    override fun viewModelClass(): Class<OrderHistoryVM> {
        return OrderHistoryVM::class.java
    }

    override fun initViewModel(viewModel: OrderHistoryVM) {
        viewModel.run {
            init(this@OrderHistoryFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {
        ordersMenuGroupAdapter = OrdersMenuGroupAdapter(listener =object :
            BaseItemClickListener<OrderSummaryPrimary> {
            override fun onItemClick(adapterPosition: Int, item: OrderSummaryPrimary) {
                item._id?.let { navigator.goTo(OrderDetailViewFragment(it))  }
            }

        })
        binding.orderHistoryList.apply {
            adapter = ordersMenuGroupAdapter
            addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        /// User had scrolled to last
                        context?.let { viewModel.getOrderHistory(it) }
                    }
                }
            })

        }
    }

    override fun initData() {
        context?.let { viewModel.getOrderHistory(it) }
    }

    override fun initAction() {

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onLoadOrderHistory(orders: List<OrderMenuGroupItem>?) {
        ordersMenuGroupAdapter.submitList(orders)
        ordersMenuGroupAdapter.notifyDataSetChanged()

    }

}