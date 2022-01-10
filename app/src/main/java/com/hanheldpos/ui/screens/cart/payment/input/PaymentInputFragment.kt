package com.hanheldpos.ui.screens.cart.payment.input

import android.annotation.SuppressLint
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp
import com.hanheldpos.databinding.FragmentPaymentInputBinding
import com.hanheldpos.extension.toNiceString
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.home.table.input.NumberPadVM

class PaymentInputFragment(
    private val payable: Double,
    private val paymentMethod: PaymentMethodResp,
    private val listener: PaymentInputListener? = null
) :
    BaseFragment<FragmentPaymentInputBinding, PaymentInputVM>(), PaymentInputUV {

    interface PaymentInputListener {
        fun onCompleteTable(numberCustomer: Int)
    }

    //ViewModel
    private val numberPadVM = NumberPadVM();

    override fun layoutRes() = R.layout.fragment_payment_input

    override fun viewModelClass(): Class<PaymentInputVM> {
        return PaymentInputVM::class.java
    }

    override fun initViewModel(viewModel: PaymentInputVM) {
        viewModel.run {
            init(this@PaymentInputFragment);
            binding.viewModel = this;
            binding.numberPadVM = numberPadVM;
            binding.numberPad.viewModel = numberPadVM;
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        numberPadVM.onListener(listener = object : NumberPadVM.NumberPadCallBack {
            override fun onComplete() {
                viewModel.onComplete();
            }

            override fun onCancel() {
                viewModel.onCancel();
            }
        });
        binding.paymentInputTitle.setText(paymentMethod.Title + " (Amount Due " + payable.toNiceString() + ")")
    }

    override fun onCancel() {
        navigator.goOneBack();
    }

    override fun onComplete() {
        navigator.goOneBack();
        if (!numberPadVM.input.value?.trim().equals(""))
            listener?.onCompleteTable(Integer.valueOf(numberPadVM.input.value));
    }

    override fun initData() {
        numberPadVM.input.value = payable.toNiceString();
    }

    override fun initAction() {
    }

    companion object {
        fun getInstance(
            listener: PaymentInputFragment.PaymentInputListener? = null,
            paymentMethod: PaymentMethodResp,
            payable: Double
        ): PaymentInputFragment {
            return PaymentInputFragment(
                listener = listener,
                paymentMethod = paymentMethod,
                payable = payable
            ).apply {

            };
        }
    }
}