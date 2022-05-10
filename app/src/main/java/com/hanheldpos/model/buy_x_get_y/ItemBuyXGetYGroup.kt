package com.hanheldpos.model.buy_x_get_y

import com.hanheldpos.data.api.pojo.discount.CustomerBuys
import com.hanheldpos.data.api.pojo.fee.CustomerGets
import com.hanheldpos.data.api.pojo.order.settings.DiningOption
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.cart.Regular

data class ItemBuyXGetYGroup(

    var groupBuyXGetY: GroupBuyXGetY,
    // only show buy x get y  list when item in cart is focused by user
    var isFocused: Boolean = false,
) {
    fun isMaxItemSelected(): Boolean {
        val result = groupBuyXGetY.isCompleted
        return result
    }

    fun getGroupName(): String {
        return groupBuyXGetY.groupName
    }

    fun getProductListApplyToBuyXGetY(
        appliesTo: List<Product>,
        diningOption: DiningOption
    ): List<Regular> {
        val baseProductList: MutableList<Regular> = mutableListOf()
        appliesTo.forEach { productDisc ->
            val baseProduct = DataHelper.menuLocalStorage?.ProductList?.firstOrNull { p ->
                p._id == productDisc._id
            }
            if (baseProduct != null) {
                baseProductList.add(
                    Regular(
                        productItem = baseProduct,
                        diningOptionItem = diningOption,
                        null,
                        null,
                        null,
                        null
                    )
                )
            }
        }
        return baseProductList
    }

    fun isContainProduct(): Boolean {
        return groupBuyXGetY.productList.isNotEmpty()
    }

    fun getMinimumValueFormat(): String {
        val conditionCustomer: Any
        return if (groupBuyXGetY.condition is CustomerBuys) {
            conditionCustomer = groupBuyXGetY.condition as CustomerBuys
            conditionCustomer.MinimumValueFormat
        } else{
            conditionCustomer = groupBuyXGetY.condition as CustomerGets
            conditionCustomer.Quantity.toString() + " item required"
        }
    }
}