package com.hanheldpos.ui.screens.cart

import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.data.api.pojo.order.settings.DiningOptionItem
import com.hanheldpos.data.api.pojo.order.settings.ListReasonsItem
import com.hanheldpos.databinding.FragmentCartBinding
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.cart.Combo
import com.hanheldpos.model.cart.DiscountCart
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.cart.payment.PaymentOrder
import com.hanheldpos.model.combo.ItemActionType
import com.hanheldpos.model.discount.DiscountUser
import com.hanheldpos.model.product.BaseProductInCart
import com.hanheldpos.model.product.ProductType
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cart.adapter.CartDiningOptionAdapter
import com.hanheldpos.ui.screens.cart.adapter.CartDiscountAdapter
import com.hanheldpos.ui.screens.cart.adapter.CartProductAdapter
import com.hanheldpos.ui.screens.cart.customer.AddCustomerFragment
import com.hanheldpos.ui.screens.cart.payment.PaymentFragment
import com.hanheldpos.ui.screens.combo.ComboFragment
import com.hanheldpos.ui.screens.discount.DiscountFragment
import com.hanheldpos.ui.screens.home.order.OrderFragment
import com.hanheldpos.ui.screens.product.ProductDetailFragment


class CartFragment(
) : BaseFragment<FragmentCartBinding, CartVM>(), CartUV {
    override fun layoutRes() = R.layout.fragment_cart;

    private lateinit var cartDiningOptionAdapter: CartDiningOptionAdapter;
    private lateinit var cartProductAdapter: CartProductAdapter;
    private lateinit var cartDiscountAdapter: CartDiscountAdapter;
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
            onProductClickListener = object : BaseItemClickListener<BaseProductInCart> {
                override fun onItemClick(adapterPosition: Int, item: BaseProductInCart) {
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

        //region setup discount recycle view
        cartDiscountAdapter = CartDiscountAdapter(listener = object : BaseItemClickListener<DiscountCart>{
            override fun onItemClick(adapterPosition: Int, item: DiscountCart) {
                    cartDataVM.deleteDiscount(item);
            }
        })

        binding.discountRecycleView.apply {
            adapter = cartDiscountAdapter
        }

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
        cartProductAdapter.submitList(cartDataVM.cartModelLD.value?.productsList);
        //endregion


    }

    override fun initAction() {
        cartDataVM.cartModelLD.observe(this,{
            val list =viewModel.processDataDiscount(it);
            binding.isShowDiscount = list.isNotEmpty();
            cartDiscountAdapter.submitList(list);
            cartDiscountAdapter.notifyDataSetChanged();
        })
    }

    override fun getBack() {
        onFragmentBackPressed()
    }

    override fun deleteCart() {
        cartDataVM.deleteCart(
            getString(R.string.confirmation),
            getString(R.string.delete_cart_warning),
            getString(R.string.delete),
            getString(R.string.alert_btn_negative),
            this::onFragmentBackPressed
        )
    }

    override fun onOpenDiscount() {
        navigator
            .goToWithCustomAnimation(DiscountFragment(listener = object :
                DiscountFragment.DiscountCallback {
                override fun onDiscountUserChoose(discount: DiscountUser) {
                    cartDataVM.addDiscountUser(discount);
                }

                override fun onCompReasonChoose(reason: ListReasonsItem) {
                    cartDataVM.addCompReason(reason);
                }

                override fun onCompOrderRemove() {
                    cartDataVM.removeCompOrder();
                }
            }));
    }

    override fun openSelectPayment(payable: Double) {
        navigator.goToWithCustomAnimation(PaymentFragment(payable,listener = object : PaymentFragment.PaymentCallback {
            override fun onPaymentComplete(paymentOrder: PaymentOrder) {
                cartDataVM.addPaymentOrder(paymentOrder)
            }
        }));
    }

    override fun onOpenAddCustomer() {
        navigator.goToWithCustomAnimation(AddCustomerFragment(listener = object :
            AddCustomerFragment.CustomerEvent {
            override fun onSelectedCustomer(item: CustomerResp) {
                cartDataVM.addCustomerToCart(item);
            }
        }));
    }

    override fun onBillCart() {
        cartDataVM.billCart();
    }

    fun onEditItemIntCart(position: Int, item: BaseProductInCart) {

        val callbackEdit = object : OrderFragment.OrderMenuListener {
            override fun onCartAdded(
                item: BaseProductInCart,
                action: ItemActionType
            ) {
                onUpdateItemInCart(position, item);
            }
        }

        when (item.productType) {
            ProductType.REGULAR -> {
                navigator.goToWithCustomAnimation(
                    ProductDetailFragment(
                        item = (item as Regular).clone(),
                        action = ItemActionType.Modify,
                        quantityCanChoose = 100,
                        listener = callbackEdit
                    )
                )
            }
            ProductType.BUNDLE -> {
                navigator.goToWithCustomAnimation(
                    ComboFragment(
                        item = (item as Combo).clone(),
                        action = ItemActionType.Modify,
                        quantityCanChoose = 100,
                        listener = callbackEdit
                    )
                );
            }
        }
    }

    fun onUpdateItemInCart(position: Int, item: BaseProductInCart) {
        cartDataVM.updateItemInCart(position, item);
        cartProductAdapter.notifyDataSetChanged();
    }

    companion object {
        fun getInstance(
        ): CartFragment {
            return CartFragment(

            ).apply {

            };
        }
    }
}