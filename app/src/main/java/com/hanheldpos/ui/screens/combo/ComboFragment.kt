package com.hanheldpos.ui.screens.combo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentComboBinding
import com.hanheldpos.model.cart.Combo
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.cart.order.OrderItemModel
import com.hanheldpos.model.combo.ItemActionType
import com.hanheldpos.model.combo.ItemComboGroup
import com.hanheldpos.model.home.order.menu.ProductMenuItem
import com.hanheldpos.model.product.BaseProductInCart
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.combo.adapter.ComboGroupAdapter
import com.hanheldpos.ui.screens.home.order.OrderFragment
import com.hanheldpos.ui.screens.product.ProductDetailFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ComboFragment(
    private val item: Combo,
    private val quantityCanChoose: Int = -1,
    private val action: ItemActionType,
    private val listener: OrderFragment.OrderMenuListener
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
                group: ItemComboGroup,
                item: ProductMenuItem
            ) {

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
        viewModel.bundleInCart.value = item;
        viewModel.actionType.value = action;
        GlobalScope.launch(Dispatchers.IO) {
            viewModel.getCombo()?.let { viewModel.initDefaultComboList(it) };
        }


    }

    override fun initAction() {
    }



    override fun onBack() {
        navigator.goOneBack();
    }

    fun openProductDetail(
        maxQuantity: Int,
//        comboManager: ItemComboGroupManager,
//        item: ComboPickedItemViewModel,
        action: ItemActionType
    ) {
//        navigator.goToWithCustomAnimation(ProductDetailFragment(
//            item = Regular(),
//            quantityCanChoose = maxQuantity,
//            action = action,
//            listener = object : ProductDetailFragment.ProductDetailListener {
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
//
//                override fun onCartAdded(item: BaseProductInCart, action: ItemActionType) {
//                    TODO("Not yet implemented")
//                }
//            }
//        ))
    }


    override fun cartAdded(item: OrderItemModel, action: ItemActionType) {
//        onBack();
//        listener.onCartAdded(item, action);
    }

    override fun onLoadComboSuccess(list: List<ItemComboGroup>) {
        GlobalScope.launch(Dispatchers.Main) {
            comboGroupAdapter.submitList(list.toMutableList());
            comboGroupAdapter.notifyDataSetChanged();
        }
    }

}