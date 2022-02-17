package com.hanheldpos.ui.screens.welcome

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.welcome.WelcomeRespModel
import com.hanheldpos.databinding.ActivityWelcomeBinding
import com.hanheldpos.extension.navigateTo
import com.hanheldpos.ui.base.activity.BaseActivity
import com.hanheldpos.ui.base.activity.BaseFragmentBindingActivity
import com.hanheldpos.ui.base.fragment.FragmentNavigator
import com.hanheldpos.ui.screens.devicecode.DeviceCodeActivity
import com.hanheldpos.ui.screens.main.MainActivity
import com.hanheldpos.ui.screens.pincode.PinCodeActivity
import com.utils.helper.SystemHelper

class WelcomeActivity : BaseActivity<ActivityWelcomeBinding, WelcomeVM>(), WelcomeUV {

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
        viewModel.initUI()
    }

    override fun initAction() {
        viewModel.checkDeviceCode();
    }

    override fun openDeviceCode() {
        navigateTo(DeviceCodeActivity::class.java, false);
    }

    override fun openPinCode() {
        navigateTo(PinCodeActivity::class.java, true);
    }

    override fun updateUI(welcomeResp: WelcomeRespModel?) {
        welcomeResp ?: return;
        welcomeResp.Welcome.firstOrNull()?.Pages_Welcome?.firstOrNull()?.let {
            Glide.with(this).applyDefaultRequestOptions(RequestOptions()).load(it.Logo)
                .error(ContextCompat.getDrawable(context, R.drawable.ic_app)).into(binding.iconApp)
            Glide.with(this).applyDefaultRequestOptions(RequestOptions()).load(it.Background)
                .error(ContextCompat.getDrawable(context, R.drawable.bg_welcome))
                .into(binding.bgWelcome)
        }
        welcomeResp.Welcome.firstOrNull()?.Pages_Welcome_Text?.firstOrNull()?.let {
            binding.titleWelcome.text = it.Title;
            binding.descriptionWelcome.text = it.Description;
            binding.btnWelcome.text = it.Button;
        }

    }

}