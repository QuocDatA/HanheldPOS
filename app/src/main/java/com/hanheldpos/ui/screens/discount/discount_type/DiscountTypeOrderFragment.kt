package com.hanheldpos.ui.screens.discount.discount_type

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.order.settings.Reason
import com.hanheldpos.databinding.FragmentDiscountTypeOrderBinding
import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.model.cart.CartModel
import com.hanheldpos.model.discount.DiscApplyTo
import com.hanheldpos.model.discount.DiscountTypeFor
import com.hanheldpos.model.discount.DiscountTypeTab
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.discount.DiscountFragment
import com.hanheldpos.ui.screens.discount.adapter.OptionsPagerAdapter
import com.hanheldpos.ui.screens.discount.discount_type.adapter.DiscountTabAdapter
import com.hanheldpos.ui.screens.discount.discount_type.amount.DiscountAmountFragment
import com.hanheldpos.ui.screens.discount.discount_type.automatic.DiscountAutomaticFragment
import com.hanheldpos.ui.screens.discount.discount_type.comp.DiscountCompFragment
import com.hanheldpos.ui.screens.discount.discount_type.discount_code.DiscountCodeFragment
import com.hanheldpos.ui.screens.discount.discount_type.percentage.DiscountPercentageFragment


class DiscountTypeOrderFragment(
    private val applyToType: DiscApplyTo,
    private val cart: CartModel,
    private val product: BaseProductInCart? = null,
    private val listener: DiscountFragment.DiscountTypeListener
) : BaseFragment<FragmentDiscountTypeOrderBinding, DiscountTypeVM>(),
    DiscountTypeUV {
    // Adapter
    private lateinit var adapter: DiscountTabAdapter;
    private lateinit var optionsPagerAdapter: OptionsPagerAdapter;

    // Frament child
    private val fragmentMap: MutableMap<DiscountTypeFor, Fragment> = mutableMapOf()

    override fun layoutRes(): Int = R.layout.fragment_discount_type_order;

    override fun viewModelClass(): Class<DiscountTypeVM> {
        return DiscountTypeVM::class.java;
    }

    override fun initViewModel(viewModel: DiscountTypeVM) {
        viewModel.run {
            init(this@DiscountTypeOrderFragment);
            binding.viewModel = this;
            binding.discountType = applyToType;
        }
    }

    override fun initView() {
        viewModel.isAlreadyExistDiscountSelect.observe(this) {
            binding.btnClearDiscount.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (it) R.color.color_0 else R.color.color_8
                )
            )
        }

        // Discount Tab Adapter
        adapter = DiscountTabAdapter(listener = object : BaseItemClickListener<DiscountTypeTab> {
            override fun onItemClick(adapterPosition: Int, item: DiscountTypeTab) {
                binding.discountFragmentContainer.currentItem = item.type.value;

                if (item.type == DiscountTypeFor.COMP) {
                    binding.btnClearDiscount.visibility = View.GONE
                } else binding.btnClearDiscount.visibility = View.VISIBLE

                viewModel.isAlreadyExistDiscountSelect.postValue(
                    !cart.discountServerList.isNullOrEmpty() ||
                            !cart.discountUserList.isNullOrEmpty()
                )
                viewModel.typeDiscountSelect.postValue(item.type);
                listener.discountFocus(item.type);
            }
        })
        binding.tabDiscountType.apply {
            adapter = this@DiscountTypeOrderFragment.adapter;
            setHasFixedSize(true);
        }

        // Container Fragment Type For Adapter
        optionsPagerAdapter = OptionsPagerAdapter(childFragmentManager, lifecycle);
        binding.discountFragmentContainer.apply {
            adapter = optionsPagerAdapter;
        }

    }

    override fun initData() {

        // Data Discount Tab Adapter
        val listTab = mutableListOf(
            DiscountTypeTab(title = "Amount (đ)", type = DiscountTypeFor.AMOUNT),
            DiscountTypeTab(title = "Percentage (%)", type = DiscountTypeFor.PERCENTAGE),

            DiscountTypeTab(title = "Comp", type = DiscountTypeFor.COMP),
        )
        if (applyToType == DiscApplyTo.ORDER) {
            listTab.add(2, DiscountTypeTab(title = "Automatic", type = DiscountTypeFor.AUTOMATIC));
            listTab.add(
                2,
                DiscountTypeTab(title = "Discount Code", type = DiscountTypeFor.DISCOUNT_CODE)
            );
        }

        adapter.submitList(listTab);

        // Data Container Fragment Type
        fragmentMap[DiscountTypeFor.AMOUNT] =
            DiscountAmountFragment(

                listener = listener,
                applyToType = applyToType
            );
        fragmentMap[DiscountTypeFor.PERCENTAGE] =
            DiscountPercentageFragment(
                applyToType,
                listener = listener);
        fragmentMap[DiscountTypeFor.DISCOUNT_CODE] = DiscountCodeFragment(
            applyToType,
            listener
        );
        fragmentMap[DiscountTypeFor.AUTOMATIC] = DiscountAutomaticFragment(
            applyToType,
            cart,
            product,
            listener
        );
        fragmentMap[DiscountTypeFor.COMP] =
            DiscountCompFragment(
                comp = when (applyToType) {
                    DiscApplyTo.ITEM -> product?.compReason
                    DiscApplyTo.ORDER -> cart.compReason
                    else -> null
                },
                listener = object : DiscountFragment.DiscountTypeListener {
                    override fun compReasonChoose(item: Reason) {
                        listener.compReasonChoose(item)
                    }

                    override fun compRemoveAll() {
                        listener.compRemoveAll()
                    }

                    override fun validDiscount(isValid: Boolean) {
                        listener.validDiscount(isValid);
                    }
                }, applyToType = applyToType
            )

        optionsPagerAdapter.submitList(fragmentMap.values);

    }

    override fun initAction() {
        binding.btnClearDiscount.setOnClickListener {
            if (!cart.discountServerList.isNullOrEmpty() || !cart.discountUserList.isNullOrEmpty()) {
                listener.clearAllDiscountCoupon()
                viewModel.isAlreadyExistDiscountSelect.postValue(false)
            }
        }
    }


}