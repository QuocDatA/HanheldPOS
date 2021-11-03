package com.hanheldpos.ui.screens.cart.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentPaymentBinding
import com.hanheldpos.ui.base.fragment.BaseFragment


class PaymentFragment : BaseFragment<FragmentPaymentBinding,PaymentVM>(), PaymentUV {
    override fun layoutRes(): Int = R.layout.fragment_payment;

    override fun viewModelClass(): Class<PaymentVM> {
        return PaymentVM::class.java;
    }

    override fun initViewModel(viewModel: PaymentVM) {

    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun initAction() {

    }

}