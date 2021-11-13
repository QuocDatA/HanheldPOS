package com.hanheldpos.ui.screens.product.options.modifier

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentModifierBinding
import com.hanheldpos.model.cart.ModifierCart
import com.hanheldpos.model.product.GroupExtra
import com.hanheldpos.model.product.ItemExtra
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.product.adapter.modifier.ContainerModifierAdapter
import com.hanheldpos.ui.screens.product.adapter.modifier.ModifierSelectedItemModel
import com.hanheldpos.ui.screens.product.options.OptionVM


class ModifierFragment(
    private val modifierInCarts : List<ModifierCart>,
    private val listGroupExtra: List<GroupExtra>?
) : BaseFragment<FragmentModifierBinding,ModifierVM>(),ModifierUV {

    //ViewModel
    private val optionVM by activityViewModels<OptionVM>();

    // Adapter
    private lateinit var containerModifierAdapter: ContainerModifierAdapter;

    override fun layoutRes() = R.layout.fragment_modifier;

    override fun viewModelClass(): Class<ModifierVM> {
        return ModifierVM::class.java;
    }

    override fun initViewModel(viewModel: ModifierVM) {
        viewModel.run {
            init(this@ModifierFragment);
            initLifeCycle(this@ModifierFragment);
        }
    }

    override fun initView() {

        containerModifierAdapter = ContainerModifierAdapter(
            itemSelected = modifierInCarts,
            listener = object : BaseItemClickListener<ItemExtra>{
            override fun onItemClick(adapterPosition: Int, item: ItemExtra) {
                onSelectedItemExtra(item);
            }

        }).also {
            binding.containerModifier.adapter = it;
        }
    }

    override fun initData() {
        this.listGroupExtra?.let { viewModel.listGroupExtra.addAll(it) };
        containerModifierAdapter.submitList(viewModel.listGroupExtra);
    }

    override fun initAction() {

    }

    fun onSelectedItemExtra(item: ItemExtra){
        optionVM.modifierItemChange(item);
    }

}

