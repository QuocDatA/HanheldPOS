package com.hanheldpos.model.buy_x_get_y

import com.hanheldpos.data.api.pojo.discount.CustomerBuys
import com.hanheldpos.data.api.pojo.discount.ListApplyTo
import com.hanheldpos.data.api.pojo.fee.CustomerGets
import com.hanheldpos.data.api.pojo.order.settings.DiningOption
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.discount.DiscountEntireType
import com.hanheldpos.model.discount.DiscountUser

data class ItemBuyXGetYGroup(

    var groupBuyXGetY: GroupBuyXGetY,
    var listApplyTo: MutableList<Product>? = null,
    var groupListRegular: MutableList<List<Regular>>? = null,
    // only show buy x get y  list when item in cart is focused by user
    var isFocused: Boolean = false,
    var isApplyToEntireOrder: Boolean? = false,
) {
    fun requireQuantity(): Int = groupBuyXGetY.requireQuantity

    fun isMaxItemSelected(): Boolean {
        return groupBuyXGetY.isCompleted
    }

    fun getGroupName(): String {
        return groupBuyXGetY.groupName
    }

    fun isMutableTab(): Boolean {
        if(groupListRegular.isNullOrEmpty())
            return false
        return groupListRegular?.size!! > 1
    }

    fun getProductListApplyToBuyXGetY(
        appliesTo: List<Product>,
        diningOption: DiningOption,
    ): List<Regular> {
        val baseProductList: MutableList<Regular> = mutableListOf()
        appliesTo.forEach { productDisc ->
            val baseProduct = DataHelper.menuLocalStorage?.ProductList?.firstOrNull { p ->
                p._id == productDisc._id
            }
            if (baseProduct != null) {
                baseProductList.add(Regular(
                    productItem = baseProduct,
                    diningOptionItem = diningOption,
                    null,
                    null,
                    null,
                    null
                ))
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
        } else {
            conditionCustomer = groupBuyXGetY.condition as CustomerGets
            conditionCustomer.Quantity.toString() + " item required"
        }
    }
}