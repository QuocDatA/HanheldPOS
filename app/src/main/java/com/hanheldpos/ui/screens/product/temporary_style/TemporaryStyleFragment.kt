package com.hanheldpos.ui.screens.product.temporary_style

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentTemporaryStyleBinding
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.product.adapter.GridSpacingItemDecoration
import com.hanheldpos.ui.screens.product.temporary_style.adapter.TemporaryVariantAdapter

class TemporaryStyleFragment : BaseFragment<FragmentTemporaryStyleBinding, TemporaryStyleVM>(),
    TemporaryStyleUV {

    private lateinit var variantAdapter: TemporaryVariantAdapter;

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
        variantAdapter = TemporaryVariantAdapter()
        binding.containerVariant.apply {
            adapter = variantAdapter; addItemDecoration(
            GridSpacingItemDecoration(4, resources.getDimensionPixelSize(R.dimen._15sdp), false)
        );
        }
    }

    override fun initData() {
        variantAdapter.submitList(mutableListOf(0, 1, 2, 3, 4,6,7,8));
    }

    override fun initAction() {

    }


}