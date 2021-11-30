package com.hanheldpos.ui.screens.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.data.api.pojo.order.settings.DiningOptionItem
import com.hanheldpos.data.api.pojo.order.settings.Reason
import com.hanheldpos.data.api.pojo.table.FloorTableItem
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.cart.CartModel
import com.hanheldpos.model.cart.Combo
import com.hanheldpos.model.cart.DiscountCart
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.cart.payment.PaymentOrder
import com.hanheldpos.model.discount.DiscountUser
import com.hanheldpos.model.home.table.TableStatusType
import com.hanheldpos.model.home.table.TableSummary
import com.hanheldpos.model.product.BaseProductInCart
import com.hanheldpos.ui.base.dialog.AppAlertDialog
import com.hanheldpos.ui.base.viewmodel.BaseViewModel

class CartDataVM : BaseViewModel() {

    val cartModelLD: MutableLiveData<CartModel> = MutableLiveData();

    val diningOptionLD: LiveData<DiningOptionItem> = Transformations.map(cartModelLD) {
        return@map it?.diningOption ?: DataHelper.getDefaultDiningOptionItem()
    }
    val linePerTotalQuantity: LiveData<String> = Transformations.map(cartModelLD) {
        return@map "${it.productsList.size}/${it.productsList.sumOf { it1 -> it1.quantity?: 0 }}"
    }

    val numberOfCustomer: LiveData<Int> = Transformations.map(cartModelLD) {
        return@map it?.table?.PeopleQuantity;
    }

    fun initCart(numberCustomer: Int, table: FloorTableItem) {
        table.tableStatus = TableStatusType.Pending;
        cartModelLD.value = CartModel(
            table = TableSummary(
                _id = table.id!!,
                TableName = table.tableName!!,
                PeopleQuantity = numberCustomer
            ),
            fees = DataHelper.findFeeOrderList()?: mutableListOf(),
            productsList = mutableListOf(),
            paymentsList = mutableListOf(),
            discountUserList = mutableListOf(),
            discountServerList = mutableListOf(),
            diningOption = DataHelper.getDefaultDiningOptionItem()!!,
        );
    }

    fun addCustomerToCart(customer : CustomerResp){
        this.cartModelLD.value!!.customer = customer;
        this.cartModelLD.notifyValueChange();
    }

    fun addItemToCart(item: BaseProductInCart) {
        this.cartModelLD.value?.run {
            if(item is Regular){
                addRegular(item);
            } else if(item is Combo){
                addBundle(item);
            }
        }
        this.cartModelLD.notifyValueChange();
    }

    fun updatePriceList(menuLocation_id : String){
        this.cartModelLD.value!!.updatePriceList(menuLocation_id);
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

    fun deleteCart(title: String, message: String, positiveText : String , negativeText: String, callback: () -> Unit) {
        AppAlertDialog.get()
            .show(
                title,
                message,
                positiveText = positiveText,
                negativeText = negativeText,
                onClickListener = object : AppAlertDialog.AlertDialogOnClickListener {
                    override fun onPositiveClick() {
                        this@CartDataVM.cartModelLD.value!!.clearCart();
                        this@CartDataVM.cartModelLD.notifyValueChange()
                        callback()
                    }
                }
            )
    }

    fun deleteDiscount(discount : DiscountCart){
        discount.disOriginal.let {
            if (it is Reason){
                cartModelLD.value!!.compReason = null;
            }
            else if (it is DiscountUser) {
                cartModelLD.value!!.discountUserList.remove(it);
            }
        }
        cartModelLD.notifyValueChange();
    }

    fun removeCompOrder(){
        cartModelLD.value!!.compReason = null;
        cartModelLD.notifyValueChange();
    }

    fun addCompReason(reason : Reason){
        this.cartModelLD.value!!.addCompReason(reason);
        cartModelLD.notifyValueChange();
    }

    fun addDiscountUser(discount : DiscountUser){
        this.cartModelLD.value!!.addDiscountUser(discount);
        cartModelLD.notifyValueChange();
    }

    fun billCart() {
        if(cartModelLD.value!!.paymentsList.isEmpty()){
            AppAlertDialog.get()
                .show(
                    "Warning",
                    "Please choose payment method",
                )
            return;
        }

    }
}