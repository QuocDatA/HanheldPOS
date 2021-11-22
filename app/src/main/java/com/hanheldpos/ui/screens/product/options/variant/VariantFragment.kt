package com.hanheldpos.ui.screens.product.options.variant

import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.product.ProductItem
import com.hanheldpos.data.api.pojo.product.VariantsGroup
import com.hanheldpos.databinding.FragmentVariantBinding
import com.hanheldpos.model.UserHelper
import com.hanheldpos.model.cart.VariantCart
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.product.adapter.variant.ContainerVariantAdapter
import com.hanheldpos.ui.screens.product.options.OptionVM


class VariantFragment(
    private val item: VariantsGroup?,
    private val variantCart: List<VariantCart>?,
    private val product: ProductItem
) : BaseFragment<FragmentVariantBinding, VariantVM>(), VariantUV {

    init {

    }

    //ViewModel
    private val optionVM by activityViewModels<OptionVM>();

    // Adapter
    private lateinit var containerVariantAdapter: ContainerVariantAdapter;

    override fun layoutRes() = R.layout.fragment_variant;

    override fun viewModelClass(): Class<VariantVM> {
        return VariantVM::class.java;
    }

    override fun initViewModel(viewModel: VariantVM) {
        viewModel.run {
            init(this@VariantFragment);
            initLifeCycle(this@VariantFragment);
        }
    }

    override fun initView() {

        containerVariantAdapter = ContainerVariantAdapter(
            listener = object : BaseItemClickListener<VariantsGroup.OptionValueVariantsGroup> {
                override fun onItemClick(
                    adapterPosition: Int,
                    item: VariantsGroup.OptionValueVariantsGroup
                ) {
                    onSelectedItem(item);
                }
            }
        ).also {
            binding.containerVariant.adapter = it;
        }

        containerVariantAdapter.submitList(viewModel.listVariantGroups);
    }

    fun onSelectedItem(item: VariantsGroup.OptionValueVariantsGroup) {

        // Add variant selected
        if (viewModel.listVariants.size >= item.Level) {
            viewModel.listVariants[item.Level-1] = VariantCart(item.Id,item.Value);
        } else{
            viewModel.listVariants.add(VariantCart(item.Id,item.Value))
        }
        containerVariantAdapter.itemSelected = viewModel.listVariants

        // Last level variant
        if (item.Variant?.OptionValueList == null || !item.Variant.OptionValueList.any()) {
            val priceOverride =
                product.priceOverride(UserHelper.getLocationGui(), item.Sku, item.Price)
            optionVM.variantItemChange(
                viewModel.listVariants,
                item.GroupValue,
                priceOverride,
                item.Sku
            );
            return;
        }
        item.Variant.let { group ->
            if (viewModel.listVariantGroups.size > item.Level) {
                viewModel.listVariantGroups[item.Level] = group
                binding.containerVariant.post {
                    containerVariantAdapter.notifyItemChanged(item.Level);
                }
            } else{
                viewModel.listVariantGroups.add(group)
                binding.containerVariant.post{
                    containerVariantAdapter.notifyItemInserted(item.Level);
                }

            }

        }
    }

    override fun initData() {
        item?.let { viewModel.listVariantGroups.add(it) }
        variantCart?.let {
            viewModel.listVariants.addAll(it)
            containerVariantAdapter.itemSelected = viewModel.listVariants
        };
    }

    override fun initAction() {

    }

}