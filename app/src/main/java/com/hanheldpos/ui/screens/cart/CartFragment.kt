package com.hanheldpos.ui.screens.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentCartBinding
import com.hanheldpos.ui.base.fragment.BaseFragment


class CartFragment(
    private val listener: CartListener
) : BaseFragment<FragmentCartBinding, CartVM>(), CartUV {
    override fun layoutRes() = R.layout.fragment_cart;

    override fun viewModelClass(): Class<CartVM> {
        return CartVM::class.java;
    }

    override fun initViewModel(viewModel: CartVM) {
        viewModel.run {
            init(this@CartFragment);
            binding.viewModel = this;
            initLifeCycle(this@CartFragment);
        }
    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun initAction() {

    }

    override fun getBack() {
        navigator.goOneBack();
    }

    override fun deleteCart() {
        listener.onDeleteCart();
        getBack();
    }

    interface CartListener {
        fun onDeleteCart()
    }

    companion object {
        fun getIntance(
            listener: CartListener
        ): CartFragment {
            return CartFragment(
                listener = listener
            ).apply {

            };
        }
    }
}