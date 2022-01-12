package com.hanheldpos.ui.screens.product

import android.view.View
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.product.ProductItem
import com.hanheldpos.data.api.pojo.product.VariantsGroup
import com.hanheldpos.data.api.pojo.product.getModifierList
import com.hanheldpos.databinding.FragmentProductDetailBinding
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.UserHelper
import com.hanheldpos.model.cart.GroupBundle
import com.hanheldpos.model.cart.ModifierCart
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.cart.VariantCart
import com.hanheldpos.model.combo.ItemActionType
import com.hanheldpos.model.product.BaseProductInCart
import com.hanheldpos.model.product.ItemExtra
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.home.order.OrderFragment
import com.hanheldpos.ui.screens.product.adapter.GroupModifierAdapter
import com.hanheldpos.ui.screens.product.adapter.GroupVariantAdapter

class ProductDetailFragment(
    private val regular: Regular,
    private val groupBundle: GroupBundle? = null,
    private val productBundle: ProductItem? = null,
    private val quantityCanChoose: Int = -1,
    private val action: ItemActionType,
    private val listener: OrderFragment.OrderMenuListener? = null,
) : BaseFragment<FragmentProductDetailBinding, ProductDetailVM>(), ProductDetailUV {

    private lateinit var groupVariantAdapter: GroupVariantAdapter;
    private lateinit var groupModifierAdapter: GroupModifierAdapter;

    override fun layoutRes(): Int = R.layout.fragment_product_detail;

    override fun viewModelClass(): Class<ProductDetailVM> {
        return ProductDetailVM::class.java;
    }

    override fun initViewModel(viewModel: ProductDetailVM) {
        viewModel.run {
            init(this@ProductDetailFragment);
            initLifeCycle(this@ProductDetailFragment);
            binding.viewModel = this;
        }


    }


    override fun initView() {

        groupVariantAdapter = GroupVariantAdapter(
            listener = object :
                BaseItemClickListener<VariantsGroup.OptionValueVariantsGroup> {
                override fun onItemClick(
                    adapterPosition: Int,
                    item: VariantsGroup.OptionValueVariantsGroup
                ) {
                    onSelectedVariant(item);
                }
            },
        ).also {
            binding.groupVariants.adapter = it;
            binding.groupVariants.itemAnimator = null
        }

        groupModifierAdapter = GroupModifierAdapter(
            regular.modifierList,
            listener = object : BaseItemClickListener<ItemExtra> {
                override fun onItemClick(adapterPosition: Int, item: ItemExtra) {
                    onSelectedModifier(item);
                }

            }
        ).also {
            binding.groupModifiers.adapter = it;
            binding.groupVariants.itemAnimator = null
        }

    }

    override fun initData() {
        // init data
        viewModel.actionType.value = action;
        viewModel.regularInCart.value = regular;
        viewModel.maxQuantity = quantityCanChoose;

        groupVariantAdapter.submitList(viewModel.listVariantGroups);
        groupModifierAdapter.submitList(viewModel.listModifierGroups);

        regular.apply {

            proOriginal?.variantsGroup.let {
                if (it == null) {
                    binding.groupVariants.visibility = View.GONE;
                } else
                    viewModel.listVariantGroups.add(it)
            }
            variantList?.let {
                groupVariantAdapter.itemSelected = it;
            };

            proOriginal?.getModifierList(
                DataHelper.orderMenuResp!!
            ).let {
                if (it == null) {
                    binding.groupModifiers.visibility = View.GONE;
                } else
                    viewModel.listModifierGroups.addAll(it)
            }

        }

    }

    override fun initAction() {

    }

    fun onSelectedVariant(item: VariantsGroup.OptionValueVariantsGroup) {

        // Add variant selected
        if (viewModel.regularInCart.value!!.variantList?.size ?: 0 >= item.Level) {
            viewModel.regularInCart.value!!.variantList!![item.Level - 1] =
                VariantCart(item.Id, item.Value);
        } else {
            if (viewModel.regularInCart.value!!.variantList == null) viewModel.regularInCart.value!!.variantList =
                mutableListOf()
            viewModel.regularInCart.value!!.variantList?.add(VariantCart(item.Id, item.Value))
        }
        groupVariantAdapter.itemSelected = viewModel.regularInCart.value!!.variantList

        // Last level variant
        if (item.Variant?.OptionValueList == null || !item.Variant.OptionValueList.any()) {
            val priceOverride =
                viewModel.regularInCart.value!!.proOriginal?.priceOverride(
                    UserHelper.getLocationGui(),
                    item.Sku,
                    item.Price
                )

            viewModel.regularInCart.value!!.apply {
                this.sku = item.Sku
                if (productBundle != null)
                    this.priceOverride =
                        viewModel.regularInCart.value!!.groupPrice(groupBundle!!, productBundle)
                else
                    this.priceOverride = priceOverride
            }
            viewModel.regularInCart.notifyValueChange();
            return;
        }
        item.Variant.let { group ->
            if (viewModel.listVariantGroups.size > item.Level) {
                viewModel.listVariantGroups[item.Level] = group
                binding.groupVariants.post {
                    groupVariantAdapter.notifyItemChanged(item.Level);
                }
            } else {
                viewModel.listVariantGroups.add(group)
                binding.groupVariants.post {
                    groupVariantAdapter.notifyItemInserted(item.Level);
                }
            }
        }
        viewModel.regularInCart.notifyValueChange();

    }

    fun onSelectedModifier(item: ItemExtra) {
        val modifier = ModifierCart(
            item.modifier.id!!,
            item.modifier.modifierGuid!!,
            item.modifier.modifier!!,
            item.extraQuantity,
            item.modifier.price
        )
        if (item.extraQuantity > 0)
        {
            viewModel.regularInCart.value?.apply {
                modifierList.find { m ->
                    m.modifierId == modifier.modifierId
                }.let {
                    if (it != null) {
                        val index = modifierList.indexOf(it);
                        modifierList[index] = modifier;
                    } else {
                        modifierList.add(modifier)
                    }
                }
            }

        }
        else {
            viewModel.regularInCart.value
                ?.apply {
                    modifierList.removeAll { it.modifierGuid == modifier.modifierGuid }
                }
        }
        viewModel.regularInCart.notifyValueChange();
    }

    override fun getBack() {
        onFragmentBackPressed();
    }

    override fun onAddCart(item: BaseProductInCart) {
        getBack();
        listener?.onCartAdded(item, viewModel.actionType.value!!);
    }

}