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
        binding.deviceApply.text = "This Device Only ,Current Drawer"
        binding.dateFromTo.text =
            DateTimeHelper.dateToString(DateTimeHelper.curDate, DateTimeHelper.Format.DD_MMM_YYYY)
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
        navigator.goToWithCustomAnimation(CustomizeReportFragment(listener = object :
            CustomizeReportFragment.CustomizeReportCallBack {
            override fun onComplete(
                startDay: Date?,
                endDay: Date?,
                isAllDay: Boolean,
                startTime: String,
                endTime: String,
                isAllDevice: Boolean,
                isCurrentDrawer: Boolean
            ) {
                if (startDay == endDay) {
                    binding.dateFromTo.text =
                        DateTimeHelper.dateToString(startDay, DateTimeHelper.Format.DD_MMM_YYYY)
                } else {
                    val dateStr = DateTimeHelper.dateToString(
                        startDay,
                        DateTimeHelper.Format.dd_MMM_YYYY
                    ) + " - " + DateTimeHelper.dateToString(
                        endDay,
                        DateTimeHelper.Format.dd_MMM_YYYY
                    )
                    binding.dateFromTo.text = dateStr
                }
                if (isAllDevice) {
                    if (isAllDay) {
                        binding.deviceApply.text = "All Device"
                    } else {
                        // Show Time Selected
                    }
                } else {
                    binding.deviceApply.text = "This Device Only"
                }
                if (isCurrentDrawer) {
                    binding.deviceApply.text = "${binding.deviceApply.text} ,Current Drawer"
                }
            }

            override fun onCancel() {

            }
        }));
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