package com.hanheldpos.ui.screens.discount.discount_type

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.order.settings.Reason
import com.hanheldpos.databinding.FragmentDiscountTypeBinding
import com.hanheldpos.model.cart.CartModel
import com.hanheldpos.model.discount.*
import com.hanheldpos.model.product.BaseProductInCart
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.discount.adapter.OptionsPagerAdapter
import com.hanheldpos.ui.screens.discount.discount_type.adapter.DiscountTabAdapter
import com.hanheldpos.ui.screens.discount.discount_type.amount.DiscountAmountFragment
import com.hanheldpos.ui.screens.discount.discount_type.automatic.DiscountAutomaticFragment
import com.hanheldpos.ui.screens.discount.discount_type.comp.DiscountCompFragment
import com.hanheldpos.ui.screens.discount.discount_type.discount_code.DiscountCodeFragment
import com.hanheldpos.ui.screens.discount.discount_type.percentage.DiscountPercentageFragment


class DiscountTypeFragment(
    private val applyToType: DiscountApplyToType,
    private val cart: CartModel,
    private val product: BaseProductInCart? = null,
    private val listener: DiscountTypeListener
) :
    BaseFragment<FragmentDiscountTypeBinding, DiscountTypeVM>(),
    DiscountTypeUV {
    // Adapter
    private lateinit var adapter: DiscountTabAdapter;
    private lateinit var optionsPagerAdapter: OptionsPagerAdapter;

    // Frament child
    private val fragmentMap: MutableMap<DiscountTypeFor, Fragment> = mutableMapOf()

    override fun layoutRes(): Int = R.layout.fragment_discount_type;

    override fun viewModelClass(): Class<DiscountTypeVM> {
        return DiscountTypeVM::class.java;
    }

    override fun initViewModel(viewModel: DiscountTypeVM) {
        viewModel.run {
            init(this@DiscountTypeFragment);
            binding.viewModel = this;
            binding.discountType = applyToType;
        }
    }

    override fun initView() {

        // Discount Tab Adapter
        adapter = DiscountTabAdapter(listener = object : BaseItemClickListener<DiscountTypeTab> {
            override fun onItemClick(adapterPosition: Int, item: DiscountTypeTab) {
                binding.discountFragmentContainer.currentItem = item.type.value;
                viewModel.typeDiscountSelect.postValue(item.type);
                listener.discountFocus(item.type);
            }
        })
        binding.tabDiscountType.apply {
            adapter = this@DiscountTypeFragment.adapter;
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
        if (applyToType == DiscountApplyToType.ORDER_DISCOUNT_APPLY_TO){
            listTab.add(2,DiscountTypeTab(title = "Automatic", type = DiscountTypeFor.AUTOMATIC));
            listTab.add(2, DiscountTypeTab(title = "Discount Code", type = DiscountTypeFor.DISCOUNT_CODE));
        }

        adapter.submitList(listTab);

        // Data Container Fragment Type
        fragmentMap[DiscountTypeFor.AMOUNT] =
            DiscountAmountFragment(listener = object : DiscountTypeListener {
                override fun discountUserChoose(discount: DiscountUser) {
                    listener.discountUserChoose(discount);
                }

                override fun validDiscount(isValid: Boolean) {
                    listener.validDiscount(isValid);
                }
            });
        fragmentMap[DiscountTypeFor.PERCENTAGE] =
            DiscountPercentageFragment(listener = object : DiscountTypeListener {
                override fun discountUserChoose(discount: DiscountUser) {
                    listener.discountUserChoose(discount);
                }

                override fun validDiscount(isValid: Boolean) {
                    listener.validDiscount(isValid);
                }
            });
        fragmentMap[DiscountTypeFor.DISCOUNT_CODE] = DiscountCodeFragment(applyToType);
        fragmentMap[DiscountTypeFor.AUTOMATIC] = DiscountAutomaticFragment(cart);
        fragmentMap[DiscountTypeFor.COMP] =
            DiscountCompFragment(
                comp = when(applyToType){
                    DiscountApplyToType.ITEM_DISCOUNT_APPLY_TO-> product?.compReason;
                    DiscountApplyToType.ORDER_DISCOUNT_APPLY_TO-> cart.compReason;
                    else -> null
                } ,
                listener = object : DiscountTypeListener {
                    override fun compReasonChoose(item: Reason) {
                        listener.compReasonChoose(item);
                    }

                    override fun compRemoveAll() {
                        listener.compRemoveAll();
                    }

                    override fun validDiscount(isValid: Boolean) {
                        listener.validDiscount(isValid);
                    }
                })

        optionsPagerAdapter.submitList(fragmentMap.values);

    }

    override fun initAction() {
    }

    interface DiscountTypeListener {
        fun discountUserChoose(discount: DiscountUser): Unit {};
        fun compReasonChoose(item: Reason): Unit {};
        fun compRemoveAll(): Unit {};
        fun discountFocus(type : DiscountTypeFor) : Unit{}
        fun validDiscount(isValid : Boolean);
    }
}