package com.hanheldpos.ui.screens.menu.report.sale.menu.overview

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.report.ReportSalesResp
import com.hanheldpos.databinding.FragmentSaleOverviewBinding
import com.hanheldpos.extension.setOnClickDebounce
import com.hanheldpos.ui.base.adapter.GridSpacingItemDecoration
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.report.sale.SaleReportCommonVM
import com.hanheldpos.ui.screens.menu.report.sale.adapter.SaleReportAdapter
import com.hanheldpos.ui.screens.menu.report.sale.adapter.SaleReportDetailAdapter


class SaleOverviewFragment(private val salesReport: ReportSalesResp?) :
    BaseFragment<FragmentSaleOverviewBinding, SaleOverviewVM>(),
    SaleOverviewUV {
    private lateinit var saleReportAdapter: SaleReportAdapter
    private lateinit var saleReportDetailAdapter: SaleReportDetailAdapter
    private val saleReportCommon by activityViewModels<SaleReportCommonVM>()
    override fun layoutRes(): Int {
        return R.layout.fragment_sale_overview
    }

    override fun viewModelClass(): Class<SaleOverviewVM> {
        return SaleOverviewVM::class.java
    }

    override fun initViewModel(viewModel: SaleOverviewVM) {
        viewModel.run {
            init(this@SaleOverviewFragment)
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
        val salesSummary = salesReport?.SalesSummary?.firstOrNull()
        if (salesSummary != null) {
            viewModel.getReportItems(salesSummary).let {
                with(saleReportAdapter) {
                    submitList(it)
                    notifyDataSetChanged()
                }
            }
            viewModel.getReportItemsDetail(salesSummary).let {
                with(saleReportDetailAdapter) {
                    submitList(it)
                    notifyDataSetChanged()
                }
            }

        } else {
            with(saleReportAdapter) {
                submitList(emptyList())
                notifyDataSetChanged()
            }
            with(saleReportDetailAdapter) {
                submitList(emptyList())
                notifyDataSetChanged()
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