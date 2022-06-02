package com.hanheldpos.ui.screens.menu.order_detail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentOrderDetailViewBinding
import com.hanheldpos.model.order.OrderModel
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.order_detail.adapter.OrderDetailItemViewAdapter
import com.hanheldpos.ui.screens.menu.order_detail.adapter.OrderDetailPaymentAdapter


class OrderDetailViewFragment(private val orderId: String) :
    BaseFragment<FragmentOrderDetailViewBinding, OrderDetailViewVM>(), OrderDetailViewUV {
    private lateinit var orderDetailItemViewAdapter: OrderDetailItemViewAdapter
    private lateinit var orderDetailPaymentViewAdapter : OrderDetailPaymentAdapter
    override fun layoutRes(): Int {
        return R.layout.fragment_order_detail_view
    }

    override fun viewModelClass(): Class<OrderDetailViewVM> {
        return OrderDetailViewVM::class.java
    }

    override fun initViewModel(viewModel: OrderDetailViewVM) {
        viewModel.run {
            init(this@OrderDetailViewFragment)
            binding.viewModel = this
            binding.lifecycleOwner = this@OrderDetailViewFragment
        }
    }

    override fun initView() {
        orderDetailItemViewAdapter = OrderDetailItemViewAdapter()
        binding.itemList.adapter = orderDetailItemViewAdapter

        orderDetailPaymentViewAdapter = OrderDetailPaymentAdapter()
        binding.paymentList.adapter = orderDetailPaymentViewAdapter
    }

    override fun initData() {
        context?.let { viewModel.getOrderDetail(it, orderId) }
    }

    override fun initAction() {

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onShowOrderDetail(order: OrderModel) {
        binding.order = order
        orderDetailItemViewAdapter.submitList(order.OrderDetail.OrderProducts)
        orderDetailItemViewAdapter.notifyDataSetChanged()
        orderDetailPaymentViewAdapter.submitList(order.OrderDetail.PaymentList)
        orderDetailPaymentViewAdapter.notifyDataSetChanged()
    }

}