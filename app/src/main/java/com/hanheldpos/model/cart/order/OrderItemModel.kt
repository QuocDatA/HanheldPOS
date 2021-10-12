package com.hanheldpos.model.cart.order

import android.os.Parcelable
import com.diadiem.pos_config.utils.Const
import com.hanheldpos.data.repository.GenerateId
import com.hanheldpos.model.home.order.menu.OrderMenuComboItemModel
import com.hanheldpos.model.product.*
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderItemModel(
    /**
     * @OrderItemId we generate the Id so we can check save compare in the future, ex: when save edited item
     */
    val orderItemId: String = GenerateId.getOrderItemId(),

    var productOrderItem: ProductOrderItem ?= null,

    var extraDone: ExtraDoneModel?= null,

    /**
     * If order item has combo list
     */
    var menuComboItem: OrderMenuComboItemModel? = null,

    var type: OrderItemType? = null,

    var quantity: Int = 1,
    var note: String? = null,

) : Parcelable {

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
        var quantity = (quantity ?: 0);
        quantity = if (quantity > 0) quantity.minus(num) else 0
    }

    /**
     * Get Info of Order
     */

    fun getOrderName(): String? {
        return productOrderItem?.text;
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
        return note;
    }

    fun getVariantStr(): String? {
        return getVariantStr(Const.SymBol.SplashSeparator)
    }

    private fun getVariantStr(separator: String): String? {
        return extraDone?.getVariantStr(separator)
    }

    fun getModifierStr() : String? {
        return getModifierStr(Const.SymBol.CommaSeparator);
    }

    private fun getModifierStr(separator: String): String? {
        return extraDone?.getVariantStr(separator)
    }

    fun getDescription(): String {
        return "${productOrderItem?.text} x${quantity}"
    }



    fun getPriceRegular(): Double {
        var price = productOrderItem?.price ?: 0.0
        if (extraDone?.selectedVariant != null) {
            price = extraDone?.selectedVariant!!.price!!
        }
        return price
    }

    fun getPriceModSubTotal() : Double {
        return extraDone?.selectedModifierGroup?.sumOf {
            it.getSubtotalModifier()
        } ?: 0.0;
    }

    fun getPriceProModSubTotal() : Double {
        return getPriceRegular() + getPriceModSubTotal();
    }

    fun getPriceSubTotal() : Double {
        return getPriceProModSubTotal() * quantity;
    }

    fun getPriceLineTotal() : Double {
        val subtotal = getPriceSubTotal();
        // TODO : add discount below
        return  subtotal;
    }

    fun getOrderPrice(): Double {
        var sum : Double = 0.0;
        if(type == OrderItemType.Combo){

            sum = if (productOrderItem?.isPriceFixed != true) productOrderItem?.price!! else productOrderItem?.comparePrice!!;

            when(productOrderItem!!.pricingMethodType){
                PricingMethodType.BasePrice->{
                    sum = sum.plus(menuComboItem!!.listItemsByGroup?.sumOf {
                        it!!.listSelectedComboItems.sumOf { itPicked->
                            itPicked?.selectedComboItem?.getPriceModSubTotal()!!
                        }
                    } ?: 0.0);
                }
                PricingMethodType.GroupPrice->{
                    sum = sum.plus(menuComboItem!!.listItemsByGroup?.sumOf {
                        it!!.listSelectedComboItems.sumOf { itPicked->
                            itPicked?.selectedComboItem?.getPriceLineTotal()!!
                        }
                    } ?: 0.0);
                }
            }

            menuComboItem!!.listItemsByGroup?.forEach {
                it?.listSelectedComboItems?.forEach { itPicked->
                    sum = sum.plus(itPicked?.selectedComboItem?.getPriceLineTotal() ?: 0.0);
                }
            }
        }
        else {
            sum = getPriceLineTotal()
        }
        return sum;
    }
}