package com.hanheldpos.ui.screens.cart

import android.annotation.SuppressLint
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
import com.hanheldpos.model.cart.order.OrderItemType
import com.hanheldpos.model.home.order.combo.ItemActionType
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cart.adapter.CartDiningOptionAdapter
import com.hanheldpos.ui.screens.cart.adapter.CartProductAdapter
import com.hanheldpos.ui.screens.cart.payment.PaymentFragment
import com.hanheldpos.ui.screens.discount.DiscountFragment
import com.hanheldpos.ui.screens.home.order.combo.ComboFragment
import com.hanheldpos.ui.screens.product.ProductDetailFragment


class CartFragment(
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
        binding.cartDataVM = cartDataVM;


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
                    onEditItemIntCart(adapterPosition, item);
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
        cartProductAdapter.submitList(products = cartDataVM.cartModelLD.value?.listOrderItem);
        //endregion
    }

    override fun initAction() {

    }

    override fun getBack() {
        onFragmentBackPressed()
    }

    override fun deleteCart() {
        cartDataVM.deleteCart(
            getString(R.string.confirmation),
            getString(R.string.delete_cart_warning),
            getString(R.string.alert_btn_negative),
            this::onFragmentBackPressed
        )
    }

    override fun onOpenDiscount() {
        navigator
            .goToWithCustomAnimation(DiscountFragment(listener = object :
                DiscountFragment.ItemCallback {
                override fun onItemSelected() {
                    TODO("Not yet implemented")
                }
            }));
    }

    override fun openSelectPayment() {
        navigator.goToWithCustomAnimation(PaymentFragment());
    }

    fun onEditItemIntCart(position: Int, item: OrderItemModel) {
        when (item.type) {
            OrderItemType.Product -> {
                navigator.goToWithCustomAnimation(
                    ProductDetailFragment.getInstance(
                        item = item.clone(),
                        action = ItemActionType.Modify,
                        quantityCanChoose = 100,
                        listener = object : ProductDetailFragment.ProductDetailListener {
                            override fun onCartAdded(item: OrderItemModel, action: ItemActionType) {
                                onUpdateItemInCart(position, item);
                            }
                        }
                    )
                )
            }
            OrderItemType.Combo -> {
                navigator.goToWithCustomAnimation(
                    ComboFragment.getInstance(
                        item = item.clone(),
                        action = ItemActionType.Modify,
                        quantityCanChoose = 100,
                        listener = object : ComboFragment.ComboListener {
                            override fun onCartAdded(item: OrderItemModel, action: ItemActionType) {
                                onUpdateItemInCart(position, item);
                            }
                        }
                    )
                );
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun onUpdateItemInCart(position: Int, item: OrderItemModel) {
        cartDataVM.updateItemInCart(position, item);
        cartProductAdapter.notifyDataSetChanged();
    }

    companion object {
        fun getIntance(
        ): CartFragment {
            return CartFragment(

            ).apply {

            };
        }
    }
}