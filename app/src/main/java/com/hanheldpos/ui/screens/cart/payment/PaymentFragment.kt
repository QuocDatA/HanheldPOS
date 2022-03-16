package com.hanheldpos.ui.screens.cart.payment

import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.payment.PaymentSuggestionItem
import com.hanheldpos.databinding.FragmentPaymentBinding
import com.hanheldpos.model.cart.payment.PaymentFactory
import com.hanheldpos.model.cart.payment.PaymentOrder
import com.hanheldpos.model.cart.payment.method.BasePayment
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.adapter.GridSpacingItemDecoration
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cart.CurCartData
import com.hanheldpos.ui.screens.cart.payment.adapter.PaymentMethodAdapter
import com.hanheldpos.ui.screens.cart.payment.adapter.PaymentSuggestionAdapter
import com.hanheldpos.ui.screens.cart.payment.detail.PaymentDetailFragment
import com.hanheldpos.ui.screens.cart.payment.input.PaymentInputFragment
import com.hanheldpos.utils.PriceUtils


class PaymentFragment(
    private val alreadyBill: Boolean,
    private var balance: Double,
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
                    item.startPayment(viewModel.balance.value!!,CurCartData.cartModel?.orderGuid!!,CurCartData.cartModel?.customer?._Id)
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

    }

    override fun initData() {
        viewModel.balance.postValue(this.balance)
        //region init payment method data
        val paymentMethods = viewModel.getPaymentMethods().map { payment ->
            PaymentFactory.getPaymentMethod(
                payment,
                callback = object : BasePayment.PaymentMethodCallback {
                    override fun onShowPaymentInput(
                        balance: Double,
                        orderId: String,
                        customerId: String?
                    ) {
                        navigator.goTo(
                            PaymentInputFragment(
                                title = "${payment.Title} (${getString(R.string.amount_due)} ${
                                    PriceUtils.formatStringPrice(
                                        balance
                                    )
                                })", balance = balance,
                            )
                        )
                    }

                    override fun onShowCashVoucherList() {

                    }

                    override fun onPaymentSuccess(payment: PaymentOrder) {

                    }

                    override fun onPayFail() {

                    }

                })
        }
        paymentMethodAdapter.submitList(paymentMethods)
        //endregion

        //region init payment suggestion data
        val paymentSuggestion: MutableList<PaymentSuggestionItem> =
            viewModel.initPaymentSuggestion().toMutableList()
        paymentSuggestionAdapter.submitList(paymentSuggestion)
        //endregion
    }

    override fun initAction() {

    }

    override fun getBack() {
        listener.onPayment(false)
        onFragmentBackPressed()
    }

    override fun getPayment() {
        viewModel.completedPayment(alreadyBill, listener)
    }

    override fun openPaymentDetail() {
        navigator.goTo(PaymentDetailFragment(viewModel.listPaymentChosen.value))
    }

    interface PaymentCallback {
        fun onPaymentComplete(paymentOrderList: List<PaymentOrder>)
        fun onPayment(isSuccess: Boolean)
    }


}

