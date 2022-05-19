package com.hanheldpos.ui.screens.menu.report.sale.menu.cash_voucher

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentCashVoucherReportBinding
import com.hanheldpos.extension.setOnClickDebounce
import com.hanheldpos.model.menu.report.ReportItem
import com.hanheldpos.ui.base.adapter.GridSpacingItemDecoration
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.report.sale.SaleReportCommonVM
import com.hanheldpos.ui.screens.menu.report.sale.adapter.SaleReportAdapter
import com.hanheldpos.ui.screens.menu.report.sale.adapter.SaleReportDetailAdapter
import com.hanheldpos.utils.PriceUtils

class CashVoucherReportFragment : BaseFragment<FragmentCashVoucherReportBinding,CashVoucherReportVM>() , CashVoucherReportUV {
    private lateinit var saleReportAdapter: SaleReportAdapter
    private lateinit var saleReportDetailAdapter: SaleReportDetailAdapter
    private val saleReportCommon by activityViewModels<SaleReportCommonVM>()
    override fun layoutRes(): Int {
        return R.layout.fragment_cash_voucher_report
    }

    override fun viewModelClass(): Class<CashVoucherReportVM> {
        return CashVoucherReportVM::class.java
    }

    override fun initViewModel(viewModel: CashVoucherReportVM) {
        viewModel.run {
            init(this@CashVoucherReportFragment)
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

        saleReportDetailAdapter = SaleReportDetailAdapter()
        binding.detail.apply {
            adapter = saleReportDetailAdapter
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    LinearLayoutManager.VERTICAL
                ).apply {
                    setDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.divider_vertical
                        )!!
                    )
                }
            )

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initData() {
        saleReportCommon.saleReport.observe(this) { reportSalesResp ->
            val cashVoucher = reportSalesResp?.CashVoucher
            viewModel.getCashVoucherSummary(cashVoucher).let {
                binding.totalPayment.text = PriceUtils.formatStringPrice(it[0].toString())
                with(saleReportAdapter) {
                    submitList(it[1] as List<ReportItem>)
                    notifyDataSetChanged()
                }
            }
            viewModel.getCashVoucherDetail(requireContext(), cashVoucher).let {
                with(saleReportDetailAdapter){
                    submitList(it)
                    notifyDataSetChanged()
                }
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