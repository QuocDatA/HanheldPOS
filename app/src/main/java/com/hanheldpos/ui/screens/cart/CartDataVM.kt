package com.hanheldpos.ui.screens.cart

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hanheldpos.PosApp
import com.hanheldpos.data.api.pojo.customer.CustomerResp
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
import com.hanheldpos.model.payment.PaymentOrder
import com.hanheldpos.model.discount.DiscountUser
import com.hanheldpos.model.home.table.TableSummary
import com.hanheldpos.ui.base.dialog.AppAlertDialog
import com.hanheldpos.ui.base.viewmodel.BaseViewModel
import kotlin.coroutines.coroutineContext

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
    }

    fun addCustomerToCart(customer: CustomerResp) {
        this.cartModelLD.value!!.customer = customer
        this.cartModelLD.notifyValueChange()
    }

    fun removeCustomerFromCart() {
        this.cartModelLD.value!!.customer = null
        this.cartModelLD.notifyValueChange()
    }

    fun addItemToCart(item: BaseProductInCart) {
        this.cartModelLD.value?.run {
            if (item is Regular) {
                addRegular(item)
            } else if (item is Combo) {
                addBundle(item)
            }
        }
        this.cartModelLD.notifyValueChange()
    }

    private fun updatePriceList(menuLocation_id: String?) {
        this.cartModelLD.value?.updatePriceList(menuLocation_id ?: UserHelper.getLocationGuid())
        this.cartModelLD.notifyValueChange()
    }

    fun updateItemInCart(index: Int, item: BaseProductInCart) {
        if (item.quantity!! > 0) {
            cartModelLD.value!!.productsList[index] = item
        } else {
            cartModelLD.value!!.productsList.removeAt(index)
        }
        cartModelLD.notifyValueChange()
    }

    fun addPaymentOrder(payments: List<PaymentOrder>) {
        cartModelLD.value!!.addPayment(payments)
        cartModelLD.notifyValueChange()
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
                        this@CartDataVM.cartModelLD.notifyValueChange()
                        callback()
                    }
                }
            )
    }

    fun removeCart() {
        this.cartModelLD.postValue(null)
        this.currentTableFocus.postValue(null)
    }

    fun deleteDiscountCart(discount: DiscountCart, productInCart: BaseProductInCart?) {
        if (productInCart != null) {
            discount.disOriginal.let {
                when (it.javaClass) {
                    Reason::class.java -> {
                        productInCart.clearCompReason()
                    }
                    DiscountUser::class.java -> {
                        productInCart.clearAllDiscountCoupon()
                    }
                    DiscountResp::class.java -> {
                        productInCart.clearAllDiscountCoupon()
                    }
                    else -> {}
                }
            }
        } else
            discount.disOriginal.let {
                when (it.javaClass) {
                    Reason::class.java -> {
                        cartModelLD.value!!.compReason = null
                    }
                    DiscountUser::class.java -> {
                        cartModelLD.value!!.discountUserList.remove(it)
                    }
                    DiscountResp::class.java -> {
                        cartModelLD.value!!.discountServerList.remove(it)
                    }
                    else -> {}
                }
            }
        cartModelLD.notifyValueChange()
    }

    fun removeCompReason() {
        cartModelLD.value!!.compReason = null
        cartModelLD.notifyValueChange()
    }

    fun addCompReason(reason: Reason) {

        this.cartModelLD.value!!.addCompReason(reason)
        cartModelLD.notifyValueChange()
    }

    fun addDiscountUser(discount: DiscountUser) {
        this.cartModelLD.value!!.addDiscountUser(discount)
        cartModelLD.notifyValueChange()
    }

    fun addDiscountServer(discount: DiscountResp , applyTo: DiscApplyTo) {
        this.cartModelLD.value!!.addDiscountServer(discount,applyTo)
        cartModelLD.notifyValueChange()
    }

    fun clearAllDiscountCoupon() {
        this.cartModelLD.value!!.clearAllDiscounts()
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


}