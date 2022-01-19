package com.hanheldpos.ui.screens.menu.option.report.current_drawer.payin_payout

import androidx.core.widget.doAfterTextChanged
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentPayInPayOutBinding
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.utils.PriceHelper

class PayInPayOutFragment : BaseFragment<FragmentPayInPayOutBinding, PayInPayOutVM>(),
    PayInPayOutUV {
    override fun layoutRes() = R.layout.fragment_pay_in_pay_out

    override fun viewModelClass(): Class<PayInPayOutVM> {
        return PayInPayOutVM::class.java
    }

    override fun initViewModel(viewModel: PayInPayOutVM) {
        viewModel.run {
            init(this@PayInPayOutFragment)
            initLifeCycle(this@PayInPayOutFragment);
            binding.viewModel = this
        }
    }

    override fun initView() {
        binding.amountInput.let { input ->
            var isEditing = false
            input.doAfterTextChanged {
                if (isEditing) return@doAfterTextChanged;
                isEditing = true;
                val text = it.toString()
                input.setText(PriceHelper.formatStringPrice(text))
                input.setSelection(input.length());
                isEditing = false;
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