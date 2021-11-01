package com.hanheldpos.ui.screens.discount.discounttype.comp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentDiscountCompBinding
import com.hanheldpos.ui.base.fragment.BaseFragment


class DiscountCompFragment : BaseFragment<FragmentDiscountCompBinding,DiscountCompVM>(), DiscountCompUV {
    override fun layoutRes(): Int = R.layout.fragment_discount_comp

    override fun viewModelClass(): Class<DiscountCompVM> {
        return DiscountCompVM::class.java;
    }

    override fun initViewModel(viewModel: DiscountCompVM) {
        viewModel.run {
            init(this@DiscountCompFragment);
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