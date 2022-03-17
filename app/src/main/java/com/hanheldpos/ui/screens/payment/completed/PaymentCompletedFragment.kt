package com.hanheldpos.ui.screens.payment.completed

import com.hanheldpos.R
import com.hanheldpos.binding.setPriceView
import com.hanheldpos.databinding.FragmentPaymentCompletedBinding
import com.hanheldpos.ui.base.fragment.BaseFragment

class PaymentCompletedFragment(
    private val isHasEmployee: Boolean,
    private val payable: Double,
    private val overPay: Double,
    private val listener: PaymentCompletedCallBack,
) : BaseFragment<FragmentPaymentCompletedBinding, PaymentCompletedVM>(), PaymentCompletedUV {
    override fun layoutRes(): Int {
        return R.layout.fragment_payment_completed
    }

    override fun viewModelClass(): Class<PaymentCompletedVM> {
        return PaymentCompletedVM::class.java
    }

    override fun initViewModel(viewModel: PaymentCompletedVM) {
        viewModel.run {
            this.init(this@PaymentCompletedFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {
        setPriceView(binding.textChangeValue, overPay - payable)
        setPriceView(binding.textReceived, overPay)
    }

    override fun initData() {
        viewModel.isAlreadyHasEmployee.postValue(isHasEmployee)

    }

    override fun initAction() {
        binding.btnNewSale.setOnClickListener {
            listener.newSale()
            super.onFragmentBackPressed()
        }
        binding.btnBecomeAMember.setOnClickListener {
            listener.becomeAMember()
        }
    }

    override fun onFragmentBackPressed() {

    }

    interface PaymentCompletedCallBack {
        fun becomeAMember()
        fun newSale()
    }
}