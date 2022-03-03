package com.hanheldpos.ui.screens.discount

import android.os.Bundle
import android.view.View
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.order.settings.Reason
import com.hanheldpos.databinding.FragmentDiscountBinding
import com.hanheldpos.model.discount.DiscountApplyToType
import com.hanheldpos.model.discount.DiscountTypeFor
import com.hanheldpos.model.discount.DiscountUser
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cart.CurCartData
import com.hanheldpos.ui.screens.discount.discount_type.DiscountTypeFragment


class DiscountFragment(private val listener: DiscountCallback) :
    BaseFragment<FragmentDiscountBinding, DiscountVM>(), DiscountUV {

    

    override fun layoutRes(): Int = R.layout.fragment_discount

    override fun viewModelClass(): Class<DiscountVM> {
        return DiscountVM::class.java;
    }

    override fun initViewModel(viewModel: DiscountVM) {
        viewModel.run {
            init(this@DiscountFragment);
            binding.viewModel = this
        }

    }

    override fun initView() {
//        viewModel.typeDiscountSelect.observe(this) {
//            if (it in mutableListOf(DiscountTypeFor.DISCOUNT_CODE,DiscountTypeFor.AUTOMATIC)){
//                binding.btnSave.visibility = View.GONE
//            }else {
//                binding.btnSave.visibility = View.VISIBLE
//            }
//
//        }
    }

    override fun initData() {


        childFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            DiscountTypeFragment(
                applyToType = DiscountApplyToType.ORDER_DISCOUNT_APPLY_TO,
                cart = CurCartData.cartModelLD.value!!,
                listener = object : DiscountTypeFragment.DiscountTypeListener {
                    override fun discountUserChoose(discount: DiscountUser) {
                        listener.onDiscountUserChoose(discount);
                        backPress();
                    }

                    override fun compReasonChoose(item: Reason) {
                        listener.onCompReasonChoose(item);
                        backPress();
                    }

                    override fun compRemoveAll() {
                        listener.onCompRemove();
                        backPress();
                    }

                    override fun discountFocus(type: DiscountTypeFor) {
                        viewModel.typeDiscountSelect.postValue(type);
                    }

                    override fun validDiscount(isValid: Boolean) {
                        binding.btnSave.isEnabled = isValid;
                    }
                }
            )
        ).commit();


    }

    override fun initAction() {
        binding.btnSave.setOnClickListener {
            requireActivity().supportFragmentManager.setFragmentResult(
                "saveDiscount",
                Bundle().apply { putSerializable("DiscountTypeFor", viewModel.typeDiscountSelect.value) });
        }
    }

    interface DiscountCallback {
        fun onDiscountUserChoose(discount: DiscountUser);
        fun onCompReasonChoose(reason: Reason);
        fun onCompRemove();
    }

    override fun backPress() {
        navigator.goOneBack();
    }
}