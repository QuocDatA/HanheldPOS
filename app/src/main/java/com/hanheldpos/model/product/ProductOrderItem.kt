package com.hanheldpos.model.product

import android.os.Parcelable
import androidx.core.graphics.ColorUtils
import com.hanheldpos.data.api.pojo.order.menu.ProductItem
import com.hanheldpos.model.home.order.ProductModeViewType
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductOrderItem(
    var id: String? = null,
    var text: String? = null,
    var sku: String? = null,
    var description: String? = null,
    var price: Double? = null,
    var comparePrice: Double? = null,
    var img: String? = null,
    var unitStr: String? = null,
    var mappedItem : Parcelable? = null,
    var extraData: ExtraData? = null,
    var maxQuantity:Int = -1,
    var uiType : ProductModeViewType? = null,
) : Parcelable, Cloneable {
    var color: String? = null;
    fun getProductItem(): ProductItem? {
        if (mappedItem is ProductItem) {
            return mappedItem as ProductItem
        }
        return null
    }
}