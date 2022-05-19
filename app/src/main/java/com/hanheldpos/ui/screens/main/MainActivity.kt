package com.hanheldpos.ui.screens.main

import android.graphics.Color
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.databinding.ActivityMainBinding
import com.hanheldpos.ui.base.activity.BaseFragmentBindingActivity
import com.hanheldpos.ui.base.dialog.AppAlertDialog
import com.hanheldpos.ui.base.fragment.FragmentNavigator
import com.hanheldpos.ui.screens.home.HomeFragment
import com.hanheldpos.ui.screens.home.ScreenViewModel
import com.hanheldpos.ui.screens.pincode.PinCodeFragment
import com.hanheldpos.ui.screens.root.RootFragment
import com.hanheldpos.ui.screens.welcome.WelcomeFragment
import com.hanheldpos.utils.NetworkUtils
import com.hanheldpos.utils.StringUtils
import com.utils.helper.SystemHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : BaseFragmentBindingActivity<ActivityMainBinding, MainVM>(), MainUV {

    override fun createFragmentNavigator(): FragmentNavigator {
        return FragmentNavigator(supportFragmentManager, R.id.main_fragment_container)
    }

    override fun layoutRes() = R.layout.activity_main

    override fun viewModelClass(): Class<MainVM> {
        return MainVM::class.java
    }

    override fun initViewModel(viewModel: MainVM) {
        viewModel.run {
            init(this@MainActivity)
        }
    }

    override fun initView() {
        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            SystemHelper.hideSystemUI(window);
        }
        viewModel.initView()
    }

    override fun initData() {
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

    override fun initAction() {

    }

    override fun openPinCode() {
        getNavigator().rootFragment = PinCodeFragment()
    }

    override fun openWelcome() {
        getNavigator().rootFragment = WelcomeFragment()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        SystemHelper.hideSystemUI(window);
        super.onWindowFocusChanged(hasFocus)
    }
}