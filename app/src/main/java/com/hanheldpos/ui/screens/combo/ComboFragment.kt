package com.hanheldpos.ui.screens.combo

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.SystemClock
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentComboBinding
import com.hanheldpos.model.UserHelper
import com.hanheldpos.model.cart.Combo
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.cart.order.OrderItemModel
import com.hanheldpos.model.combo.ItemActionType
import com.hanheldpos.model.combo.ItemComboGroup
import com.hanheldpos.model.home.order.menu.ProductMenuItem
import com.hanheldpos.model.product.BaseProductInCart
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cart.CartDataVM
import com.hanheldpos.ui.screens.combo.adapter.ComboGroupAdapter
import com.hanheldpos.ui.screens.combo.adapter.ComboItemChosenAdapter
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

    private val cartDataVM by activityViewModels<CartDataVM>()

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
                item: Regular
            ) {
                openProductDetail(maxQuantity, group, item, ItemActionType.Add)
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
            viewModel.getCombo()?.let {
                viewModel.initDefaultComboList(
                    it,
                    cartDataVM.diningOptionLD.value!!,
                    UserHelper.getLocationGui()
                )
            };
        }


    }

    override fun initAction() {
    }


    override fun onBack() {
        navigator.goOneBack();
    }

    fun openProductDetail(
        maxQuantity: Int,
        group: ItemComboGroup,
        item: Regular,
        action: ItemActionType
    ) {
        if (SystemClock.elapsedRealtime() - viewModel.mLastTimeClick <= 500) return;
        viewModel.mLastTimeClick = SystemClock.elapsedRealtime();
        navigator.goToWithCustomAnimation(ProductDetailFragment(
            item = item,
            quantityCanChoose = maxQuantity,
            action = action,
            listener = object : OrderFragment.OrderMenuListener {
                override fun onCartAdded(item: BaseProductInCart, action: ItemActionType) {
                    TODO("Not yet implemented")
                }

            }
        ))
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