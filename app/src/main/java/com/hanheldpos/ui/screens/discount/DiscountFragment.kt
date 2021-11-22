package com.hanheldpos.ui.screens.discount

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentDiscountBinding
import com.hanheldpos.model.discount.DiscountType
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.discount.discounttype.DiscountTypeFragment
import com.hanheldpos.ui.screens.product.adapter.OptionsPagerAdapter


class DiscountFragment(private val listener: ItemCallback) :
    BaseFragment<FragmentDiscountBinding, DiscountVM>(), DiscountUV {
    override fun layoutRes(): Int = R.layout.fragment_discount



    // Adapter
    private lateinit var optionsPagerAdapter: OptionsPagerAdapter;

    override fun viewModelClass(): Class<DiscountVM> {
        return DiscountVM::class.java;
    }

    override fun initViewModel(viewModel: DiscountVM) {
        viewModel.run {
            init(this@DiscountFragment);
            binding.viewModel = this
        }

    }

    override fun initView() {
        optionsPagerAdapter = OptionsPagerAdapter(childFragmentManager, lifecycle);
        binding.fragmentContainer.adapter = optionsPagerAdapter;
        TabLayoutMediator(binding.tabLayout, binding.fragmentContainer) { tab, position ->
            run {
                when (position) {
                    0 -> {
                        tab.text = getString(R.string.item_discount)
                    }
                    1 -> {
                        tab.text = getString(R.string.order_discount)
                    }
                }
            }
        }.attach()
    }

    override fun initData() {
        optionsPagerAdapter.submitList(
            listOf(
                DiscountTypeFragment(type = DiscountType.ITEM_DISCOUNT),
                DiscountTypeFragment(type = DiscountType.ORDER_DISCOUNT),
            )
        )
    }

    override fun initAction() {

    }

    interface ItemCallback {
        fun onItemSelected();
    }

    override fun backPress() {
        navigator.goOneBack();
    }
}