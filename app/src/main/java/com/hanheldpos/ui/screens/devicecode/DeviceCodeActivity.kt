package com.hanheldpos.ui.screens.devicecode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.hanheldpos.R
import com.hanheldpos.databinding.ActivityDeviceCodeBinding
import com.hanheldpos.extension.navigateTo
import com.hanheldpos.ui.base.activity.BaseActivity
import com.hanheldpos.ui.screens.pincode.PinCodeActivity
import com.hanheldpos.utils.NetworkUtils
import com.utils.helper.SystemHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeviceCodeActivity : BaseActivity<ActivityDeviceCodeBinding, DeviceCodeVM>(), DeviceCodeUV {


    override fun layoutRes() = R.layout.activity_device_code;

    override fun initView() {
        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
            SystemHelper.hideSystemUI(this.window);
        }

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
        navigateTo(PinCodeActivity::class.java, false);
    }

}