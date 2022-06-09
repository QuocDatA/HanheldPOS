package com.hanheldpos.ui.screens.product

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.order.settings.Reason
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.data.api.pojo.product.VariantsGroup
import com.hanheldpos.databinding.FragmentProductDetailBinding
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.cart.*
import com.hanheldpos.model.combo.ItemActionType
import com.hanheldpos.model.discount.DiscApplyTo
import com.hanheldpos.model.discount.DiscountTypeFor
import com.hanheldpos.model.discount.DiscountUser
import com.hanheldpos.model.product.GroupExtra
import com.hanheldpos.model.product.ItemExtra
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cart.CartDataVM
import com.hanheldpos.ui.screens.discount.DiscountFragment
import com.hanheldpos.ui.screens.discount.discount_type.DiscountTypeItemFragment
import com.hanheldpos.ui.screens.home.order.OrderFragment
import com.hanheldpos.ui.screens.product.adapter.GroupModifierAdapter
import com.hanheldpos.ui.screens.product.adapter.GroupVariantAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailFragment(
    private val regular: Regular,
    private val groupBundle: GroupBundle? = null,
    private val productBundle: Product? = null,
    private val quantityCanChoose: Int = -1,
    private val action: ItemActionType,
    private val listener: OrderFragment.OrderMenuListener? = null,
    private val isDiscountBuyXGetY: Boolean? = false,
    private val isGroupBuy: Boolean? = false
) : BaseFragment<FragmentProductDetailBinding, ProductDetailVM>(), ProductDetailUV {

    private val cartDataVM by activityViewModels<CartDataVM>()

    private lateinit var groupVariantAdapter: GroupVariantAdapter
    private lateinit var groupModifierAdapter: GroupModifierAdapter

    override fun layoutRes(): Int = R.layout.fragment_product_detail

    override fun viewModelClass(): Class<ProductDetailVM> {
        return ProductDetailVM::class.java
    }

    override fun initViewModel(viewModel: ProductDetailVM) {
        viewModel.run {
            init(this@ProductDetailFragment)
            binding.viewModel = this
        }
    }


    override fun initView() {
        // Check to show discount fragment or not
        binding.isDiscountBuyXGetY = isDiscountBuyXGetY
        viewModel.isGroupBuy = isGroupBuy

        // Variant Group
        groupVariantAdapter = GroupVariantAdapter(
            listener = object :
                BaseItemClickListener<VariantsGroup.OptionValueVariantsGroup> {
                override fun onItemClick(
                    adapterPosition: Int,
                    item: VariantsGroup.OptionValueVariantsGroup
                ) {
                    onSelectedVariant(item)
                }
            },
        ).also {
            binding.groupVariants.adapter = it
            binding.groupVariants.itemAnimator = null
        }

        // Modifier Group
        groupModifierAdapter = GroupModifierAdapter(
            productBundle ?: regular.proOriginal!!,
            regular.modifierList,
            listener = object : BaseItemClickListener<ItemExtra> {
                override fun onItemClick(adapterPosition: Int, item: ItemExtra) {
                    onSelectedModifier(item)
                }

            }
        ).also {
            binding.groupModifiers.adapter = it
            binding.groupVariants.itemAnimator = null
        }

        if (action == ItemActionType.Modify && productBundle == null)
            childFragmentManager.beginTransaction().replace(
                R.id.fragment_container_discount,
                DiscountTypeItemFragment(
                    product = regular,
                    applyToType = DiscApplyTo.ITEM,
                    cart = cartDataVM.cartModelLD.value!!,
                    listener = object : DiscountFragment.DiscountTypeListener {
                        override fun discountUserChoose(discount: DiscountUser, isBuyXGetY: Boolean?) {
                            if (viewModel.isValidDiscount.value != true) return
                            viewModel.regularInCart.value?.addDiscountUser(discount)
                            viewModel.regularInCart.notifyValueChange()
                        }

                        override fun compReasonChoose(item: Reason) {
                            if (viewModel.isValidDiscount.value != true) return
                            viewModel.regularInCart.value?.addCompReason(item)
                            viewModel.regularInCart.notifyValueChange()
                        }

                        override fun compRemoveAll() {
                            viewModel.regularInCart.value?.clearCompReason()
                            viewModel.regularInCart.notifyValueChange()
                        }

                        override fun discountFocus(type: DiscountTypeFor) {
                            viewModel.typeDiscountSelect = type
                        }

                        override fun validDiscount(isValid: Boolean) {
                            viewModel.isValidDiscount.postValue(isValid)
                        }

                        override fun clearAllDiscountCoupon() {
                            viewModel.regularInCart.value?.clearAllDiscountCoupon()
                            viewModel.regularInCart.notifyValueChange()
                        }
                    })
            ).commit()
    }

    override fun initData() {
        // init data
        viewModel.actionType.value = action
        viewModel.productBundle = productBundle
        viewModel.groupBundle = groupBundle
        viewModel.regularInCart.value = regular
        viewModel.maxQuantity = quantityCanChoose

        regular.apply {
            proOriginal?.VariantsGroup.let {
                if (it == null) {
                    binding.groupVariants.visibility = View.GONE
                } else
                    viewModel.listVariantGroups.add(it)
            }
            variantList?.let {
                groupVariantAdapter.itemSelected = it
            }
            proOriginal?.ProductModifiersList.let { modifiers ->
                if (modifiers == null) {
                    binding.groupModifiers.visibility = View.GONE
                } else
                    viewModel.listModifierGroups.addAll(modifiers.map {
                        GroupExtra(
                            modifierExtra = it,
                            modifierList = it.ModifierItemList.map { modifierItem ->
                                ItemExtra(
                                    modifier = modifierItem,
                                    productPricing = proOriginal!!,
                                    //maxExtraQuantity = it.MaximumModifier,
                                    // TODO : Temp replace max quantity
                                    maxExtraQuantity = 1000,
                                )
                            }
                        )
                    })
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            while (!isVisible) {
            }
            launch(Dispatchers.Main) {
                groupVariantAdapter.submitList(viewModel.listVariantGroups)
                groupModifierAdapter.submitList(viewModel.listModifierGroups)
            }

        }


    }

    override fun initAction() {

    }

    fun onSelectedVariant(item: VariantsGroup.OptionValueVariantsGroup) {
        // Add variant selected
        if (viewModel.regularInCart.value!!.variantList?.size ?: 0 >= item.Level) {
            viewModel.regularInCart.value!!.variantList!![item.Level - 1] =
                VariantCart(item.Id, item.Value)
        } else {
            if (viewModel.regularInCart.value!!.variantList == null) viewModel.regularInCart.value!!.variantList =
                mutableListOf()
            viewModel.regularInCart.value!!.variantList?.add(VariantCart(item.Id, item.Value))
        }
        groupVariantAdapter.itemSelected = viewModel.regularInCart.value!!.variantList

        // Last level variant
        if (item.Variant?.OptionValueList == null || !item.Variant.OptionValueList.any()) {

            viewModel.regularInCart.value!!.apply {
                updateVariant(item.Sku, item.GroupValue)
            }
            viewModel.regularInCart.notifyValueChange()
            return
        }
        item.Variant.let { group ->
            if (viewModel.listVariantGroups.size > item.Level) {
                viewModel.listVariantGroups[item.Level] = group
                binding.groupVariants.post {
                    groupVariantAdapter.notifyItemChanged(item.Level)
                }
            } else {
                viewModel.listVariantGroups.add(group)
                binding.groupVariants.post {
                    groupVariantAdapter.notifyItemInserted(item.Level)
                }
            }
        }
        viewModel.regularInCart.notifyValueChange()

    }

    fun onSelectedModifier(item: ItemExtra) {
        val modifier = ModifierCart(
            item.modifier._Id,
            item.modifier.Modifier,
            item.extraQuantity,
            item.modifier.Price,
        )
        if (item.extraQuantity > 0) {
            viewModel.regularInCart.value?.apply {
                addModifier(modifier)
            }

        } else {
            viewModel.regularInCart.value
                ?.apply {
                    removeModifier(modifier)
                }
        }
        viewModel.regularInCart.notifyValueChange()
    }

    override fun getBack() {
        onFragmentBackPressed()
    }

    override fun onAddCart(item: BaseProductInCart) {
        // Check if product is combo or regular to check discount
        if (productBundle == null && (viewModel.numberQuantity.value
                ?: 0) > 0 && (viewModel.isValidDiscount.value == false && action == ItemActionType.Modify) && !isDiscountBuyXGetY!!
        ) return

        requireActivity().supportFragmentManager.setFragmentResult(
            "saveDiscount",
            Bundle().apply {
                putSerializable("DiscountTypeFor", viewModel.typeDiscountSelect)
            })
        getBack()
        listener?.onCartAdded(item, viewModel.actionType.value!!)
    }

}