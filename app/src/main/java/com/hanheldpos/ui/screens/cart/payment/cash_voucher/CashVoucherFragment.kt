package com.hanheldpos.ui.screens.cart.payment.cash_voucher

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp
import com.hanheldpos.data.api.pojo.payment.Voucher
import com.hanheldpos.databinding.FragmentCashVoucherBinding
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.adapter.GridSpacingItemDecoration
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cart.payment.cash_voucher.adapter.CashVoucherAdapter

class CashVoucherFragment(private val paymentMethodResp: PaymentMethodResp) : BaseFragment<FragmentCashVoucherBinding, CashVoucherVM>(),
    CashVoucherUV {

    private lateinit var cashVoucherAdapter: CashVoucherAdapter

    override fun layoutRes(): Int = R.layout.fragment_cash_voucher

    override fun viewModelClass(): Class<CashVoucherVM> {
        return CashVoucherVM::class.java
    }

    override fun initViewModel(viewModel: CashVoucherVM) {
        viewModel.run {
            init(this@CashVoucherFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {
        cashVoucherAdapter = CashVoucherAdapter(onVoucherClickListener = object : BaseItemClickListener<Voucher> {
            override fun onItemClick(adapterPosition: Int, item: Voucher) {

            }
        })
        binding.cashVoucherContainer.apply {
            addItemDecoration(
                GridSpacingItemDecoration(
                    spanCount = 2,
                    includeEdge = false,
                    spacing = resources.getDimensionPixelSize(R.dimen._10sdp)
                )
            )
            binding.cashVoucherContainer.adapter = cashVoucherAdapter
        }
    }

    override fun initData() {
        cashVoucherAdapter.submitList(paymentMethodResp.ListPayment.toMutableList())
    }

    override fun initAction() {
    }

    override fun getBack() {
        onFragmentBackPressed()
    }

}