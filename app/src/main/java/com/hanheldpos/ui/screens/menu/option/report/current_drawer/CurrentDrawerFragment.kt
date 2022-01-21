package com.hanheldpos.ui.screens.menu.option.report.current_drawer


import android.annotation.SuppressLint
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.cashdrawer.report.ReportCashDrawerResp
import com.hanheldpos.databinding.FragmentCurrentDrawerBinding
import com.hanheldpos.model.DataHelper
import com.hanheldpos.ui.base.dialog.AppAlertDialog
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cashdrawer.enddrawer.EndDrawerFragment
import com.hanheldpos.ui.screens.menu.option.report.current_drawer.adapter.ReportDrawerInfoAdapter
import com.hanheldpos.ui.screens.menu.option.report.current_drawer.payin_payout.PayInPayOutFragment

class CurrentDrawerFragment : BaseFragment<FragmentCurrentDrawerBinding, CurrentDrawerVM>(),
    CurrentDrawerUV {

    private lateinit var reportDrawerInfoAdapter: ReportDrawerInfoAdapter;

    override fun layoutRes() = R.layout.fragment_current_drawer

    override fun viewModelClass(): Class<CurrentDrawerVM> {
        return CurrentDrawerVM::class.java
    }

    override fun initViewModel(viewModel: CurrentDrawerVM) {
        return viewModel.run {
            init(this@CurrentDrawerFragment);
            binding.viewModel = this;
        }
    }

    override fun initView() {

        if (DataHelper.currentDrawerId != null) {
            binding.currentDrawerText.text = DataHelper.currentDrawerId;
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


        viewModel.getCashDrawerDetail(requireContext());


    }

    override fun initData() {
    }

    override fun initAction() {
    }

    override fun getBack() {
        onFragmentBackPressed()
    }

    override fun onOpenEndDrawer() {
        if (DataHelper.ordersCompleted == null || DataHelper.ordersCompleted?.isEmpty() == true) {
            AppAlertDialog.get().show(
                getString(R.string.notification),
                getString(R.string.please_sync_local_data_before_ending_this_cash_drawer)
            )
        } else
            navigator.goTo(EndDrawerFragment())


    }

    override fun onOpenPayInPayOut() {
        navigator.goTo(PayInPayOutFragment())
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun showInfoCurrentDrawer(report: ReportCashDrawerResp?) {
        report?.let {
            reportDrawerInfoAdapter.submitList(report.Reports);
            reportDrawerInfoAdapter.notifyDataSetChanged();
        }
    }


}