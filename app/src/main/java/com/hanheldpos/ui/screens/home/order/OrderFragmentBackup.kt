package com.hanheldpos.ui.screens.home.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentOrderBackupBinding
import com.hanheldpos.ui.base.fragment.BaseFragment


class OrderFragmentBackup : BaseFragment<FragmentOrderBackupBinding,OrderVM>(), OrderUV {
    override fun layoutRes() = R.layout.fragment_order_backup

    override fun viewModelClass(): Class<OrderVM> {
        return OrderVM::class.java;
    }

    override fun initViewModel(viewModel: OrderVM) {
        viewModel.run {
            init(this@OrderFragmentBackup);
            binding.viewModel = this;
        }
    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun initAction() {

    }

    override fun showCategoryDialog() {

    }

    override fun showCart() {

    }

}