package com.hanheldpos.ui.screens.menu.option.report.sale.customize

import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentCustomizeReportBinding
import com.hanheldpos.ui.base.fragment.BaseFragment

class CustomizeReportFragment : BaseFragment<FragmentCustomizeReportBinding, CustomizeReportVM>() , CustomizeReportUV {
    override fun layoutRes() = R.layout.fragment_customize_report
    override fun viewModelClass(): Class<CustomizeReportVM> {
        return CustomizeReportVM::class.java
    }

    override fun initViewModel(viewModel: CustomizeReportVM) {
        viewModel.run {
            init(this@CustomizeReportFragment)
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