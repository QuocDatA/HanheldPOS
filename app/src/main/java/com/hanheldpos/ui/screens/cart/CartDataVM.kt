package com.hanheldpos.ui.screens.cart

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.data.api.pojo.discount.DiscountCoupon
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.api.pojo.floor.FloorTable
import com.hanheldpos.data.api.pojo.order.settings.DiningOption
import com.hanheldpos.data.api.pojo.order.settings.Reason
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.OrderHelper
import com.hanheldpos.model.UserHelper
import com.hanheldpos.model.cart.*
import com.hanheldpos.model.discount.DiscApplyTo
import com.hanheldpos.model.discount.DiscountUser
import com.hanheldpos.model.home.table.TableSummary
import com.hanheldpos.model.payment.PaymentOrder
import com.hanheldpos.ui.base.dialog.AppAlertDialog
import com.hanheldpos.ui.base.viewmodel.BaseViewModel

class CartDataVM : BaseViewModel() {


    val cartModelLD: MutableLiveData<CartModel> = MutableLiveData()
    val currentTableFocus: MutableLiveData<FloorTable> = MutableLiveData()

    val diningOptionLD: LiveData<DiningOption> = Transformations.map(cartModelLD) {
        return@map it?.diningOption
            ?: DataHelper.orderSettingLocalStorage?.ListDiningOptions?.firstOrNull()
    }
    val linePerTotalQuantity: LiveData<String> = Transformations.map(cartModelLD) {
        it ?: return@map null
        return@map "${it.productsList.sumOf { it1 -> it1.quantity ?: 0 }}/${it.productsList.size}"
    }

    val numberOfCustomer: LiveData<Int> = Transformations.map(cartModelLD) {
        return@map it?.table?.PeopleQuantity
    }

    fun initObserveData(owner: LifecycleOwner) {

        cartModelLD.observe(owner) {
            CurCartData.cartModel = it
        }

        currentTableFocus.observe(owner) {
            CurCartData.tableFocus = it
        }
    }

    fun initCart(numberCustomer: Int, table: FloorTable) {
        cartModelLD.value =
            CartModel(
                table = TableSummary(
                    _id = table._Id,
                    TableName = table.TableName,
                    PeopleQuantity = numberCustomer
                ),
                fees = OrderHelper.findFeeOrderList() ?: mutableListOf(),
                productsList = mutableListOf(),
                discountUserList = mutableListOf(),
                discountServerList = mutableListOf(),
                diningOption = DataHelper.orderSettingLocalStorage?.ListDiningOptions?.firstOrNull()!!,
            )

        currentTableFocus.value = table

        val diningOptionId =
            DataHelper.floorLocalStorage?.Floor?.firstOrNull { floorTable -> floorTable._Id == table.FloorGuid }?.DiningOptionId
        val diningOption = OrderHelper.getDiningOptionItem(diningOptionId)
        diningOptionChange(diningOption)

    }

    fun initCart(cart: CartModel, table: FloorTable) {
        cartModelLD.value = cart
        currentTableFocus.value = table
        notifyCartChange()
    }

    fun addCustomerToCart(customer: CustomerResp) {
        this.cartModelLD.value!!.customer = customer
        this.notifyCartChange()
    }

    fun removeCustomerFromCart() {
        this.cartModelLD.value!!.customer = null
        this.notifyCartChange()
    }

    fun addItemToCart(item: BaseProductInCart) {
        this.cartModelLD.value?.run {
            if (item is Regular) {
                addRegular(item)
            } else if (item is Combo) {
                addBundle(item)
            }
        }
        this.notifyCartChange()
    }

    private fun updatePriceList(menuLocation_id: String?) {
        this.cartModelLD.value?.updatePriceList(menuLocation_id ?: UserHelper.getLocationGuid())
        this.notifyCartChange()
    }

    fun updateItemInCart(index: Int, item: BaseProductInCart) {
        if (item.quantity!! > 0) {
            cartModelLD.value!!.productsList[index] = item
        } else {
            cartModelLD.value!!.productsList.removeAt(index)
        }
        notifyCartChange()
    }

    fun addPaymentOrder(payments: List<PaymentOrder>) {
        cartModelLD.value!!.addPayment(payments)
        notifyCartChange(false)
    }

    fun deleteCart(
        title: String,
        message: String,
        positiveText: String,
        negativeText: String,
        callback: () -> Unit
    ) {
        AppAlertDialog.get()
            .show(
                title,
                message,
                positiveText = positiveText,
                negativeText = negativeText,
                onClickListener = object : AppAlertDialog.AlertDialogOnClickListener {
                    override fun onPositiveClick() {
                        this@CartDataVM.cartModelLD.value!!.clearCart()
                        this@CartDataVM.notifyCartChange()
                        callback()
                    }
                }
            )
    }

    fun removeCart() {
        this.cartModelLD.postValue(null)
        this.currentTableFocus.postValue(null)
    }

    fun deleteDiscountCart(discount: DiscountCart?, productInCart: BaseProductInCart?) {
        if (productInCart != null) {
            productInCart.clearAllDiscountCoupon()
        } else
            discount?.disOriginal?.let {
                when (it.javaClass) {
                    DiscountUser::class.java -> {
                        cartModelLD.value!!.discountUserList.remove(it)
                    }
                    DiscountResp::class.java -> {
                        cartModelLD.value!!.discountServerList.remove(it)
                    }
                    Reason::class.java -> {
                        removeCompReason()
                    }
                    else -> {

                    }
                }
            }

        notifyCartChange()
    }

    fun removeCompReason(item: BaseProductInCart? = null) {
        if (item != null) {
            item.clearCompReason()
        } else {
            cartModelLD.value!!.compReason = null
        }

        notifyCartChange()
    }

    fun addCompReason(reason: Reason) {
        this.cartModelLD.value!!.addCompReason(reason)
        notifyCartChange()
    }

    fun addDiscountUser(discount: DiscountUser) {
        this.cartModelLD.value!!.addDiscountUser(discount)
        notifyCartChange()
    }

    fun addDiscountServer(discount: DiscountResp, applyTo: DiscApplyTo) {
        this.cartModelLD.value!!.addDiscountAutoServer(discount, applyTo)
        notifyCartChange()
    }

    fun clearAllDiscountCoupon() {
        this.cartModelLD.value!!.clearAllDiscounts()
        notifyCartChange()
    }

    fun notifyCartChange(isRemoveCoupon: Boolean = true) {
        cartModelLD.value?.updateDiscount(isRemoveCoupon)
        cartModelLD.notifyValueChange()
    }


    fun diningOptionChange(diningOption: DiningOption?) {
        diningOption?.let {
            cartModelLD.value?.diningOption = diningOption
        }
        updatePriceList(
            diningOption?.SubDiningOption?.firstOrNull()?.LocationGuid
        )
    }

    fun updateDiscountCouponCode(discountCouponList: List<DiscountCoupon>?) {
        // Find and append discounts for product list.
        discountCouponList?.filter { disc -> disc.DiscountApplyTo == DiscApplyTo.ITEM.value }
            ?.forEach { disc ->
                cartModelLD.value?.productsList?.forEachIndexed { index, baseProduct ->
                    if (disc.OrderDetailId == index + 1)
                        this.cartModelLD.value?.addDiscountCouponServer(
                            disc.toDiscount(),
                            DiscApplyTo.ITEM,
                            baseProduct
                        )
                }
            }
        // Find and append discounts for order.
        discountCouponList?.filter { disc -> disc.DiscountApplyTo == DiscApplyTo.ORDER.value }
            ?.forEach { disc ->
                this.cartModelLD.value?.addDiscountCouponServer(
                    disc.toDiscount(),
                    DiscApplyTo.ORDER
                )
            }
        notifyCartChange(false)
    }


    fun updateNote(note: String?) {
        this.cartModelLD.value?.note = note
        notifyCartChange(false)
    }
}