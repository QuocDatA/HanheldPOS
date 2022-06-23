package com.hanheldpos.ui.screens.cart

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.data.api.pojo.discount.DiscountCoupon
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.api.pojo.order.settings.DiningOption
import com.hanheldpos.data.api.pojo.order.settings.Reason
import com.hanheldpos.databinding.FragmentCartBinding
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.extension.setOnClickDebounce
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.OrderHelper
import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.model.cart.Combo
import com.hanheldpos.model.cart.DiscountCart
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.cart.fee.FeeTip
import com.hanheldpos.model.discount.DiscApplyTo
import com.hanheldpos.model.discount.DiscountUser
import com.hanheldpos.model.payment.PaymentOrder
import com.hanheldpos.model.product.ProductType
import com.hanheldpos.model.product.buy_x_get_y.BuyXGetY
import com.hanheldpos.model.product.combo.ItemActionType
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cart.adapter.CartDiningOptionAdapter
import com.hanheldpos.ui.screens.cart.adapter.CartDiscountAdapter
import com.hanheldpos.ui.screens.cart.adapter.CartProductAdapter
import com.hanheldpos.ui.screens.cart.adapter.CartTipAdapter
import com.hanheldpos.ui.screens.cart.customer.add_customer.AddCustomerFragment
import com.hanheldpos.ui.screens.cart.customer.detail_customer.CustomerDetailFragment
import com.hanheldpos.ui.screens.discount.DiscountFragment
import com.hanheldpos.ui.screens.discount.discount_type.discount_code.DiscountCodeFragment
import com.hanheldpos.ui.screens.home.order.OrderFragment
import com.hanheldpos.ui.screens.payment.PaymentFragment
import com.hanheldpos.ui.screens.payment.completed.PaymentCompletedFragment
import com.hanheldpos.ui.screens.product.buy_x_get_y.BuyXGetYFragment
import com.hanheldpos.ui.screens.product.combo.ComboFragment
import com.hanheldpos.ui.screens.product.regular.RegularDetailFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class CartFragment(
    private val listener: CartCallBack,
    private val discountCouponList: List<DiscountCoupon>? = null,
    private val discount: DiscountResp? = null
) :
    BaseFragment<FragmentCartBinding, CartVM>(), CartUV {

    private val cartDataVM by activityViewModels<CartDataVM>()

    override fun layoutRes() = R.layout.fragment_cart

    private lateinit var cartDiningOptionAdapter: CartDiningOptionAdapter
    private lateinit var cartProductAdapter: CartProductAdapter
    private lateinit var cartDiscountAdapter: CartDiscountAdapter
    private lateinit var cartTipAdapter: CartTipAdapter


    override fun viewModelClass(): Class<CartVM> {
        return CartVM::class.java
    }

    override fun initViewModel(viewModel: CartVM) {
        viewModel.run {
            init(this@CartFragment)
            binding.viewModel = this
            binding.cartDataVM = cartDataVM
        }

    }

    override fun initView() {

        //setup dining option recyclerview
        cartDiningOptionAdapter =
            CartDiningOptionAdapter(
                onItemClickListener = object : BaseItemClickListener<DiningOption> {
                    override fun onItemClick(adapterPosition: Int, item: DiningOption) {
                        cartDataVM.diningOptionChange(item)
                    }
                },
            )
        binding.diningOptionRecyclerView.adapter = cartDiningOptionAdapter


        // setup product recycler view
        cartProductAdapter = CartProductAdapter(
            listener = object : CartProductAdapter.CartProductListener {
                override fun onItemClick(adapterPosition: Int, item: BaseProductInCart) {
                    onEditItemInCart(adapterPosition, item)
                }

                override fun onDiscountDelete(
                    adapterPosition: Int,
                    item: BaseProductInCart
                ) {
                    cartDataVM.deleteDiscountCart(discount = null, productInCart = item)
                }

                override fun onCompDelete(adapterPosition: Int, item: BaseProductInCart) {
                    cartDataVM.removeCompReason(item)
                }


            }
        )
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
            adapter = cartProductAdapter
        }

        // setup discount recycle view
        cartDiscountAdapter =
            CartDiscountAdapter(listener = object : BaseItemClickListener<DiscountCart> {
                override fun onItemClick(adapterPosition: Int, item: DiscountCart) {
                    cartDataVM.deleteDiscountCart(item, null)
                }
            })

        binding.discountRecycleView.apply {
            adapter = cartDiscountAdapter
        }

        // setup cart tip adapter
        cartTipAdapter = CartTipAdapter(
            onItemClickListener = object : BaseItemClickListener<FeeTip> {
                override fun onItemClick(adapterPosition: Int, item: FeeTip) {

                }
            },
        )
        binding.tipRecyclerView.apply {
            binding.tipRecyclerView.adapter = cartTipAdapter
        }

        binding.btnBill.setOnClickDebounce {
            onBillCart()
        }

        // Listener note cart change
        binding.noteCart.doAfterTextChanged {
            cartDataVM.updateNote(it.toString())
        }
    }

    override fun initData() {
        //init dining option data
        val diningOptions: MutableList<DiningOption> =
            (DataHelper.orderSettingLocalStorage?.ListDiningOptions as List<DiningOption>).toMutableList()
        cartDiningOptionAdapter.submitList(diningOptions)

        if (cartDataVM.diningOptionLD.value != null) {
            diningOptions.forEachIndexed { index, diningOptionItem ->
                if (diningOptionItem.Id == cartDataVM.diningOptionLD.value?.Id) {
                    cartDiningOptionAdapter.setSelectedIndex(index)
                    return@forEachIndexed
                }
            }
        }

        //init tip option
        val tipOptions: MutableList<FeeTip> = mutableListOf(
            FeeTip("0", 0.0),
            FeeTip("5K", 5000.0),
            FeeTip("10K", 10000.0),
            FeeTip("15K", 15000.0),
            FeeTip("Other", -1.0),
        )
        cartTipAdapter.setSelectedIndex(0)
        cartTipAdapter.submitList(tipOptions)

        //product data
        cartProductAdapter.submitList(cartDataVM.cartModelLD.value?.productsList)

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initAction() {
        cartDataVM.cartModelLD.observe(this) {
            it ?: return@observe
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
        cartDataVM.deleteCart(
            getString(R.string.confirmation),
            getString(R.string.delete_cart_warning),
            getString(R.string.delete),
            getString(R.string.alert_btn_negative),
            callback = object : () -> Unit {
                override fun invoke() {
                    onFragmentBackPressed()
                    listener.onCartDelete()
                }
            }
        )
    }

    override fun onOpenDiscount() {
        navigator
            .goToWithCustomAnimation(DiscountFragment(listener = object :
                DiscountFragment.DiscountCallback {
                override fun onDiscountUserChoose(discount: DiscountUser) {
                    cartDataVM.addDiscountUser(discount)
                }

                override fun onDiscountServerChoose(discount: DiscountResp ,discApplyTo: DiscApplyTo) {
                    cartDataVM.addDiscountServer(discount,discApplyTo)
                }

                override fun onDiscountCodeChoose(discount: List<DiscountCoupon>?) {
                    cartDataVM.updateDiscountCouponCode(discount)
                }

                override fun onCompReasonChoose(reason: Reason) {
                    cartDataVM.addCompReason(reason)
                }

                override fun onCompRemove() {
                    cartDataVM.removeCompReason()
                }

                override fun clearAllDiscountCoupon() {
                    cartDataVM.clearAllDiscountCoupon()
                }

                override fun addDiscountBuyXGetYToCart(discount: DiscountResp, buyXGetY: BuyXGetY) {
                    cartDataVM.addBuyXGetY(discount, buyXGetY)
                }
            }))
    }

    override fun openSelectPayment(
        alreadyBill: Boolean,
        payable: Double,
        paymentList: List<PaymentOrder>?
    ) {
        navigator.goToWithCustomAnimation(
            PaymentFragment(
                alreadyBill,
                payable,
                paymentList,
                listener = object : PaymentFragment.PaymentCallback {

                    override fun onPaymentComplete(paymentOrderList: List<PaymentOrder>) {
                        cartDataVM.addPaymentOrder(paymentOrderList)
                    }

                    override fun onPaymentSelected() {
                        onBillCart(true)
                    }

                    override fun onPaymentState(isSuccess: Boolean) {
                        if (isSuccess) {
                            onBillCart()
                        } else
                            this@CartFragment.onFinishOrder(false)
                    }
                })
        )
    }

    override fun onOpenAddCustomer() {
        navigator.goToWithCustomAnimation(AddCustomerFragment(listener = object :
            AddCustomerFragment.CustomerEvent {
            override fun onSelectedCustomer(item: CustomerResp) {
                cartDataVM.addCustomerToCart(item)
            }
        }))
    }

    override fun onBillSuccess() {
        listener.onBillSuccess()
        if (!OrderHelper.isPaymentSuccess(cartDataVM.cartModelLD.value!!)) {
            val totalNeedPay = cartDataVM.cartModelLD.value!!.getTotalPrice()
            this.openSelectPayment(true, totalNeedPay, CurCartData.cartModel!!.paymentsList)
        } else {
            this.onFinishOrder(true)
        }

    }

    override fun onFinishOrder(isSuccess: Boolean) {
        navigator.goOneBack()
        if (isSuccess) {

            cartDataVM.removeCart()
            cartDataVM.cartModelLD.value?.let {
                navigator.goTo(
                    PaymentCompletedFragment(
                        it.customer != null,
                        it.paymentsList?.sumOf { payment ->
                            payment.Payable ?: 0.0
                        } ?: 0.0,
                        it.paymentsList?.sumOf { payment ->
                            payment.OverPay ?: 0.0
                        } ?: 0.0,
                        listener = object : PaymentCompletedFragment.PaymentCompletedCallBack {
                            override fun becomeAMember() {

                            }

                            override fun newSale() {
                            }
                        },
                    ),
                )
            }
            listener.onOrderSuccess()
        }

    }

    override fun onShowCustomerDetail() {
        navigator.goToWithCustomAnimation(CustomerDetailFragment(cartDataVM.cartModelLD.value?.customer))
    }

    private fun onBillCart(onPaymentSelected : Boolean = false) {
        viewModel.billCart(
            requireContext(),
            CurCartData.cartModel!!,
            onPaymentSelected,
            listener = object : CartVM.CartActionCallBack {
                override fun onTableChange() {
                    cartDataVM.currentTableFocus.notifyValueChange()
                }
            })
    }

    fun onEditItemInCart(position: Int, item: BaseProductInCart) {

        val callbackEdit = object : OrderFragment.OrderMenuListener {
            override fun onCartAdded(
                item: BaseProductInCart,
                action: ItemActionType
            ) {
                onUpdateItemInCart(position, item)
                onUpdateBuyXGetYInCart()
            }
        }

        val callbackEditBuyXGetY = object : DiscountCodeFragment.BuyXGetYCallBack {
            override fun onCartAdded(
                item: BaseProductInCart,
                action: ItemActionType
            ) {
                // remove discount of buy x get y entire order when remove buy x get y out of cart
                if (action == ItemActionType.Modify && item.quantity!! <= 0)
                    cartDataVM.removeDiscountById((item as BuyXGetY).disc?._id ?: "")

                onUpdateItemInCart(position, item)
            }

            override fun onDiscountBuyXGetYEntireOrder(discount: DiscountResp) {
                cartDataVM.addDiscountServer(discount, DiscApplyTo.ORDER)
            }
        }

        when (item.productType) {
            ProductType.REGULAR -> {
                navigator.goToWithCustomAnimation(
                    RegularDetailFragment(
                        regular = (item as Regular).clone(),
                        action = ItemActionType.Modify,
                        quantityCanChoose = 1000,
                        listener = callbackEdit
                    )
                )
            }
            ProductType.BUNDLE -> {
                navigator.goToWithCustomAnimation(
                    ComboFragment(
                        combo = (item as Combo).clone(),
                        action = ItemActionType.Modify,
                        quantityCanChoose = 1000,
                        listener = callbackEdit
                    )
                )
            }
            ProductType.BUYX_GETY_DISC -> {
                navigator.goTo(
                    BuyXGetYFragment(
                        buyXGetY = (item as BuyXGetY).clone(),
                        discount = item.clone().disc!!,
                        actionType = ItemActionType.Modify,
                        quantityCanChoose = 1,
                        listener = callbackEditBuyXGetY
                    )
                )
            }
            else -> {}
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun onUpdateItemInCart(position: Int, item: BaseProductInCart) {
        cartDataVM.updateItemInCart(position, item)
        cartProductAdapter.notifyDataSetChanged()
    }

    fun onUpdateBuyXGetYInCart() {
        cartDataVM.updateItemBuyXGetYInCart()
    }

    override fun onResume() {
        super.onResume()
        if (discount != null) {
            showLoading(true)
            CoroutineScope(Dispatchers.IO).launch {
                delay(1000)
                val buyXGetY = cartDataVM.initDefaultBuyXGetY(discount)
                launch(Dispatchers.Main) { showLoading(false) }
                if (buyXGetY.isCompleted())
                    cartDataVM.addBuyXGetY(discount, buyXGetY)
                else
                    launch(Dispatchers.Main) {
                        navigator.goTo(BuyXGetYFragment(
                                buyXGetY = cartDataVM.initDefaultBuyXGetY(discount),
                                discount = discount,
                                actionType = ItemActionType.Add,
                                quantityCanChoose = 1,
                                listener = object : DiscountCodeFragment.BuyXGetYCallBack {
                                    override fun onCartAdded(
                                        item: BaseProductInCart,
                                        action: ItemActionType
                                    ) {
                                        // remove discount of buy x get y entire order when remove buy x get y out of cart
                                        if (action == ItemActionType.Modify && item.quantity!! <= 0)
                                            cartDataVM.removeDiscountById(
                                                (item as BuyXGetY).disc?._id ?: ""
                                            )

                                        cartDataVM.addBuyXGetY(discount, buyXGetY)
                                    }

                                    override fun onDiscountBuyXGetYEntireOrder(discount: DiscountResp) {
                                        cartDataVM.addDiscountServer(discount, DiscApplyTo.ORDER)
                                    }
                                }
                            ),
                        )
                    }

            }
        } else if (!discountCouponList.isNullOrEmpty()) {
            cartDataVM.updateDiscountCouponCode(discountCouponList)
        }
    }

    interface CartCallBack {
        fun onCartDelete()
        fun onBillSuccess()
        fun onOrderSuccess()
    }
}