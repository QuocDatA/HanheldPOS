package com.hanheldpos.ui.screens.product.options.variant

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentVariantBinding
import com.hanheldpos.model.product.ExtraData
import com.hanheldpos.model.product.getDefaultVariantProduct
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.product.adapter.variant.ContainerVariantAdapter
import com.hanheldpos.ui.screens.product.adapter.variant.VariantLayoutItem
import com.hanheldpos.ui.screens.product.options.OptionVM


class VariantFragment : BaseFragment<FragmentVariantBinding, VariantVM>(), VariantUV {

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
            itemSelected = optionVM.extraDoneModel?.selectedVariant,
            listener = object : BaseItemClickListener<String?> {
                override fun onItemClick(adapterPosition: Int, item: String?) {
                    viewModel.getGroupItemByGroupName(item)?.let { optionVM.variantItemChange(it) };
                }

            }
        ).also {
            binding.containerVariant.adapter = it;
        }

        viewModel.listVariantHeader.observe(this, {
            containerVariantAdapter.submitList(it);
            containerVariantAdapter.notifyDataSetChanged();
        });
    }

    override fun initData() {
        arguments?.let {
            val a: ExtraData? = it.getParcelable(ARG_PRODUCT_EXTRA_FRAGMENT)
            viewModel.listVariantHeader.value =
                viewModel.initDefaultVariantHeaderList(a?.getDefaultVariantProduct());
        }
    }

    override fun initAction() {

    }

    companion object {
        private const val ARG_PRODUCT_EXTRA_FRAGMENT = "ARG_PRODUCT_EXTRA_FRAGMENT"
        fun getInstance(
            item: ExtraData,
        ): VariantFragment {
            return VariantFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PRODUCT_EXTRA_FRAGMENT, item)
                }
            };
        }
    }

}