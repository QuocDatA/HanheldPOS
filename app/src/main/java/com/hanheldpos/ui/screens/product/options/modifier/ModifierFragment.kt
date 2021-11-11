package com.hanheldpos.ui.screens.product.options.modifier

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentModifierBinding
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.product.adapter.modifier.ContainerModifierAdapter
import com.hanheldpos.ui.screens.product.adapter.modifier.ModifierSelectedItemModel
import com.hanheldpos.ui.screens.product.options.OptionVM


class ModifierFragment(

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

//        containerModifierAdapter = ContainerModifierAdapter(
//            itemSeleted = optionVM.extraDoneModel?.selectedModifiers,
//            listener = object : BaseItemClickListener<ModifierSelectedItemModel>{
//            override fun onItemClick(adapterPosition: Int, item: ModifierSelectedItemModel) {
//                optionVM.modifierItemChange(item);
//            }
//
//        }).also {
//            binding.containerModifier.adapter = it;
//        }
//
//        viewModel.defaultModifierListLD.observe(this,{
//            containerModifierAdapter.submitList(it);
//            containerModifierAdapter.notifyDataSetChanged();
//        });
    }

    override fun initData() {
//        arguments?.let {
//            val a: ExtraData? = it.getParcelable(ARG_PRODUCT_EXTRA_FRAGMENT)
//            viewModel.defaultModifierListLD.value = (a?.getDefaultModifierList());
//        }
    }

    override fun initAction() {

    }

}

