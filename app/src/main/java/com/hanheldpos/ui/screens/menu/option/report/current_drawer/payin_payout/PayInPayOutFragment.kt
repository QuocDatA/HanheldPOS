package com.hanheldpos.ui.screens.menu.option.report.current_drawer.payin_payout

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentPayInPayOutBinding
import com.hanheldpos.ui.base.fragment.BaseFragment

class PayInPayOutFragment : BaseFragment<FragmentPayInPayOutBinding, PayInPayOutVM>(),
    PayInPayOutUV {
    override fun layoutRes() = R.layout.fragment_pay_in_pay_out

    override fun viewModelClass(): Class<PayInPayOutVM> {
        return PayInPayOutVM::class.java
    }

    override fun initViewModel(viewModel: PayInPayOutVM) {
        return viewModel.run {
            init(this@PayInPayOutFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {
        binding.amountInput.doAfterTextChanged {
            binding.isActive = false
            binding.btnPayIn.setBackgroundResource(R.drawable.bg_outline)
            binding.btnPayOut.setBackgroundResource(R.drawable.bg_outline)
            if(binding.amountInput.text.toString().toInt() > 1000) {
                if(binding.noteInput.text?.length ?: 0 > 0){
                    binding.isActive = true
                    binding.btnPayIn.setBackgroundColor(ContextCompat.getColor(this.requireContext(), R.color.color_11))
                    binding.btnPayOut.setBackgroundColor(ContextCompat.getColor(this.requireContext(), R.color.color_11))
                }
            }
        }
        binding.noteInput.doAfterTextChanged {
            binding.isActive = false
            binding.btnPayIn.setBackgroundResource(R.drawable.bg_outline)
            binding.btnPayOut.setBackgroundResource(R.drawable.bg_outline)
            if(binding.amountInput.text.toString().toInt() > 1000) {
                if(binding.noteInput.text?.length ?: 0 > 0){
                    binding.isActive = true
                    binding.btnPayIn.setBackgroundColor(ContextCompat.getColor(this.requireContext(), R.color.color_0))
                    binding.btnPayOut.setBackgroundColor(ContextCompat.getColor(this.requireContext(), R.color.color_0))
                }
            }
        }
    }

    override fun initData() {
        binding.isActive = false
    }

    override fun initAction() {

    }

    override fun getBack() {
        onFragmentBackPressed()
    }

}