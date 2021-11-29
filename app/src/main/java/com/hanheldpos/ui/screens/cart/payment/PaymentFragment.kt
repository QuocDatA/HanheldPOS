package com.hanheldpos.ui.screens.cart.payment

import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp
import com.hanheldpos.data.api.pojo.payment.PaymentSuggestionItem
import com.hanheldpos.databinding.FragmentPaymentBinding
import com.hanheldpos.model.cart.payment.PaymentOrder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cart.CartDataVM
import com.hanheldpos.ui.screens.cart.payment.adapter.PaymentMethodAdapter
import com.hanheldpos.ui.screens.cart.payment.adapter.PaymentSuggestionAdapter
import com.hanheldpos.ui.screens.product.adapter.GridSpacingItemDecoration


class PaymentFragment(private var listener: PaymentCallback) :
    BaseFragment<FragmentPaymentBinding, PaymentVM>(), PaymentUV {
    override fun layoutRes(): Int = R.layout.fragment_payment;

    private lateinit var paymentMethodAdapter: PaymentMethodAdapter
    private lateinit var paymentSuggestionAdapter: PaymentSuggestionAdapter

    private val paymentVM by activityViewModels<PaymentVM>();
    private val cartDataVM by activityViewModels<CartDataVM>();

    override fun viewModelClass(): Class<PaymentVM> {
        return PaymentVM::class.java;
    }

    override fun initViewModel(viewModel: PaymentVM) {
        viewModel.run {
            init(this@PaymentFragment);
            binding.viewModel = this;
        }
        binding.cartDataVMPayment = cartDataVM;
    }

    override fun initView() {

        //region setup payment method recycler view
        paymentMethodAdapter = PaymentMethodAdapter(
            onPaymentMethodClickListener = object : BaseItemClickListener<PaymentMethodResp> {
                override fun onItemClick(adapterPosition: Int, item: PaymentMethodResp) {
                }
            },
        );
        binding.paymentMethodContainer.apply {
            addItemDecoration(
                GridSpacingItemDecoration(spanCount = 2,includeEdge = false, spacing = 35)
            )
            binding.paymentMethodContainer.adapter = paymentMethodAdapter;
        };
        //endregion

        //region setup payment suggestion pay in cash recycler view
        paymentSuggestionAdapter = PaymentSuggestionAdapter(
            onPaymentSuggestionClickListener = object : BaseItemClickListener<PaymentSuggestionItem> {
                override fun onItemClick(adapterPosition: Int, item: PaymentSuggestionItem) {
                }
            },
        );
        binding.paymentSuggestionContainer.apply {
            addItemDecoration(
                GridSpacingItemDecoration(spanCount = 4,includeEdge = false, spacing = 35)
            )
            binding.paymentSuggestionContainer.adapter = paymentSuggestionAdapter;
        };
        //endregion

    }

    override fun initData() {

        //region init payment method data
        val paymentMethods = viewModel.getPaymentMethods();
        paymentMethodAdapter.submitList(paymentMethods)
        //endregion

        //region init payment suggestion data
        val paymentSuggestion: MutableList<PaymentSuggestionItem> =
            (paymentVM.initPaymentSuggestion() as List<PaymentSuggestionItem>).toMutableList();
        paymentSuggestionAdapter.submitList(paymentSuggestion);
        //endregion
    }

    override fun initAction() {

    }

    override fun getBack() {
        onFragmentBackPressed()
    }

    override fun getPayment() {
        listener.onPaymentComplete(PaymentOrder(1, 1, 1, "TIEN MAT", 0.0, 0.0, "Nhi Nguyen", "001","01/01/2001"))
        onFragmentBackPressed()
    }

    interface PaymentCallback {
        fun onPaymentComplete(paymentOrder: PaymentOrder)
    }
}

