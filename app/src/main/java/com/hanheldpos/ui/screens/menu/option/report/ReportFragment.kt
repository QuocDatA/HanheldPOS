package com.hanheldpos.ui.screens.menu.option.report

import android.view.View
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentReportBinding
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.option.report.drawer.CurrentDrawerFragment

class ReportFragment : BaseFragment<FragmentReportBinding, ReportVM>(), ReportUV {
    override fun layoutRes() = R.layout.fragment_report

    override fun viewModelClass(): Class<ReportVM> {
        return ReportVM::class.java
    }

    override fun initViewModel(viewModel: ReportVM) {
        viewModel.run {
            init(this@ReportFragment);
            binding.viewModel = this;
        }
    }

    override fun initView() {
        binding.currentDrawerOption.setOnClickListener {
            navigator.goTo(CurrentDrawerFragment())
        }
        binding.currentDrawerText.setOnClickListener {
            navigator.goTo(CurrentDrawerFragment())
        }
    }

    override fun initData() {
    }

    override fun initAction() {
    }

    override fun getBack() {
        onFragmentBackPressed()
    }
}