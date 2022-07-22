package com.hanheldpos.ui.screens.menu.report.sale

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.report.ReportSalesResp
import com.hanheldpos.databinding.FragmentSalesReportBinding
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.extension.setOnClickDebounce
import com.hanheldpos.model.menu.report.SaleOptionPage
import com.hanheldpos.model.report.ReportFilterModel
import com.hanheldpos.printer.BillPrinterManager
import com.hanheldpos.printer.layouts.LayoutType
import com.hanheldpos.printer.printer_setup.PrinterTypes
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.report.sale.adapter.NumberDayReportAdapter
import com.hanheldpos.ui.screens.menu.report.sale.adapter.NumberDayReportItem
import com.hanheldpos.ui.screens.menu.report.sale.customize.CustomizeReportFragment
import com.hanheldpos.ui.screens.menu.report.sale.history.HistoryRequestFragment
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
import java.time.temporal.ChronoUnit
import java.util.*

class SalesReportFragment(
    private var filter: ReportFilterModel?,
    private var saleReport: ReportSalesResp?,
    private val type: SaleOptionPage? = null,
    private val isPreviewHistory: Boolean = false,
) :
    BaseFragment<FragmentSalesReportBinding, SalesReportVM>(),
    SalesReportUV {
    private lateinit var numberDayReportAdapter: NumberDayReportAdapter
    private val saleReportCommon by activityViewModels<SaleReportCommonVM>()

    override fun layoutRes(): Int {
        return R.layout.fragment_sales_report
    }

    override fun viewModelClass(): Class<SalesReportVM> {
        return SalesReportVM::class.java
    }

    override fun initViewModel(viewModel: SalesReportVM) {
        viewModel.run {
            init(this@SalesReportFragment)
            binding.viewModel = this
        }
        binding.saleReportCommonVM = saleReportCommon
    }

    override fun initView() {

        // Number day select
        numberDayReportAdapter =
            NumberDayReportAdapter(
                listener = object : BaseItemClickListener<NumberDayReportItem> {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onItemClick(adapterPosition: Int, item: NumberDayReportItem) {
                        val currentFilter = viewModel.saleReportFilter.value?.apply {
                            startDay = DateTimeUtils.dateToString(Date.from(
                                DateTimeUtils.strToDate(endDay,DateTimeUtils.Format.YYYY_MM_DD)?.toInstant()?.minus(item.value.toLong(), ChronoUnit.DAYS)
                            ),DateTimeUtils.Format.YYYY_MM_DD )
                        }
                        viewModel.saleReportFilter.postValue(currentFilter)

                    }
                },
            )
        binding.dayNumberAdapter.adapter = numberDayReportAdapter

    }

    override fun initData() {
        viewModel.isPreviewHistory.postValue(isPreviewHistory)


        numberDayReportAdapter.submitList(viewModel.initNumberDaySelected())

        var firstObserver = true
        viewModel.saleReportFilter.observe(this) {
            setUpDateTitle(it)
            saleReportCommon.reportFilter.postValue(it)
            if (firstObserver) {
                firstObserver = false
                return@observe
            }
            if (isPreviewHistory) return@observe
            showLoading(true)
            saleReportCommon.fetchDataSaleReport(it, succeed = { report ->
                setFragmentResult(SaleReportsMenuFragment.SALE_REPORT_RESP, Bundle().apply {
                    putParcelable("data", report)
                })
                this.saleReport = report
                initializeFragmentChild()
                showLoading(false)
            }, failed = {
                showLoading(false)
            })
        }

        initializeFragmentChild()
        updateDataFromReportFilter()
    }

    override fun initAction() {

        binding.btnSyncOrders.setOnClickDebounce {
            showLoading(true)
            saleReportCommon.onSyncOrders(this.requireView(), succeed = {
                showLoading(false)
                saleReportCommon.reportFilter.notifyValueChange()
            }, failed = {
                showLoading(false)
            })
        }
        binding.btnPrintReport.setOnClickDebounce {

            lifecycleScope.launch(Dispatchers.IO) {
                when (type) {
                    SaleOptionPage.Overview -> {
                        BillPrinterManager.get { }.printReport(
                            LayoutType.Report.Overview,
                            saleReport,
                            saleReportCommon.reportFilter.value,
                            PrinterTypes.CASHIER
                        )
                    }
                    SaleOptionPage.InventorySales -> {
                        BillPrinterManager.get { }.printReport(
                            LayoutType.Report.Inventory,
                            saleReport,
                            saleReportCommon.reportFilter.value,
                            PrinterTypes.CASHIER
                        )
                    }
                    else -> {

                    }
                }
            }


        }


    }

    override fun onOpenCustomizeReport() {
        navigator.goToWithCustomAnimation(CustomizeReportFragment(listener = object :
            CustomizeReportFragment.CustomizeReportCallBack {
            override fun onComplete(
                saleReportCustomData: ReportFilterModel
            ) {
                numberDayReportAdapter.clearSelected()
                if (!isPreviewHistory) {
                    viewModel.saleReportFilter.postValue(saleReportCustomData)
                }

            }

        }, saleReportCustomData = saleReportCommon.reportFilter.value!!))
    }

    override fun onOpenHistoryRequest() {
        navigator.goTo(HistoryRequestFragment())
    }

    override fun backPress() {
        onFragmentBackPressed()
    }

    private fun setUpDateTitle(saleReportCustomData: ReportFilterModel) {

        val dateStr: String = DateTimeUtils.strToStr(
            saleReportCustomData.startDay,
            DateTimeUtils.Format.YYYY_MM_DD,
            DateTimeUtils.Format.dd_MMM_YYYY
        ) + " - " + DateTimeUtils.strToStr(
            saleReportCustomData.endDay,
            DateTimeUtils.Format.YYYY_MM_DD,
            DateTimeUtils.Format.dd_MMM_YYYY
        )
        binding.dateFromTo.text = dateStr

        if (saleReportCustomData.isAllDevice == true) {
            if (saleReportCustomData.endHour.isNullOrEmpty() && saleReportCustomData.endHour.isNullOrEmpty()) {
                binding.deviceApply.text = getString(R.string.all_device)
            } else {
                // Show Time Selected
            }
        } else {
            binding.deviceApply.text = getString(R.string.this_device_only)
        }
        if (saleReportCustomData.isCurrentCashDrawer == true) {
            binding.deviceApply.text = "${binding.deviceApply.text}, Current Drawer"
        }
    }

    private fun initializeFragmentChild() {
        binding.fragmentContainer.removeAllViews()
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()

        val fragment = when (type as SaleOptionPage) {
            SaleOptionPage.Overview -> SaleOverviewFragment::class.java
            SaleOptionPage.PaymentSummary -> PaymentReportFragment::class.java
            SaleOptionPage.DiningOptions -> DiningOptionsFragment::class.java
            SaleOptionPage.CashVoucher -> CashVoucherReportFragment::class.java
            SaleOptionPage.ItemSales -> ItemSalesReportFragment::class.java
            SaleOptionPage.Discounts -> DiscountsReportFragment::class.java
            SaleOptionPage.Comps -> CompsReportFragment::class.java
            SaleOptionPage.SectionSales -> SectionSalesReportFragment::class.java
            SaleOptionPage.InventorySales -> InventorySalesReportFragment::class.java
            SaleOptionPage.CategorySales -> CategorySalesReportFragment::class.java
            SaleOptionPage.Refund -> RefundReportFragment::class.java
            SaleOptionPage.Taxes -> TaxesReportFragment::class.java
            SaleOptionPage.Service -> ServicesReportFragment::class.java
            SaleOptionPage.Surcharge -> SurchargesReportFragment::class.java
        }

        transaction.add(
            R.id.fragmentContainer,
            fragment.getConstructor(ReportSalesResp::class.java).newInstance(saleReport)
        )
        transaction.commit()
    }


    private fun updateDataFromReportFilter() {
        viewModel.saleReportFilter.postValue(filter)
    }
}