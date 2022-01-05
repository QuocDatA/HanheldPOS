package com.hanheldpos.ui.screens.discount.discount_type

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.order.settings.Reason
import com.hanheldpos.databinding.FragmentDiscountTypeBinding
import com.hanheldpos.model.cart.CartModel
import com.hanheldpos.model.discount.DiscountApplyToType
import com.hanheldpos.model.discount.DiscountTypeFor
import com.hanheldpos.model.discount.DiscountTypeTab
import com.hanheldpos.model.discount.DiscountUser
import com.hanheldpos.model.product.BaseProductInCart
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.discount.discount_type.adapter.DiscountItemAdapter
import com.hanheldpos.ui.screens.discount.discount_type.adapter.DiscountTabAdapter
import com.hanheldpos.ui.screens.discount.discount_type.amount.DiscountAmountFragment
import com.hanheldpos.ui.screens.discount.discount_type.automatic.DiscountAutomaticFragment
import com.hanheldpos.ui.screens.discount.discount_type.comp.DiscountCompFragment
import com.hanheldpos.ui.screens.discount.discount_type.discount_code.DiscountCodeFragment
import com.hanheldpos.ui.screens.discount.discount_type.percentage.DiscountPercentageFragment
import com.hanheldpos.ui.screens.product.adapter.OptionsPagerAdapter

class DiscountTypeFragment(
    private val applyToType: DiscountApplyToType,
    private val cart: CartModel,
    private val listener: DiscountTypeListener
) :
    BaseFragment<FragmentDiscountTypeBinding, DiscountTypeVM>(),
    DiscountTypeUV {
    // Adapter
    private lateinit var adapter: DiscountTabAdapter;
    private lateinit var optionsPagerAdapter: OptionsPagerAdapter;
    private lateinit var itemDiscountAdapter: DiscountItemAdapter;


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
            }
        })
        binding.tabDiscountType.apply {
            adapter = this@DiscountTypeFragment.adapter;
            setHasFixedSize(true);
        }

        //Discount Item Adapter
        itemDiscountAdapter =
            DiscountItemAdapter(listener = object : BaseItemClickListener<BaseProductInCart> {
                override fun onItemClick(adapterPosition: Int, item: BaseProductInCart) {
                    viewModel.reasonChosen.postValue(item.compReason);
                    fragmentMap[DiscountTypeFor.AUTOMATIC] = DiscountAutomaticFragment(item,cart);
                    optionsPagerAdapter.submitList(fragmentMap.values);
                    listener.onProductChooseForDiscount(item);
                }
            })
        binding.itemDiscountContainer.apply {
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    LinearLayoutManager.VERTICAL
                ).apply {
                    setDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.divider_vertical
                        )!!
                    )
                }
            )
        }
        binding.itemDiscountContainer.adapter = itemDiscountAdapter

        // Container Fragment Type For Adapter
        optionsPagerAdapter = OptionsPagerAdapter(childFragmentManager, lifecycle);
        binding.discountFragmentContainer.adapter = optionsPagerAdapter;
    }

    override fun initData() {

        // Data Discount Tab Adapter
        val listTab = mutableListOf(
            DiscountTypeTab(title = "Amount (đ)", type = DiscountTypeFor.AMOUNT),
            DiscountTypeTab(title = "Percentage (%)", type = DiscountTypeFor.PERCENTAGE),
            DiscountTypeTab(title = "Automatic" , type = DiscountTypeFor.AUTOMATIC ),
            DiscountTypeTab(title = "Comp", type = DiscountTypeFor.COMP),
        )
        if (applyToType == DiscountApplyToType.ORDER_DISCOUNT_APPLY_TO)
            listTab.add(
                2,
                DiscountTypeTab(title = "Discount Code", type = DiscountTypeFor.DISCOUNT_CODE)
            )
        adapter.submitList(listTab);

        // Data Discount Item Adapter
        itemDiscountAdapter.submitList(cart.productsList)

        // Reason Init
        if (applyToType == DiscountApplyToType.ORDER_DISCOUNT_APPLY_TO)
            viewModel.reasonChosen.postValue(cart.compReason);
        else viewModel.reasonChosen.postValue(null);

        // Data Container Fragment Type
        fragmentMap[DiscountTypeFor.AMOUNT] =
            DiscountAmountFragment(listener = object : DiscountTypeListener {
                override fun discountUserChoose(discount: DiscountUser) {
                    listener.discountUserChoose(discount);
                }
            });
        fragmentMap[DiscountTypeFor.PERCENTAGE] =
            DiscountPercentageFragment(listener = object : DiscountTypeListener {
                override fun discountUserChoose(discount: DiscountUser) {
                    listener.discountUserChoose(discount);
                }
            });
        fragmentMap[DiscountTypeFor.DISCOUNT_CODE] = DiscountCodeFragment(applyToType);
        fragmentMap[DiscountTypeFor.AUTOMATIC] = DiscountAutomaticFragment(null,cart);
        viewModel.reasonChosen.observe(this, {
            fragmentMap[DiscountTypeFor.COMP] =
                DiscountCompFragment(comp = it, listener = object : DiscountTypeListener {
                    override fun compReasonChoose(item: Reason) {
                        listener.compReasonChoose(item);
                    }

                    override fun compRemoveAll() {
                        listener.compRemoveAll();
                    }
                })
            optionsPagerAdapter.submitList(fragmentMap.values);
        });

        optionsPagerAdapter.submitList(fragmentMap.values);

    }

    override fun initAction() {

    }

    interface DiscountTypeListener {
        fun discountUserChoose(discount: DiscountUser): Unit {};
        fun compReasonChoose(item: Reason): Unit {};
        fun compRemoveAll(): Unit {};
        fun onProductChooseForDiscount(product: BaseProductInCart): Unit {}
    }

}