package com.hanheldpos.ui.screens.product

import android.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.product.ProductItem
import com.hanheldpos.data.api.pojo.product.getModifierList
import com.hanheldpos.databinding.FragmentProductDetailBinding
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.cart.*
import com.hanheldpos.model.combo.ItemActionType
import com.hanheldpos.model.product.BaseProductInCart
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.home.order.OrderFragment
import com.hanheldpos.ui.screens.product.adapter.OptionsPagerAdapter
import com.hanheldpos.ui.screens.product.options.OptionVM
import com.hanheldpos.ui.screens.product.options.modifier.ModifierFragment
import com.hanheldpos.ui.screens.product.options.variant.VariantFragment
import kotlinx.coroutines.*


class ProductDetailFragment(
    private val item: Regular,
    private val groupBundle : GroupBundle? = null,
    private val productBundle : ProductItem? = null,
    private val quantityCanChoose: Int = -1,
    private val action: ItemActionType,
    private val listener: OrderFragment.OrderMenuListener? = null,
) : BaseFragment<FragmentProductDetailBinding, ProductDetailVM>(), ProductDetailUV,
    OptionVM.OptionListener {
    enum class OptionPage(val pos: Int, val text: String) {
        Variant(0, "Variant"),
        Modifier(1, "Modifier");
    }

    private val fragmentMap: MutableMap<OptionPage, Fragment> = mutableMapOf()

    // ViewModel
    private val optionVM by activityViewModels<OptionVM>();

    // Adapter
    private lateinit var optionsPagerAdapter: OptionsPagerAdapter;

    // Dialog Note
    private lateinit var dialogCategory: AlertDialog;

    override fun layoutRes() = R.layout.fragment_product_detail;

    override fun viewModelClass(): Class<ProductDetailVM> {
        return ProductDetailVM::class.java;
    }

    override fun initViewModel(viewModel: ProductDetailVM) {
        viewModel.run {
            init(this@ProductDetailFragment);
            initLifeCycle(this@ProductDetailFragment);
            binding.viewModel = viewModel;
        }
        optionVM.init(this);
    }

    override fun initView() {
        // Init Options
        optionsPagerAdapter = OptionsPagerAdapter(childFragmentManager, lifecycle);
        binding.optionContainer.adapter = optionsPagerAdapter;
        TabLayoutMediator(binding.tabOption, binding.optionContainer) { tab, position ->
            run {
                when (position) {
                    0 -> {
                        tab.text = "VARIANT"
                    }
                    1 -> {
                        tab.text = "MODIFIER"
                    }
                }
            }
        }.attach()

    }

    override fun initData() {
        viewModel.actionType.value = action;
        viewModel.regularInCart.value = item;
        viewModel.maxQuantity = quantityCanChoose;

        viewModel.regularInCart.value.let {

            fragmentMap[OptionPage.Variant] =
                VariantFragment(it?.proOriginal?.variantsGroup, it?.variantList, it?.proOriginal!!);
            fragmentMap[OptionPage.Modifier] = ModifierFragment(
                it.modifierList,
                it.proOriginal?.getModifierList(
                    DataHelper.orderMenuResp!!
                )
            );
            optionsPagerAdapter.submitList(fragmentMap.values);

        }
    }

    override fun initAction() {
        if (viewModel.regularInCart.value?.proOriginal?.variantsGroup == null) {
            GlobalScope.launch(Dispatchers.IO) {
                delay(300);
                launch(Dispatchers.Main) {
                    binding.tabOption.getTabAt(0)?.view?.isClickable = false;
                    binding.tabOption.getTabAt(1)?.select();
                    binding.optionContainer.currentItem = 1;
                }
            }
        }
    }

    override fun onBack() {
        navigator.goOneBack();
    }

    override fun onAddCart(item: BaseProductInCart) {
        onBack();
        listener?.onCartAdded(item, viewModel.actionType.value!!);
    }

    override fun onModifierAddItem(item: ModifierCart) {
        viewModel.onModifierAddItem(item);
    }

    override fun onModifierRemoveItem(item: ModifierCart) {
        viewModel.onModifierRemoveItem(item);
    }

    override fun onVariantItemChange(
        item: List<VariantCart>,
        groupValue: String,
        priceOverride: Double,
        sku: String
    ) {
        viewModel.onVariantItemChange(item, groupBundle, productBundle ,groupValue, priceOverride, sku);
    }


}