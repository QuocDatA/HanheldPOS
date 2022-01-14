package com.hanheldpos.ui.screens.cart

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.data.api.pojo.order.settings.DiningOptionItem
import com.hanheldpos.data.api.pojo.order.settings.Reason
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
import com.hanheldpos.ui.screens.home.ScreenViewModel
import com.hanheldpos.ui.screens.home.order.OrderFragment
import com.hanheldpos.ui.screens.product.ProductDetailFragment


class CartFragment( private val listener : CartCallBack) : BaseFragment<FragmentCartBinding, CartVM>(), CartUV {
    override fun layoutRes() = R.layout.fragment_cart;

    private lateinit var cartDiningOptionAdapter: CartDiningOptionAdapter;
    private lateinit var cartProductAdapter: CartProductAdapter;
    private lateinit var cartDiscountAdapter: CartDiscountAdapter;
    private val cartDataVM by activityViewModels<CartDataVM>();
    private val screenViewModel by activityViewModels<ScreenViewModel>();


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
            listener = object : CartProductAdapter.CartProductListener {
                override fun onItemClick(adapterPosition: Int, item: BaseProductInCart) {
                    onEditItemInCart(adapterPosition, item);
                }

                override fun onDiscountDelete(adapterPosition: Int, discount : DiscountCart , item: BaseProductInCart) {
                    cartDataVM.deleteDiscountCart(discount = discount, productInCart = item);
                }


            }
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
                    cartDataVM.deleteDiscountCart(item,null);
            }
        })

        binding.discountRecycleView.apply {
            adapter = cartDiscountAdapter
        }

        //endregion

        binding.btnBill.setOnClickListener {
            onBillCart();
        }
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

    @SuppressLint("NotifyDataSetChanged")
    override fun initAction() {
        cartDataVM.cartModelLD.observe(this,{
            val list = viewModel.processDataDiscount(it);
            binding.isShowDiscount = list.isNotEmpty();
            cartDiscountAdapter.submitList(list);
            cartDiscountAdapter.notifyDataSetChanged();
            cartProductAdapter.notifyDataSetChanged();
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
            callback = object : () -> Unit {
                override fun invoke() {
                    onFragmentBackPressed();
                    listener.onCartDelete();
                }
            }
        )
    }

    override fun onOpenDiscount() {
        navigator
            .goToWithCustomAnimation(DiscountFragment(listener = object :
                DiscountFragment.DiscountCallback {
                override fun onDiscountUserChoose(discount: DiscountUser) {
                    cartDataVM.addDiscountUser(discount);
                }

                override fun onCompReasonChoose(reason: Reason) {
                    cartDataVM.addCompReason(reason);
                }

                override fun onCompRemove() {
                    cartDataVM.removeCompReason();
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

    override fun onBillSuccess() {
        getBack();
        cartDataVM.removeCart();
        listener.onBillSuccess();
    }

    private fun onBillCart() {
        viewModel.billCart(cartDataVM.cartModelLD.value!!);
    }

    fun onEditItemInCart(position: Int, item: BaseProductInCart) {

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
                        regular = (item as Regular).clone(),
                        action = ItemActionType.Modify,
                        quantityCanChoose = 100,
                        listener = callbackEdit
                    )
                )
            }
            ProductType.BUNDLE -> {
                navigator.goToWithCustomAnimation(
                    ComboFragment(
                        combo = (item as Combo).clone(),
                        action = ItemActionType.Modify,
                        quantityCanChoose = 100,
                        listener = callbackEdit
                    )
                );
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun onUpdateItemInCart(position: Int, item: BaseProductInCart) {
        cartDataVM.updateItemInCart(position, item);
        cartProductAdapter.notifyDataSetChanged();
    }

    interface CartCallBack {
        fun onCartDelete();
        fun onBillSuccess();
    }
}