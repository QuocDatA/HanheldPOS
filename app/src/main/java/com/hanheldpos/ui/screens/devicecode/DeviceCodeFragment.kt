package com.hanheldpos.ui.screens.devicecode

import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentDeviceCodeBinding
import com.hanheldpos.extension.navigateTo
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.main.MainActivity
import com.hanheldpos.ui.screens.pincode.PinCodeFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeviceCodeFragment : BaseFragment<FragmentDeviceCodeBinding, DeviceCodeVM>(), DeviceCodeUV {


    override fun layoutRes() = R.layout.fragment_device_code;

    override fun initView() {

    }

    override fun initData() {
    }

    override fun initAction() {
    }

    override fun viewModelClass(): Class<DeviceCodeVM> {
        return DeviceCodeVM::class.java;
    }

    override fun initViewModel(viewModel: DeviceCodeVM) {
        viewModel.run {
            init(this@DeviceCodeFragment);
            binding.viewModel = this;
        }
    }

    override fun goBack() {
        requireActivity().finish()
    }

    override fun openPinCode() {
        CoroutineScope(Dispatchers.Main).launch {
            navigator.goTo(PinCodeFragment())
        }

    }

}