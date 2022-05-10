package com.hanheldpos.ui.screens.menu.report.sale

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentSalesReportBinding
import com.hanheldpos.model.report.SaleReportCustomData
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.report.sale.adapter.NumberDayReportAdapter
import com.hanheldpos.ui.screens.menu.report.sale.adapter.NumberDayReportItem
import com.hanheldpos.ui.screens.menu.report.sale.customize.CustomizeReportFragment
import com.hanheldpos.utils.DateTimeUtils
import java.time.temporal.ChronoUnit
import java.util.*

class SalesReportFragment(private val fragment: Fragment) :
    BaseFragment<FragmentSalesReportBinding, SalesReportVM>(),
    SalesReportUV {
    private lateinit var numberDayReportAdapter: NumberDayReportAdapter;
    private val saleReportCommon by activityViewModels<SaleReportCommonVM>()

    override fun layoutRes(): Int {
        return R.layout.fragment_sales_report;
    }

    override fun viewModelClass(): Class<SalesReportVM> {
        return SalesReportVM::class.java;
    }

    override fun initViewModel(viewModel: SalesReportVM) {
        viewModel.run {
            init(this@SalesReportFragment);
            binding.viewModel = this;
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
                        saleReportCommon.saleReportCustomData.postValue(saleReportCommon.saleReportCustomData.value!!.apply {
                            startDay = Date.from(
                                endDay?.toInstant()?.minus(item.value.toLong(), ChronoUnit.DAYS)
                            );
                        });
                    }
                },
            );
        binding.dayNumberAdapter.adapter = numberDayReportAdapter;

    }

    override fun initData() {
        numberDayReportAdapter.submitList(viewModel.initNumberDaySelected());

        saleReportCommon.saleReportCustomData.observe(this) {
            setUpDateTitle(it);
        };

        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.fragmentContainer, fragment)
        transaction.commit()

    }

    override fun initAction() {

        binding.btnSyncOrders.setOnClickListener {
            showLoading(true)
            saleReportCommon.onSyncOrders(this.requireView(), succeed = {
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
                saleReportCustomData: SaleReportCustomData
            ) {
                numberDayReportAdapter.clearSelected();
                saleReportCommon.saleReportCustomData.postValue(saleReportCustomData);
            }

        }, saleReportCustomData = saleReportCommon.saleReportCustomData.value!!));
    }

    override fun backPress() {
        onFragmentBackPressed()
    }

    private fun setUpDateTitle(saleReportCustomData: SaleReportCustomData) {

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
                binding.deviceApply.text = getString(R.string.all_device);
            } else {
                // Show Time Selected
            }
        } else {
            binding.deviceApply.text = getString(R.string.this_device_only);
        }
        if (saleReportCustomData.isCurrentDrawer) {
            binding.deviceApply.text = "${binding.deviceApply.text}, Current Drawer"
        }
    }

    private enum class ReportOptionPage(val pos: Int) {
        Overview(0),
        PaymentSummary(1),
        DiningOptions(2),
        SectionSales(3);

        companion object {
            fun fromInt(value: Int): ReportOptionPage? {
                values().forEach {
                    if (it.pos == value) {
                        return it
                    }
                }
                return null
            }
        }
    }

}