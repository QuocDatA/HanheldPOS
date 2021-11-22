package com.hanheldpos.ui.screens.discount.discounttype.percentage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentDiscountPercentageBinding
import com.hanheldpos.ui.base.fragment.BaseFragment
import java.text.DecimalFormat


class DiscountPercentageFragment :
    BaseFragment<FragmentDiscountPercentageBinding, DiscountPercentageVM>(), DiscountPercentageUV {
    override fun layoutRes(): Int = R.layout.fragment_discount_percentage;

    override fun viewModelClass(): Class<DiscountPercentageVM> {
        return DiscountPercentageVM::class.java;
    }

    override fun initViewModel(viewModel: DiscountPercentageVM) {
        viewModel.run {
            init(this@DiscountPercentageFragment);
            binding.viewModel = this;
        }
    }

    override fun initView() {
        binding.percentDiscount.let { input ->
            var isEditing = false
            input.doAfterTextChanged {
                if (isEditing) return@doAfterTextChanged;
                isEditing = true;
                if (it.toString().isEmpty())
                    input.setText(0.toString())
                else if (it.toString().toInt() > 100)
                    input.setText(100.toString())
                else input.setText(it.toString().toInt().toString())
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