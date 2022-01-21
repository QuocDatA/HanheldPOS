package com.hanheldpos.ui.screens.cashdrawer.enddrawer

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.cashdrawer.report.ReportCashDrawerResp
import com.hanheldpos.databinding.FragmentEndDrawerBinding
import com.hanheldpos.extension.navigateTo
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cashdrawer.CashDrawerHelper
import com.hanheldpos.ui.screens.menu.option.report.current_drawer.adapter.ReportDrawerInfoAdapter
import com.hanheldpos.ui.screens.pincode.PinCodeActivity
import com.hanheldpos.utils.PriceHelper

class EndDrawerFragment(private val report: ReportCashDrawerResp?) : BaseFragment<FragmentEndDrawerBinding,EndDrawerVM>() , EndDrawerUV {

    private lateinit var reportDrawerInfoAdapter: ReportDrawerInfoAdapter;

    override fun layoutRes(): Int {
        return R.layout.fragment_end_drawer;
    }

    override fun viewModelClass(): Class<EndDrawerVM> {
        return EndDrawerVM::class.java;
    }

    override fun initViewModel(viewModel: EndDrawerVM) {
        viewModel.run {
            init(this@EndDrawerFragment);
            binding.viewModel = this;
        }
    }

    override fun initView() {
        reportDrawerInfoAdapter = ReportDrawerInfoAdapter();

        binding.currentDrawerRecycle.apply {
            adapter = reportDrawerInfoAdapter;
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
            });
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initData() {
        binding.btnEndDrawer.setOnClickListener {
            activity?.navigateTo(
                PinCodeActivity::class.java,
                alsoFinishCurrentActivity = true,
                alsoClearActivity = true,
            )
            CashDrawerHelper.isEndDrawer = true
        }
        binding.amountInput.let { input->
            var isEditing = false
            input.doAfterTextChanged {
                if (isEditing) return@doAfterTextChanged;
                if (it.toString().isEmpty()) input.setText("0");
                else {
                    isEditing = true;
                    input.setText(PriceHelper.formatStringPrice(it.toString()));
                }
                input.setSelection(input.length());
                isEditing = false;
            }
        }

        report?.let {
            reportDrawerInfoAdapter.submitList(report.Reports);
            reportDrawerInfoAdapter.notifyDataSetChanged();
        }

    }

    override fun initAction() {

    }

    override fun backPress() {
        onFragmentBackPressed();
    }

}