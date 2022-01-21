package com.hanheldpos.ui.screens.cashdrawer.enddrawer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentEndDrawerBinding
import com.hanheldpos.extension.navigateTo
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cashdrawer.CashDrawerHelper
import com.hanheldpos.ui.screens.pincode.PinCodeActivity
import com.hanheldpos.utils.PriceHelper

class EndDrawerFragment : BaseFragment<FragmentEndDrawerBinding,EndDrawerVM>() , EndDrawerUV {
    override fun layoutRes(): Int {
        return R.layout.fragment_end_drawer;
    }

    override fun viewModelClass(): Class<EndDrawerVM> {
        return EndDrawerVM::class.java;
    }

    override fun initViewModel(viewModel: EndDrawerVM) {
        viewModel.run {
            init(this@EndDrawerFragment);
            binding.viewModel = this;
        }
    }

    override fun initView() {

    }

    override fun initData() {
        binding.btnEndDrawer.setOnClickListener {
            activity?.navigateTo(
                PinCodeActivity::class.java,
                alsoFinishCurrentActivity = true,
                alsoClearActivity = true,
            )
            CashDrawerHelper.isEndDrawer = true
        }
        binding.amountInput.let { input->
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
    }

    override fun initAction() {

    }

    override fun backPress() {
        onFragmentBackPressed();
    }

}