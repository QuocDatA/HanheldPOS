package com.hanheldpos.ui.screens.menu.settings.hardware

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentHardwareBinding
import com.hanheldpos.ui.base.fragment.BaseFragment

class HardwareFragment : BaseFragment<FragmentHardwareBinding,HardwareVM>() , HardwareUV {
    override fun layoutRes(): Int {
        return R.layout.fragment_hardware
    }

    override fun viewModelClass(): Class<HardwareVM> {
        return HardwareVM::class.java
    }

    override fun initViewModel(viewModel: HardwareVM) {
        viewModel.run {
            init(this@HardwareFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun initAction() {

    }

}