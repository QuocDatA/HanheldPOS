package com.hanheldpos.ui.screens.discount.discount_type.amount

import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResultListener
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentDiscountAmountBinding
import com.hanheldpos.model.discount.DiscountTypeEnum
import com.hanheldpos.model.discount.DiscountTypeFor
import com.hanheldpos.model.discount.DiscountUser
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.discount.discount_type.DiscountTypeFragment
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class DiscountAmountFragment(private val listener: DiscountTypeFragment.DiscountTypeListener) :
    BaseFragment<FragmentDiscountAmountBinding, DiscountAmountVM>(),
    DiscountAmountUV {
    override fun layoutRes(): Int = R.layout.fragment_discount_amount

    override fun viewModelClass(): Class<DiscountAmountVM> {
        return DiscountAmountVM::class.java;
    }

    override fun initViewModel(viewModel: DiscountAmountVM) {
        viewModel.run {
            init(this@DiscountAmountFragment);
            initLifeCycle(this@DiscountAmountFragment);
            binding.viewModel = this;
        }

    }

    override fun initView() {
        requireActivity().supportFragmentManager.setFragmentResultListener("saveDiscount",this) { _, bundle ->
            if (bundle.getSerializable("DiscountTypeFor") == DiscountTypeFor.AMOUNT) {

                listener.discountUserChoose(
                    DiscountUser(
                        DiscountName = viewModel.title.value!!,
                        DiscountValue = viewModel.amountValue,
                        DiscountType = DiscountTypeEnum.AMOUNT.value
                    )
                )
            }

        }

        binding.amountDiscount.let { input ->
            var isEditing = false
            input.doAfterTextChanged {
                if (isEditing) return@doAfterTextChanged;
                if (it.toString().isEmpty()) input.setText("0");
                else {
                    isEditing = true;
                    val dfSymbols = DecimalFormatSymbols()
                    dfSymbols.decimalSeparator = '.'
                    dfSymbols.groupingSeparator = ','
                    val df = DecimalFormat("###", dfSymbols)
                    df.groupingSize = 3
                    df.isGroupingUsed = true
                    val text = df.format(it.toString().replace(",", "").toDouble());
                    input.setText(text);

                }
                input.setSelection(input.length());
                isEditing = false;
            }
        }




    }

    override fun initData() {

    }

    override fun initAction() {
        viewModel.amount.observe(this,{
            listener.validDiscount(viewModel.amountValue > 0.0 && !viewModel.title.value.isNullOrEmpty());
        });
        viewModel.title.observe(this,{
            listener.validDiscount(viewModel.amountValue > 0.0 && !viewModel.title.value.isNullOrEmpty());
        })
    }
}