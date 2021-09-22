package com.hanheldpos.ui.screens.home.order.combo

import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentComboBinding
import com.hanheldpos.ui.base.fragment.BaseFragment

class ComboFragment : BaseFragment<FragmentComboBinding,ComboVM>(), ComboUV {
    override fun layoutRes() = R.layout.fragment_combo;

    override fun viewModelClass(): Class<ComboVM> {
        return ComboVM::class.java;
    }

    override fun initViewModel(viewModel: ComboVM) {
        viewModel.run {
            init(this@ComboFragment);
            binding.viewModel = viewModel
        }
    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun initAction() {

    }

    companion object {
        fun getInstance(
        ): ComboFragment {
            return ComboFragment(
            ).apply {

            };
        }
    }

}