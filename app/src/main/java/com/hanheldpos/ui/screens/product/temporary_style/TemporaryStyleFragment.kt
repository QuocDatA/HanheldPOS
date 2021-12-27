package com.hanheldpos.ui.screens.product.temporary_style

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentTemporaryStyleBinding
import com.hanheldpos.ui.base.fragment.BaseFragment

class TemporaryStyleFragment : BaseFragment<FragmentTemporaryStyleBinding, TemporaryStyleVM>(), TemporaryStyleUV {
    override fun layoutRes(): Int {
        return R.layout.fragment_temporary_style;
    }

    override fun viewModelClass(): Class<TemporaryStyleVM> {
        return TemporaryStyleVM::class.java;
    }

    override fun initViewModel(viewModel: TemporaryStyleVM) {
        viewModel.run {
            init(this@TemporaryStyleFragment);
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