package com.hanheldpos.ui.screens.cart

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.data.api.pojo.order.settings.DiningOption
import com.hanheldpos.data.api.pojo.order.settings.Reason
import com.hanheldpos.databinding.FragmentCartBinding
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.model.cart.Combo
import com.hanheldpos.model.cart.DiscountCart
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.cart.fee.FeeTip
import com.hanheldpos.model.cart.payment.PaymentOrder
import com.hanheldpos.model.combo.ItemActionType
import com.hanheldpos.model.discount.DiscountUser
import com.hanheldpos.model.product.ProductType
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cart.adapter.CartDiningOptionAdapter
import com.hanheldpos.ui.screens.cart.adapter.CartDiscountAdapter
import com.hanheldpos.ui.screens.cart.adapter.CartProductAdapter
import com.hanheldpos.ui.screens.cart.adapter.CartTipAdapter
import com.hanheldpos.ui.screens.cart.customer.add_customer.AddCustomerFragment
import com.hanheldpos.ui.screens.cart.customer.detail_customer.CustomerDetailFragment
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
    private lateinit var cartTipAdapter: CartTipAdapter;
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

    }

    override fun initView() {

        //region setup dining option recyclerview
        cartDiningOptionAdapter =
            CartDiningOptionAdapter(
                onItemClickListener = object : BaseItemClickListener<DiningOption> {
                    override fun onItemClick(adapterPosition: Int, item: DiningOption) {
                        CurCartData.diningOptionChange(item)
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
                    CurCartData.deleteDiscountCart(discount = discount, productInCart = item);
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
                CurCartData.deleteDiscountCart(item,null);
            }
        })

        binding.discountRecycleView.apply {
            adapter = cartDiscountAdapter
        }

        //endregion

        //region setup cart tip adapter
        cartTipAdapter = CartTipAdapter(
            onItemClickListener = object : BaseItemClickListener<FeeTip> {
                override fun onItemClick(adapterPosition: Int, item: FeeTip) {

                }
            },
        )
        binding.tipRecyclerView.apply {
            binding.tipRecyclerView.adapter = cartTipAdapter;
        };
        //endregion

        binding.btnBill.setOnClickListener {
            onBillCart();
        }
    }

    override fun initData() {
        //region init dining option data
        val diningOptions: MutableList<DiningOption> =
            (DataHelper.orderSettingLocalStorage?.ListDiningOptions as List<DiningOption>).toMutableList();
        cartDiningOptionAdapter.submitList(diningOptions);

        if (CurCartData.diningOptionLD.value != null) {
            diningOptions.forEachIndexed { index, diningOptionItem ->
                if (diningOptionItem.Id == CurCartData.diningOptionLD.value?.Id){
                    cartDiningOptionAdapter.setSelectedIndex(index);
                    return@forEachIndexed
                }
            }
        }
        //endregion

        //region init tip option
        val tipOptions: MutableList<FeeTip> = mutableListOf<FeeTip>(
            FeeTip("0", 0.0),
            FeeTip("5K", 5000.0),
            FeeTip("10K", 10000.0),
            FeeTip("15K", 15000.0),
            FeeTip("Other", -1.0),
        )
        cartTipAdapter.setSelectedIndex(0)
        cartTipAdapter.submitList(tipOptions)
        //endregion

        //init product data
        cartProductAdapter.submitList(CurCartData.cartModelLD.value?.productsList);
        //endregion


    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initAction() {
        CurCartData.cartModelLD.observe(this) {
            it?:return@observe
            val list = viewModel.processDataDiscount(it)
            binding.isShowDiscount = list.isNotEmpty()
            cartDiscountAdapter.submitList(list)
            cartDiscountAdapter.notifyDataSetChanged()
            cartProductAdapter.notifyDataSetChanged()
        }
    }

    override fun getBack() {
        onFragmentBackPressed()
    }

    override fun deleteCart() {
        CurCartData.deleteCart(
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
                    CurCartData.addDiscountUser(discount);
                }

                override fun onCompReasonChoose(reason: Reason) {
                    CurCartData.addCompReason(reason);
                }

                override fun onCompRemove() {
                    CurCartData.removeCompReason();
                }
            }));
    }

    override fun openSelectPayment(alreadyBill : Boolean,payable: Double) {
        navigator.goToWithCustomAnimation(PaymentFragment(alreadyBill,payable,listener = object : PaymentFragment.PaymentCallback {
            override fun onPaymentComplete(paymentOrder: PaymentOrder) {
                CurCartData.addPaymentOrder(paymentOrder)
            }

            override fun onPayment(isSuccess: Boolean) {
                this@CartFragment.onPayment(isSuccess)
            }
        }))
    }

    override fun onOpenAddCustomer() {
        navigator.goToWithCustomAnimation(AddCustomerFragment(listener = object :
            AddCustomerFragment.CustomerEvent {
            override fun onSelectedCustomer(item: CustomerResp) {
                CurCartData.addCustomerToCart(item);
            }
        }))
    }

    override fun onBillSuccess() {
        listener.onBillSuccess()
        val totalNeedPay = CurCartData.cartModelLD.value!!.getTotalPrice()
        openSelectPayment(true,totalNeedPay)
    }

    override fun onPayment(isSuccess : Boolean) {
        onFragmentBackPressed()
        if (isSuccess){
            CurCartData.removeCart()
            listener.onPaymentSuccess()
        }

    }

    override fun onShowCustomerDetail() {
        navigator.goToWithCustomAnimation(CustomerDetailFragment(CurCartData.cartModelLD.value?.customer));
    }

    private fun onBillCart() {
        viewModel.billCart(requireContext(),CurCartData.cartModelLD.value!!)
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
            else -> {}
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun onUpdateItemInCart(position: Int, item: BaseProductInCart) {
        CurCartData.updateItemInCart(position, item);
        cartProductAdapter.notifyDataSetChanged();
    }

    interface CartCallBack {
        fun onCartDelete()
        fun onBillSuccess()
        fun onPaymentSuccess()
    }
}