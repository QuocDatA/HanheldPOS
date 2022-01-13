package com.hanheldpos.ui.screens.cart.payment.input

import android.annotation.SuppressLint
import android.view.View
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp
import com.hanheldpos.databinding.FragmentPaymentInputBinding
import com.hanheldpos.extension.toNiceString
import com.hanheldpos.model.keyboard.KeyBoardType
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.input.KeyBoardVM

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
    private val keyBoardVM = KeyBoardVM();

    override fun layoutRes() = R.layout.fragment_payment_input

    override fun viewModelClass(): Class<PaymentInputVM> {
        return PaymentInputVM::class.java
    }

    override fun initViewModel(viewModel: PaymentInputVM) {
        viewModel.run {
            init(this@PaymentInputFragment);
            binding.viewModel = this;
            binding.keyboardVM = keyBoardVM;
            binding.keyBoardContainer.textPad.viewModel = keyBoardVM;
            binding.keyBoardContainer.numberPad.viewModel = keyBoardVM;
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        keyBoardVM.onListener(listener = object : KeyBoardVM.KeyBoardCallBack {
            override fun onComplete() {
                viewModel.onComplete();
            }

            override fun onCancel() {
                viewModel.onCancel();
            }

            override fun onSwitch() {
                binding.keyBoardContainer.keyBoardType = keyBoardVM.keyBoardType
            }

            override fun onCapLock() {
                binding.keyBoardContainer.textPad.isCapLock = keyBoardVM.isCapLock
            }
        });
        binding.paymentInputTitle.setText(paymentMethod.Title + " (Amount Due " + payable.toNiceString() + ")")
    }

    override fun onCancel() {
        navigator.goOneBack();
    }

    override fun onComplete() {
        navigator.goOneBack();
        if (!keyBoardVM.input.value?.trim().equals(""))
            listener?.onCompleteTable(Integer.valueOf(keyBoardVM.input.value));
    }

    override fun initData() {
        keyBoardVM.input.value = payable.toNiceString();
        keyBoardVM.keyBoardType = KeyBoardType.Number
        binding.keyBoardContainer.textPad.isCapLock = keyBoardVM.isCapLock
        binding.keyBoardContainer.keyBoardType = keyBoardVM.keyBoardType
        binding.keyBoardContainer.numberPad.keyBoardType = keyBoardVM.keyBoardType
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