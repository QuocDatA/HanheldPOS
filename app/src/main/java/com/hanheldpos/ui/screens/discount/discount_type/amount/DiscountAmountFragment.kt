package com.hanheldpos.ui.screens.discount.discount_type.amount

import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentDiscountAmountBinding
import com.hanheldpos.model.discount.DiscApplyTo
import com.hanheldpos.model.discount.DiscountTypeEnum
import com.hanheldpos.model.discount.DiscountTypeFor
import com.hanheldpos.model.discount.DiscountUser
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.discount.DiscountFragment
import com.hanheldpos.utils.PriceUtils

class DiscountAmountFragment(
    private val isAlreadyExistDiscountSelect: Boolean = false,
    private val applyToType: DiscApplyTo,
    private val listener: DiscountFragment.DiscountTypeListener
) :
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
        viewModel.isAlreadyExistDiscountSelect.observe(this) {
            binding.btnClearDiscount.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (it) R.color.color_0 else R.color.color_8
                )
            )
        }


        binding.amountDiscount.let { input ->
            var isEditing = false
            input.doAfterTextChanged {
                if (isEditing) return@doAfterTextChanged;
                if (it.toString().isEmpty()) input.setText("0");
                else {
                    isEditing = true;
                    input.setText(PriceUtils.formatStringPrice(it.toString()));
                }
                input.setSelection(input.length());
                isEditing = false;
            }
        }


    }

    override fun initData() {
        viewModel.isAlreadyExistDiscountSelect.postValue(isAlreadyExistDiscountSelect)
    }

    override fun initAction() {
        viewModel.amount.observe(this) {
            listener.validDiscount(validDiscount());
        };
        viewModel.title.observe(this) {
            listener.validDiscount(validDiscount());
        }
        binding.btnClearDiscount.setOnClickListener {
            if (isAlreadyExistDiscountSelect) {
                listener.clearAllDiscountCoupon()
                viewModel.isAlreadyExistDiscountSelect.postValue(false)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().supportFragmentManager.setFragmentResultListener(
            "saveDiscount",
            this
        ) { _, bundle ->
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

    private fun validChooseDiscount(): Boolean {
        return (viewModel.amountValue > 0.0 && !viewModel.title.value.isNullOrEmpty())
    }

    private fun validDiscount(): Boolean {
        return when (applyToType) {
            DiscApplyTo.ITEM -> {
                (viewModel.amountValue == 0.0 && viewModel.title.value.isNullOrEmpty()) || validChooseDiscount()
            }
            DiscApplyTo.ORDER -> {
                validChooseDiscount()
            }
            else -> false
        }
    }
}