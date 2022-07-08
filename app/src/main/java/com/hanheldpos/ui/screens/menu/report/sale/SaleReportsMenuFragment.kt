package com.hanheldpos.ui.screens.menu.report.sale

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.report.ReportSalesResp
import com.hanheldpos.databinding.FragmentSaleReportsMenuBinding
import com.hanheldpos.model.menu.report.SaleOptionPage
import com.hanheldpos.model.report.SaleReportFilter
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.adapter.ItemOptionNav
import com.hanheldpos.ui.screens.menu.adapter.OptionNavAdapter
import com.hanheldpos.ui.screens.menu.report.sale.menu.cash_voucher.CashVoucherReportFragment
import com.hanheldpos.ui.screens.menu.report.sale.menu.category_sales.CategorySalesReportFragment
import com.hanheldpos.ui.screens.menu.report.sale.menu.comps.CompsReportFragment
import com.hanheldpos.ui.screens.menu.report.sale.menu.dining_options.DiningOptionsFragment
import com.hanheldpos.ui.screens.menu.report.sale.menu.discounts.DiscountsReportFragment
import com.hanheldpos.ui.screens.menu.report.sale.menu.inventory_sales.InventorySalesReportFragment
import com.hanheldpos.ui.screens.menu.report.sale.menu.item_sales.ItemSalesReportFragment
import com.hanheldpos.ui.screens.menu.report.sale.menu.overview.SaleOverviewFragment
import com.hanheldpos.ui.screens.menu.report.sale.menu.payment_summary.PaymentReportFragment
import com.hanheldpos.ui.screens.menu.report.sale.menu.refund.RefundReportFragment
import com.hanheldpos.ui.screens.menu.report.sale.menu.section_sales.SectionSalesReportFragment
import com.hanheldpos.ui.screens.menu.report.sale.menu.services.ServicesReportFragment
import com.hanheldpos.ui.screens.menu.report.sale.menu.surcharges.SurchargesReportFragment
import com.hanheldpos.ui.screens.menu.report.sale.menu.taxes.TaxesReportFragment
import com.hanheldpos.utils.DateTimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SaleReportsMenuFragment(private val isPreviewHistory: Boolean = false) :
    BaseFragment<FragmentSaleReportsMenuBinding, SaleReportsMenuVM>(),
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
        saleReportCommon.saleReportFilter.postValue(
            SaleReportFilter(
                startDay = DateTimeUtils.curDate,
                endDay = DateTimeUtils.curDate,
                isCurrentDrawer = true,
                isAllDevice = false,
                isAllDay = true,
                startTime = null,
                endTime = null
            )
        )

        viewModel.getListOptionPages(requireContext()).let {
            saleReportMenuAdapter.submitList(it)
            saleReportMenuAdapter.notifyDataSetChanged()
        }


        lifecycleScope.launch(Dispatchers.IO) {
            while (true) {
                if (isVisible) break
            }
            launch(Dispatchers.Main) {
                showLoading(true)
                saleReportCommon.fetchDataSaleReport(succeed = {
                    viewModel.saleReport.postValue(it)
                    showLoading(false)
                }, failed = {
                    viewModel.saleReport.postValue(null)
                    showLoading(false)
                    showMessage(it)
                })
            }

        }

    }

    override fun initAction() {
        setFragmentResultListener(SALE_REPORT_RESP) { _, bundle ->
            viewModel.saleReport.postValue(bundle.get("data") as ReportSalesResp?)
        }
    }

    fun onNavOptionClick(option: ItemOptionNav) {
        navigator.goToWithAnimationEnterFromRight(
            SalesReportFragment(
                viewModel.saleReport.value,
                type = option.type as SaleOptionPage,
                isPreviewHistory = isPreviewHistory,
            )
        )

    }

    companion object {
        const val SALE_REPORT_RESP: String = "SALE_REPORT_RESP";
    }

}