package com.hanheldpos.ui.screens.product

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.order.menu.GroupItem
import com.hanheldpos.databinding.FragmentProductDetailBinding
import com.hanheldpos.model.cart.order.OrderItemModel
import com.hanheldpos.model.product.ExtraDoneModel
import com.hanheldpos.model.product.ProductOrderItem
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.home.order.OrderDataVM
import com.hanheldpos.ui.screens.product.adapter.OptionsPagerAdapter
import com.hanheldpos.ui.screens.product.adapter.modifier.ModifierSelectedItemModel
import com.hanheldpos.ui.screens.product.options.OptionVM
import com.hanheldpos.ui.screens.product.options.modifier.ModifierFragment
import com.hanheldpos.ui.screens.product.options.variant.VariantFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ProductDetailFragment(
    private val listener: ProductDetailListener? = null
) : BaseFragment<FragmentProductDetailBinding, ProductDetailVM>(), ProductDetailUV , OptionVM.OptionListener {
    enum class OptionPage(val pos: Int, val text: String) {
        Variant(0, "Variant"),
        Modifier(1, "Modifier");
    }

    private val fragmentMap: MutableMap<OptionPage, Fragment> = mutableMapOf()
    // ViewModel
    private val orderDataVM by activityViewModels<OrderDataVM>();
    private val optionVM by activityViewModels<OptionVM>();

    // Adapter
    private lateinit var optionsPagerAdapter: OptionsPagerAdapter;

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
        arguments?.let {
            val a: ProductOrderItem? = it.getParcelable(ARG_PRODUCT_ITEM_FRAGMENT)
            val quantityCanChoose: Int = it.getInt(ARG_PRODUCT_DETAIL_QUANTITY)
            viewModel.extraDoneModel.value = ExtraDoneModel(
                productOrderItem = a,
            );
            viewModel.maxQuantity = quantityCanChoose;
        }

        GlobalScope.launch(Dispatchers.IO) {
            viewModel.extraDoneModel.value?.productOrderItem?.extraData?.let {
                fragmentMap[OptionPage.Variant] = VariantFragment.getInstance(it);
                fragmentMap[OptionPage.Modifier] = ModifierFragment.getInstance(it);
                launch(Dispatchers.Main) {
                    optionsPagerAdapter.submitList(fragmentMap.values);
                }
            }


        }
    }

    override fun initAction() {

    }

    interface ProductDetailListener {
        fun onAddCart(item: ExtraDoneModel)
    }

    companion object {
        private const val ARG_PRODUCT_ITEM_FRAGMENT = "ARG_PRODUCT_ITEM_FRAGMENT"
        private const val ARG_PRODUCT_DETAIL_QUANTITY = "ARG_PRODUCT_DETAIL_QUANTITY"
        fun getInstance(
            item: ProductOrderItem,
            quantityCanChoose: Int = -1,
            listener: ProductDetailListener? = null
        ): ProductDetailFragment {
            return ProductDetailFragment(
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

    override fun onAddCart(item: ExtraDoneModel) {
        onBack()
        listener?.onAddCart(item);
    }

    override fun onModifierItemChange(item: ModifierSelectedItemModel) {
        viewModel.onModifierQuantityChange(item.realItem?.modifier,item);
    }

    override fun onVariantItemChange(item: GroupItem) {
        viewModel.onVariantItemChange(item);
    }


}