package com.hanheldpos.ui.screens.discount.discount_type.automatic

import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentDiscountAutomaticBinding
import com.hanheldpos.ui.base.fragment.BaseFragment


class DiscountAutomaticFragment : BaseFragment<FragmentDiscountAutomaticBinding,DiscountAutomaticVM>() , DiscountAutomaticUV {
    override fun layoutRes(): Int {
        return R.layout.fragment_discount_automatic;
    }

    override fun viewModelClass(): Class<DiscountAutomaticVM> {
        return DiscountAutomaticVM::class.java;
    }

    override fun initViewModel(viewModel: DiscountAutomaticVM) {
        viewModel.run {
            init(this@DiscountAutomaticFragment);
        }
        binding.viewModel = viewModel;
    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun initAction() {

    }

}