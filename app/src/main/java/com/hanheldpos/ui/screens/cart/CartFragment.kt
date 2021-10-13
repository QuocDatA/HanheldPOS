package com.hanheldpos.ui.screens.cart

import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.order.settings.DiningOptionItem
import com.hanheldpos.databinding.FragmentCartBinding
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.cart.order.OrderItemModel
import com.hanheldpos.model.home.order.menu.OrderMenuItemModel
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cart.adapter.CartDiningOptionAdapter
import com.hanheldpos.ui.screens.cart.adapter.CartProductAdapter


class CartFragment(
    private val listener: CartListener
) : BaseFragment<FragmentCartBinding, CartVM>(), CartUV {
    override fun layoutRes() = R.layout.fragment_cart;

    private lateinit var cartDiningOptionAdapter: CartDiningOptionAdapter;
    private lateinit var cartProductAdapter: CartProductAdapter;
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

        //region setup dining option recyclerview
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
        //endregion


        //region setup product recycler view
        cartProductAdapter = CartProductAdapter(
            onProductClickListener = object : BaseItemClickListener<OrderItemModel> {
                override fun onItemClick(adapterPosition: Int, item: OrderItemModel) {
                    TODO("Not yet implemented")
                }
            },
        );
        binding.productRecyclerView.apply {
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    LinearLayoutManager.VERTICAL
                ).apply {
                    setDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.divider_vertical
                        )!!
                    )
                }
            )
            adapter = cartProductAdapter;
        };
        //endregion
    }

    override fun initData() {
        //region init dining option data
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
        //endregion

        //init product data
        val products: MutableList<OrderItemModel>? =
            cartDataVM.cartModelLD.value?.listOrderItem?.toMutableList();

        cartProductAdapter.submitList(products = products);
        //endregion
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