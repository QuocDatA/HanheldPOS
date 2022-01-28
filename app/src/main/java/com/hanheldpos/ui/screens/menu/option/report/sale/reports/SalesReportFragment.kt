package com.hanheldpos.ui.screens.menu.option.report.sale.reports

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentSalesReportBinding
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.option.report.sale.customize.CustomizeReportFragment

class SalesReportFragment : BaseFragment<FragmentSalesReportBinding,SalesReportVM>() , SalesReportUV {
    override fun layoutRes(): Int {
        return R.layout.fragment_sales_report;
    }

    override fun viewModelClass(): Class<SalesReportVM> {
        return SalesReportVM::class.java;
    }

    override fun initViewModel(viewModel: SalesReportVM) {
        viewModel.run {
            init(this@SalesReportFragment);
            binding.viewModel = this;
        }
    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun initAction() {

    }

    override fun onOpenCustomizeReport() {
        navigator.goToWithCustomAnimation(CustomizeReportFragment());
    }

    override fun backPress() {
        onFragmentBackPressed()
    }

}