package com.hanheldpos.ui.screens.discount.discount_detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.discount.CouponResp
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.databinding.FragmentDiscountDetailBinding
import com.hanheldpos.model.DataHelper
import com.hanheldpos.ui.base.fragment.BaseFragment

class DiscountDetailFragment(private val discountResp: DiscountResp) : BaseFragment<FragmentDiscountDetailBinding, DiscountDetailVM>() , DiscountDetailUV {
    override fun layoutRes(): Int = R.layout.fragment_discount_detail

    override fun viewModelClass(): Class<DiscountDetailVM> {
        return DiscountDetailVM::class.java
    }

    override fun initViewModel(viewModel: DiscountDetailVM) {
        viewModel.run {
            init(this@DiscountDetailFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {

    }

    override fun initData() {
        val discountDetailResp = DataHelper.discountDetailsLocalStorage?.find {
            discountDetailResp ->
            discountDetailResp._id == discountResp._id
        }
        discountDetailResp?.let { binding.discountDetail = it }
    }

    override fun initAction() {

    }

    override fun getBack() {
        onFragmentBackPressed()
    }
}