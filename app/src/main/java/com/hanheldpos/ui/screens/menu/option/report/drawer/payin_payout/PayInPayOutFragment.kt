package com.hanheldpos.ui.screens.menu.option.report.drawer.payin_payout

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
            if(binding.amountInput.text.toString().toInt() > 1000) {
                if(binding.noteInput.text?.length ?: 0 > 0){
                    binding.btnPayIn.setBackgroundColor(ContextCompat.getColor(this.requireContext(), R.color.color_11))
                    binding.btnPayInText.setTextColor(ContextCompat.getColor(this.requireContext(), R.color.color_0))

                    binding.btnPayOut.setBackgroundColor(ContextCompat.getColor(this.requireContext(), R.color.color_11))
                    binding.btnPayOutText.setTextColor(ContextCompat.getColor(this.requireContext(), R.color.color_0))

                    binding.btnPayInText.setText(R.string.pay_in)
                    binding.btnPayOutText.setText(R.string.pay_out)
                } else {
                    binding.btnPayIn.setBackgroundResource(R.drawable.bg_outline)
                    binding.btnPayInText.setTextColor(ContextCompat.getColor(this.requireContext(), R.color.color_8))

                    binding.btnPayOut.setBackgroundResource(R.drawable.bg_outline)
                    binding.btnPayOutText.setTextColor(ContextCompat.getColor(this.requireContext(), R.color.color_8))

                    binding.btnPayInText.setText(R.string.end_drawer)
                    binding.btnPayOutText.setText(R.string.pay_in_out)
                }
            } else {
                binding.btnPayIn.setBackgroundResource(R.drawable.bg_outline)
                binding.btnPayInText.setTextColor(ContextCompat.getColor(this.requireContext(), R.color.color_8))

                binding.btnPayOut.setBackgroundResource(R.drawable.bg_outline)
                binding.btnPayOutText.setTextColor(ContextCompat.getColor(this.requireContext(), R.color.color_8))

                binding.btnPayInText.setText(R.string.end_drawer)
                binding.btnPayOutText.setText(R.string.pay_in_out)
            }
        }
        binding.noteInput.doAfterTextChanged {
            if(binding.amountInput.text.toString().toInt() > 1000) {
                if(binding.noteInput.text?.length ?: 0 > 0){
                    binding.btnPayIn.setBackgroundColor(ContextCompat.getColor(this.requireContext(), R.color.color_11))
                    binding.btnPayInText.setTextColor(ContextCompat.getColor(this.requireContext(), R.color.color_0))

                    binding.btnPayOut.setBackgroundColor(ContextCompat.getColor(this.requireContext(), R.color.color_11))
                    binding.btnPayOutText.setTextColor(ContextCompat.getColor(this.requireContext(), R.color.color_0))

                    binding.btnPayInText.setText(R.string.pay_in)
                    binding.btnPayOutText.setText(R.string.pay_out)
                } else {
                    binding.btnPayIn.setBackgroundResource(R.drawable.bg_outline)
                    binding.btnPayInText.setTextColor(ContextCompat.getColor(this.requireContext(), R.color.color_8))

                    binding.btnPayOut.setBackgroundResource(R.drawable.bg_outline)
                    binding.btnPayOutText.setTextColor(ContextCompat.getColor(this.requireContext(), R.color.color_8))


                    binding.btnPayInText.setText(R.string.end_drawer)
                    binding.btnPayOutText.setText(R.string.pay_in_out)
                }
            } else {
                binding.btnPayIn.setBackgroundResource(R.drawable.bg_outline)
                binding.btnPayInText.setTextColor(ContextCompat.getColor(this.requireContext(), R.color.color_8))

                binding.btnPayOut.setBackgroundResource(R.drawable.bg_outline)
                binding.btnPayOutText.setTextColor(ContextCompat.getColor(this.requireContext(), R.color.color_8))

                binding.btnPayInText.setText(R.string.end_drawer)
                binding.btnPayOutText.setText(R.string.pay_in_out)
            }
        }
    }

    override fun initData() {

    }

    override fun initAction() {

    }

    override fun getBack() {
        onFragmentBackPressed()
    }

}