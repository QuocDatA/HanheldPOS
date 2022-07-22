package com.hanheldpos.ui.screens.menu.report.sale.menu.inventory_sales

import android.annotation.SuppressLint
import android.view.Gravity
import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.report.ReportSalesResp
import com.hanheldpos.databinding.FragmentInventorySalesReportBinding
import com.hanheldpos.extension.setOnClickDebounce
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.report.sale.SaleReportCommonVM


class InventorySalesReportFragment(private val salesReport: ReportSalesResp?) :
    BaseFragment<FragmentInventorySalesReportBinding, InventorySalesReportVM>(),
    InventorySalesReportUV {

    private val saleReportCommon by activityViewModels<SaleReportCommonVM>()
    override fun layoutRes(): Int {
        return R.layout.fragment_inventory_sales_report
    }

    override fun viewModelClass(): Class<InventorySalesReportVM> {
        return InventorySalesReportVM::class.java
    }

    override fun initViewModel(viewModel: InventorySalesReportVM) {
        viewModel.run {
            init(this@InventorySalesReportFragment)
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
            )
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initData() {

        val inventories = salesReport?.ListInventory
        viewModel.getInventoryHeaders(requireContext()).let {
            binding.tableLayout.clearHeader()
            binding.tableLayout.addRangeHeaders(it.toMutableList())
        }
        viewModel.getInventoryRows(requireContext(), inventories).let {
            binding.tableLayout.clearRow()
            binding.tableLayout.addRangeRows(it)
        }
        viewModel.getInventorySummary(requireContext(), inventories).let {
            binding.totalGrossQty.text = it[0].toString()
            binding.totalNetQty.text = it[1].toString()
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