package com.hanheldpos.ui.screens.menu.option.report.drawer.payin_payout

import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentPayInPayOutBinding
import com.hanheldpos.ui.base.fragment.BaseFragment

class PayInPayOutFragment : BaseFragment<FragmentPayInPayOutBinding, PayInPayOutVM>(),
    PayInPayOutUV {
    override fun layoutRes() = R.layout.fragment_pay_in_pay_out

    override fun viewModelClass(): Class<PayInPayOutVM> {
        return PayInPayOutVM::class.java
    }

    override fun initViewModel(viewModel: PayInPayOutVM) {
        return viewModel.run {
            init(this@PayInPayOutFragment)
            binding.viewModel = this
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

}