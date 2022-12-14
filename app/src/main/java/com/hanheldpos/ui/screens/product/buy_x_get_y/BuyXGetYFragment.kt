package com.hanheldpos.ui.screens.product.buy_x_get_y

import android.annotation.SuppressLint
import android.os.SystemClock
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.discount.CustomerBuys
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.api.pojo.fee.CustomerGets
import com.hanheldpos.databinding.FragmentBuyXGetYBinding
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.model.cart.Combo
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.product.buy_x_get_y.BuyXGetY
import com.hanheldpos.model.product.buy_x_get_y.GroupBuyXGetY
import com.hanheldpos.model.product.combo.ItemActionType
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.discount.discount_type.discount_code.DiscountCodeFragment
import com.hanheldpos.ui.screens.home.order.OrderFragment
import com.hanheldpos.ui.screens.product.buy_x_get_y.adapter.BuyXGetYGroupAdapter
import com.hanheldpos.ui.screens.product.combo.ComboFragment
import com.hanheldpos.ui.screens.product.regular.RegularDetailFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BuyXGetYFragment(
    private val buyXGetY: BuyXGetY,
    private val discount: DiscountResp,
    private val actionType: ItemActionType,
    private val quantityCanChoose: Int = -1,
    private val listener: DiscountCodeFragment.BuyXGetYCallBack
) :
    BaseFragment<FragmentBuyXGetYBinding, BuyXGetYVM>(), BuyXGetYUV {
    override fun layoutRes(): Int = R.layout.fragment_buy_x_get_y

    private lateinit var buyXGetYGroupAdapter: BuyXGetYGroupAdapter

    override fun viewModelClass(): Class<BuyXGetYVM> {
        return BuyXGetYVM::class.java
    }

    override fun initViewModel(viewModel: BuyXGetYVM) {
        viewModel.run {
            init(this@BuyXGetYFragment)
            initLifeCycle(this@BuyXGetYFragment)
            binding.viewModel = this
            binding.discount = discount
        }
    }

    override fun initView() {
        buyXGetYGroupAdapter = BuyXGetYGroupAdapter(listener = object :
            BuyXGetYGroupAdapter.BuyXGetYItemListener {
            override fun onProductSelect(
                maxQuantity: Int,
                group: GroupBuyXGetY,
                item: BaseProductInCart,
                actionType: ItemActionType,
            ) {
                openProductDetail(maxQuantity, group, item, actionType)
            }
        })
        binding.rvBuyXGetYGroup.apply {
            adapter = buyXGetYGroupAdapter
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
            itemAnimator = null
        }
    }

    override fun initData() {
        viewModel.buyXGetY.value = buyXGetY
        viewModel.actionType.value = actionType
        viewModel.maxQuantity = quantityCanChoose

        binding.rvBuyXGetYGroup.visibility = View.GONE
        lifecycleScope.launch(Dispatchers.IO) {
            while(!isVisible) {}
            lifecycleScope.launch(Dispatchers.Main) {
                viewModel.listItemBuyXGetYGroup.addAll(viewModel.initDefaultList() ?: mutableListOf())
                buyXGetYGroupAdapter.submitList(viewModel.listItemBuyXGetYGroup)
                binding.rvBuyXGetYGroup.visibility = View.VISIBLE
            }
        }
    }

    override fun initAction() {
    }

    @SuppressLint("NotifyDataSetChanged")
    fun openProductDetail(
        maxQuantity: Int,
        group: GroupBuyXGetY,
        baseItem: BaseProductInCart,
        action: ItemActionType,
    ) {
        if (SystemClock.elapsedRealtime() - viewModel.mLastTimeClick <= 500) return
        viewModel.mLastTimeClick = SystemClock.elapsedRealtime()
        when (action) {
            ItemActionType.Remove -> {
                if (baseItem is Regular) {
                    viewModel.onRegularSelect(group, baseItem, baseItem, action, discount)
                } else if (baseItem is Combo) {
                    viewModel.onBundleSelect(group, baseItem, baseItem, action, discount)
                }
                buyXGetYGroupAdapter.notifyDataSetChanged()
                viewModel.buyXGetY.notifyValueChange()
                viewModel.buyXGetY.value
            }
            else -> {
                baseItem.proOriginal.let {
                    if (!it?.isBundle()!!) {
                        navigator.goTo(
                            RegularDetailFragment(
                                regular = (baseItem as Regular).clone(),
                                groupBundle = null,
                                productBundle = null,
                                quantityCanChoose = maxQuantity,
                                action = action,
                                isDiscountBuyXGetY = true,
                                isGroupBuy = group.condition is CustomerBuys,
                                listener = object : OrderFragment.OrderMenuListener {
                                    @SuppressLint("NotifyDataSetChanged")
                                    override fun onCartAdded(
                                        item: BaseProductInCart,
                                        action: ItemActionType
                                    ) {
                                        (item as Regular).sku
                                        (item).variants
                                        viewModel.onRegularSelect(
                                            group,
                                            baseItem,
                                            (item).clone(),
                                            action,
                                            if (group.condition is CustomerGets) discount else null,
                                        )
                                        val isGroupBuy = group.condition is CustomerBuys
                                        viewModel.isGroupBuy.value = isGroupBuy
                                        buyXGetYGroupAdapter.notifyDataSetChanged()
                                        viewModel.buyXGetY.notifyValueChange()
                                    }
                                }
                            ),
                        )
                    } else {
                        val combo = ComboFragment(
                            combo = (baseItem as Combo).clone(),
                            action = action,
                            quantityCanChoose = maxQuantity,
                            isDiscountBuyXGetY = true,
                            listener = object : OrderFragment.OrderMenuListener {
                                override fun onCartAdded(
                                    item: BaseProductInCart,
                                    action: ItemActionType
                                ) {
                                    //add to buy x get y
                                    viewModel.onBundleSelect(
                                        group,
                                        (item as Combo).clone(),
                                        baseItem,
                                        action,
                                        if (group.condition is CustomerGets) discount else null
                                    )
                                    buyXGetYGroupAdapter.notifyDataSetChanged()
                                    viewModel.buyXGetY.notifyValueChange()
                                }
                            }
                        )
                        navigator.goTo(combo)
                    }
                }

            }
        }
    }

    override fun cartAdded(item: BaseProductInCart, action: ItemActionType) {
        listener.onCartAdded(item, action)
        onFragmentBackPressed()
    }

    override fun onDiscountBuyXGetYEntireOrder(discount: DiscountResp) {
        listener.onDiscountBuyXGetYEntireOrder(discount)
    }
}