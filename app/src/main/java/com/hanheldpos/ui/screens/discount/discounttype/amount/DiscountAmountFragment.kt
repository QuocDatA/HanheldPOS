package com.hanheldpos.ui.screens.discount.discounttype.amount

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentDiscountAmountBinding
import com.hanheldpos.databinding.FragmentDiscountBinding
import com.hanheldpos.ui.base.fragment.BaseFragment
import java.text.DecimalFormat

class DiscountAmountFragment : BaseFragment<FragmentDiscountAmountBinding, DiscountAmountVM>(),
    DiscountAmountUV {
    override fun layoutRes(): Int = R.layout.fragment_discount_amount

    override fun viewModelClass(): Class<DiscountAmountVM> {
        return DiscountAmountVM::class.java;
    }

    override fun initViewModel(viewModel: DiscountAmountVM) {
        viewModel.run {
            init(this@DiscountAmountFragment);
            binding.viewModel =this;
        }

    }

    override fun initView() {
        binding.amountDiscount.let { input ->
            var isEditing = false
            input.doAfterTextChanged {
                if (it.toString().isEmpty() || isEditing) return@doAfterTextChanged;
                isEditing = true;
                val formatter = DecimalFormat("###,###,###.###")
                input.setText(formatter.format(it.toString().replace(",", "").toDouble()));
                input.setSelection(input.length());
                isEditing = false;
            }
        }
    }

    override fun initData() {

    }

    override fun initAction() {

    }
}