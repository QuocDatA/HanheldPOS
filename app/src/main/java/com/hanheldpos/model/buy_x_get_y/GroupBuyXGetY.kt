package com.hanheldpos.model.buy_x_get_y

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.discount.CustomerBuys
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.api.pojo.fee.CustomerGets
import com.hanheldpos.data.api.pojo.order.settings.DiningOption
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.data.api.pojo.product.VariantsGroup
import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.model.cart.Combo
import com.hanheldpos.model.cart.GroupBundle
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.product.ProductComboItem
import com.hanheldpos.ui.screens.cart.CurCartData
import com.hanheldpos.utils.GSonUtils
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class GroupBuyXGetY(
    var parentDisc_Id: String,

    var condition: @RawValue Any? = null,

    var type: GroupType,

    ) : Parcelable {

    var productList: MutableList<BaseProductInCart> = mutableListOf()

    val totalQuantity get() = productList.sumOf { basePro -> basePro.quantity ?: 0 }
    val requireQuantity
        get() = if (condition is CustomerBuys) (condition as CustomerBuys).requireQuantity.minus(
            totalQuantity
        ) else (condition as CustomerGets).requireQuantity.minus(totalQuantity);
    val totalPrice get() = productList.sumOf { basePro -> basePro.lineTotalValue }
    val groupName get() = getGroupName(type)
    val isCompleted get() = getIsCompleted()

    private val isGetEntire
        get() = GroupType.fromInt(type.value) == GroupType.GET && BuyXGetYApplyTo.fromInt((condition as CustomerGets).ApplyTo) == BuyXGetYApplyTo.ENTIRE_ORDER
    private val isBuyEntire
        get() = GroupType.fromInt(type.value) == GroupType.BUY && BuyXGetYApplyTo.fromInt((condition as CustomerBuys).ApplyTo) == BuyXGetYApplyTo.ENTIRE_ORDER

    private fun getIsCompleted(): Boolean {
        val result = productList.firstOrNull { p ->
            !p.isCompleted()
        } == null &&
                isConditionCompleted()
        return result
    }

    private fun isConditionCompleted(): Boolean {
        if (isBuyEntire) {
            val totalOrder = CurCartData.cartModel?.total() ?: 0.0
            val totalQuantityOrder = CurCartData.cartModel?.getTotalQuantity() ?: 0
            return (condition as CustomerBuys).isBuyCompleted(totalOrder, totalQuantityOrder)
        }

        if (isGetEntire) {
            return CurCartData.cartModel?.discountServerList?.firstOrNull { d -> d._id == parentDisc_Id } != null
        }

        val minType =
            if (GroupType.fromInt(type.value) == GroupType.BUY) MinimumType.fromInt(
                (condition as CustomerBuys).MinimumTypeId ?: 1
            ) else MinimumType.QUANTITY
        val minValue =
            if (GroupType.fromInt(type.value) == GroupType.BUY) ((condition as CustomerBuys).MinimumValue
                ?: 0) else ((condition as CustomerGets).Quantity)

        return when (minType) {
            MinimumType.QUANTITY -> {
                totalQuantity >= (minValue.toInt())
            }
            MinimumType.AMOUNT -> {
                totalPrice >= (minValue as Double)
            }
            else -> {
                false
            }
        }
    }

    private fun getGroupName(type: GroupType): String {
        return when (type) {
            GroupType.BUY -> {
                (this.condition as CustomerBuys).CustomerName
            }
            GroupType.GET -> {
                (this.condition as CustomerGets).CustomerName
            }
        }
    }

    private fun getVariantGroup(product_id: String): VariantsGroup? {
        return when (type) {
            GroupType.BUY -> {
                ((condition as CustomerBuys).findVariantGroup(product_id))
            }
            GroupType.GET -> {
                ((condition as CustomerGets).findVariantGroup(product_id))
            }
        }
    }

    fun addProduct(discount: DiscountResp, product: Product, diningOption: DiningOption) {
        val comboGroupList = GSonUtils.toObject<List<ProductComboItem>>(product.Combo)
        if (comboGroupList == null || !comboGroupList.any()) {
            addRegular(product, diningOption, discount)
        } else {
            addBundle(product, diningOption, discount, comboGroupList)
        }
    }

    private fun addBundle(
        product: Product,
        diningOption: DiningOption,
        discount: DiscountResp,
        comboGroupList: List<ProductComboItem>
    ) {
        val groupList = comboGroupList.map { comboGroup ->
            GroupBundle(
                comboInfo = comboGroup,
                productList = mutableListOf()
            )
        }.toList()
        val bundle = Combo(
            product,
            groupList,
            diningOption,
            1,
            product.skuDefault,
            product.variantDefault,
            null
        )
        bundle.discountServersList?.add(discount)
        productList.toMutableList().add(bundle)
    }

    private fun addRegular(product: Product, diningOption: DiningOption, discount: DiscountResp) {
        //Update variant group
        if (product.VariantsGroup != null) {
            val variantGroup = getVariantGroup(product._id)
            if (variantGroup != null)
                product.VariantsGroup = variantGroup
        }

        val regular =
            Regular(product, diningOption, 1, product.skuDefault, product.variantDefault, null)
        regular.discountServersList?.add(discount)
        productList.add(regular)
    }

    fun clone(): GroupBuyXGetY {
        return this.copy()
    }
}

enum class GroupType(val value: Int) {
    BUY(1),
    GET(2);

    companion object {
        fun fromInt(value: Int): GroupType? {
            values().forEach {
                if (it.value == value) {
                    return it
                }
            }
            return null
        }
    }
}