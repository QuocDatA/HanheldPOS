package com.hanheldpos.ui.screens.menu.report.sale

import android.annotation.SuppressLint
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.report.ReportSalesResp
import com.hanheldpos.databinding.FragmentSaleReportsMenuBinding
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.menu.report.SaleOptionPage
import com.hanheldpos.model.report.ReportFilterModel
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.adapter.ItemOptionNav
import com.hanheldpos.ui.screens.menu.adapter.OptionNavAdapter
import com.hanheldpos.utils.DateTimeUtils
import com.hanheldpos.utils.GSonUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SaleReportsMenuFragment(
    private val isPreviewHistory: Boolean = false,
    private var filterReport: ReportFilterModel? = null
) :
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
        saleReportCommon.resetDefaultReport()

        viewModel.getListOptionPages(requireContext()).let {
            saleReportMenuAdapter.submitList(it)
            saleReportMenuAdapter.notifyDataSetChanged()
        }

        if (!isPreviewHistory) {
            lifecycleScope.launch(Dispatchers.IO) {
                while (true) {
                    if (isVisible) break
                }
                launch(Dispatchers.Main) {
                    showLoading(true)
                    saleReportCommon.fetchDataSaleReport(
                        filter = filterReport ?: saleReportCommon.reportFilter.value, succeed = {
                            viewModel.saleReport.postValue(it)
                            showLoading(false)
                        }, failed = {
                            viewModel.saleReport.postValue(null)
                            showLoading(false)
                            showMessage(it)
                        })
                }
            }
        } else {
            showLoading(true)
            val reportResult = GSonUtils.toObject<ReportSalesResp>(filterReport?.reportResult)
            viewModel.saleReport.postValue(reportResult)
            showLoading(false)
        }


    }

    override fun initAction() {

        // Setup firebase
        DataHelper.firebaseSettingLocalStorage?.fireStorePath?.let { path ->
            Firebase.firestore.collection(path.reportList ?: "")
                .addSnapshotListener { value, _ ->
                    Log.d("Data Version Firebase", value.toString())
                    value?.documents?.let {
                        val list = it.map { data -> data.data }.mapNotNull { map ->
                            GSonUtils.mapToObject(map, ReportFilterModel::class.java)
                        }
                        saleReportCommon.reportRequestHistory.postValue(list)
                    }
                }
        }

        setFragmentResultListener(SALE_REPORT_RESP) { _, bundle ->
            viewModel.saleReport.postValue(bundle.get("data") as ReportSalesResp?)
        }
    }

    fun onNavOptionClick(option: ItemOptionNav) {
        navigator.goToWithAnimationEnterFromRight(
            SalesReportFragment(
                filterReport ?: saleReportCommon.reportFilter.value,
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