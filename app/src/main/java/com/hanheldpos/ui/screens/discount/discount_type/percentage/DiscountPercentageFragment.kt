package com.hanheldpos.ui.screens.discount.discount_type.percentage

import androidx.core.widget.doAfterTextChanged
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentDiscountPercentageBinding
import com.hanheldpos.model.discount.DiscApplyTo
import com.hanheldpos.model.discount.DiscountTypeEnum
import com.hanheldpos.model.discount.DiscountTypeFor
import com.hanheldpos.model.discount.DiscountUser
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.discount.DiscountFragment


class DiscountPercentageFragment(
    private val applyToType: DiscApplyTo,
    private val listener: DiscountFragment.DiscountTypeListener
) :
    BaseFragment<FragmentDiscountPercentageBinding, DiscountPercentageVM>(), DiscountPercentageUV {
    override fun layoutRes(): Int = R.layout.fragment_discount_percentage

    override fun viewModelClass(): Class<DiscountPercentageVM> {
        return DiscountPercentageVM::class.java
    }

    override fun initViewModel(viewModel: DiscountPercentageVM) {
        viewModel.run {
            init(this@DiscountPercentageFragment)
            initLifeCycle(this@DiscountPercentageFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {

        binding.percentDiscount.let { input ->
            var isEditing = false
            input.doAfterTextChanged {
                if (isEditing) return@doAfterTextChanged
                isEditing = true
                when {
                    it.toString().isEmpty() -> input.setText(0.toString())
                    it.toString().toInt() > 100 -> input.setText(100.toString())
                    else -> input.setText(it.toString().toInt().toString())
                }
                input.setSelection(input.length())
                isEditing = false
            }
        }
    }

    override fun initData() {
    }

    override fun initAction() {
        viewModel.percent.observe(this) {
            listener.validDiscount(validDiscount())
        }
        viewModel.title.observe(this) {
            listener.validDiscount(validDiscount())
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().supportFragmentManager.setFragmentResultListener(
            "saveDiscount",
            this
        ) { _, bundle ->
            if (bundle.getSerializable("DiscountTypeFor") == DiscountTypeFor.PERCENTAGE && validChooseDiscount()) {
                listener.discountUserChoose(
                    DiscountUser(
                        DiscountName = viewModel.title.value!!,
                        DiscountValue = viewModel.percentValue,
                        DiscountType = DiscountTypeEnum.PERCENT.value
                    )
                )
            }
        }
        listener.validDiscount(validDiscount())
    }

    private fun validChooseDiscount(): Boolean {
        return (viewModel.percentValue > 0.0 && !viewModel.title.value.isNullOrEmpty())
    }

    private fun validDiscount(): Boolean {
        return when (applyToType) {
            DiscApplyTo.ITEM -> {
                (viewModel.percentValue == 0.0 && viewModel.title.value.isNullOrEmpty()) || validChooseDiscount()
            }
            DiscApplyTo.ORDER -> {
                validChooseDiscount()
            }
            else -> false
        }
    }
}