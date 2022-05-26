package com.hanheldpos.ui.screens.discount

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.discount.DiscountCoupon
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.api.pojo.order.settings.Reason
import com.hanheldpos.databinding.FragmentDiscountBinding
import com.hanheldpos.extension.setOnClickDebounce
import com.hanheldpos.model.buy_x_get_y.BuyXGetY
import com.hanheldpos.model.discount.DiscApplyTo
import com.hanheldpos.model.discount.DiscountTypeFor
import com.hanheldpos.model.discount.DiscountUser
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cart.CartDataVM
import com.hanheldpos.ui.screens.discount.discount_type.DiscountTypeOrderFragment


class DiscountFragment(private val listener: DiscountCallback) :
    BaseFragment<FragmentDiscountBinding, DiscountVM>(), DiscountUV {

    private val cartDataVM by activityViewModels<CartDataVM>()

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
        viewModel.typeDiscountSelect.observe(this) {


        }
    }

    override fun initData() {


        childFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            DiscountTypeOrderFragment(
                applyToType = DiscApplyTo.ORDER,
                cart = cartDataVM.cartModelLD.value!!,
                listener = object : DiscountTypeListener {
                    override fun discountUserChoose(discount: DiscountUser) {
                        listener.onDiscountUserChoose(discount);
                        onFragmentBackPressed()
                    }

                    override fun discountServerChoose(discount: DiscountResp, discApplyTo: DiscApplyTo) {
                        listener.onDiscountServerChoose(discount,discApplyTo)
                        onFragmentBackPressed()
                    }

                    override fun discountCodeChoose(discount: List<DiscountCoupon>?) {
                        listener.onDiscountCodeChoose(discount)
                        onFragmentBackPressed()
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

                    override fun addDiscountBuyXGetY(discount: DiscountResp, buyXGetY: BuyXGetY) {
                        listener.addDiscountBuyXGetYToCart(discount, buyXGetY)
                        backPress()
                    }

                    override fun clearAllDiscountCoupon() {
                        listener.clearAllDiscountCoupon()
                    }

                }
            )
        ).commit();


    }

    override fun initAction() {
        binding.btnSave.setOnClickDebounce {
            requireActivity().supportFragmentManager.setFragmentResult(
                "saveDiscount",
                Bundle().apply { putSerializable("DiscountTypeFor", viewModel.typeDiscountSelect.value) });
        }
    }

    interface DiscountCallback {
        fun onDiscountUserChoose(discount: DiscountUser);
        fun onDiscountServerChoose(discount : DiscountResp,discApplyTo : DiscApplyTo)
        fun onDiscountCodeChoose(discount: List<DiscountCoupon>?)
        fun onCompReasonChoose(reason: Reason);
        fun onCompRemove();
        fun clearAllDiscountCoupon()
        fun addDiscountBuyXGetYToCart(discount: DiscountResp, buyXGetY: BuyXGetY)
    }

    interface DiscountTypeListener {
        fun discountUserChoose(discount: DiscountUser): Unit {}
        fun discountServerChoose(discount : DiscountResp,discApplyTo: DiscApplyTo) : Unit {}
        fun discountCodeChoose(discount: List<DiscountCoupon>?): Unit{}
        fun compReasonChoose(item: Reason): Unit {}
        fun compRemoveAll(): Unit {}
        fun clearAllDiscountCoupon() : Unit {}
        fun discountFocus(type : DiscountTypeFor) : Unit{}
        fun validDiscount(isValid : Boolean)
        fun addDiscountBuyXGetY(discount: DiscountResp, buyXGetY: BuyXGetY): Unit{}
    }

    override fun backPress() {
        navigator.goOneBack();
    }
}