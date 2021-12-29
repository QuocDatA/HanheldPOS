package com.hanheldpos.model.cart

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.order.settings.DiningOptionItem
import com.hanheldpos.data.api.pojo.product.ProductItem
import com.hanheldpos.model.home.order.menu.OrderMenuDataMapper
import com.hanheldpos.model.product.ProductComboItem
import kotlinx.parcelize.Parcelize


data class GroupBundle(
    var comboInfo: ProductComboItem,
    var productList: MutableList<Regular>,
) : Cloneable {
    val totalQuantity get() = productList.sumOf { it.quantity?: 0 }
    val requireQuantity get() = comboInfo.quantity?.minus(totalQuantity);
    val groupName get() = OrderMenuDataMapper.getGroupNameFromGroupGuid(comboInfo.comboGuid);
    fun isComplete() = totalQuantity >= comboInfo.quantity ?: 0;

    fun addRegular(regular: Regular){
//        val priceOverride  = productItem.priceOverride(menuLocation_id,productItem.skuDefault,productItem.price)
//        val regular = Regular( productItem,diningOptionItem,1,productItem.skuDefault,productItem.variantDefault,priceOverride,null)
        productList.add(regular)
    }

    override fun clone(): GroupBundle {
        return copy().apply {
            productList = productList.toMutableList().map { it.clone() }.toMutableList()
        }
    }

}