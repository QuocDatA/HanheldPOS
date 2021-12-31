package com.hanheldpos.ui.screens.main

import android.graphics.Rect
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

//        this.window.decorView.viewTreeObserver.addOnGlobalLayoutListener {
//            val r = Rect()
//            window.decorView.getWindowVisibleDisplayFrame(r)
//            val height: Int =
//                binding.mainFragmentContainer.context.resources.displayMetrics.heightPixels
//            val diff: Int = height - r.bottom
//            if (diff != 0) {
//                if (binding.mainFragmentContainer.paddingBottom !== diff) {
//                    binding.mainFragmentContainer.setPadding(0, 0, 0, diff)
//                }
//            } else {
//                if (binding.mainFragmentContainer.paddingBottom !== 0) {
//                    binding.mainFragmentContainer.setPadding(0, 0, 0, 0)
//                }
//            }
//        }

        getNavigator().rootFragment = HomeFragment();
    }

    override fun initData() {

    }

    override fun initAction() {

    }


}