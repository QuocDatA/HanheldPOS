package com.hanheldpos.ui.screens.menu.report.sale.menu.payment_summary

import android.annotation.SuppressLint
import android.view.Gravity
import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentPaymentReportBinding
import com.hanheldpos.extension.setOnClickDebounce
import com.hanheldpos.model.menu.report.ReportItem
import com.hanheldpos.ui.base.adapter.GridSpacingItemDecoration
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.report.sale.SaleReportCommonVM
import com.hanheldpos.ui.screens.menu.report.sale.adapter.SaleReportAdapter
import com.hanheldpos.utils.PriceUtils


class PaymentReportFragment : BaseFragment<FragmentPaymentReportBinding, PaymentReportVM>(),
    PaymentReportUV {
    private lateinit var saleReportAdapter: SaleReportAdapter
    private val saleReportCommon by activityViewModels<SaleReportCommonVM>()

    override fun layoutRes(): Int {
        return R.layout.fragment_payment_report
    }

    override fun viewModelClass(): Class<PaymentReportVM> {
        return PaymentReportVM::class.java
    }

    override fun initViewModel(viewModel: PaymentReportVM) {
        viewModel.run {
            init(this@PaymentReportFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {
        saleReportAdapter = SaleReportAdapter()
        binding.consum.apply {
            adapter = saleReportAdapter
            addItemDecoration(
                GridSpacingItemDecoration(
                    2,
                    resources.getDimension(R.dimen._30sdp).toInt(),
                    false
                )
            )
        }
        binding.tableLayout.setColumnAligns(mutableListOf(
            Gravity.START,
            Gravity.CENTER,
            Gravity.CENTER,
            Gravity.CENTER,
            Gravity.CENTER,
            Gravity.END,
        ))
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initData() {
        saleReportCommon.saleReport.observe(this) { reportSalesResp ->
            val orderPayment = reportSalesResp?.OrderPayment
            viewModel.getPaymentHeaders(requireContext()).let {
                binding.tableLayout.clearHeader()
                binding.tableLayout.addRangeHeaders(it)
            }
            viewModel.getPaymentRows(requireContext(), orderPayment).let {
                binding.tableLayout.clearRow()
                binding.tableLayout.addRangeRows(it)
            }
            viewModel.getPaymentSummary(requireContext(), orderPayment).let {

                binding.totalPayment.text = PriceUtils.formatStringPrice(it[0] as Double)

                saleReportAdapter.submitList((it[1] as List<ReportItem>).toMutableList())
                saleReportAdapter.notifyDataSetChanged()
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