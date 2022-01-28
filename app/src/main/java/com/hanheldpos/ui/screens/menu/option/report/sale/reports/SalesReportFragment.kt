package com.hanheldpos.ui.screens.menu.option.report.sale.reports

import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentSalesReportBinding
import com.hanheldpos.model.fee.ChooseProductApplyTo
import com.hanheldpos.model.menu_nav_opt.ReportOptionType
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.option.report.sale.customize.CustomizeReportFragment
import com.hanheldpos.ui.screens.menu.option.report.sale.reports.adapter.NumberDayReportAdapter
import com.hanheldpos.ui.screens.menu.option.report.sale.reports.adapter.NumberDayReportItem
import com.hanheldpos.ui.screens.menu.option.report.sale.reports.adapter.ReportOptionPageAdapter
import com.hanheldpos.ui.screens.menu.option.report.sale.reports.overview.OverviewReportFragment

class SalesReportFragment : BaseFragment<FragmentSalesReportBinding,SalesReportVM>() , SalesReportUV {

    private lateinit var numberDayReportAdapter : NumberDayReportAdapter;
    private lateinit var reportOptionPageAdapter : ReportOptionPageAdapter;



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

        // Layout
        reportOptionPageAdapter = ReportOptionPageAdapter(childFragmentManager, lifecycle);

        binding.layoutReportOptions.adapter = reportOptionPageAdapter;

        // Tab View
        TabLayoutMediator(binding.tabReportOptions, binding.layoutReportOptions) { tab, position ->
            run {
                when (ReportOptionPage.fromInt(position)) {
                    ReportOptionPage.Overview -> {
                        tab.text = getString(R.string.overview);
                    }
                    ReportOptionPage.PaymentSummary -> {
                        tab.text = getString(R.string.payment_summary);
                    }
                    ReportOptionPage.DiningOptions -> {
                        tab.text = getString(R.string.dining_options);
                    }
                    ReportOptionPage.SectionSales -> {
                        tab.text = getString(R.string.section_sales)
                    }
                    null -> {

                    }
                }
            }
        }.attach()

        numberDayReportAdapter = NumberDayReportAdapter(listener = object : BaseItemClickListener<NumberDayReportItem>{
            override fun onItemClick(adapterPosition: Int, item: NumberDayReportItem) {

            }
        })
        binding.dayNumberAdapter.adapter = numberDayReportAdapter;
    }

    override fun initData() {
        numberDayReportAdapter.submitList(viewModel.initNumberDaySelected());

        reportOptionPageAdapter.submitList(mutableListOf(
            OverviewReportFragment()
        ));
    }

    override fun initAction() {

    }

    override fun onOpenCustomizeReport() {

        navigator.goToWithCustomAnimation(CustomizeReportFragment());
    }

    override fun backPress() {
        onFragmentBackPressed()
    }

    private enum class ReportOptionPage(val pos: Int) {
        Overview(0),
        PaymentSummary(1),
        DiningOptions(2),
        SectionSales(3);
        companion object {
            fun fromInt(value: Int): ReportOptionPage? {
                ReportOptionPage.values().forEach {
                    if (it.pos == value) {
                        return it
                    }
                }
                return null
            }
        }
    }

}