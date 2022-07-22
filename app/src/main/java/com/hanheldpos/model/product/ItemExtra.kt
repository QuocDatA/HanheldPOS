package com.hanheldpos.model.product

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.product.ModifierItem
import com.hanheldpos.data.api.pojo.product.Product
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemExtra(
    val modifier: ModifierItem,
    private val productPricing: Product,
    var extraQuantity: Int = 0,
    var maxExtraQuantity : Int = 100,
) : Parcelable {
    @IgnoredOnParcel
    val price: Double = modifier.pricing(productPricing)

    fun addQuantity(num : Int) {
        if(extraQuantity < maxExtraQuantity){
            extraQuantity = extraQuantity.plus(num)
        }
    }

    fun deleteQuantity(num: Int){
        if (extraQuantity > 0) {
            extraQuantity = extraQuantity.minus(1)
        }
    }
}