package com.hanheldpos.data.api.pojo.fee

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.data.api.pojo.product.VariantsGroup
import com.hanheldpos.model.cart.buy_x_get_y.BuyXGetYApplyTo
import com.hanheldpos.model.fee.ChooseProductApplyTo
import kotlinx.parcelize.Parcelize

@Parcelize
data class CustomerGets(
    val ApplyTo: Int,
    val CustomerName: String,
    val DiscountValue: Double,
    val DiscountValueType: Int,
    val Handle: String,
    val ListApplyTo: List<Product>,
    val ProductApplyTo: ChooseProductApplyTo?,
    val Quantity: Int
) : Parcelable {
    fun findVariantGroup(product_id: String): VariantsGroup? {
        when (BuyXGetYApplyTo.fromInt(ApplyTo)) {
            BuyXGetYApplyTo.PRODUCT -> {
                return ListApplyTo.firstOrNull { p -> p._id == product_id }?.VariantsGroup
            }
            BuyXGetYApplyTo.GROUP -> {
                return ListApplyTo.map { p -> p.ProductList }.flatten()
                    .firstOrNull { p -> p._id == product_id }?.VariantsGroup
            }
            BuyXGetYApplyTo.CATEGORY -> {
                return ListApplyTo.map { p -> p.ProductList }.flatten()
                    .firstOrNull { p -> p._id == product_id }?.VariantsGroup
            }
            else -> {
                return null
            }
        }
    }
}