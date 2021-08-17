package com.hanheldpos.ui.screens.product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.appbar.AppBarLayout
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentProductDetailBinding
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.base.widget.AppBarStateChangeListener

class ProductDetailFragment : BaseFragment<FragmentProductDetailBinding,ProductDetailVM>(), ProductDetailUV {

    override fun layoutRes() = R.layout.fragment_product_detail;

    override fun viewModelClass(): Class<ProductDetailVM> {
        return ProductDetailVM::class.java;
    }

    override fun initViewModel(viewModel: ProductDetailVM) {
        /*viewModel.apply {
            init(this@ProductDetailFragment);
            initLifeCycle(this@ProductDetailFragment);
            binding.viewModel = this;
        }*/
    }

    override fun initView() {
        /*binding.appBarProductDetail.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                when(state) {
                    State.COLLAPSED ->{
                        viewModel.isToolbarExpand.value = false;
                    }
                    else -> {
                        viewModel.isToolbarExpand.value = true;

                    }
                }
            }

        })*/
    }

    override fun initData() {

    }

    override fun initAction() {

    }

    companion object {



    }

    override fun goBack() {
        navigator.goOneBack();
    }
}