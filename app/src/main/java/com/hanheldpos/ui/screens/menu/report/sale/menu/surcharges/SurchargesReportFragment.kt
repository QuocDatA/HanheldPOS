package com.hanheldpos.ui.screens.menu.report.sale.menu.surcharges

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentSurchargesReportBinding
import com.hanheldpos.model.menu.report.ReportItem
import com.hanheldpos.ui.base.adapter.GridSpacingItemDecoration
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.report.sale.SaleReportCommonVM
import com.hanheldpos.ui.screens.menu.report.sale.adapter.SaleReportAdapter
import com.hanheldpos.ui.screens.menu.report.sale.adapter.SaleReportDetailAdapter
import com.hanheldpos.utils.PriceUtils


class SurchargesReportFragment : BaseFragment<FragmentSurchargesReportBinding,SurchargesReportVM>(), SurchargesReportUV {
    private lateinit var saleReportAdapter: SaleReportAdapter
    private lateinit var saleReportDetailAdapter: SaleReportDetailAdapter
    private val saleReportCommon by activityViewModels<SaleReportCommonVM>()
    override fun layoutRes(): Int {
        return R.layout.fragment_surcharges_report
    }

    override fun viewModelClass(): Class<SurchargesReportVM> {
        return SurchargesReportVM::class.java
    }

    override fun initViewModel(viewModel: SurchargesReportVM) {
        viewModel.run {
            init(this@SurchargesReportFragment)
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
            val surcharges = reportSalesResp?.ListSurchargeFee
            viewModel.getSurchargesSummary(surcharges).let {
                binding.totalPayment.text = PriceUtils.formatStringPrice(it[0].toString())
                with(saleReportAdapter) {
                    submitList(it[1] as List<ReportItem>)
                    notifyDataSetChanged()
                }
            }
            viewModel.getSurchargesDetail(requireContext(), surcharges).let {
                with(saleReportDetailAdapter){
                    submitList(it)
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun initAction() {
        binding.btnShowDetail.setOnClickListener {
            viewModel.isShowDetail.postValue(!viewModel.isShowDetail.value!!)
        }

        viewModel.isShowDetail.observe(this) {
            binding.btnShowDetail.text = if (!it) getString(R.string.show_detail) else getString(
                R.string.hide_detail
            )
        }
    }
}