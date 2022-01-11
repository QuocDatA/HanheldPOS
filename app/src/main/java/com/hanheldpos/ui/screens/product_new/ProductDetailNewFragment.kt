package com.hanheldpos.ui.screens.product_new

import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.product.ProductItem
import com.hanheldpos.data.api.pojo.product.VariantsGroup
import com.hanheldpos.databinding.FragmentProductDetailNewBinding
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.UserHelper
import com.hanheldpos.model.cart.GroupBundle
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.cart.VariantCart
import com.hanheldpos.model.combo.ItemActionType
import com.hanheldpos.model.product.BaseProductInCart
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.home.order.OrderFragment
import com.hanheldpos.ui.screens.product_new.adapter.GroupVariantAdapter

class ProductDetailNewFragment(
    private val regular: Regular,
    private val groupBundle: GroupBundle? = null,
    private val productBundle: ProductItem? = null,
    private val quantityCanChoose: Int = -1,
    private val action: ItemActionType,
    private val listener: OrderFragment.OrderMenuListener? = null,
) : BaseFragment<FragmentProductDetailNewBinding, ProductDetailNewVM>(), ProductDetailNewUV {

    private lateinit var groupVariantAdapter: GroupVariantAdapter;

    override fun layoutRes(): Int = R.layout.fragment_product_detail_new;

    override fun viewModelClass(): Class<ProductDetailNewVM> {
        return ProductDetailNewVM::class.java;
    }

    override fun initViewModel(viewModel: ProductDetailNewVM) {
        viewModel.run {
            init(this@ProductDetailNewFragment);
            initLifeCycle(this@ProductDetailNewFragment);
            binding.viewModel = this;
        }
    }

    override fun initView() {

        binding.headerTitle.text = regular.getProductName();

        groupVariantAdapter = GroupVariantAdapter(
            listener = object :
                BaseItemClickListener<VariantsGroup.OptionValueVariantsGroup> {
                override fun onItemClick(
                    adapterPosition: Int,
                    item: VariantsGroup.OptionValueVariantsGroup
                ) {
                    onSelectedVariant(item);
                }
            }).also {
            binding.groupVariants.adapter = it;
            binding.groupVariants.itemAnimator = null
        }

    }

    override fun initData() {
        viewModel.actionType.value = action;
        viewModel.regularInCart.value = regular;
        viewModel.maxQuantity = quantityCanChoose;

        groupVariantAdapter.submitList(viewModel.listVariantGroups);

        regular.apply {
            proOriginal?.variantsGroup?.let { viewModel.listVariantGroups.add(it) }
            variantList?.let {
                groupVariantAdapter.itemSelected = it;
            };
        }

    }

    override fun initAction() {

    }

    fun onSelectedVariant(item: VariantsGroup.OptionValueVariantsGroup) {

        // Add variant selected
        if (viewModel.regularInCart.value!!.variantList?.size ?: 0 >= item.Level) {
            viewModel.regularInCart.value!!.variantList!![item.Level - 1] = VariantCart(item.Id, item.Value);
        } else {
            if (viewModel.regularInCart.value!!.variantList == null) viewModel.regularInCart.value!!.variantList = mutableListOf()
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
                    this.priceOverride = viewModel.regularInCart.value!!.groupPrice(groupBundle!!, productBundle)
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

    override fun getBack() {
        onFragmentBackPressed();
    }

    override fun onAddCart(item: BaseProductInCart) {
        getBack();
        listener?.onCartAdded(item, viewModel.actionType.value!!);
    }

}