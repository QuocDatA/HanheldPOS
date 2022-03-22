package com.hanheldpos.ui.screens.payment

import android.view.KeyEvent
import android.view.View
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.payment.PaymentSuggestionItem
import com.hanheldpos.databinding.FragmentPaymentBinding
import com.hanheldpos.model.keyboard.KeyBoardType
import com.hanheldpos.model.payment.PaymentFactory
import com.hanheldpos.model.payment.PaymentMethodType
import com.hanheldpos.model.payment.PaymentOrder
import com.hanheldpos.model.payment.method.BaseCardPayment
import com.hanheldpos.model.payment.method.BasePayment
import com.hanheldpos.model.payment.method.GiftCartPayment
import com.hanheldpos.model.payment.method.WalletPayment
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.adapter.GridSpacingItemDecoration
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cart.CurCartData
import com.hanheldpos.ui.screens.payment.adapter.PaymentMethodAdapter
import com.hanheldpos.ui.screens.payment.adapter.PaymentSuggestionAdapter
import com.hanheldpos.ui.screens.payment.detail.PaymentDetailFragment
import com.hanheldpos.ui.screens.payment.input.PaymentInputFragment
import com.hanheldpos.ui.screens.payment.voucher.VoucherFragment
import com.hanheldpos.utils.PriceUtils


class PaymentFragment(
    private val alreadyBill: Boolean,
    private var balance: Double,
    private val paymentList: List<PaymentOrder>?,
    private var listener: PaymentCallback
) :
    BaseFragment<FragmentPaymentBinding, PaymentVM>(), PaymentUV {

    override fun layoutRes(): Int = R.layout.fragment_payment

    private lateinit var paymentMethodAdapter: PaymentMethodAdapter
    private lateinit var paymentSuggestionAdapter: PaymentSuggestionAdapter


    override fun viewModelClass(): Class<PaymentVM> {
        return PaymentVM::class.java
    }

    override fun initViewModel(viewModel: PaymentVM) {
        viewModel.run {
            init(this@PaymentFragment)
            binding.viewModel = this
        }

    }

    override fun initView() {

        //region setup payment method recycler view
        paymentMethodAdapter = PaymentMethodAdapter(
            onPaymentMethodClickListener = object : BaseItemClickListener<BasePayment> {
                override fun onItemClick(adapterPosition: Int, item: BasePayment) {
                    item.startPayment(
                        viewModel.balance.value!!,
                        CurCartData.cartModel?.orderGuid!!,
                        CurCartData.cartModel?.customer?._Id
                    )
                }
            },
        )
        binding.paymentMethodContainer.apply {
            addItemDecoration(
                GridSpacingItemDecoration(
                    spanCount = 2,
                    includeEdge = false,
                    spacing = resources.getDimensionPixelSize(R.dimen._10sdp)
                )
            )
            binding.paymentMethodContainer.adapter = paymentMethodAdapter
        }
        //endregion

        //region setup payment suggestion pay in cash recycler view
        paymentSuggestionAdapter = PaymentSuggestionAdapter(
            onPaymentSuggestionClickListener = object :
                BaseItemClickListener<PaymentSuggestionItem> {
                override fun onItemClick(adapterPosition: Int, item: PaymentSuggestionItem) {
                    paymentMethodAdapter.currentList.find { PaymentMethodType.fromInt(it.paymentMethod.PaymentMethodType) == PaymentMethodType.CASH }
                        ?.let {
                            paymentChosenSuccess(it, item.price)
                        }
                }
            },
        )
        binding.paymentSuggestionContainer.apply {
            addItemDecoration(
                GridSpacingItemDecoration(
                    spanCount = 4,
                    includeEdge = false,
                    spacing = resources.getDimensionPixelSize(R.dimen._7sdp)
                )
            )
            binding.paymentSuggestionContainer.adapter = paymentSuggestionAdapter
        }
        //endregion

        viewModel.listPaymentChosen.observe(this) {
            binding.paymentDetail.visibility = if (it.isNullOrEmpty()) View.GONE else View.VISIBLE
        }

    }

    override fun initData() {
        viewModel.balance.postValue(this.balance)
        viewModel.listPaymentChosen.postValue(this.paymentList?.toMutableList() ?: mutableListOf())
        val paymentMethods = viewModel.getPaymentMethods().map { payment ->
            PaymentFactory.getPaymentMethod(
                payment,
                callback = object : BasePayment.PaymentMethodCallback {
                    override fun onShowPaymentInputAmount(
                        base: BasePayment,
                        balance: Double,
                        orderId: String,
                        customerId: String?,
                        cardCode: String?,
                        balanceCart: Double?
                    ) {
                        navigator.goTo(
                            PaymentInputFragment(
                                title = "${payment.Title} (${getString(R.string.amount_due)} ${
                                    PriceUtils.formatStringPrice(
                                        balance
                                    )
                                })",
                                amountDue = balance,
                                keyBoardType = KeyBoardType.NumberOnly,
                                listener = object : PaymentInputFragment.PaymentInputListener {
                                    override fun onSave(amount: Double, balance: Double?) {
                                        if (amount <= 0) return
                                        if (balance != null && cardCode != null) {
                                            viewModel.syncPaymentCard(
                                                base as BaseCardPayment,
                                                CurCartData.cartModel?.orderGuid!!,
                                                cardCode,
                                                amount,
                                                balance,
                                                CurCartData.cartModel?.customer?._Id
                                            )
                                        } else
                                            paymentChosenSuccess(base, amount)
                                    }
                                },
                                balance = balanceCart
                            )
                        )
                    }

                    override fun onShowPaymentInputCartNumber(
                        base: BaseCardPayment,
                        balance: Double,
                        orderId: String,
                        customerId: String?
                    ) {
                        navigator.goTo(
                            PaymentInputFragment(
                                title = getString(R.string.input_card_number),
                                amountDue = balance,
                                keyBoardType = KeyBoardType.Text,
                                listener = object : PaymentInputFragment.PaymentInputListener {
                                    override fun onSave(cardNumber: String) {
                                        when (base.javaClass) {
                                            GiftCartPayment::class.java -> {
                                                viewModel.processPaymentGiftCard(base, cardNumber)
                                            }
                                            WalletPayment::class.java -> {
                                                viewModel.processPaymentWalletCard(base, cardNumber)
                                            }
                                        }

                                    }
                                }
                            )
                        )
                    }

                    override fun onShowCashVoucherList(
                        base: BasePayment,
                    ) {
                        navigator.goTo(
                            VoucherFragment(
                                payment.ListPayment,
                                listener = object : VoucherFragment.CashVoucherCallBack {
                                    override fun onVoucherSelected(amount: Double) {
                                        paymentChosenSuccess(base, amount)
                                    }
                                },
                            )
                        )
                    }
                },
            )
        }
        paymentMethodAdapter.submitList(paymentMethods)

        val paymentSuggestion: MutableList<PaymentSuggestionItem> =
            viewModel.initPaymentSuggestion().toMutableList()
        paymentSuggestionAdapter.submitList(paymentSuggestion)

    }

    override fun initAction() {
        viewModel.balance.observe(this) {
            if (it != null && it <= 0) {
                viewModel.completedPayment(alreadyBill, listener)
            }
        }
        binding.totalPriceButton.setOnClickListener {
            paymentMethodAdapter.currentList.find { PaymentMethodType.fromInt(it.paymentMethod.PaymentMethodType) == PaymentMethodType.CASH }
                ?.let {
                    paymentChosenSuccess(it, viewModel.balance.value!!)
                }
        }
        viewModel.listPaymentChosen.observe(this) {
            if (it.size > 0) {
                disableBackButton()
            }
        }
    }

    override fun getBack() {
        if(viewModel.listPaymentChosen.value?.isNullOrEmpty() == true) {
            listener.onPaymentState(false)
            onFragmentBackPressed()
        }
    }


    override fun openPaymentDetail() {
        navigator.goTo(PaymentDetailFragment(viewModel.listPaymentChosen.value))
    }

    override fun onValidCardNumber(
        payment: BaseCardPayment,
        cardCode: String?,
        balanceCart: Double?
    ) {
        payment.onValidPayment(
            viewModel.balance.value!!,
            cardCode,
            balanceCart,
            CurCartData.cartModel?.orderGuid!!,
            CurCartData.cartModel?.customer?._Id
        )
    }

    override fun paymentChosenSuccess(payment: BasePayment, amount: Double) {
        val balance = viewModel.balance.value!!
        viewModel.balance.postValue(balance - amount)
        val payable = payment.getPayable(amount, balance)
        viewModel.addPaymentChosen(
            PaymentOrder(
                payment.paymentMethod,
                payable = payable,
                overPay = amount,
            ), listener
        )
    }

    interface PaymentCallback {
        fun onPaymentComplete(paymentOrderList: List<PaymentOrder>)
        fun onPaymentSelected()
        fun onPaymentState(isSuccess: Boolean)
    }

    private fun disableBackButton() {
        this.requireView().isFocusableInTouchMode = true
        this.requireView().requestFocus()
        this.requireView().setOnKeyListener { _, keyCode, _ ->
            keyCode == KeyEvent.KEYCODE_BACK
        }
    }
}

