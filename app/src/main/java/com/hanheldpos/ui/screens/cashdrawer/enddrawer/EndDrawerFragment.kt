package com.hanheldpos.ui.screens.cashdrawer.enddrawer

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.cashdrawer.report.ReportCashDrawerResp
import com.hanheldpos.databinding.FragmentEndDrawerBinding
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.report.current_drawer.adapter.ReportDrawerInfoAdapter
import com.hanheldpos.ui.screens.pincode.PinCodeFragment
import com.hanheldpos.utils.PriceUtils

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
            initLifeCycle(this@EndDrawerFragment);
            binding.viewModel = this;
        }
    }

    override fun initView() {

        // Init Amount
        val price = report?.Reports?.find { it.Id == 6 }?.Value as Double?
        viewModel.amountExpected.postValue(price?:0.0);
        viewModel.amountString.postValue(PriceUtils.formatStringPrice((price?:0.0).toString()));

        // Listener Click
        binding.btnEndDrawer.setOnClickListener {
            viewModel.endDrawer(requireContext());
        }

        // Init Adatper
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

        binding.amountInput.let { input->
            var isEditing = false
            input.doAfterTextChanged {
                if (isEditing) return@doAfterTextChanged;
                if (it.toString().isEmpty()) input.setText("0");
                else {
                    isEditing = true;
                    input.setText(PriceUtils.formatStringPrice(it.toString()));
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

    override fun onEndDrawer() {
        navigator.goTo(PinCodeFragment());
    }

}