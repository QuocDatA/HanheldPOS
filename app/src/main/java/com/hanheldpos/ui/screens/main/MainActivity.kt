package com.hanheldpos.ui.screens.main

import android.graphics.Color
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.WindowManager
import com.hanheldpos.R
import com.hanheldpos.databinding.ActivityMainBinding
import com.hanheldpos.ui.base.activity.BaseFragmentBindingActivity
import com.hanheldpos.ui.base.dialog.AppAlertDialog
import com.hanheldpos.ui.base.fragment.FragmentNavigator
import com.hanheldpos.ui.screens.home.HomeFragment
import com.hanheldpos.ui.screens.home.ScreenViewModel
import com.hanheldpos.ui.screens.root.RootFragment
import com.utils.helper.SystemHelper

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

        getNavigator().rootFragment = HomeFragment();
    }

    override fun initData() {

    }

    override fun initAction() {

    }


}