package com.hanheldpos.ui.screens.menu.report.sale.menu.overview

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentSaleOverviewBinding
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.GridSpacingItemDecoration
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.report.sale.adapter.SaleReportAdapter


class SaleOverviewFragment : BaseFragment<FragmentSaleOverviewBinding, SaleOverviewVM>(),
    SaleOverviewUV {
    private lateinit var saleReportAdapter: SaleReportAdapter
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
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initData() {
        viewModel.getReportItems().let {
            with(saleReportAdapter) {
                submitList(it)
                notifyDataSetChanged()
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