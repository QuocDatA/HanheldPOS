package com.hanheldpos.ui.screens.menu.orders.synced.filter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentFilterSyncedOrdersBinding
import com.hanheldpos.ui.base.fragment.BaseFragment


class FilterSyncedOrdersFragment : BaseFragment<FragmentFilterSyncedOrdersBinding,FilterSyncedOrdersVM>() , FilterSyncedOrdersUV {
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

    }

    override fun initData() {

    }

    override fun initAction() {

    }

}