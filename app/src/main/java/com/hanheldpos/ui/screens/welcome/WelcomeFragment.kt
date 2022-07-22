package com.hanheldpos.ui.screens.welcome

import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.welcome.WelcomeRespModel
import com.hanheldpos.databinding.FragmentWelcomeBinding
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.devicecode.DeviceCodeFragment

class WelcomeFragment : BaseFragment<FragmentWelcomeBinding, WelcomeVM>(), WelcomeUV {

    override fun layoutRes() = R.layout.fragment_welcome

    override fun viewModelClass(): Class<WelcomeVM> {
        return WelcomeVM::class.java
    }

    override fun initViewModel(viewModel: WelcomeVM) {
        viewModel.run {
            init(this@WelcomeFragment)
            initLifeCycle(this@WelcomeFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {

    }

    override fun initData() {
        viewModel.initUI()
    }

    override fun initAction() {
    }

    override fun openDeviceCode() {
        navigator.goTo(DeviceCodeFragment())
    }

    override fun updateUI(welcomeResp: WelcomeRespModel?) {
        welcomeResp ?: return
        welcomeResp.Welcome.firstOrNull()?.Pages_Welcome?.firstOrNull()?.let {
            Glide.with(this).applyDefaultRequestOptions(RequestOptions()).load(it.Logo)
                .error(ContextCompat.getDrawable(requireContext(), R.drawable.ic_app)).into(binding.iconApp)
            Glide.with(this).applyDefaultRequestOptions(RequestOptions()).load(it.Background)
                .error(ContextCompat.getDrawable(requireContext(), R.drawable.bg_welcome))
                .into(binding.bgWelcome)
        }
        welcomeResp.Welcome.firstOrNull()?.Pages_Welcome_Text?.firstOrNull()?.let {
            binding.titleWelcome.text = it.Title
            binding.descriptionWelcome.text = it.Description
            binding.btnWelcome.text = it.Button
        }

    }

}