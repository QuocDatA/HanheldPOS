package com.hanheldpos.ui.screens.menu.option.report.current_drawer


import android.view.View
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentCurrentDrawerBinding
import com.hanheldpos.model.DataHelper
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cashdrawer.enddrawer.EndDrawerFragment
import com.hanheldpos.ui.screens.menu.option.report.current_drawer.payin_payout.PayInPayOutFragment

class CurrentDrawerFragment : BaseFragment<FragmentCurrentDrawerBinding, CurrentDrawerVM>() , CurrentDrawerUV {
    override fun layoutRes() = R.layout.fragment_current_drawer

    override fun viewModelClass(): Class<CurrentDrawerVM> {
        return CurrentDrawerVM::class.java
    }

    override fun initViewModel(viewModel: CurrentDrawerVM) {
        viewModel.run {
            init(this@CurrentDrawerFragment);
            binding.viewModel = this;
        }
    }

    override fun initView() {
        if(DataHelper.CurrentDrawerId != null) {
           binding.currentDrawerText.text = "(${DataHelper.CurrentDrawerId})"
        }
        else binding.currentDrawerText.visibility = View.GONE;
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