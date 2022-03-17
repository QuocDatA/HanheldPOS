package com.hanheldpos.ui.screens.payment.input

import android.text.Editable
import android.text.TextWatcher
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentPaymentInputBinding
import com.hanheldpos.model.keyboard.KeyBoardType
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.input.KeyBoardVM
import com.hanheldpos.utils.PriceUtils

class PaymentInputFragment(
    private val title: String,
    private val balance: Double,
    private val keyBoardType: KeyBoardType,
    private val listener: PaymentInputListener
) :
    BaseFragment<FragmentPaymentInputBinding, PaymentInputVM>(), PaymentInputUV {

    //ViewModel
    private val keyBoardVM = KeyBoardVM(keyBoardType)

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

    override fun initView() {

        when (keyBoardType) {
            KeyBoardType.NumberOnly -> {
                setViewForAmount()
            }
            KeyBoardType.Text -> {
                setViewForCardNumber()
            }
            else -> {

            }
        }
        binding.paymentInputTitle.text = title

    }

    private fun setViewForAmount() {
        viewModel.inputAmount = balance
        binding.numberPaymentInput.hint = getString(R.string.enter_amount)

        keyBoardVM.onListener(
            this,
            binding.numberPaymentInput,
            initInput = PriceUtils.formatStringPrice(viewModel.inputAmount),
            listener = object : KeyBoardVM.KeyBoardCallBack {
                override fun onComplete() {
                    onCancel()
                    listener.onSave(viewModel.inputAmount)
                }

                override fun onCancel() {
                    onFragmentBackPressed()
                }
            })
        binding.numberPaymentInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    viewModel.inputAmount = 0.0
                    return
                }
                binding.numberPaymentInput.removeTextChangedListener(this)
                viewModel.inputAmount = s.replace(Regex("[,]"), "").toDouble()
                binding.numberPaymentInput.setText(PriceUtils.formatStringPrice(viewModel.inputAmount))
                binding.numberPaymentInput.addTextChangedListener(this)
            }
        })
    }

    private fun setViewForCardNumber() {
        binding.numberPaymentInput.hint = getString(R.string.card_number)

        keyBoardVM.onListener(
            this,
            binding.numberPaymentInput,
            initInput = viewModel.inputCardNumber,
            listener = object : KeyBoardVM.KeyBoardCallBack {
                override fun onComplete() {
                    onCancel()
                    listener.onSave(viewModel.inputCardNumber)
                }

                override fun onCancel() {
                    onFragmentBackPressed()
                }
            })
        binding.numberPaymentInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    viewModel.inputCardNumber = ""
                    return
                }
                binding.numberPaymentInput.removeTextChangedListener(this)
                viewModel.inputCardNumber = s.replace(Regex("[ ]"), "")
                binding.numberPaymentInput.setText(viewModel.inputCardNumber)
                binding.numberPaymentInput.addTextChangedListener(this)
            }
        })
    }

    override fun initData() {
    }

    override fun clearInput() {
        keyBoardVM.clearText()
    }

    override fun initAction() {
    }

    interface PaymentInputListener {
        fun onSave(amount: Double): Unit {}
        fun onSave(cartNumber: String): Unit {}
    }
}