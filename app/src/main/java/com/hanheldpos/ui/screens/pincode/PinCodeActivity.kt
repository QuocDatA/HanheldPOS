package com.hanheldpos.ui.screens.pincode

import android.view.ViewTreeObserver
import android.view.WindowManager
import com.hanheldpos.R
import com.hanheldpos.databinding.ActivityPinCodeBinding
import com.hanheldpos.extension.navigateTo
import com.hanheldpos.ui.base.activity.BaseActivity
import com.hanheldpos.ui.screens.cashdrawer.CashDrawerHelper
import com.hanheldpos.ui.screens.cashdrawer.startdrawer.StartDrawerActivity
import com.hanheldpos.ui.screens.main.MainActivity
import com.hanheldpos.ui.screens.pincode.adapter.PinCodeAdapter
import com.hanheldpos.utils.NetworkUtils
import com.utils.helper.SystemHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PinCodeActivity : BaseActivity<ActivityPinCodeBinding, PinCodeVM>(), PinCodeUV {
    override fun layoutRes() = R.layout.activity_pin_code;

    override fun initView() {
        binding.root.viewTreeObserver.addOnGlobalLayoutListener{
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            SystemHelper.hideSystemUI(this.window);
        }

        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                if (CashDrawerHelper.isEndDrawer)
                    CashDrawerHelper.showDrawerNotification(this@PinCodeActivity, isOnStarting = false);
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this);
            }
        })
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

        NetworkUtils.hasActiveInternetConnection(context.applicationContext, listener = object : NetworkUtils.NetworkConnectionCallBack {
            override fun onAvailable() {

            }

            override fun onLost() {
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.showError(getString(R.string.no_network_connection))
                }
            }
        })
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