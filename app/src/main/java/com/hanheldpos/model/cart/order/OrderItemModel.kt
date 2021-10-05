package com.hanheldpos.model.cart.order

import android.os.Parcelable
import com.diadiem.pos_config.utils.Const
import com.hanheldpos.data.repository.GenerateId
import com.hanheldpos.model.home.order.menu.OrderMenuComboItemModel
import com.hanheldpos.model.product.ExtraDoneModel
import com.hanheldpos.model.product.ProductOrderItem
import com.hanheldpos.model.product.getPriceLineSubTotal
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderItemModel(
    /**
     * @OrderItemId we generate the Id so we can check save compare in the future, ex: when save edited item
     */
    val orderItemId: String = GenerateId.getOrderItemId(),

    var productOrderItem: ProductOrderItem? = null,

    var extraDone: ExtraDoneModel? = null,

    /**
     * If order item has combo list
     */
    var menuComboItem: OrderMenuComboItemModel? = null,

    var type: OrderItemType? = null,
) : Parcelable {


    /**
     *  Quantity of order
     */

    fun getOrderQuantity(): Int {
        return extraDone?.quantity ?: 0;
    }

    fun plusOrderQuantity(num: Int) {
        extraDone?.quantity = (extraDone?.quantity ?: 0).plus(num)
    }

    fun minusOrderQuantity(num: Int) {
        var quantity = (extraDone?.quantity ?: 0);
        extraDone?.quantity = if (quantity > 0) quantity.minus(num) else 0
    }

    /**
     * Get Info of Order
     */

    fun getOrderName(): String? {
        /*var rs = discountDetailDone?.getName()
        if (rs.isNullOrEmpty())
            rs = productOrderItem?.text*/
        var rs = productOrderItem?.text
        return rs
    }

    fun getOrderImage(): String? {
        return productOrderItem?.img
    }

    fun getOrderSku(): String? {
        var sku = productOrderItem?.sku

        extraDone?.let { it ->
            it.selectedVariant?.sku?.let {
                sku = it
            }
        }

        return sku
    }

    fun getNoteStr(): String? {
        var noteStr: String? = null

        extraDone?.let {
            noteStr = it.note
        }

        return noteStr
    }

    fun getVariantStr(): String? {
        return getVariantStr(Const.SymBol.SplashSeparator)
    }

    private fun getVariantStr(separator: String): String? {
        return extraDone?.getVariantStr(separator)
    }

    fun getDescription(): String? {
        return if (extraDone != null) {
            extraDone?.getDescription()
        } else {
            ""
        }
    }

    fun getOrderPrice(): Double? {
        var sum : Double = 0.0;
        // Get price of main product
        sum = extraDone?.getPriceLineSubTotal() ?: 0.0;
        /**
         * get subtotal proce of each item in combo
        */
        if(menuComboItem != null){
            menuComboItem!!.listItemsByGroup?.forEach {
                it?.listSelectedComboItems?.forEach { itPicked->
                    sum = sum.plus(itPicked?.extraDoneModel?.getPriceLineSubTotal() ?: 0.0);
                }
            }
        }
        return sum;
    }
}