package com.hanheldpos.ui.screens.home.order.combo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentComboBinding
import com.hanheldpos.model.product.ProductCompleteModel
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.product.ProductDetailFragment

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