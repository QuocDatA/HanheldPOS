package com.hanheldpos.ui.screens.menu.report.sale.menu.section_sales

import android.annotation.SuppressLint
import android.view.Gravity
import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentSectionSalesReportBinding
import com.hanheldpos.extension.setOnClickDebounce
import com.hanheldpos.model.menu.report.ReportItem
import com.hanheldpos.ui.base.adapter.GridSpacingItemDecoration
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.report.sale.SaleReportCommonVM
import com.hanheldpos.ui.screens.menu.report.sale.adapter.SaleReportAdapter
import com.hanheldpos.utils.PriceUtils

class SectionSalesReportFragment :
    BaseFragment<FragmentSectionSalesReportBinding, SectionSalesReportVM>(), SectionSalesReportUV {
    private val saleReportCommon by activityViewModels<SaleReportCommonVM>()
    override fun layoutRes(): Int {
        return R.layout.fragment_section_sales_report
    }

    override fun viewModelClass(): Class<SectionSalesReportVM> {
        return SectionSalesReportVM::class.java
    }

    override fun initViewModel(viewModel: SectionSalesReportVM) {
        viewModel.run {
            init(this@SectionSalesReportFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {
        binding.tableLayout.setColumnAligns(
            mutableListOf(
                Gravity.START,
                Gravity.CENTER,
                Gravity.CENTER,
                Gravity.CENTER,
                Gravity.CENTER,
                Gravity.END
            )
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initData() {
        saleReportCommon.saleReport.observe(this) { reportSalesResp ->
            val sections = reportSalesResp?.ListSection

            viewModel.getSectionSalesHeaders(requireContext()).let {
                binding.tableLayout.clearHeader()
                binding.tableLayout.addRangeHeaders(it.toMutableList())
            }
            viewModel.getSectionSalesRows(requireContext(), sections).let {
                binding.tableLayout.clearRow()
                binding.tableLayout.addRangeRows(it)
            }
            viewModel.getSectionSalesSummary(requireContext(), sections).let {

                binding.totalGrossSales.text = PriceUtils.formatStringPrice(it[0] as Double)

                binding.totalNetSales.text = PriceUtils.formatStringPrice(it[1] as Double)
            }
        }
    }

    override fun initAction() {
        binding.btnShowDetail.setOnClickDebounce {
            viewModel.isShowDetail.postValue(!viewModel.isShowDetail.value!!)
        }

        viewModel.isShowDetail.observe(this) {
            binding.btnShowDetail.text = if (!it) getString(R.string.show_detail) else getString(
                R.string.hide_detail
            )
        }
    }


}