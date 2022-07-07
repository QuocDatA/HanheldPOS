package com.hanheldpos.ui.screens.menu.report.sale.history

import android.util.Log
import android.view.Gravity
import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentHistoryRequestBinding
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.report.sale.SaleReportCommonVM
import com.hanheldpos.utils.GSonUtils


class HistoryRequestFragment : BaseFragment<FragmentHistoryRequestBinding, HistoryRequestVM>(),
    HistoryRequestUV {

    private val saleReportCommon by activityViewModels<SaleReportCommonVM>()
    override fun layoutRes(): Int {
        return R.layout.fragment_history_request
    }

    override fun viewModelClass(): Class<HistoryRequestVM> {
        return HistoryRequestVM::class.java
    }

    override fun initViewModel(viewModel: HistoryRequestVM) {
        viewModel.run {
            init(this@HistoryRequestFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {
        binding.tableHistoryReportData.setColumnAligns(
            mutableListOf(
                Gravity.START,
                Gravity.CENTER,
                Gravity.CENTER,
                Gravity.CENTER,
                Gravity.START,
            )
        )
    }

    override fun initData() {
        viewModel.getRequestHeaders(requireContext()).run {
            binding.tableHistoryReportData.addRangeHeaders(this)

        }
        viewModel.getRequestData(requireContext(), saleReportCommon.reportRequestHistory.value)
            .run {
                binding.tableHistoryReportData.addRangeRows(this)
            }

    }

    override fun initAction() {
        binding.tableHistoryReportData.setOnClickRowListener {
            Log.d("Row Click",GSonUtils.toJson(it))
        }
    }

}