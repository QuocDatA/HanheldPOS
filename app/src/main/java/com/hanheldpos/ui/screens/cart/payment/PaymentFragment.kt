package com.hanheldpos.ui.screens.cart.payment

import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp
import com.hanheldpos.data.api.pojo.payment.PaymentSuggestionItem
import com.hanheldpos.databinding.FragmentPaymentBinding
import com.hanheldpos.model.UserHelper
import com.hanheldpos.model.cart.payment.PaymentMethodType
import com.hanheldpos.model.cart.payment.PaymentOrder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.adapter.GridSpacingItemDecoration
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cart.payment.adapter.PaymentMethodAdapter
import com.hanheldpos.ui.screens.cart.payment.adapter.PaymentSuggestionAdapter
import com.hanheldpos.ui.screens.cart.payment.input.PaymentInputFragment
import com.hanheldpos.utils.time.DateTimeHelper
import java.util.*


class PaymentFragment(private val alreadyBill : Boolean, private val payable: Double, private var listener: PaymentCallback) :
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
            onPaymentMethodClickListener = object : BaseItemClickListener<PaymentMethodResp> {
                override fun onItemClick(adapterPosition: Int, item: PaymentMethodResp) {
                    navigator.goTo(PaymentInputFragment(listener = object : PaymentInputFragment.PaymentInputListener {
                        override fun onCompleteTable(numberCustomer: Int) {

                        }
                    }, paymentMethod = item, payable = payable))
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
        binding.payable = this.payable
        //region init payment method data
        val paymentMethods = viewModel.getPaymentMethods()
        paymentMethodAdapter.submitList(paymentMethods)
        //endregion

        //region init payment suggestion data
        val paymentSuggestion: MutableList<PaymentSuggestionItem> =
            (viewModel.initPaymentSuggestion() as List<PaymentSuggestionItem>).toMutableList()
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
        //TODO : Fake payment cash for testing
        val paymentCash = viewModel.getPaymentMethods().find { payment-> payment.PaymentMethodType == PaymentMethodType.CASH.value }
        paymentCash?.let {
            onFragmentBackPressed()
            listener.onPaymentComplete(
                PaymentOrder(
                    paymentCash._id,
                    paymentCash.ApplyToId,
                    paymentCash.PaymentMethodType,
                    paymentCash.Title,
                    payable,
                    payable,
                    UserHelper.curEmployee?.FullName,
                    null,
                    DateTimeHelper.dateToString(Date(), DateTimeHelper.Format.FULL_DATE_UTC_TIMEZONE)
                )
            )
            if(alreadyBill) listener.onPayment(true)

        }

    }

    interface PaymentCallback {
        fun onPaymentComplete(paymentOrder: PaymentOrder)
        fun onPayment(isSuccess : Boolean)
    }
}

