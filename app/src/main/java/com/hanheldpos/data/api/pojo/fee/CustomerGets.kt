package com.hanheldpos.data.api.pojo.fee

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.data.api.pojo.product.VariantsGroup
import com.hanheldpos.model.buy_x_get_y.CustomerDiscApplyTo
import com.hanheldpos.model.buy_x_get_y.ItemBuyXGetYGroup
import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.model.fee.ChooseProductApplyTo
import com.hanheldpos.ui.screens.cart.CurCartData
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
    val requireQuantity get() = ListApplyTo.sumOf { basePro -> basePro.MaxQuantity ?: 0 }
    fun findVariantGroup(product_id: String): VariantsGroup? {
        when (CustomerDiscApplyTo.fromInt(ApplyTo)) {
            CustomerDiscApplyTo.PRODUCT -> {
                return ListApplyTo.firstOrNull { p -> p._id == product_id }?.VariantsGroup
            }
            CustomerDiscApplyTo.GROUP -> {
                return ListApplyTo.map { p -> p.ProductList ?: mutableListOf() }.flatten()
                    .firstOrNull { p -> p._id == product_id }?.VariantsGroup
            }
            CustomerDiscApplyTo.CATEGORY -> {
                return ListApplyTo.map { p -> p.ProductList ?: mutableListOf() }.flatten()
                    .firstOrNull { p -> p._id == product_id }?.VariantsGroup
            }
            else -> {
                return null
            }
        }
    }

    fun filterListApplyTo(item: ItemBuyXGetYGroup, discount: DiscountResp): MutableList<List<BaseProductInCart>> {
        val listRegularFilter: MutableList<List<BaseProductInCart>> = mutableListOf()
        when (CustomerDiscApplyTo.fromInt(ApplyTo)) {
            CustomerDiscApplyTo.ENTIRE_ORDER -> {

            }
            CustomerDiscApplyTo.PRODUCT -> {
                listRegularFilter.add(
                    item.getProductListApplyToBuyXGetY(
                        ListApplyTo,
                        CurCartData.cartModel?.diningOption!!,
                        discount,
                    ).toMutableList()
                )
            }
            CustomerDiscApplyTo.GROUP -> {
                ListApplyTo.forEach { list ->
                    listRegularFilter.add(
                        item.getProductListApplyToBuyXGetY(
                            list.ProductList ?: listOf(),
                            CurCartData.cartModel?.diningOption!!,
                            discount,
                        )
                    )
                }
            }
            CustomerDiscApplyTo.CATEGORY -> {
                ListApplyTo.forEach { list ->
                    listRegularFilter.add(
                        item.getProductListApplyToBuyXGetY(
                            list.ProductList ?: listOf(),
                            CurCartData.cartModel?.diningOption!!,
                            discount
                        )
                    )
                }
            }
            else -> {}
        }
        return listRegularFilter
    }
}