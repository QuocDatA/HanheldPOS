package com.hanheldpos.ui.screens.discount

import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.order.settings.Reason
import com.hanheldpos.databinding.FragmentDiscountBinding
import com.hanheldpos.model.discount.DiscountApplyToType
import com.hanheldpos.model.discount.DiscountUser
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cart.CartDataVM
import com.hanheldpos.ui.screens.discount.discounttype.DiscountTypeFragment
import com.hanheldpos.ui.screens.product.adapter.OptionsPagerAdapter


class DiscountFragment(private val listener: DiscountCallback) :
    BaseFragment<FragmentDiscountBinding, DiscountVM>(), DiscountUV {
    override fun layoutRes(): Int = R.layout.fragment_discount


    // Adapter
    private lateinit var optionsPagerAdapter: OptionsPagerAdapter;
    private val cartDataVM by activityViewModels<CartDataVM>();

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
                DiscountTypeFragment(
                    applyToType = DiscountApplyToType.ITEM_DISCOUNT_APPLY_TO, cart = cartDataVM.cartModelLD.value!!,
                    listener = object : DiscountTypeFragment.DiscountTypeListener {
                        override fun discountUserChoose(discount: DiscountUser) {

                        }

                        override fun compReasonChoose(item: Reason) {

                        }

                        override fun compRemoveAll() {

                        }
                    }),
                DiscountTypeFragment(
                    applyToType = DiscountApplyToType.ORDER_DISCOUNT_APPLY_TO,
                    cart = cartDataVM.cartModelLD.value!!,
                    listener = object : DiscountTypeFragment.DiscountTypeListener {
                        override fun discountUserChoose(discount: DiscountUser) {
                            listener.onDiscountUserChoose(discount);
                            backPress();
                        }

                        override fun compReasonChoose(item: Reason) {
                            listener.onCompReasonChoose(item);
                            backPress();
                        }

                        override fun compRemoveAll() {
                            listener.onCompOrderRemove();
                            backPress();
                        }
                    }),
            )
        )
    }

    override fun initAction() {

    }

    interface DiscountCallback {
        fun onDiscountUserChoose(discount: DiscountUser);
        fun onCompReasonChoose(reason: Reason);
        fun onCompOrderRemove();
    }

    override fun backPress() {
        navigator.goOneBack();
    }
}