package com.hanheldpos.model.cart

import com.hanheldpos.data.api.pojo.order.settings.DiningOptionItem
import com.hanheldpos.data.api.pojo.product.ProductItem
import com.hanheldpos.model.home.order.menu.OrderMenuDataMapper
import com.hanheldpos.model.product.ProductComboItem

class GroupBundle(
    var comboInfo: ProductComboItem,
    var productList: MutableList<Regular>,
) {
    val totalQuantity get() = productList.sumOf { it.quantity?: 0 }
    val requireQuantity get() = comboInfo.quantity?.minus(totalQuantity);
    val groupName get() = OrderMenuDataMapper.getGroupNameFromGroupGuid(comboInfo.comboGuid);
    fun isComplete() = totalQuantity >= comboInfo?.quantity ?: 0;

    fun addRegular(productItem: ProductItem, menuLocation_id : String, diningOptionItem: DiningOptionItem ){
        val priceOverride  = productItem.priceOverride(menuLocation_id,productItem.skuDefault,productItem.price)
        val regular = Regular( productItem,diningOptionItem,1,productItem.skuDefault,productItem.variantDefault,priceOverride,null)
        productList.add(regular)
    }
}