package com.hanheldpos.model.buy_x_get_y

import com.hanheldpos.data.api.pojo.discount.CustomerBuys
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.api.pojo.fee.CustomerGets
import com.hanheldpos.data.api.pojo.order.settings.DiningOption
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.model.cart.Combo
import com.hanheldpos.model.cart.GroupBundle
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.product.ProductComboItem
import com.hanheldpos.utils.GSonUtils

data class ItemBuyXGetYGroup(

    var groupBuyXGetY: GroupBuyXGetY,
    var listApplyTo: MutableList<Product>? = null,
    var groupListBaseProduct: MutableList<List<BaseProductInCart>>? = null,
    // only show buy x get y  list when item in cart is focused by user
    var isFocused: Boolean = false,
    var isApplyToEntireOrder: Boolean? = false,
) {
    fun requireQuantity(): Int =
        if (groupBuyXGetY.requireQuantity is Int) (groupBuyXGetY.requireQuantity as Int) else (groupBuyXGetY.requireQuantity as Double).toInt()

    fun isMaxItemSelected(): Boolean {
        return groupBuyXGetY.isCompleted
    }

    fun getGroupName(): String {
        return groupBuyXGetY.groupName
    }

    fun isMutableTab(): Boolean {
        if (groupListBaseProduct.isNullOrEmpty())
            return false
        return groupListBaseProduct?.size!! > 1
    }

    fun getProductListApplyToBuyXGetY(
        appliesTo: List<Product>,
        diningOption: DiningOption,
        discount: DiscountResp,
    ): List<BaseProductInCart> {
        val baseProductList: MutableList<BaseProductInCart> = mutableListOf()
        appliesTo.forEach { productDisc ->
            val baseProduct = DataHelper.menuLocalStorage?.ProductList?.firstOrNull { p ->
                p._id == productDisc._id
            }
            if (baseProduct != null) {
                if (!baseProduct.isBundle())
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
                else
                    baseProductList.add(initCombo(baseProduct, diningOption, discount))
            }
        }
        return baseProductList
    }

    private fun initCombo(product: Product, diningOption: DiningOption, discount: DiscountResp): Combo {
        val comboGroupList = GSonUtils.toList<List<ProductComboItem>>(product.Combo)
        val groupList = comboGroupList?.map { comboGroup ->
            GroupBundle(
                comboInfo = comboGroup,
                productList = mutableListOf()
            )
        }?.toList()
        val bundle = Combo(
            product,
            groupList!!,
            diningOption,
            1,
            product.skuDefault,
            product.variantDefault,
            null
        )
        bundle.discountServersList?.add(discount)
        return bundle
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