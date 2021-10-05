package com.hanheldpos.ui.screens.home.order.combo

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentComboBinding
import com.hanheldpos.model.cart.order.OrderItemModel
import com.hanheldpos.model.home.order.combo.ComboItemActionType
import com.hanheldpos.model.home.order.menu.ComboPickedItemViewModel
import com.hanheldpos.model.product.ExtraDoneModel
import com.hanheldpos.model.product.ItemApplyToType
import com.hanheldpos.model.product.ProductOrderItem
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.home.order.combo.adapter.ComboGroupAdapter
import com.hanheldpos.ui.screens.product.ProductDetailFragment

class ComboFragment(
    private val listener: ComboListener
) : BaseFragment<FragmentComboBinding, ComboVM>(), ComboUV {
    override fun layoutRes() = R.layout.fragment_combo;

    override fun viewModelClass(): Class<ComboVM> {
        return ComboVM::class.java;
    }

    override fun initViewModel(viewModel: ComboVM) {
        viewModel.run {
            init(this@ComboFragment);
            initLifeCycle(this@ComboFragment);
            binding.viewModel = viewModel
        }
    }

    override fun initView() {

    }

    override fun initData() {
        arguments?.let {
            val a: ProductOrderItem? = it.getParcelable(ARG_PRODUCT_ITEM_FRAGMENT)
            val quantityCanChoose: Int = it.getInt(ARG_PRODUCT_DETAIL_QUANTITY)
            viewModel.extraDoneModel.value = ExtraDoneModel(
                productOrderItem = a,
                itemApplyToType=ItemApplyToType.Combo,
            );
            viewModel.maxQuantity = quantityCanChoose;
        }

        viewModel.selectedCombo.observe(this, {
            binding.comboGroupAdapter.apply {
                adapter = it.data?.comboAdapter as ComboGroupAdapter
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
        })
        viewModel.getCombo()?.let { viewModel.initDefaultComboList(it) };

    }

    override fun initAction() {

    }

    interface ComboListener {
        fun onAddToCart(item: OrderItemModel)
    }

    companion object {
        private const val ARG_PRODUCT_ITEM_FRAGMENT = "ARG_PRODUCT_ITEM_FRAGMENT"
        private const val ARG_PRODUCT_DETAIL_QUANTITY = "ARG_PRODUCT_DETAIL_QUANTITY"
        fun getInstance(
            item: ProductOrderItem,
            quantityCanChoose: Int = -1,
            listener: ComboListener
        ): ComboFragment {
            return ComboFragment(
                listener = listener
            ).apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PRODUCT_ITEM_FRAGMENT, item)
                    putInt(ARG_PRODUCT_DETAIL_QUANTITY, quantityCanChoose)
                }
            };
        }
    }

    override fun onBack() {
        navigator.goOneBack();
    }

    override fun openProductDetail(parent : ProductOrderItem?,maxQuantity: Int, item: ComboPickedItemViewModel,action: ComboItemActionType?) {
        val cloneProductOrderItem = item.selectedComboItem!!.copy();
        navigator.goToWithCustomAnimation(ProductDetailFragment.getInstance(
            item = cloneProductOrderItem,
            extra = item.extraDoneModel ?: ExtraDoneModel().apply {
                productOrderItem = cloneProductOrderItem.apply {
                    modPricingType = parent!!.modPricingType;
                    modPricingValue = parent!!.modPricingValue;
                };
                itemApplyToType = ItemApplyToType.Combo;

            },
            quantityCanChoose = maxQuantity,
            listener = object : ProductDetailFragment.ProductDetailListener {
                override fun onAddCart(itemDone: ExtraDoneModel) {
                    viewModel.onChooseItemComboSuccess(
                        item.comboParentId,
                        item.apply { extraDoneModel = itemDone },
                        action
                    );
                }
            }
        ))
    }

}