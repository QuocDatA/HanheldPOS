package com.hanheldpos.ui.screens.combo

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.SystemClock
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.order.settings.Reason
import com.hanheldpos.databinding.FragmentComboBinding
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.UserHelper
import com.hanheldpos.model.cart.Combo
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.combo.ItemActionType
import com.hanheldpos.model.combo.ItemComboGroup
import com.hanheldpos.model.discount.DiscountTypeFor
import com.hanheldpos.model.discount.DiscountUser
import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.model.discount.DiscApplyTo
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cart.CartDataVM
import com.hanheldpos.ui.screens.combo.adapter.ComboGroupAdapter
import com.hanheldpos.ui.screens.discount.DiscountFragment
import com.hanheldpos.ui.screens.discount.discount_type.DiscountTypeItemFragment
import com.hanheldpos.ui.screens.home.order.OrderFragment
import com.hanheldpos.ui.screens.product.ProductDetailFragment
import kotlinx.coroutines.*

class ComboFragment(
    private val combo: Combo,
    private val quantityCanChoose: Int = -1,
    private val action: ItemActionType,
    private val listener: OrderFragment.OrderMenuListener,
    private val isDiscountBuyXGetY: Boolean? = false,
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
            binding
        }
    }

    override fun initView() {
        // check to show discount fragment
        binding.isDiscountBuyXGetY = isDiscountBuyXGetY

        comboGroupAdapter = ComboGroupAdapter(
            proOriginal = combo.proOriginal!!, listener = object : ComboGroupAdapter.ItemListener {
            override fun onProductSelect(
                maxQuantity: Int,
                group: ItemComboGroup,
                item: Regular,
                actionType: ItemActionType
            ) {
                openProductDetail(maxQuantity, group, item,actionType)
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

        if (action == ItemActionType.Modify)
            childFragmentManager.beginTransaction().replace(
                R.id.fragment_container_discount,
                DiscountTypeItemFragment(
                    product = combo,
                    applyToType = DiscApplyTo.ITEM,
                    cart = cartDataVM.cartModelLD.value!!,
                    listener = object : DiscountFragment.DiscountTypeListener {
                        override fun discountUserChoose(
                            discount: DiscountUser,
                            isBuyXGetY: Boolean?
                        ) {
                            if (viewModel.isValidDiscount.value != true) return;
                            viewModel.bundleInCart.value?.addDiscountUser(discount)
                            viewModel.bundleInCart.notifyValueChange();
                        }

                        override fun compReasonChoose(item: Reason) {
                            if (viewModel.isValidDiscount.value != true) return;
                            viewModel.bundleInCart.value?.addCompReason(item)
                            viewModel.bundleInCart.notifyValueChange();
                        }

                        override fun compRemoveAll() {
                            viewModel.bundleInCart.value?.clearCompReason()
                            viewModel.bundleInCart.notifyValueChange();
                        }

                        override fun discountFocus(type: DiscountTypeFor) {
                            viewModel.typeDiscountSelect = type;
                        }

                        override fun validDiscount(isValid: Boolean) {
                            viewModel.isValidDiscount.postValue(isValid);
                        }

                        override fun clearAllDiscountCoupon() {
                            viewModel.bundleInCart.value?.clearAllDiscountCoupon()
                            viewModel.bundleInCart.notifyValueChange()
                        }

                    })
            ).commit();
    }

    override fun initData() {
        viewModel.bundleInCart.value = combo;
        viewModel.actionType.value = action;
        viewModel.maxQuantity = quantityCanChoose;
        CoroutineScope(Dispatchers.IO).launch {
            while(!isVisible){}
            viewModel.getCombo()?.let {
                viewModel.initDefaultComboList(
                    it,
                    cartDataVM.diningOptionLD.value!!,
                    UserHelper.getLocationGuid()
                )
            };
        }
    }

    override fun initAction() {
    }

    override fun onBack() {
        navigator.goOneBack();
    }

    @SuppressLint("NotifyDataSetChanged")
    fun openProductDetail(
        maxQuantity: Int,
        group: ItemComboGroup,
        item: Regular,
        action: ItemActionType
    ) {
        if (SystemClock.elapsedRealtime() - viewModel.mLastTimeClick <= 500) return;
        viewModel.mLastTimeClick = SystemClock.elapsedRealtime();
        when(action){
            ItemActionType.Remove -> {
                viewModel.onRegularSelect(group.groupBundle, item, item , action)
                comboGroupAdapter.notifyDataSetChanged()
            }
            else->{
                navigator.goTo(ProductDetailFragment(
                    regular = item.clone(),
                    groupBundle = group.groupBundle,
                    productBundle = this.combo.proOriginal,
                    quantityCanChoose = maxQuantity,
                    action = action,
                    listener = object : OrderFragment.OrderMenuListener {
                        @SuppressLint("NotifyDataSetChanged")
                        override fun onCartAdded(itemAfter: BaseProductInCart, action: ItemActionType) {
                            viewModel.onRegularSelect(group.groupBundle, item,itemAfter as Regular, action)
                            comboGroupAdapter.notifyDataSetChanged()
                        }
                    }
                ))
            }
        }

    }


    override fun cartAdded(item: BaseProductInCart, action: ItemActionType) {
        if ((viewModel.numberQuantity.value
                ?: 0) > 0 && (viewModel.isValidDiscount.value == false && action == ItemActionType.Modify) && !isDiscountBuyXGetY!!
        ) return;
        requireActivity().supportFragmentManager.setFragmentResult("saveDiscount", Bundle().apply { putSerializable("DiscountTypeFor", viewModel.typeDiscountSelect) });
        onBack();
        listener.onCartAdded(item, action);
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onLoadComboSuccess(list: List<ItemComboGroup>) {
        CoroutineScope(Dispatchers.Main).launch {
            comboGroupAdapter.submitList(list.toMutableList());
            comboGroupAdapter.notifyDataSetChanged();
        }
    }

}