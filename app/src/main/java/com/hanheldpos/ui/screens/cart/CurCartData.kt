package com.hanheldpos.ui.screens.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.data.api.pojo.floor.FloorTable
import com.hanheldpos.data.api.pojo.order.settings.DiningOption
import com.hanheldpos.data.api.pojo.order.settings.Reason
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.OrderHelper
import com.hanheldpos.model.cart.*
import com.hanheldpos.model.cart.payment.PaymentOrder
import com.hanheldpos.model.discount.DiscountUser
import com.hanheldpos.model.home.table.TableSummary
import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.ui.base.dialog.AppAlertDialog

object CurCartData  {

    val cartModelLD: MutableLiveData<CartModel> = MutableLiveData();

    val diningOptionLD: LiveData<DiningOption> = Transformations.map(cartModelLD) {
        return@map it?.diningOption
            ?: DataHelper.orderSettingLocalStorage?.ListDiningOptions?.firstOrNull()
    }
    val linePerTotalQuantity: LiveData<String> = Transformations.map(cartModelLD) {
        it ?: return@map null;
        return@map "${it.productsList.sumOf { it1 -> it1.quantity ?: 0 }}/${it.productsList.size}"
    }

    val numberOfCustomer: LiveData<Int> = Transformations.map(cartModelLD) {
        return@map it?.table?.PeopleQuantity;
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
                paymentsList = mutableListOf(),
                discountUserList = mutableListOf(),
                discountServerList = mutableListOf(),
                diningOption = DataHelper.orderSettingLocalStorage?.ListDiningOptions?.firstOrNull()!!,
            )

        val diningOptionId =
            DataHelper.floorLocalStorage?.Floor?.firstOrNull { floorTable -> floorTable._Id == table.FloorGuid }?.DiningOptionId
        val diningOption = OrderHelper.getDiningOptionItem(diningOptionId)
        diningOption?.let {
            diningOptionChange(it);
        }

    }

    fun addCustomerToCart(customer: CustomerResp) {
        this.cartModelLD.value!!.customer = customer;
        this.cartModelLD.notifyValueChange();
    }

    fun removeCustomerFromCart() {
        this.cartModelLD.value!!.customer = null;
        this.cartModelLD.notifyValueChange();
    }

    fun addItemToCart(item: BaseProductInCart) {
        this.cartModelLD.value?.run {
            if (item is Regular) {
                addRegular(item);
            } else if (item is Combo) {
                addBundle(item);
            }
        }
        this.cartModelLD.notifyValueChange();
    }

    fun updatePriceList(menuLocation_id: String) {
        this.cartModelLD.value?.updatePriceList(menuLocation_id);
        this.cartModelLD.notifyValueChange();
    }

    fun updateItemInCart(index: Int, item: BaseProductInCart) {
        if (item.quantity!! > 0) {
            cartModelLD.value!!.productsList[index] = item;
        } else {
            cartModelLD.value!!.productsList.removeAt(index);
        }
        cartModelLD.notifyValueChange();
    }

    fun addPaymentOrder(payment: PaymentOrder) {
        cartModelLD.value!!.addPayment(payment);
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
                        this@CurCartData.cartModelLD.value!!.clearCart();
                        this@CurCartData.cartModelLD.notifyValueChange()
                        callback()
                    }
                }
            )
    }

    fun removeCart() {
        this@CurCartData.cartModelLD.value = null;
        this@CurCartData.cartModelLD.notifyValueChange();
    }

    fun deleteDiscountCart(discount: DiscountCart, productInCart: BaseProductInCart?) {
        if (productInCart != null) {
            discount.disOriginal.let {
                if (it is Reason) {
                    productInCart.compReason = null;
                } else if (it is DiscountUser) {
                    productInCart.discountUsersList?.remove(it);
                }
            }
        } else
            discount.disOriginal.let {
                if (it is Reason) {
                    cartModelLD.value!!.compReason = null;
                } else if (it is DiscountUser) {
                    cartModelLD.value!!.discountUserList.remove(it);
                }
            }
        cartModelLD.notifyValueChange();
    }

    fun removeCompReason() {
        cartModelLD.value!!.compReason = null;
        cartModelLD.notifyValueChange();
    }

    fun addCompReason(reason: Reason) {

        this.cartModelLD.value!!.addCompReason(reason);
        cartModelLD.notifyValueChange();
    }

    fun addDiscountUser(discount: DiscountUser) {
        this.cartModelLD.value!!.addDiscountUser(discount);
        cartModelLD.notifyValueChange();
    }

    fun diningOptionChange(diningOption: DiningOption) {
        cartModelLD.value?.diningOption = diningOption;
        updatePriceList(diningOption.SubDiningOption?.firstOrNull()?.LocationGuid!!);
    }


}