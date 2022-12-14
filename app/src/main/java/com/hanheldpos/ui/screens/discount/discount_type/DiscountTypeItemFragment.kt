package com.hanheldpos.ui.screens.discount.discount_type

import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.diadiem.pos_components.PTextView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.order.settings.Reason
import com.hanheldpos.databinding.FragmentDiscountTypeItemBinding
import com.hanheldpos.extension.setOnClickDebounce
import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.model.cart.CartModel
import com.hanheldpos.model.discount.DiscApplyTo
import com.hanheldpos.model.discount.DiscountTypeFor
import com.hanheldpos.model.discount.DiscountTypeTab
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.discount.DiscountFragment
import com.hanheldpos.ui.screens.discount.adapter.OptionsPagerAdapter
import com.hanheldpos.ui.screens.discount.discount_type.amount.DiscountAmountFragment
import com.hanheldpos.ui.screens.discount.discount_type.comp.DiscountCompFragment
import com.hanheldpos.ui.screens.discount.discount_type.percentage.DiscountPercentageFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DiscountTypeItemFragment(
    private val applyToType: DiscApplyTo,
    private val cart: CartModel,
    private val product: BaseProductInCart? = null,
    private val listener: DiscountFragment.DiscountTypeListener
) :
    BaseFragment<FragmentDiscountTypeItemBinding, DiscountTypeVM>(),
    DiscountTypeUV {
    // Adapter
    private lateinit var optionsPagerAdapter: OptionsPagerAdapter

    // Fragment child
    private val fragmentMap: MutableMap<DiscountTypeFor, Fragment> = mutableMapOf()
    private val listTab: MutableList<DiscountTypeTab> = mutableListOf()

    override fun layoutRes(): Int = R.layout.fragment_discount_type_item

    override fun viewModelClass(): Class<DiscountTypeVM> {
        return DiscountTypeVM::class.java
    }

    override fun initViewModel(viewModel: DiscountTypeVM) {
        viewModel.run {
            init(this@DiscountTypeItemFragment)
            binding.viewModel = this
            binding.discountType = applyToType
        }
    }

    override fun initView() {


        // Container Fragment Type For Adapter
        optionsPagerAdapter = OptionsPagerAdapter(childFragmentManager, lifecycle)
        binding.discountFragmentContainer.apply {
            adapter = optionsPagerAdapter
        }
    }

    override fun initData() {
        // Data Container Fragment Type
        fragmentMap[DiscountTypeFor.AMOUNT] =
            DiscountAmountFragment(
                listener = listener,
                applyToType = applyToType
            )
        fragmentMap[DiscountTypeFor.PERCENTAGE] =
            DiscountPercentageFragment(
                applyToType,
                listener = listener
            )

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
                        listener.validDiscount(isValid)
                    }
                }, applyToType = applyToType
            )

        optionsPagerAdapter.submitList(fragmentMap.values)

        // Tab
        listTab.addAll(
            mutableListOf(
                DiscountTypeTab(title = "Amount (??)", type = DiscountTypeFor.AMOUNT),
                DiscountTypeTab(title = "Percentage (%)", type = DiscountTypeFor.PERCENTAGE),
                DiscountTypeTab(title = "Comp", type = DiscountTypeFor.COMP),
            )
        )

        TabLayoutMediator(
            binding.tabDiscountType, binding.discountFragmentContainer
        ) { tab, position ->
            tab.text = listTab[position].title
        }.attach()
    }

    override fun initAction() {
        viewModel.isExistDiscountToClear.observe(this) {
            viewModel.typeDiscountSelect.value?.let { type ->
                Log.d("Tab Selection", fragmentMap[type].toString())
                lifecycleScope.launch(Dispatchers.IO) {
                    do {
                        if (fragmentMap[type]?.isVisible == true) {
                            break
                        }
                    } while (true)
                    withContext(Dispatchers.Main) {
                        val btn =
                            fragmentMap[type]?.view?.findViewById<PTextView>(R.id.btnClearDiscount)
                        btn?.run {
                            setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    if (it) R.color.color_0 else R.color.color_8
                                )
                            )
                            setOnClickDebounce {
                                if (it) {
                                    listener.clearAllDiscountCoupon()
                                    viewModel.isExistDiscountToClear.postValue(false)
                                }
                            }
                        }
                    }
                }
            }

        }

        binding.tabDiscountType.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tabSelected(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                tabSelected(tab)
            }

        })
        binding.tabDiscountType.getTabAt(0)?.select()
    }

    private fun tabSelected(tab: TabLayout.Tab?) {
        tab ?: return
        viewModel.typeDiscountSelect.postValue(listTab[tab.position].type)
        viewModel.isExistDiscountToClear.postValue(
           product?.isExistDiscountToDelete() ?: false
        )
        listener.discountFocus(listTab[tab.position].type)
    }
}