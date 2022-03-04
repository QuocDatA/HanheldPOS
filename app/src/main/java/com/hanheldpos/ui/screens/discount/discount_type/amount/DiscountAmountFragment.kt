package com.hanheldpos.ui.screens.discount.discount_type.amount

import androidx.core.widget.doAfterTextChanged
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentDiscountAmountBinding
import com.hanheldpos.model.discount.DiscountApplyToType
import com.hanheldpos.model.discount.DiscountTypeEnum
import com.hanheldpos.model.discount.DiscountTypeFor
import com.hanheldpos.model.discount.DiscountUser
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.discount.DiscountFragment
import com.hanheldpos.ui.screens.discount.discount_type.DiscountTypeItemFragment
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class DiscountAmountFragment(private val applyToType: DiscountApplyToType, private val listener: DiscountFragment.DiscountTypeListener) :
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
        viewModel.amount.observe(this) {
            listener.validDiscount(validDiscount());
        };
        viewModel.title.observe(this) {
            listener.validDiscount(validDiscount());
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().supportFragmentManager.setFragmentResultListener("saveDiscount",this) { _, bundle ->
            if (bundle.getSerializable("DiscountTypeFor") == DiscountTypeFor.AMOUNT && validChooseDiscount()) {
                listener.discountUserChoose(
                    DiscountUser(
                        DiscountName = viewModel.title.value!!,
                        DiscountValue = viewModel.amountValue,
                        DiscountType = DiscountTypeEnum.AMOUNT.value
                    )
                )
            }
        }
        listener.validDiscount(validDiscount());
    }

    private fun validChooseDiscount() : Boolean {
        return (viewModel.amountValue > 0.0 && !viewModel.title.value.isNullOrEmpty())
    }

    private fun validDiscount(): Boolean {
        return when (applyToType) {
            DiscountApplyToType.ITEM_DISCOUNT_APPLY_TO -> {
                (viewModel.amountValue == 0.0 && viewModel.title.value.isNullOrEmpty()) || validChooseDiscount()
            }
            DiscountApplyToType.ORDER_DISCOUNT_APPLY_TO -> {
                validChooseDiscount()
            }
        }
    }
}