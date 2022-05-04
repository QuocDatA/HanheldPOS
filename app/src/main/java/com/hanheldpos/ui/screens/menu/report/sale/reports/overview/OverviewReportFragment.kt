package com.hanheldpos.ui.screens.menu.report.sale.reports.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentOverViewReportBinding
import com.hanheldpos.ui.base.fragment.BaseFragment

class OverviewReportFragment : BaseFragment<FragmentOverViewReportBinding,OverviewVM>() , OverviewUV {
    override fun layoutRes(): Int {
        return R.layout.fragment_over_view_report;
    }

    override fun viewModelClass(): Class<OverviewVM> {
        return OverviewVM::class.java;
    }

    override fun initViewModel(viewModel: OverviewVM) {
        viewModel.run {
            init(this@OverviewReportFragment);
            binding.viewModel = this;
        }
    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun initAction() {

    }

}