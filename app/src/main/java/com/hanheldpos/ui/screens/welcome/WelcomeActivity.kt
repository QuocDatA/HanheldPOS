package com.hanheldpos.ui.screens.welcome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hanheldpos.R
import com.hanheldpos.databinding.ActivityWelcomeBinding
import com.hanheldpos.extension.navigateTo
import com.hanheldpos.ui.base.activity.BaseActivity
import com.hanheldpos.ui.base.activity.BaseFragmentBindingActivity
import com.hanheldpos.ui.base.fragment.FragmentNavigator
import com.hanheldpos.ui.screens.devicecode.DeviceCodeActivity
import com.hanheldpos.ui.screens.main.MainActivity
import com.hanheldpos.ui.screens.pincode.PinCodeActivity
import com.utils.helper.SystemHelper

class WelcomeActivity : BaseActivity<ActivityWelcomeBinding,WelcomeVM>(), WelcomeUV {

    override fun layoutRes() = R.layout.activity_welcome

    override fun viewModelClass(): Class<WelcomeVM> {
        return WelcomeVM::class.java;
    }

    override fun initViewModel(viewModel: WelcomeVM) {
        viewModel.run {
            init(this@WelcomeActivity)
            initLifeCycle(this@WelcomeActivity)
            binding.viewModel = this;
        }
    }

    override fun initView() {
        SystemHelper.hideSystemUI(window);

    }

    override fun initData() {

    }

    override fun initAction() {
        viewModel.checkDeviceCode();
    }

    override fun openDeviceCode() {
        navigateTo(DeviceCodeActivity::class.java,false);
    }

    override fun openPinCode() {
        navigateTo(PinCodeActivity::class.java,true);
    }

}