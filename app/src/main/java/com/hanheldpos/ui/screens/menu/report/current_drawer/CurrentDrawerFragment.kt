package com.hanheldpos.ui.screens.menu.report.current_drawer


import android.annotation.SuppressLint
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.cashdrawer.report.ReportCashDrawerResp
import com.hanheldpos.databinding.FragmentCurrentDrawerBinding
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.DatabaseHelper
import com.hanheldpos.model.OrderHelper
import com.hanheldpos.ui.base.dialog.AppAlertDialog
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cashdrawer.enddrawer.EndDrawerFragment
import com.hanheldpos.ui.screens.menu.report.current_drawer.adapter.ReportDrawerInfoAdapter
import com.hanheldpos.ui.screens.menu.report.current_drawer.payin_payout.PayInPayOutFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class CurrentDrawerFragment : BaseFragment<FragmentCurrentDrawerBinding, CurrentDrawerVM>(),
    CurrentDrawerUV {

    private lateinit var reportDrawerInfoAdapter: ReportDrawerInfoAdapter;

    private lateinit var report: ReportCashDrawerResp;

    override fun layoutRes() = R.layout.fragment_current_drawer

    override fun viewModelClass(): Class<CurrentDrawerVM> {
        return CurrentDrawerVM::class.java
    }

    override fun initViewModel(viewModel: CurrentDrawerVM) {
        viewModel.run {
            init(this@CurrentDrawerFragment);
            binding.viewModel = this;
        }
    }

    override fun initView() {

        if (DataHelper.currentDrawerId != null) {
            binding.currentDrawerText.text = "(${DataHelper.currentDrawerId})";
        } else binding.currentDrawerText.visibility = View.GONE;

        reportDrawerInfoAdapter = ReportDrawerInfoAdapter();

        binding.currentDrawerRecycle.apply {
            adapter = reportDrawerInfoAdapter;
            addItemDecoration(DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            ).apply {
                setDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.divider_vertical
                    )!!
                )
            });
        }

    }

    override fun initData() {
        viewModel.getCashDrawerDetail(requireContext());
    }

    override fun initAction() {
    }

    override fun getBack() {
        showLoading(false);
        onFragmentBackPressed()
    }

    override fun onOpenEndDrawer() {
        CoroutineScope(Dispatchers.IO).launch {
            val ordersCompletedFlow = DatabaseHelper.ordersCompleted.getAll();
            ordersCompletedFlow.take(1).collectLatest { ordersCompleted ->
                val listOrder = ordersCompleted.filter { OrderHelper.isValidOrderPush(it) }
                if (!listOrder.isNullOrEmpty()) {
                    launch(Dispatchers.Main) {
                        AppAlertDialog.get().show(
                            getString(R.string.notification),
                            getString(R.string.please_sync_local_data_before_ending_this_cash_drawer)
                        )
                    }

                } else {

                    if (this@CurrentDrawerFragment::report.isInitialized) {
                        launch(Dispatchers.Main) {
                            navigator.goTo(EndDrawerFragment(report = report));
                        }

                    }

                }
            }

        }


    }

    override fun onOpenPayInPayOut() {
        navigator.goTo(PayInPayOutFragment(listener = object :
            PayInPayOutFragment.PayInOutCallback {
            override fun onLoadReport() {
                viewModel.getCashDrawerDetail(requireContext());
            }
        }))
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun showInfoCurrentDrawer(report: ReportCashDrawerResp?) {

        report?.let {
            this.report = report;
            reportDrawerInfoAdapter.submitList(report.Reports);
            reportDrawerInfoAdapter.notifyDataSetChanged();
        }
    }


}