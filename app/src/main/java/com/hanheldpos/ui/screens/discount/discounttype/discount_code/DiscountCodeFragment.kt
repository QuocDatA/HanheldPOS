package com.hanheldpos.ui.screens.discount.discounttype.discount_code

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentDiscountCodeBinding
import com.hanheldpos.ui.base.fragment.BaseFragment


class DiscountCodeFragment : BaseFragment<FragmentDiscountCodeBinding,DiscountCodeVM>(), DiscountCodeUV {
    override fun layoutRes(): Int  = R.layout.fragment_discount_code

    override fun viewModelClass(): Class<DiscountCodeVM> {
        return DiscountCodeVM::class.java;
    }

    override fun initViewModel(viewModel: DiscountCodeVM) {
        viewModel.run {
            init(this@DiscountCodeFragment);
            binding.viewModel = this;
        }

    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun initAction() {

    }

}