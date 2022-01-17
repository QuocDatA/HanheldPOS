package com.hanheldpos.ui.screens.menu.option.report.current_drawer


import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentCurrentDrawerBinding
import com.hanheldpos.ui.base.dialog.AppAlertDialog
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cashdrawer.enddrawer.EndDrawerFragment
import com.hanheldpos.ui.screens.menu.option.report.current_drawer.payin_payout.PayInPayOutFragment

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
//        AppAlertDialog.get().show(
//            title = "Notification", message = "Please sync local data before ending\n" +
//                    "this cash drawer"
//        )
    }

    override fun onOpenPayInPayOut() {
        navigator.goTo(PayInPayOutFragment())
    }


}