package com.hanheldpos.ui.screens.intro

import android.view.WindowManager
import com.hanheldpos.R
import com.hanheldpos.databinding.ActivityIntroBinding
import com.hanheldpos.extension.navigateTo
import com.hanheldpos.ui.base.activity.BaseActivity
import com.hanheldpos.ui.screens.main.MainActivity
import com.hanheldpos.utils.NetworkUtils
import com.utils.helper.SystemHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IntroActivity : BaseActivity<ActivityIntroBinding, IntroVM>(), IntroUV {

    override fun layoutRes() = R.layout.activity_intro

    override fun viewModelClass() = IntroVM::class.java

    override fun initViewModel(viewModel: IntroVM) {
        viewModel.run {
            init(this@IntroActivity)
            initLifeCycle(this@IntroActivity)
        }
    }

    override fun initView() {
        binding.root.viewTreeObserver.addOnGlobalLayoutListener { hideSystem(); }
    }

    override fun initData() {

    }

    override fun initAction() {
        NetworkUtils.checkActiveInternetConnection(listener = object : NetworkUtils.NetworkConnectionCallBack {
            override fun onAvailable() {

            }

            override fun onLost() {
                CoroutineScope(Dispatchers.Main).launch {
                    showAlert(title =  getString(R.string.notification), message =  getString(R.string.no_network_connection))
                }
            }
        })

    }

    override fun finishIntro() {
        navigateTo(MainActivity::class.java, true)
    }
    private fun hideSystem(){
        SystemHelper.hideSystemUI(window);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
}