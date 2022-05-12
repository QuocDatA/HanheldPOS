package com.hanheldpos.ui.screens.menu.report.sale.menu.payment_summary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentPaymentReportBinding
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.widgets.TableLayoutFixedHeader


class PaymentReportFragment : BaseFragment<FragmentPaymentReportBinding, PaymentReportVM>(),
    PaymentReportUV {
    override fun layoutRes(): Int {
        return R.layout.fragment_payment_report
    }

    override fun viewModelClass(): Class<PaymentReportVM> {
        return PaymentReportVM::class.java
    }

    override fun initViewModel(viewModel: PaymentReportVM) {
        viewModel.run {
            init(this@PaymentReportFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {

    }

    override fun initData() {
        binding.tableLayout.addRangeHeaders(
            mutableListOf(
                "11111",
                "22222",
                "33333",
                "44444",
                "55555",
                "66666",
                "77777",
                "88888",
                "99999",
                "100000",
                "110000"
            )
        )
        binding.tableLayout.addRangeRows(
            mutableListOf(
                mutableListOf(
                    "test",
                    "test",
                    "test",
                    "test",
                    "test",
                    "test",
                    "test",
                    "test"
                ),
                mutableListOf("test", "test", "test", "test", "test", "test", "test", "test"),
                mutableListOf("test", "test", "test", "test", "test", "test", "test", "test"),
                mutableListOf("test", "test", "test", "test", "test", "test", "test", "test"),
                mutableListOf("test", "test", "test", "test", "test", "test", "test", "test"),
                mutableListOf("test", "test", "test", "test", "test", "test", "test", "test"),
                mutableListOf("test", "test", "test", "test", "test", "test", "test", "test"),
                mutableListOf("test", "test", "test", "test", "test", "test", "test", "test"),
                mutableListOf("test", "test", "test", "test", "test", "test", "test", "test"),
                mutableListOf("test", "test", "test", "test", "test", "test", "test", "test"),
                mutableListOf("test", "test", "test", "test", "test", "test", "test", "test"),
                mutableListOf("test", "test", "test", "test", "test", "test", "test", "test"),
                mutableListOf("test", "test", "test", "test", "test", "test", "test", "test"),
                mutableListOf("test", "test", "test", "test", "test", "test", "test", "test"),
                mutableListOf("test", "test", "test", "test", "test", "test", "test", "test"),
                mutableListOf("test", "test", "test", "test", "test", "test", "test", "test"),
                mutableListOf("test", "test", "test", "test", "test", "test", "test", "test"),
                mutableListOf("test", "test", "test", "test", "test", "test", "test", "test"),
                mutableListOf("test", "test", "test", "test", "test", "test", "test", "test"),
                mutableListOf("test", "test", "test", "test", "test", "test", "test", "test"),
                mutableListOf("test", "test", "test", "test", "test", "test", "test", "test"),
                mutableListOf("test", "test", "test", "test", "test", "test", "test", "test"),
                mutableListOf("test", "test", "test", "test", "test", "test", "test", "test"),
                mutableListOf("test", "test", "test", "test", "test", "test", "test", "test"),
                mutableListOf("test", "test", "test", "test", "test", "test", "test", "test"),
                mutableListOf("test", "test", "test", "test", "test", "test", "test", "test"),
                mutableListOf("test", "test", "test", "test", "test", "test", "test", "test"),
                mutableListOf("test", "test", "test", "test", "test", "test", "test", "test"),
                mutableListOf("test", "test", "test", "test", "test", "test", "test", "test"),
                mutableListOf("test", "test", "test", "test", "test", "test", "test", "test"),
                mutableListOf("test", "test", "test", "test", "test", "test", "test", "test"),
                mutableListOf("test", "test", "test", "test", "test", "test", "test", "test"),
                mutableListOf("test", "test", "test", "test", "test", "test", "test", "test"),

                )
        )
    }

    override fun initAction() {

    }


}