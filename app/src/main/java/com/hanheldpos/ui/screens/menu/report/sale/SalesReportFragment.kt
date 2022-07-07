package com.hanheldpos.ui.screens.menu.report.sale

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentSalesReportBinding
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.extension.setOnClickDebounce
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.menu.report.SaleOptionPage
import com.hanheldpos.model.report.ReportModel
import com.hanheldpos.model.report.SaleReportFilter
import com.hanheldpos.printer.BillPrinterManager
import com.hanheldpos.printer.layouts.LayoutType
import com.hanheldpos.printer.printer_setup.PrinterTypes
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.report.sale.adapter.NumberDayReportAdapter
import com.hanheldpos.ui.screens.menu.report.sale.adapter.NumberDayReportItem
import com.hanheldpos.ui.screens.menu.report.sale.customize.CustomizeReportFragment
import com.hanheldpos.ui.screens.menu.report.sale.history.HistoryRequestFragment
import com.hanheldpos.utils.DateTimeUtils
import com.hanheldpos.utils.GSonUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.temporal.ChronoUnit
import java.util.*

class SalesReportFragment(
    private val type: SaleOptionPage? = null,
    private val fragment: Fragment
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
                        saleReportCommon.saleReportFilter.postValue(saleReportCommon.saleReportFilter.value!!.apply {
                            startDay = Date.from(
                                endDay?.toInstant()?.minus(item.value.toLong(), ChronoUnit.DAYS)
                            )
                        })
                    }
                },
            )
        binding.dayNumberAdapter.adapter = numberDayReportAdapter

    }

    override fun initData() {

        // Setup firebase
        DataHelper.firebaseSettingLocalStorage?.fireStorePath?.let { path ->
            Firebase.firestore.collection(path.reportList ?: "")
                .addSnapshotListener { value, _ ->
                    Log.d("Data Version Firebase", value.toString())
                    value?.documents?.let {
                        val list = it.map { data -> data.data }.mapNotNull { map ->
                            GSonUtils.mapToObject(map, ReportModel::class.java)
                        }
                        saleReportCommon.reportRequestHistory.postValue(list)
                    }

                }
        }

        numberDayReportAdapter.submitList(viewModel.initNumberDaySelected())

        saleReportCommon.saleReportFilter.observe(this) {
            setUpDateTitle(it)
        }

        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.fragmentContainer, fragment)
        transaction.commit()

    }

    override fun initAction() {

        binding.btnSyncOrders.setOnClickDebounce {
            showLoading(true)
            saleReportCommon.onSyncOrders(this.requireView(), succeed = {
                showLoading(false)
                saleReportCommon.saleReportFilter.notifyValueChange()
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
                            saleReportCommon.saleReport.value,
                            saleReportCommon.saleReportFilter.value,
                            PrinterTypes.CASHIER
                        )
                    }
                    SaleOptionPage.InventorySales -> {
                        BillPrinterManager.get { }.printReport(
                            LayoutType.Report.Inventory,
                            saleReportCommon.saleReport.value,
                            saleReportCommon.saleReportFilter.value,
                            PrinterTypes.CASHIER
                        )
                    }
                    else -> {

                    }
                }
            }


        }

        var firstObserver = true
        saleReportCommon.saleReportFilter.observe(this) {
            if (firstObserver) {
                firstObserver = false
                return@observe
            }
            showLoading(true)
            saleReportCommon.fetchDataSaleReport(succeed = {
                showLoading(false)
            }, failed = {
                showLoading(false)
            })
        }
    }

    override fun onOpenCustomizeReport() {
        navigator.goToWithCustomAnimation(CustomizeReportFragment(listener = object :
            CustomizeReportFragment.CustomizeReportCallBack {
            override fun onComplete(
                saleReportCustomData: SaleReportFilter
            ) {
                numberDayReportAdapter.clearSelected()
                saleReportCommon.saleReportFilter.postValue(saleReportCustomData)
            }

        }, saleReportCustomData = saleReportCommon.saleReportFilter.value!!))
    }

    override fun onOpenHistoryRequest() {
        navigator.goToWithCustomAnimation(HistoryRequestFragment())
    }

    override fun backPress() {
        onFragmentBackPressed()
    }

    private fun setUpDateTitle(saleReportCustomData: SaleReportFilter) {

        val dateStr: String = DateTimeUtils.dateToString(
            saleReportCustomData.startDay,
            DateTimeUtils.Format.dd_MMM_YYYY
        ) + " - " + DateTimeUtils.dateToString(
            saleReportCustomData.endDay,
            DateTimeUtils.Format.dd_MMM_YYYY
        )
        binding.dateFromTo.text = dateStr

        if (saleReportCustomData.isAllDevice) {
            if (saleReportCustomData.isAllDay) {
                binding.deviceApply.text = getString(R.string.all_device)
            } else {
                // Show Time Selected
            }
        } else {
            binding.deviceApply.text = getString(R.string.this_device_only)
        }
        if (saleReportCustomData.isCurrentDrawer) {
            binding.deviceApply.text = "${binding.deviceApply.text}, Current Drawer"
        }
    }

}