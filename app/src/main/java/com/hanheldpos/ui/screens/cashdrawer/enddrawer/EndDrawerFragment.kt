package com.hanheldpos.ui.screens.cashdrawer.enddrawer

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentEndDrawerBinding
import com.hanheldpos.extension.navigateTo
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cashdrawer.CashDrawerHelper
import com.hanheldpos.ui.screens.pincode.PinCodeActivity
import com.hanheldpos.ui.screens.welcome.WelcomeActivity
import com.hanheldpos.utils.PriceHelper

class EndDrawerFragment : BaseFragment<FragmentEndDrawerBinding, EndDrawerVM>(), EndDrawerUV {
    override fun layoutRes() = R.layout.fragment_end_drawer

    override fun viewModelClass(): Class<EndDrawerVM> {
        return EndDrawerVM::class.java
    }

    override fun initViewModel(viewModel: EndDrawerVM) {
        return viewModel.run {
            init(this@EndDrawerFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {
        binding.btnEndDrawer.setOnClickListener {
            if (viewModel.isValid.value == true) {
                activity?.navigateTo(
                    PinCodeActivity::class.java,
                    alsoFinishCurrentActivity = true,
                    alsoClearActivity = true,
                )
                CashDrawerHelper.isEndDrawer = true
            }
        }
        binding.amountInput.let { input ->
            var isEditing = false
            input.doAfterTextChanged {
                if (isEditing) return@doAfterTextChanged;
                if (it.toString().isEmpty()) input.setText("0");
                else {
                    isEditing = true;
                    input.setText(PriceHelper.formatStringPrice(it.toString()));
                }
                input.setSelection(input.length());
                isEditing = false;
            }
        }

        viewModel.isValid.observe(this, {
            if (it) {
                binding.btnEndDrawerText.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_2
                    )
                );
            } else {
                binding.btnEndDrawerText.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_0
                    )
                );
            }
        });
    }

    override fun initData() {
    }

    override fun initAction() {
    }

    override fun getBack() {
        onFragmentBackPressed()
    }
}