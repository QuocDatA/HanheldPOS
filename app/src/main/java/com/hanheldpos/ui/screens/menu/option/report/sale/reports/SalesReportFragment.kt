package com.hanheldpos.ui.screens.menu.option.report.sale.reports

import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentSalesReportBinding
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.option.report.sale.customize.CustomizeReportFragment
import com.hanheldpos.utils.time.DateTimeHelper
import java.util.*

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
        binding.deviceApply.text = "This Device Only ,Current Drawer"
        binding.dateFromTo.text =
            DateTimeHelper.dateToString(DateTimeHelper.curDate, DateTimeHelper.Format.DD_MMM_YYYY)
    }

    override fun initData() {

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

}