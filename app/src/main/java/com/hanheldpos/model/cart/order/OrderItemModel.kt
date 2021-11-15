package com.hanheldpos.model.cart.order

import android.os.Parcelable
import com.diadiem.pos_config.utils.Const
import com.hanheldpos.data.repository.GenerateId
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.cart.fee.FeeApplyToType
import com.hanheldpos.model.product.*
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderItemModel(
    /**
     * @OrderItemId we generate the Id so we can check save compare in the future, ex: when save edited item
     */
    val orderItemId: String = GenerateId.getOrderItemId(),

    /**
     * If order item has combo list
     */


    var type: OrderItemType? = null,

    var quantity: Int = 1,
    var note: String? = null,
    var isShownDetail: Boolean = false,
    var feeType : FeeApplyToType? = null,
) : Parcelable, Cloneable {

    /**
     * @return Quantity of order
     */
    fun getOrderQuantity(): Int {
        return quantity ?: 0;
    }

    fun plusOrderQuantity(num: Int) {
        quantity = (quantity ?: 0).plus(num);
    }

    fun minusOrderQuantity(num: Int) {
        quantity = if (quantity > 0) quantity.minus(num) else 0
    }

    /**
     * Get Info of Order
     */

    fun getOrderName(): String? {
        return "productOrderItem?.text;"
    }

    fun getOrderImage(): String? {
        return ""
    }

    fun getOrderSku(): String? {
        var sku = ""

//        extraDone?.let { it ->
//            it.selectedVariant?.sku?.let {
//                sku = it
//            }
//        }

        return sku
    }


    fun isCombo(): Boolean {
        return false;
    }

    fun getVariantStr(): String? {
        return getVariantStr(Const.SymBol.CommaSeparator)
    }

    private fun getVariantStr(separator: String): String? {
        return ""
    }

    fun getModifierStr(): String? {

        return getModifierStr(Const.SymBol.CommaSeparator);
    }

    private fun getModifierStr(separator: String): String? {
        return ""
    }

    fun getDescription(): String {
        return ""
    }

    fun getBasePrice() : Double {
        return 0.0
    }

    fun getPriceRegular(): Double {
        var sum: Double = 0.0;
        return sum
    }

    private fun getPriceModSubTotal(): Double {
        return  0.0;
    }

    private fun getPriceProModSubTotal(): Double {
        return getPriceRegular() + getPriceModSubTotal();
    }

    fun getPriceSubTotal(): Double {
        return getPriceProModSubTotal() * quantity;
    }

    private fun getPriceTotalDisc() : Double {
        return 0.0;
    }

    @JvmOverloads
    fun getFee(subtotal: Double = getPriceSubTotal(), totalDisc: Double = getPriceTotalDisc()): Double {

        var subIncDisc = subtotal - totalDisc

        subIncDisc = if (subIncDisc < 0) 0.0 else subIncDisc


        /// TODO dealing with missing Id and Value as suggested since these fields does not available in cart
        val valueFee = DataHelper.getValueFee(feeType);
        return when (feeType) {
            FeeApplyToType.NotIncluded -> subIncDisc * ( valueFee/ 100)
            FeeApplyToType.Included -> subIncDisc - (subIncDisc / ((valueFee + 100) / 100))
            else-> 0.0
        }
    }

    fun getPriceLineTotal(): Double {
        val subtotal = getPriceSubTotal();
        // TODO : add discount below
        val fee = getFee();
        val disc = getPriceTotalDisc();
        return subtotal - disc + fee;
    }


}