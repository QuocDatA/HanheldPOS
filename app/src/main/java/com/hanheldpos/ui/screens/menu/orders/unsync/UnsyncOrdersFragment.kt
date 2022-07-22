package com.hanheldpos.ui.screens.menu.orders.unsync

import android.annotation.SuppressLint
import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentUnsyncOrdersBinding
import com.hanheldpos.extension.setOnClickDebounce
import com.hanheldpos.model.order.OrderSummaryPrimary
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.order_detail.OrderDetailViewFragment
import com.hanheldpos.ui.screens.menu.orders.adapter.OrdersMenuGroupAdapter
import com.hanheldpos.ui.screens.menu.report.sale.SaleReportCommonVM

class UnsyncOrdersFragment : BaseFragment<FragmentUnsyncOrdersBinding,UnsyncOrdersVM>() , UnsyncOrdersUV {

    private lateinit var ordersMenuGroupAdapter : OrdersMenuGroupAdapter
    private val saleReportCommonVM by activityViewModels<SaleReportCommonVM>()

    override fun layoutRes(): Int {
        return R.layout.fragment_unsync_orders
    }

    override fun viewModelClass(): Class<UnsyncOrdersVM> {
        return UnsyncOrdersVM::class.java
    }

    override fun initViewModel(viewModel: UnsyncOrdersVM) {
        viewModel.run {
            init(this@UnsyncOrdersFragment)
            binding.viewModel = this
            binding.saleReportVM = saleReportCommonVM
        }
    }

    override fun initView() {
        ordersMenuGroupAdapter = OrdersMenuGroupAdapter(listener =object :BaseItemClickListener<OrderSummaryPrimary> {
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

    @SuppressLint("NotifyDataSetChanged")
    override fun initData() {
        saleReportCommonVM.orderCompleted.observe(this) {
            viewModel.groupUnsyncOrders(it).let { list->
                ordersMenuGroupAdapter.submitList(list)
                ordersMenuGroupAdapter.notifyDataSetChanged()
            }

        }
    }

    override fun initAction() {
        binding.btnSyncOrders.setOnClickDebounce {
            showLoading(true)
            saleReportCommonVM.onSyncOrders(requireView(),{
                showLoading(false)
            },{
                showLoading(false)
            })
        }
    }

}