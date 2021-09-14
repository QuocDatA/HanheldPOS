package com.hanheldpos.ui.screens.pincode

import android.view.WindowManager
import com.hanheldpos.R
import com.hanheldpos.databinding.ActivityPinCodeBinding
import com.hanheldpos.extension.navigateTo
import com.hanheldpos.ui.base.activity.BaseActivity
import com.hanheldpos.ui.screens.main.MainActivity
import com.hanheldpos.ui.screens.pincode.adapter.PinCodeAdapter
import com.utils.helper.SystemHelper

class PinCodeActivity : BaseActivity<ActivityPinCodeBinding, PinCodeVM>(), PinCodeUV {
    override fun layoutRes() = R.layout.activity_pin_code;

    override fun initView() {
        SystemHelper.hideSystemUI(this.window)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
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

}