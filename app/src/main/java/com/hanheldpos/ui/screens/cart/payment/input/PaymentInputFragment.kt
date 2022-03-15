package com.hanheldpos.ui.screens.cart.payment.input

import android.annotation.SuppressLint
import androidx.core.widget.doAfterTextChanged
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp
import com.hanheldpos.databinding.FragmentPaymentInputBinding
import com.hanheldpos.extension.toNiceString
import com.hanheldpos.model.UserHelper
import com.hanheldpos.model.cart.payment.PaymentMethodType
import com.hanheldpos.model.cart.payment.PaymentOrder
import com.hanheldpos.model.keyboard.KeyBoardType
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.input.KeyBoardVM
import com.hanheldpos.utils.PriceHelper
import com.hanheldpos.utils.time.DateTimeHelper
import java.util.*

class PaymentInputFragment(
    private val payable: Double,
    private val paymentMethod: PaymentMethodResp,
    private val listener: PaymentInputListener? = null
) :
    BaseFragment<FragmentPaymentInputBinding, PaymentInputVM>(), PaymentInputUV {

    //ViewModel
    private val keyBoardVM = KeyBoardVM()

    override fun layoutRes() = R.layout.fragment_payment_input

    override fun viewModelClass(): Class<PaymentInputVM> {
        return PaymentInputVM::class.java
    }

    override fun initViewModel(viewModel: PaymentInputVM) {
        viewModel.run {
            init(this@PaymentInputFragment)
            binding.viewModel = this
            binding.keyboardVM = keyBoardVM
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        keyBoardVM.onListener(listener = object : KeyBoardVM.KeyBoardCallBack {
            override fun onComplete() {
                viewModel.onComplete()
            }

            override fun onCancel() {
                viewModel.onCancel()
            }
        })

        binding.textInputLayout.setEndIconOnClickListener {
            clearInput()
            binding.textInputLayout.isEndIconVisible = false
        }
    }

    override fun onCancel() {
        navigator.goOneBack()
    }

    override fun onComplete() {
        navigator.goOneBack()
        val paymentOrder = PaymentOrder(
            paymentMethod._id,
            paymentMethod.ApplyToId,
            paymentMethod.PaymentMethodType,
            paymentMethod.Title,
            keyBoardVM.input.value?.toDouble(),
            keyBoardVM.input.value?.toDouble(),
            UserHelper.curEmployee?.FullName,
            null,
            null,
            DateTimeHelper.dateToString(
                Date(), DateTimeHelper.Format.FULL_DATE_UTC_TIMEZONE)
        )
        if (!keyBoardVM.input.value?.trim().equals(""))
            listener?.onCompleteTable(paymentOrder)
    }

    override fun initData() {
        if (paymentMethod.PaymentMethodType == PaymentMethodType.CASH.value) {
            clearInput()
        } else {
            keyBoardVM.input.value = payable.toNiceString()
        }

        keyBoardVM.keyBoardType.value= viewModel.setUpKeyboard(paymentMethod, payable)

        binding.numberPaymentInput.let { input ->
            var isEditing = false
            input.doAfterTextChanged {
                binding.textInputLayout.isEndIconVisible = true
                if (it.toString().isEmpty()) {
                    binding.textInputLayout.isEndIconVisible = false
                } else {
                    binding.textInputLayout.setEndIconDrawable(R.drawable.ic_clear_text)
                }
                if (isEditing) {
                    return@doAfterTextChanged
                } else {
                    isEditing = true
                    if (keyBoardVM.keyBoardType.value == (KeyBoardType.Text)) {
                        input.setText(it.toString())
                    } else {
                        input.setText(PriceHelper.formatStringPrice(it.toString()))
                    }
                }
                input.setSelection(input.length())
                isEditing = false
            }
        }

    }

    override fun clearInput() {
        keyBoardVM.input.postValue("")
    }

    override fun initAction() {
    }

    interface PaymentInputListener {
        fun onCompleteTable(paymentOrder: PaymentOrder)
    }
}