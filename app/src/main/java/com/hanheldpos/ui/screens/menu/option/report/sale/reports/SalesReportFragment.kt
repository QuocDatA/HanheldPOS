package com.hanheldpos.ui.screens.menu.option.report.sale.reports

import com.google.android.material.tabs.TabLayoutMediator
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentSalesReportBinding
import com.hanheldpos.model.report.SaleReportCustomData
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.option.report.sale.customize.CustomizeReportFragment
import com.hanheldpos.ui.screens.menu.option.report.sale.reports.adapter.NumberDayReportAdapter
import com.hanheldpos.ui.screens.menu.option.report.sale.reports.adapter.NumberDayReportItem
import com.hanheldpos.ui.screens.menu.option.report.sale.reports.adapter.ReportOptionPageAdapter
import com.hanheldpos.utils.time.DateTimeUtils
import java.time.temporal.ChronoUnit
import java.util.*

class SalesReportFragment : BaseFragment<FragmentSalesReportBinding, SalesReportVM>(),
    SalesReportUV {

    private lateinit var reportOptionPageAdapter: ReportOptionPageAdapter;
    private lateinit var numberDayReportAdapter: NumberDayReportAdapter;

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

        // Number day select
        numberDayReportAdapter =
            NumberDayReportAdapter(
                listener = object : BaseItemClickListener<NumberDayReportItem> {
                    override fun onItemClick(adapterPosition: Int, item: NumberDayReportItem) {
                        viewModel.saleReportCustomData.postValue(viewModel.saleReportCustomData.value!!.apply {
                            startDay = Date.from(endDay?.toInstant()?.minus(item.value.toLong(), ChronoUnit.DAYS));
                        });
                    }
                },
            );
        binding.dayNumberAdapter.adapter = numberDayReportAdapter;

    }

    override fun initData() {
        numberDayReportAdapter.submitList(viewModel.initNumberDaySelected());

        reportOptionPageAdapter.submitList(
            mutableListOf(
                // TODO: Sale Report
               /* OverviewReportFragment()*/
            )
        );

        viewModel.saleReportCustomData.observe(this) {
            setUpDateTitle(it);
        };


    }

    override fun initAction() {

    }

    override fun onOpenCustomizeReport() {
        navigator.goToWithCustomAnimation(CustomizeReportFragment(listener = object :
            CustomizeReportFragment.CustomizeReportCallBack {
            override fun onComplete(
                saleReportCustomData: SaleReportCustomData
            ) {
                numberDayReportAdapter.clearSelected();
                viewModel.saleReportCustomData.postValue(saleReportCustomData);
            }

        }, saleReportCustomData = viewModel.saleReportCustomData.value!!));
    }

    override fun backPress() {
        onFragmentBackPressed()
    }

    private fun setUpDateTitle(saleReportCustomData: SaleReportCustomData) {

        val dateStr: String = DateTimeUtils.dateToString(
            saleReportCustomData.startDay,
            DateTimeUtils.Format.dd_MMM_YYYY
        ) + " - " + DateTimeUtils.dateToString(
            saleReportCustomData.endDay,
            DateTimeUtils.Format.dd_MMM_YYYY
        )
        binding.dateFromTo.text = dateStr

        if (saleReportCustomData.isAllDevice) {
            if (saleReportCustomData.isAllDay) {
                binding.deviceApply.text = getString(R.string.all_device);
            } else {
                // Show Time Selected
            }
        } else {
            binding.deviceApply.text = getString(R.string.this_device_only);
        }
        if (saleReportCustomData.isCurrentDrawer) {
            binding.deviceApply.text = "${binding.deviceApply.text}, Current Drawer"
        }
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