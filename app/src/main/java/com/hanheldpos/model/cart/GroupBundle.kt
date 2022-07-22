package com.hanheldpos.model.cart

import com.hanheldpos.data.api.pojo.order.menu.MenuResp
import com.hanheldpos.data.api.pojo.order.settings.DiningOption
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.home.order.menu.MenuDataMapper
import com.hanheldpos.model.product.ProductComboItem


data class GroupBundle(
    var comboInfo: ProductComboItem,
    var productList: MutableList<Regular>,
) : Cloneable {
    val totalQuantity get() = productList.sumOf { it.quantity?: 0 }
    val requireQuantity get() = comboInfo.Quantity?.minus(totalQuantity)
    val groupName get() = MenuDataMapper.getGroupNameFromGroupGuid(comboInfo.ComboGuid,DataHelper.menuLocalStorage!!)
    fun isComplete() = totalQuantity >= (comboInfo.Quantity ?: 0)

    fun addRegular(regular: Regular){
        productList.add(regular)
    }

    public override fun clone(): GroupBundle {
        return copy().apply {
            productList = productList.toMutableList().map { it.clone() }.toMutableList()
        }
    }

    fun productsForChoose(menuResp : MenuResp, locationGuid : String, diningOption : DiningOption, product : Product) : List<Regular> {
        val listRegular: List<Regular> =
            MenuDataMapper.getProductListByGroupGuid(comboInfo.ComboGuid,menuResp).map { it.proOriginal!! }
                .map {
                    return@map Regular(it, diningOption, 1, it.skuDefault, it.Variants, null)
                }
        return listRegular
    }



}