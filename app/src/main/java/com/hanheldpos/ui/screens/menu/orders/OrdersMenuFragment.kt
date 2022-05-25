package com.hanheldpos.ui.screens.menu.orders

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentOrdersMenuBinding
import com.hanheldpos.model.menu.orders.OrdersOptionType
import com.hanheldpos.model.menu.report.ReportOptionType
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.adapter.ItemOptionNav
import com.hanheldpos.ui.screens.menu.adapter.OptionNavAdapter
import com.hanheldpos.ui.screens.menu.orders.synced.SyncedOrdersFragment
import com.hanheldpos.ui.screens.menu.orders.unsync.UnsyncOrdersFragment
import com.hanheldpos.ui.screens.menu.report.current_drawer.CurrentDrawerFragment
import com.hanheldpos.ui.screens.menu.report.sale.SaleReportsMenuFragment


class OrdersMenuFragment : BaseFragment<FragmentOrdersMenuBinding,OrdersMenuVM>() , OrdersMenuUV {
    private lateinit var menuAdapter: OptionNavAdapter
    override fun layoutRes(): Int {
        return R.layout.fragment_orders_menu
    }

    override fun viewModelClass(): Class<OrdersMenuVM> {
        return  OrdersMenuVM::class.java
    }

    override fun initViewModel(viewModel: OrdersMenuVM) {
        viewModel.run {
            init(this@OrdersMenuFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {
        //region setup payment suggestion pay in cash recycler view
        menuAdapter = OptionNavAdapter(
            onMenuItemClickListener = object : BaseItemClickListener<ItemOptionNav> {
                override fun onItemClick(adapterPosition: Int, item: ItemOptionNav) {
                    onNavOptionClick(item);
                }
            },
        );
        binding.menuItemContainer.apply {
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    LinearLayoutManager.VERTICAL
                ).apply {
                    setDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.divider_vertical
                        )!!
                    )
                }
            )
        };
        binding.menuItemContainer.adapter = menuAdapter
        //endregion
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initData() {
        context?.let {
            viewModel.initOrdersMenuList(it).let { list->
                menuAdapter.submitList(list)
                menuAdapter.notifyDataSetChanged()
            }
        }

    }

    override fun initAction() {

    }

    private fun onNavOptionClick(option: ItemOptionNav) {
        when (option.type as OrdersOptionType) {
            OrdersOptionType.UNSYNC -> navigator.goToWithAnimationEnterFromRight(
                UnsyncOrdersFragment()
            )
            OrdersOptionType.SYNCED -> navigator.goToWithAnimationEnterFromRight(
                SyncedOrdersFragment()
            )
        }
    }


}