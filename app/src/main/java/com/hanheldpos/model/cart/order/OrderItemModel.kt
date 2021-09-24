package com.hanheldpos.model.cart.order

import android.os.Parcelable
import com.diadiem.pos_config.utils.Const
import com.hanheldpos.data.repository.GenerateId
import com.hanheldpos.model.home.order.menu.OrderMenuComboItemModel
import com.hanheldpos.model.product.ExtraDoneModel
import com.hanheldpos.model.product.ProductOrderItem
import com.hanheldpos.model.product.getPriceByExtra
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderItemModel(
    /**
     * @OrderItemId we generate the Id so we can check save compare in the future, ex: when save edited item
     */
    val orderItemId: String = GenerateId.getOrderItemId(),

    var productOrderItem: ProductOrderItem? = null,

    /**
     * If order item has combo list
     */
    var productChoosedList: OrderMenuComboItemModel? = null, //Why choosed it must be chosen
//    var orderMenuAction: OrderMenuAction = OrderMenuAction.Add,

    var type: OrderItemType? = null,

    var extraDone: ExtraDoneModel? = null,

) : Parcelable {

    fun isProductItem(): Boolean {
        return true; /*discountDetailDone == null || discountDetailDone?.flowDiscountDone is ProductComboDoneModal*/
    }

    /**
    *  Quantity of order
    */

    fun getOrderQuantity(): Int {
        return if (isProductItem()) {
            extraDone?.quantity ?: 0
        } else {
            /*discountDetailDone?.getOrderQuantity() ?: 0*/
            0
        }
    }

    fun plusOrderQuantity(num: Int) {
        if (isProductItem()) {
            extraDone?.quantity = (extraDone?.quantity ?: 0).plus(num)
        }
    }

    fun minusOrderQuantity(num: Int) {
        if (isProductItem()) {
            var quantity = (extraDone?.quantity ?: 0);
            extraDone?.quantity = if (quantity > 0) quantity.minus(num) else 0
        }
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
        return if (isProductItem()) {
            productOrderItem?.img
        } else {
            null
        }
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

    fun getVariantStr(separator: String): String? {
        return extraDone?.getVariantStr(separator)
    }

    fun getDescription(): String? {
        return if (extraDone != null) {
            extraDone?.getDescription()
        } else {
            /*discountDetailDone?.getDescription()*/
            ""
        }
    }

    fun getOrderPrice(): Double? {
        return if (isProductItem()) {
            extraDone?.getPriceByExtra()
        } else {
            0.0
        }
    }
}