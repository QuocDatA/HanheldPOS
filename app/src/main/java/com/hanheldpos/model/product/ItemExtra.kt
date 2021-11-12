package com.hanheldpos.model.product

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.order.menu.ModifierItem
import com.hanheldpos.data.api.pojo.product.ProductItem
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class ItemExtra(
    val modifier: ModifierItem,
    private val productPricing: ProductItem,
    var extraQuantity: Int = 0,
    var isSelected: Boolean = false
) : Parcelable {
    @IgnoredOnParcel
    val price: Double = modifier.pricing(productPricing);
}