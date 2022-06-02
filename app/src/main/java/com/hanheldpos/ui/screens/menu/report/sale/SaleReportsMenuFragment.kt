package com.hanheldpos.ui.screens.menu.report.sale

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
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
        saleReportCommon.saleReportFillter.postValue(
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
                    SaleOptionPage.Overview,
                    fragment = SaleOverviewFragment()
                )
            )
            SaleOptionPage.PaymentSummary -> navigator.goToWithAnimationEnterFromRight(
                SalesReportFragment(
                    fragment = PaymentReportFragment()
                )
            )
            SaleOptionPage.DiningOptions -> navigator.goToWithAnimationEnterFromRight(
                SalesReportFragment(
                    fragment = DiningOptionsFragment()
                )
            )
            SaleOptionPage.CashVoucher -> navigator.goToWithAnimationEnterFromRight(
                SalesReportFragment(
                    fragment = CashVoucherReportFragment()
                )
            )
            SaleOptionPage.ItemSales -> navigator.goToWithAnimationEnterFromRight(
                SalesReportFragment(
                    fragment = ItemSalesReportFragment()
                )
            )
            SaleOptionPage.Discounts -> navigator.goToWithAnimationEnterFromRight(
                SalesReportFragment(
                    fragment = DiscountsReportFragment()
                )
            )
            SaleOptionPage.Comps -> navigator.goToWithAnimationEnterFromRight(
                SalesReportFragment(
                    fragment = CompsReportFragment()
                )
            )
            SaleOptionPage.SectionSales -> navigator.goToWithAnimationEnterFromRight(
                SalesReportFragment(
                    fragment = SectionSalesReportFragment()
                )
            )
            SaleOptionPage.InventorySales -> navigator.goToWithAnimationEnterFromRight(
                SalesReportFragment(
                    SaleOptionPage.InventorySales,
                    fragment = InventorySalesReportFragment()
                )
            )
            SaleOptionPage.CategorySales -> navigator.goToWithAnimationEnterFromRight(
                SalesReportFragment(
                    fragment = CategorySalesReportFragment()
                )
            )
            SaleOptionPage.Refund -> navigator.goToWithAnimationEnterFromRight(
                SalesReportFragment(
                    fragment = RefundReportFragment()
                )
            )
            SaleOptionPage.Taxes -> navigator.goToWithAnimationEnterFromRight(
                SalesReportFragment(
                    fragment = TaxesReportFragment()
                )
            )
            SaleOptionPage.Service -> navigator.goToWithAnimationEnterFromRight(
                SalesReportFragment(
                    fragment = ServicesReportFragment()
                )
            )
            SaleOptionPage.Surcharge -> navigator.goToWithAnimationEnterFromRight(
                SalesReportFragment(
                    fragment = SurchargesReportFragment()
                )
            )
        }
    }

}