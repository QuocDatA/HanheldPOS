package com.hanheldpos.ui.screens.cart

import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.order.settings.DiningOptionItem
import com.hanheldpos.databinding.FragmentCartBinding
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.DataHelper
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cart.adapter.CartDiningOptionAdapter


class CartFragment(
    private val listener: CartListener
) : BaseFragment<FragmentCartBinding, CartVM>(), CartUV {
    override fun layoutRes() = R.layout.fragment_cart;

    private lateinit var cartDiningOptionAdapter: CartDiningOptionAdapter;
    private val cartDataVM by activityViewModels<CartDataVM>();

    override fun viewModelClass(): Class<CartVM> {
        return CartVM::class.java;
    }

    override fun initViewModel(viewModel: CartVM) {
        viewModel.run {
            init(this@CartFragment);
            binding.viewModel = this;
            initLifeCycle(this@CartFragment);
        }
        R.dimen._100ssp
    }

    override fun initView() {

        cartDiningOptionAdapter =
            CartDiningOptionAdapter(
                onItemClickListener = object : BaseItemClickListener<DiningOptionItem> {
                    override fun onItemClick(adapterPosition: Int, item: DiningOptionItem) {
                        cartDataVM.cartModelLD.value!!.diningOption = item;
                        cartDataVM.cartModelLD.notifyValueChange();
                    }

                },
            );
        binding.diningOptionRecyclerView.adapter = cartDiningOptionAdapter;

    }

    override fun initData() {
        val diningOptions: MutableList<DiningOptionItem> =
            (DataHelper.getDiningOptionList() as List<DiningOptionItem>).toMutableList();
        val currentDiningOption: DiningOptionItem? = cartDataVM.diningOptionLD.value;
        var selectedIndex = 0;

        if (currentDiningOption != null) {
            diningOptions.forEachIndexed { index, diningOptionItem ->
                if (diningOptionItem.id == currentDiningOption.id) selectedIndex = index
            }
        }
        cartDiningOptionAdapter.setSelectedIndex(selectedIndex);
        cartDiningOptionAdapter.submitList(diningOptions);

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