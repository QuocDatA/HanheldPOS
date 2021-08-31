package com.hanheldpos.ui.screens.product.options.variant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentVariantBinding
import com.hanheldpos.ui.base.fragment.BaseFragment


class VariantFragment : BaseFragment<FragmentVariantBinding,VariantVM>(),VariantUV {
    override fun layoutRes() = R.layout.fragment_variant;

    override fun viewModelClass(): Class<VariantVM> {
        return VariantVM::class.java;
    }

    override fun initViewModel(viewModel: VariantVM) {

    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun initAction() {

    }

}