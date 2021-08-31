package com.hanheldpos.ui.screens.product.options.modifier

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentModifierBinding
import com.hanheldpos.ui.base.fragment.BaseFragment


class ModifierFragment : BaseFragment<FragmentModifierBinding,ModifierVM>(),ModifierUV {
    override fun layoutRes() = R.layout.fragment_modifier;

    override fun viewModelClass(): Class<ModifierVM> {
        return ModifierVM::class.java;
    }

    override fun initViewModel(viewModel: ModifierVM) {

    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun initAction() {

    }

}