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
    val requireQuantity get() = comboInfo.Quantity?.minus(totalQuantity);
    val groupName get() = MenuDataMapper.getGroupNameFromGroupGuid(comboInfo.ComboGuid,DataHelper.menu!!);
    fun isComplete() = totalQuantity >= comboInfo.Quantity ?: 0;

    fun addRegular(regular: Regular){
//        val priceOverride  = productItem.priceOverride(menuLocation_id,productItem.skuDefault,productItem.price)
//        val regular = Regular( productItem,diningOptionItem,1,productItem.skuDefault,productItem.variantDefault,priceOverride,null)
        productList.add(regular)
    }

    public override fun clone(): GroupBundle {
        return copy().apply {
            productList = productList.toMutableList().map { it.clone() }.toMutableList()
        }
    }

    public fun productsForChoose(menuResp : MenuResp, locationGuid : String, diningOption : DiningOption, product : Product) : List<Regular> {
        val listRegular: List<Regular> =
            MenuDataMapper.getProductListByGroupGuid(comboInfo.ComboGuid,menuResp).map { it.proOriginal }
                .map {

                    val priceOverride =
                        it!!.priceOverride(locationGuid, it.skuDefault, it.Price?: 0.0);
                    val regular = Regular(it, diningOption, 1, it.skuDefault, it.Variants,priceOverride,null)
                    regular.priceOverride = regular.groupPrice(this,product);
                    return@map regular
                }
        return listRegular;
    }



}