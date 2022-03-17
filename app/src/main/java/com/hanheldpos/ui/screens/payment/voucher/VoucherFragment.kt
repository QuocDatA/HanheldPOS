package com.hanheldpos.ui.screens.payment.voucher

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.payment.Voucher
import com.hanheldpos.databinding.FragmentVoucherBinding
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.adapter.GridSpacingItemDecoration
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.payment.voucher.adapter.CashVoucherAdapter

class VoucherFragment(private val cashVoucherList: List<Voucher>?, private var listener: CashVoucherCallBack) : BaseFragment<FragmentVoucherBinding, VoucherVM>(), VoucherUV {
    private lateinit var cashVoucherAdapter: CashVoucherAdapter

    override fun layoutRes(): Int = R.layout.fragment_voucher

    override fun viewModelClass(): Class<VoucherVM> {
        return VoucherVM::class.java
    }

    override fun initViewModel(viewModel: VoucherVM) {
        viewModel.run {
            init(this@VoucherFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {
        cashVoucherAdapter = CashVoucherAdapter(onCashVoucherClickListener = object : BaseItemClickListener<Voucher> {
            override fun onItemClick(adapterPosition: Int, item: Voucher) {
                listener.onVoucherSelected(item.Money)
                onFragmentBackPressed()
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
        cashVoucherList?.let { cashVoucherAdapter.submitList(it) }
    }

    override fun initAction() {
    }

    interface CashVoucherCallBack {
        fun onVoucherSelected(amount : Double)
    }
}