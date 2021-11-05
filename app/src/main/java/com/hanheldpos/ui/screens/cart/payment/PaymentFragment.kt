package com.hanheldpos.ui.screens.cart.payment

import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.order.settings.DiningOptionItem
import com.hanheldpos.databinding.FragmentPaymentBinding
import com.hanheldpos.model.cart.order.OrderItemModel
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cart.adapter.CartProductAdapter
import com.hanheldpos.ui.screens.cart.payment.adapter.PaymentMethodAdapter
import com.hanheldpos.ui.screens.cart.payment.adapter.PaymentSuggestionAdapter
import com.hanheldpos.ui.screens.product.adapter.GridSpacingItemDecoration


class PaymentFragment : BaseFragment<FragmentPaymentBinding,PaymentVM>(), PaymentUV {
    override fun layoutRes(): Int = R.layout.fragment_payment;

    private lateinit var paymentMethodAdapter: PaymentMethodAdapter
    private lateinit var paymentSuggestionAdapter: PaymentSuggestionAdapter

    private val paymentVM by activityViewModels<PaymentVM>();

    override fun viewModelClass(): Class<PaymentVM> {
        return PaymentVM::class.java;
    }

    override fun initViewModel(viewModel: PaymentVM) {
        viewModel.run {
            init(this@PaymentFragment);
            binding.viewModel = this;
        }
    }

    override fun initView() {

        //region setup payment method recycler view
        paymentMethodAdapter = PaymentMethodAdapter(
            onPaymentMethodClickListener = object : BaseItemClickListener<FakeMethodModel> {
                override fun onItemClick(adapterPosition: Int, item: FakeMethodModel) {
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
            onPaymentSuggestionClickListener = object : BaseItemClickListener<FakeMethodModel> {
                override fun onItemClick(adapterPosition: Int, item: FakeMethodModel) {
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
        val paymentMethod: MutableList<FakeMethodModel> = (paymentVM.initPaymentMethod() as List<FakeMethodModel>).toMutableList() ;

        paymentMethodAdapter.submitList(paymentMethod);
        //endregion

        //region init payment suggestion data
        val paymentSuggestion: MutableList<FakeMethodModel> = (paymentVM.initPaymentSuggestion() as List<FakeMethodModel>).toMutableList() ;

        paymentSuggestionAdapter.submitList(paymentSuggestion);
        //endregion
    }

    override fun initAction() {

    }

    override fun getBack() {
        onFragmentBackPressed()
    }

    data class FakeMethodModel(val text: String? = null, val color: String? = null)
}

