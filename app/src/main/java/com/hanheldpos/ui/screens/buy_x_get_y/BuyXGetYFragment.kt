package com.hanheldpos.ui.screens.buy_x_get_y

import android.annotation.SuppressLint
import android.os.SystemClock
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.databinding.FragmentBuyXGetYBinding
import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.buy_x_get_y.GroupBuyXGetY
import com.hanheldpos.model.combo.ItemActionType
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.buy_x_get_y.adapter.BuyXGetYGroupAdapter
import com.hanheldpos.ui.screens.home.order.OrderFragment
import com.hanheldpos.ui.screens.product.ProductDetailFragment

class BuyXGetYFragment(private val discount: DiscountResp) :
    BaseFragment<FragmentBuyXGetYBinding, BuyXGetYVM>(), BuyXGetYUV {
    override fun layoutRes(): Int = R.layout.fragment_buy_x_get_y

    private lateinit var buyXGetYGroupAdapter: BuyXGetYGroupAdapter

    override fun viewModelClass(): Class<BuyXGetYVM> {
        return BuyXGetYVM::class.java
    }

    override fun initViewModel(viewModel: BuyXGetYVM) {
        viewModel.run {
            init(this@BuyXGetYFragment)
            binding.viewModel = this
            binding.discount = discount
        }
    }

    override fun initView() {
        buyXGetYGroupAdapter = BuyXGetYGroupAdapter(listener = object: BuyXGetYGroupAdapter.BuyXGetYItemListener {
            override fun onProductSelect(
                maxQuantity: Int,
                group: GroupBuyXGetY,
                item: Regular,
                actionType: ItemActionType
            ) {
                openProductDetail(maxQuantity, group, item, actionType, discount)
            }

        })
        binding.buyXGetYGroupAdapter.apply {
            adapter = buyXGetYGroupAdapter;
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
    }

    override fun initData() {
        val listItemBuyXGetYGroup = viewModel.initDefaultList(discount)
        binding.isComplete = viewModel.isSelectedComplete()
        buyXGetYGroupAdapter.submitList(listItemBuyXGetYGroup)
    }

    override fun initAction() {
    }

    @SuppressLint("NotifyDataSetChanged")
    fun openProductDetail(
        maxQuantity: Int,
        group: GroupBuyXGetY,
        item: Regular,
        action: ItemActionType,
        discount: DiscountResp
    ) {
        if (SystemClock.elapsedRealtime() - viewModel.mLastTimeClick <= 500) return;
        viewModel.mLastTimeClick = SystemClock.elapsedRealtime();
        when (action) {
            ItemActionType.Remove -> {
                viewModel.onRegularSelect(group, item, item, action, discount)
                buyXGetYGroupAdapter.notifyDataSetChanged()
            }
            else -> {
                navigator.goTo(ProductDetailFragment(
                    regular = item.clone(),
                    groupBundle = null,
                    productBundle = null,
                    quantityCanChoose = maxQuantity,
                    action = action,
                    listener = object : OrderFragment.OrderMenuListener {
                        @SuppressLint("NotifyDataSetChanged")
                        override fun onCartAdded(
                            itemAfter: BaseProductInCart,
                            action: ItemActionType
                        ) {
                            viewModel.onRegularSelect(
                                group,
                                item,
                                itemAfter as Regular,
                                action,
                                discount
                            )
                            buyXGetYGroupAdapter.notifyDataSetChanged()
                            binding.isComplete = viewModel.isSelectedComplete()
                        }
                    }
                ))
            }
        }
    }

}