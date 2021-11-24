package com.hanheldpos.ui.screens.discount.discounttype

import androidx.fragment.app.Fragment
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.order.settings.ListReasonsItem
import com.hanheldpos.databinding.FragmentDiscountTypeBinding
import com.hanheldpos.model.cart.CartModel
import com.hanheldpos.model.discount.DiscountApplyToType
import com.hanheldpos.model.discount.DiscountTypeFor
import com.hanheldpos.model.discount.DiscountTypeTab
import com.hanheldpos.model.discount.DiscountUser
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.discount.discounttype.adapter.DiscountTabAdapter
import com.hanheldpos.ui.screens.discount.discounttype.amount.DiscountAmountFragment
import com.hanheldpos.ui.screens.discount.discounttype.comp.DiscountCompFragment
import com.hanheldpos.ui.screens.discount.discounttype.discount_code.DiscountCodeFragment
import com.hanheldpos.ui.screens.discount.discounttype.percentage.DiscountPercentageFragment
import com.hanheldpos.ui.screens.product.adapter.OptionsPagerAdapter

class DiscountTypeFragment(private val applyToType: DiscountApplyToType, private val cart : CartModel, private val listener : DiscountTypeListener) :
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
        }
    }

    override fun initView() {

        // Discount Tab Adapter
        adapter = DiscountTabAdapter(listener = object : BaseItemClickListener<DiscountTypeTab> {
            override fun onItemClick(adapterPosition: Int, item: DiscountTypeTab) {
                binding.discountFragmentContainer.currentItem = item.type.value;
            }
        })
        binding.tabDiscountType.apply {
            adapter = this@DiscountTypeFragment.adapter;
            setHasFixedSize(true);
        }

        // Container Fragment Type For Adapter
        optionsPagerAdapter = OptionsPagerAdapter(childFragmentManager,lifecycle);
        binding.discountFragmentContainer.adapter = optionsPagerAdapter;

    }

    override fun initData() {
        // Data Discount Tab Adapter
        val listTab = mutableListOf(
            DiscountTypeTab(title = "Amount (đ)", type = DiscountTypeFor.AMOUNT),
            DiscountTypeTab(title = "Percentage (%)", type = DiscountTypeFor.PERCENTAGE),
            DiscountTypeTab(title = "Comp", type = DiscountTypeFor.COMP),
        )
        if (applyToType == DiscountApplyToType.ORDER_DISCOUNT_APPLY_TO)
            listTab.add(
                2,
                DiscountTypeTab(title = "Discount Code", type = DiscountTypeFor.DISCOUNT_CODE)
            )
        adapter.submitList(listTab);

        // Data Container Fragment Type
        fragmentMap[DiscountTypeFor.AMOUNT] = DiscountAmountFragment(listener = object : DiscountTypeListener {
            override fun discountUserChoose(discount: DiscountUser) {
                listener.discountUserChoose(discount);
            }
        });
        fragmentMap[DiscountTypeFor.PERCENTAGE] = DiscountPercentageFragment(listener = object : DiscountTypeListener {
            override fun discountUserChoose(discount: DiscountUser) {
                listener.discountUserChoose(discount);
            }
        });
        fragmentMap[DiscountTypeFor.DISCOUNT_CODE] = DiscountCodeFragment();
        fragmentMap[DiscountTypeFor.COMP] = DiscountCompFragment(comp = cart.compReason,listener = object : DiscountTypeListener {
            override fun compReasonChoose(item: ListReasonsItem) {
                listener.compReasonChoose(item);
            }

            override fun compRemoveAll() {
                listener.compRemoveAll();
            }
        });
        optionsPagerAdapter.submitList(fragmentMap.values);

    }

    override fun initAction() {

    }

    interface DiscountTypeListener {
        fun discountUserChoose(discount : DiscountUser) : Unit{};
        fun compReasonChoose(item : ListReasonsItem) : Unit {};
        fun compRemoveAll() : Unit{};
    }

}