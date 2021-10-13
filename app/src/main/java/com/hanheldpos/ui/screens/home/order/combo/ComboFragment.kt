package com.hanheldpos.ui.screens.home.order.combo

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentComboBinding
import com.hanheldpos.model.cart.order.OrderItemModel
import com.hanheldpos.model.home.order.combo.ItemActionType
import com.hanheldpos.model.home.order.menu.ComboPickedItemViewModel
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
            val a: OrderItemModel? = it.getParcelable(ARG_ORDER_ITEM_MODEL_FRAGMENT)
            val quantityCanChoose: Int = it.getInt(ARG_PRODUCT_DETAIL_QUANTITY)
            val actionType : ItemActionType = it.getSerializable(ARG_ITEM_ACTION_TYPE) as ItemActionType;
            viewModel.actionType.value = actionType;
            viewModel.orderItemModel.value = a;
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
        fun onCartAdded(item: OrderItemModel)
    }

    companion object {
        private const val ARG_ORDER_ITEM_MODEL_FRAGMENT = "ARG_ORDER_ITEM_MODEL_FRAGMENT"
        private const val ARG_PRODUCT_DETAIL_QUANTITY = "ARG_PRODUCT_DETAIL_QUANTITY"
        private const val ARG_ITEM_ACTION_TYPE = "ARG_ITEM_ACTION_TYPE"
        fun getInstance(
            item: OrderItemModel,
            quantityCanChoose: Int = -1,
            action : ItemActionType,
            listener: ComboListener
        ): ComboFragment {
            return ComboFragment(
                listener = listener
            ).apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_ORDER_ITEM_MODEL_FRAGMENT, item)
                    putSerializable(ARG_ITEM_ACTION_TYPE,action);
                    putInt(ARG_PRODUCT_DETAIL_QUANTITY, quantityCanChoose)
                }
            };
        }
    }

    override fun onBack() {
        navigator.goOneBack();
    }

    override fun openProductDetail(maxQuantity: Int, item: ComboPickedItemViewModel,action: ItemActionType) {
        navigator.goToWithCustomAnimation(ProductDetailFragment.getInstance(
            item = item.selectedComboItem!!,
            quantityCanChoose = maxQuantity,
            action = action,
            listener = object : ProductDetailFragment.ProductDetailListener {
                override fun onCartAdded(itemDone: OrderItemModel,action: ItemActionType) {
                    viewModel.onChooseItemComboSuccess(
                        item.comboParentId,
                        item.apply { selectedComboItem = itemDone },
                        action
                    );
                }
            }
        ))
    }

    override fun cartAdded(item: OrderItemModel) {
        listener.onCartAdded(item);
        onBack();
    }

}