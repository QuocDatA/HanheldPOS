package com.hanheldpos.ui.screens.pincode

import android.view.ViewTreeObserver
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentPinCodeBinding
import com.hanheldpos.extension.navigateTo
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cashdrawer.CashDrawerHelper
import com.hanheldpos.ui.screens.cashdrawer.startdrawer.StartDrawerFragment
import com.hanheldpos.ui.screens.home.HomeFragment
import com.hanheldpos.ui.screens.main.MainActivity
import com.hanheldpos.ui.screens.pincode.adapter.PinCodeAdapter

class PinCodeFragment : BaseFragment<FragmentPinCodeBinding, PinCodeVM>(), PinCodeUV {
    override fun layoutRes() = R.layout.fragment_pin_code;

    override fun initView() {

        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (CashDrawerHelper.isEndDrawer)
                    CashDrawerHelper.showDrawerNotification(
                        requireActivity(),
                        isOnStarting = false
                    );
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
    }

    override fun viewModelClass(): Class<PinCodeVM> {
        return PinCodeVM::class.java;
    }

    override fun initViewModel(viewModel: PinCodeVM) {
        viewModel.run {
            init(this@PinCodeFragment);
            binding.viewModel = this;
        }

    }

    override fun goBack() {
        requireActivity().finish()
    }

    override fun goHome() {
        navigator.goTo(HomeFragment())
    }

    override fun goStartDrawer() {
        navigator.goTo(StartDrawerFragment())
    }

}