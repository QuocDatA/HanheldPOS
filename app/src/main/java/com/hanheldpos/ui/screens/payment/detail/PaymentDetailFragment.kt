package com.hanheldpos.ui.screens.payment.detail

import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentPaymentDetailBinding
import com.hanheldpos.model.payment.PaymentOrder
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.payment.detail.adapter.PaymentDetailAdapter

class PaymentDetailFragment(private val paymentOrderList: List<PaymentOrder>?) :
    BaseFragment<FragmentPaymentDetailBinding, PaymentDetailVM>(), PaymentDetailUV {
    private lateinit var paymentDetailAdapter: PaymentDetailAdapter

    override fun layoutRes(): Int = R.layout.fragment_payment_detail

    override fun viewModelClass(): Class<PaymentDetailVM> {
        return PaymentDetailVM::class.java
    }

    override fun initViewModel(viewModel: PaymentDetailVM) {
        viewModel.run {
            init(this@PaymentDetailFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {
        paymentDetailAdapter = PaymentDetailAdapter()
        binding.paymentOrderContainer.adapter = paymentDetailAdapter
    }

    override fun initData() {
        paymentDetailAdapter.submitList(paymentOrderList?.toMutableList())
    }

    override fun initAction() {
    }

    override fun backPress() {
        onFragmentBackPressed()
    }
}