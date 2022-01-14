package com.hanheldpos.ui.screens.menu.option.report.drawer


import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentCurrentDrawerBinding
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.option.report.drawer.end_drawer.EndDrawerFragment
import com.hanheldpos.ui.screens.menu.option.report.drawer.payin_payout.PayInPayOutFragment

class CurrentDrawerFragment : BaseFragment<FragmentCurrentDrawerBinding, CurrentDrawerVM>() , CurrentDrawerUV {
    override fun layoutRes() = R.layout.fragment_current_drawer

    override fun viewModelClass(): Class<CurrentDrawerVM> {
        return CurrentDrawerVM::class.java
    }

    override fun initViewModel(viewModel: CurrentDrawerVM) {
        return viewModel.run {
            init(this@CurrentDrawerFragment);
            binding.viewModel = this;
        }
    }

    override fun initView() {
    }

    override fun initData() {
    }

    override fun initAction() {
    }

    override fun getBack() {
        onFragmentBackPressed()
    }

    override fun onOpenEndDrawer() {
        navigator.goTo(EndDrawerFragment())
    }

    override fun onOpenPayInPayOut() {
        navigator.goTo(PayInPayOutFragment())
    }


}