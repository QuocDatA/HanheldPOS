package com.hanheldpos.ui.screens.pincode

import android.view.WindowManager
import com.hanheldpos.R
import com.hanheldpos.databinding.ActivityPinCodeBinding
import com.hanheldpos.extension.navigateTo
import com.hanheldpos.ui.base.activity.BaseActivity
import com.hanheldpos.ui.screens.cashdrawer.startdrawer.StartDrawerActivity
import com.hanheldpos.ui.screens.main.MainActivity
import com.hanheldpos.ui.screens.pincode.adapter.PinCodeAdapter
import com.utils.helper.SystemHelper

class PinCodeActivity : BaseActivity<ActivityPinCodeBinding, PinCodeVM>(), PinCodeUV {
    override fun layoutRes() = R.layout.activity_pin_code;

    override fun initView() {
        binding.root.viewTreeObserver.addOnGlobalLayoutListener{
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            SystemHelper.hideSystemUI(this.window);
        }
    }

    override fun initData() {

    }

    override fun initAction() {
        binding.numberPad.adapter = PinCodeAdapter(
            elements = viewModel.initNumberPadList(),
            listener = object : PinCodeAdapter.ItemClickListener {
                override fun onClick(item: PinCodeRecyclerElement?) {
                    viewModel.onClick(item);
                }
            }
        )
    }

    override fun viewModelClass(): Class<PinCodeVM> {
        return PinCodeVM::class.java;
    }

    override fun initViewModel(viewModel: PinCodeVM) {
        viewModel.run {
            init(this@PinCodeActivity);
            binding.viewModel = this;
        }

    }

    override fun goBack() {
        finish();
    }

    override fun goHome() {
        navigateTo(MainActivity::class.java, alsoFinishCurrentActivity = true, alsoClearActivity = false);
    }

    override fun goStartDrawer() {
        navigateTo(StartDrawerActivity::class.java, alsoFinishCurrentActivity = true, alsoClearActivity = false);
    }

}