package com.hanheldpos.ui.screens.product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentProductDetailBinding
import com.hanheldpos.model.product.ProductCompleteModel
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.product.adapter.OptionsPagerAdapter


class ProductDetailFragment(
    private val listener: ProductDetailListener? = null
) : BaseFragment<FragmentProductDetailBinding, ProductDetailVM>(), ProductDetailUV {
    override fun layoutRes() = R.layout.fragment_product_detail;

    override fun viewModelClass(): Class<ProductDetailVM> {
        return ProductDetailVM::class.java;
    }



    override fun initViewModel(viewModel: ProductDetailVM) {
        viewModel.run {
            init(this@ProductDetailFragment);
            initLifeCycle(this@ProductDetailFragment);
            binding.viewModel = viewModel;
        }
    }

    override fun initView() {
        // Init Options
        val adapter = OptionsPagerAdapter(childFragmentManager, lifecycle);
        binding.optionContainer.adapter = adapter;
        TabLayoutMediator(binding.tabOption, binding.optionContainer) { tab, position ->
            run {
                when (position) {
                    0 -> {
                        tab.text = "VARIANT"
                    }
                    1 -> {
                        tab.text = "MODIFIER"
                    }
                }
            }
        }.attach()
    }

    override fun initData() {
        arguments?.let {
            val a: ProductCompleteModel? = it.getParcelable(ARG_PRODUCT_DETAIL_FRAGMENT)
            val quantityCanChoose: Int = it.getInt(ARG_PRODUCT_DETAIL_QUANTITY)
            viewModel.productCompletelLD.value = a;
            viewModel.productDetailLD.value = a?.productDetail;
            viewModel.maxQuantity = quantityCanChoose;
        }
    }

    override fun initAction() {

    }

    interface ProductDetailListener {
        fun onAddCart(item: ProductCompleteModel)
    }

    companion object {
        private const val ARG_PRODUCT_DETAIL_FRAGMENT = "ARG_PRODUCT_DETAIL_FRAGMENT"
        private const val ARG_PRODUCT_DETAIL_QUANTITY = "ARG_PRODUCT_DETAIL_QUANTITY"
        fun getInstance(
            item: ProductCompleteModel,
            quantityCanChoose: Int = -1,
            listener: ProductDetailListener? = null
        ): ProductDetailFragment {
            return ProductDetailFragment(
                listener = listener
            ).apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PRODUCT_DETAIL_FRAGMENT, item)
                    putInt(ARG_PRODUCT_DETAIL_QUANTITY, quantityCanChoose)
                }
            };
        }
    }

    override fun onBack() {
        navigator.goOneBack();
    }



}