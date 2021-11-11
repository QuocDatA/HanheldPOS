package com.hanheldpos.ui.screens.combo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentComboBinding
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.cart.order.OrderItemModel
import com.hanheldpos.model.home.order.combo.ItemActionType
import com.hanheldpos.model.home.order.menu.ComboPickedItemViewModel
import com.hanheldpos.model.home.order.menu.ItemComboGroupManager
import com.hanheldpos.model.home.order.menu.OrderMenuComboItemModel
import com.hanheldpos.model.product.BaseProductInCart
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.combo.adapter.ComboGroupAdapter
import com.hanheldpos.ui.screens.product.ProductDetailFragment

class ComboFragment(
    private val listener: ComboListener
) : BaseFragment<FragmentComboBinding, ComboVM>(), ComboUV {
    override fun layoutRes() = R.layout.fragment_combo;

    override fun viewModelClass(): Class<ComboVM> {
        return ComboVM::class.java;
    }

    private lateinit var comboGroupAdapter: ComboGroupAdapter;

    override fun initViewModel(viewModel: ComboVM) {
        viewModel.run {
            init(this@ComboFragment);
            initLifeCycle(this@ComboFragment);
            binding.viewModel = viewModel
        }
    }

    override fun initView() {
        comboGroupAdapter = ComboGroupAdapter(listener = object : ComboGroupAdapter.ItemListener {
            override fun onProductSelect(
                maxQuantity: Int,
                comboManager: ItemComboGroupManager,
                item: ComboPickedItemViewModel,
                action: ItemActionType
            ) {
                when (action) {
                    ItemActionType.Remove -> {
                        viewModel.onChooseItemComboSuccess(
                            item.comboParentId,
                            comboManager,
                            item, action
                        );
                    }
                    else -> openProductDetail(maxQuantity, comboManager, item, action);
                }

            }

        });

        binding.comboGroupAdapter.apply {
            adapter = comboGroupAdapter;
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
        arguments?.let {
            val a: OrderItemModel? = it.getParcelable(ARG_ORDER_ITEM_MODEL_FRAGMENT)
            val quantityCanChoose: Int = it.getInt(ARG_PRODUCT_DETAIL_QUANTITY)
            val actionType: ItemActionType =
                it.getSerializable(ARG_ITEM_ACTION_TYPE) as ItemActionType;
            viewModel.actionType.value = actionType;
            viewModel.orderItemModel.value = a;
            viewModel.maxQuantity = quantityCanChoose;
        }
        viewModel.getCombo()?.let { viewModel.initDefaultComboList(it) };

    }

    override fun initAction() {
    }

    interface ComboListener {
        fun onCartAdded(item: OrderItemModel, action: ItemActionType)
    }

    companion object {
        private const val ARG_ORDER_ITEM_MODEL_FRAGMENT = "ARG_ORDER_ITEM_MODEL_FRAGMENT"
        private const val ARG_PRODUCT_DETAIL_QUANTITY = "ARG_PRODUCT_DETAIL_QUANTITY"
        private const val ARG_ITEM_ACTION_TYPE = "ARG_ITEM_ACTION_TYPE"
        fun getInstance(
            item: OrderItemModel,
            quantityCanChoose: Int = -1,
            action: ItemActionType,
            listener: ComboListener
        ): ComboFragment {
            return ComboFragment(
                listener = listener
            ).apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_ORDER_ITEM_MODEL_FRAGMENT, item)
                    putSerializable(ARG_ITEM_ACTION_TYPE, action);
                    putInt(ARG_PRODUCT_DETAIL_QUANTITY, quantityCanChoose)
                }
            };
        }
    }

    override fun onBack() {
        navigator.goOneBack();
    }

    fun openProductDetail(
        maxQuantity: Int,
        comboManager: ItemComboGroupManager,
        item: ComboPickedItemViewModel,
        action: ItemActionType
    ) {
        navigator.goToWithCustomAnimation(ProductDetailFragment(
            item = Regular(),
            quantityCanChoose = maxQuantity,
            action = action,
            listener = object : ProductDetailFragment.ProductDetailListener {
//                override fun onCartAdded(itemDone: OrderItemModel, action: ItemActionType) {
//                    item.apply {
//                        selectedComboItem = itemDone
//                    }
//                    viewModel.onChooseItemComboSuccess(
//                        item.comboParentId,
//                        comboManager,
//                        item,
//                        if (item.selectedComboItem!!.quantity > 0) action else ItemActionType.Remove
//                    );
//                }

                override fun onCartAdded(item: BaseProductInCart, action: ItemActionType) {
                    TODO("Not yet implemented")
                }
            }
        ))
    }


    override fun cartAdded(item: OrderItemModel, action: ItemActionType) {
        onBack();
        listener.onCartAdded(item, action);
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun updateChangeCombo(item: OrderMenuComboItemModel?) {
        comboGroupAdapter.submitList(item?.listItemsByGroup as MutableList<ItemComboGroupManager>?);
        comboGroupAdapter.notifyDataSetChanged();
    }

}