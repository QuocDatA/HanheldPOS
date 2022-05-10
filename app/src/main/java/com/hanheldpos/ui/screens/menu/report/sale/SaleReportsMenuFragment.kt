package com.hanheldpos.ui.screens.menu.report.sale

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
import com.hanheldpos.databinding.FragmentSaleReportsMenuBinding
import com.hanheldpos.model.menu.report.ReportOptionType
import com.hanheldpos.model.menu.report.SaleOptionPage
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.adapter.ItemOptionNav
import com.hanheldpos.ui.screens.menu.adapter.OptionNavAdapter
import com.hanheldpos.ui.screens.menu.report.current_drawer.CurrentDrawerFragment
import com.hanheldpos.ui.screens.menu.report.sale.menu.overview.SaleOverviewFragment


class SaleReportsMenuFragment : BaseFragment<FragmentSaleReportsMenuBinding, SaleReportsMenuVM>(),
    SaleReportsMenuUV {
    private lateinit var saleReportMenuAdapter: OptionNavAdapter
    private val saleReportCommon by activityViewModels<SaleReportCommonVM>()
    override fun layoutRes(): Int {
        return R.layout.fragment_sale_reports_menu
    }

    override fun viewModelClass(): Class<SaleReportsMenuVM> {
        return SaleReportsMenuVM::class.java
    }

    override fun initViewModel(viewModel: SaleReportsMenuVM) {
        viewModel.run {
            init(this@SaleReportsMenuFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {
        saleReportMenuAdapter = OptionNavAdapter(object : BaseItemClickListener<ItemOptionNav> {
            override fun onItemClick(adapterPosition: Int, item: ItemOptionNav) {
                onNavOptionClick(item)
            }
        })

        binding.menuItemContainer.apply {
            adapter = saleReportMenuAdapter
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
        viewModel.getListOptionPages(requireContext()).let {
            saleReportMenuAdapter.submitList(it)
            saleReportMenuAdapter.notifyDataSetChanged()
        }

        showLoading(true)
        saleReportCommon.fetchDataSaleReport(succeed = {
            showLoading(false)
        }, failed = {
            showLoading(false)
        })
    }

    override fun initAction() {

    }

    fun onNavOptionClick(option: ItemOptionNav) {
        when (option.type as SaleOptionPage) {
            SaleOptionPage.Overview -> navigator.goToWithAnimationEnterFromRight(
                SalesReportFragment(
                    fragment = SaleOverviewFragment(saleReportCommon.saleReport?.SalesSummary?.firstOrNull())
                )
            )
        }
    }

}