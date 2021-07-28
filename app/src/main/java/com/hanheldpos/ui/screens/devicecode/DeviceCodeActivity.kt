package com.hanheldpos.ui.screens.devicecode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hanheldpos.R
import com.hanheldpos.databinding.ActivityDeviceCodeBinding
import com.hanheldpos.extension.navigateTo
import com.hanheldpos.ui.base.activity.BaseActivity
import com.hanheldpos.ui.screens.pincode.PinCodeActivity
import com.utils.helper.SystemHelper

class DeviceCodeActivity : BaseActivity<ActivityDeviceCodeBinding,DeviceCodeVM>(), DeviceCodeUV {


    override fun layoutRes() = R.layout.activity_device_code;

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
            init(this@DeviceCodeActivity);
            binding.viewModel = this;
        }
    }

    override fun goBack() {
        finish();
    }

    override fun openPinCode() {
        navigateTo(PinCodeActivity::class.java,false);
    }

}