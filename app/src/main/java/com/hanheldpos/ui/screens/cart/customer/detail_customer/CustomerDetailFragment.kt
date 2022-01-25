package com.hanheldpos.ui.screens.cart.customer.detail_customer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.databinding.FragmentCustomerDetailBinding
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cart.CartDataVM

class CustomerDetailFragment(private val customer : CustomerResp?) : BaseFragment<FragmentCustomerDetailBinding,CustomerDetailVM>(), CustomerDetailUV {

    private val cartDataVM by activityViewModels<CartDataVM>();

    override fun layoutRes(): Int {
        return R.layout.fragment_customer_detail;
    }

    override fun viewModelClass(): Class<CustomerDetailVM> {
        return CustomerDetailVM::class.java;
    }

    override fun initViewModel(viewModel: CustomerDetailVM) {
        viewModel.run {
            init(this@CustomerDetailFragment);
            binding.viewModel = this;
        }
    }

    override fun initView() {
        viewModel.customer.value = customer;
    }

    override fun initData() {
    }

    override fun initAction() {

    }

    override fun backPress() {
        onFragmentBackPressed();
    }

    override fun removeCustomer() {
        cartDataVM.removeCustomerFromCart();
        backPress();
    }

}