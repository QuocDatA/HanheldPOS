package com.hanheldpos.model.product.buy_x_get_y

import android.os.Parcelable
import com.hanheldpos.PosApp
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.discount.CustomerBuys
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.api.pojo.fee.CustomerGets
import com.hanheldpos.data.api.pojo.order.settings.DiningOption
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.data.api.pojo.product.VariantsGroup
import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.model.cart.Combo
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.discount.DiscountEntireType
import com.hanheldpos.ui.screens.cart.CurCartData
import com.hanheldpos.utils.PriceUtils
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class GroupBuyXGetY(
    var parentDisc_Id: String,

    var condition: @RawValue Any? = null,

    var type: GroupType,

    ) : Parcelable, Cloneable {

    var productList: MutableList<BaseProductInCart> = mutableListOf()

    val totalQuantity get() = productList.sumOf { basePro -> basePro.quantity ?: 0 }
    val requireQuantity
        get() = if (condition is CustomerBuys) (condition as CustomerBuys).requireQuantity?.minus(
            totalQuantity
        ) else (condition as CustomerGets).Quantity.minus(totalQuantity);
    val totalPrice get() = productList.sumOf { basePro -> basePro.lineTotalValue }
    val groupName get() = getGroupName(type)
    val isCompleted get() = getIsCompleted()

    private val isGetEntire
        get() = GroupType.fromInt(type.value) == GroupType.GET && CustomerDiscApplyTo.fromInt((condition as CustomerGets).ApplyTo) == CustomerDiscApplyTo.ENTIRE_ORDER
    private val isBuyEntire
        get() = GroupType.fromInt(type.value) == GroupType.BUY && CustomerDiscApplyTo.fromInt((condition as CustomerBuys).ApplyTo) == CustomerDiscApplyTo.ENTIRE_ORDER

    private fun getIsCompleted(): Boolean {
        val result = productList.firstOrNull { p ->
            !p.isCompleted()
        } == null && isConditionCompleted()
        return result
    }

    private fun isConditionCompleted(): Boolean {
        if (isBuyEntire) {
            val totalOrder = CurCartData.cartModel?.total() ?: 0.0
            val totalQuantityOrder = CurCartData.cartModel?.getBuyXGetYQuantity(parentDisc_Id) ?: 0
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

    fun addProduct(discount: DiscountResp?, product: Product, baseProductInCart: BaseProductInCart, diningOption: DiningOption) {
        if (baseProductInCart is Regular) {
            addRegular(product, diningOption, discount, baseProductInCart)
        } else {
            addBundle(product, diningOption, discount, baseProductInCart)
        }
    }

    private fun addBundle(
        product: Product,
        diningOption: DiningOption,
        discount: DiscountResp?,
        baseProduct: BaseProductInCart,
    ) {
        val bundle = Combo(
            product,
            (baseProduct as Combo).groupList,
            diningOption,
            1,
            product.skuDefault,
            product.variantDefault,
            null
        )
        if(discount != null) {
            if(bundle.discountServersList == null) bundle.discountServersList = mutableListOf()
            bundle.discountServersList?.add(discount)
        }
        productList.add(bundle)
        productList
    }

    private fun addRegular(product: Product, diningOption: DiningOption, discount: DiscountResp?, baseProductInCart: BaseProductInCart?) {
        //Update variant group
        if (product.VariantsGroup != null) {
            val variantGroup = getVariantGroup(product._id)
            if (variantGroup != null)
                product.VariantsGroup = variantGroup
        }

        val regular =
            Regular(product, diningOption, 1, (baseProductInCart as Regular).sku, baseProductInCart.variants, null)
        regular.modifierList = baseProductInCart.modifierList
        regular.variantList = baseProductInCart.variantList
        if(discount != null) {
            if(regular.discountServersList == null) regular.discountServersList = mutableListOf()
            regular.discountServersList?.add(discount)
        }
        productList.add(regular)
    }

    public override fun clone(): GroupBuyXGetY {
        val result = copy()
        result.productList = productList.map {
            when (it) {
                is Combo -> {
                    it.clone()
                }
                is Regular -> {
                    it.clone()
                }
                else ->
                    it.clone()
            }
        }.toMutableList()
        return result
    }

    fun getRequireQuantityFormat(): String {
        if (condition is CustomerBuys) {
            val customerBuys = condition as CustomerBuys
            return when (MinimumType.fromInt(customerBuys.MinimumTypeId ?: 0)) {
                MinimumType.AMOUNT -> {
                    PosApp.instance.getString(R.string.minimum_purchase_amount) + " " + PriceUtils.formatStringPrice(
                        customerBuys.MinimumValue ?: 0.0
                    )
                }
                MinimumType.QUANTITY -> {
                    (PriceUtils.formatStringPrice(customerBuys.MinimumValue.toString()) + " " + getItemRequired(
                        customerBuys.MinimumValue ?: 0.0
                    ))
                }
                else -> {
                    ""
                }
            }

        } else { // condition is CustomerGets
            val customerGets = condition as CustomerGets
            when (CustomerDiscApplyTo.fromInt(customerGets.ApplyTo)) {
                CustomerDiscApplyTo.ENTIRE_ORDER -> {
                    return when (DiscountEntireType.fromInt(customerGets.DiscountValueType)) {
                        DiscountEntireType.PERCENT -> {
                            PosApp.instance.getString(R.string.discount) + " " + PriceUtils.formatStringPrice(
                                customerGets.DiscountValue
                            ) + "%"
                        }
                        else -> {
                            PosApp.instance.getString(R.string.discount) + " " + PriceUtils.formatStringPrice(
                                customerGets.DiscountValue
                            )
                        }
                    }
                }
                else -> {
                    return PriceUtils.formatStringPrice(customerGets.Quantity.toString()) + " " + getItemRequired(
                        customerGets.Quantity
                    )
                }
            }
        }
    }

    fun getRequirementFormat(requireAmount : Int): String {
        if (condition is CustomerBuys) {
            val customerBuys = condition as CustomerBuys
            return when (MinimumType.fromInt(customerBuys.MinimumTypeId ?: 0)) {
                MinimumType.QUANTITY -> {
                    "$requireAmount " + getSelectionRequired(
                        requireAmount
                    )
                }
                else -> {
                    ""
                }
            }

        } else { // condition is CustomerGets
            val customerGets = condition as CustomerGets
            return when (CustomerDiscApplyTo.fromInt(customerGets.ApplyTo)) {
                CustomerDiscApplyTo.ENTIRE_ORDER -> {
                    ""
                }
                else -> {
                    "$requireAmount " + getSelectionRequired(
                        requireAmount
                    )
                }
            }
        }
    }

    private fun getItemRequired(value: Double): String {
        return if (value > 1) (PosApp.instance.getString(R.string.items_required)) else (PosApp.instance.getString(
            R.string.item_required
        ))
    }

    private fun getSelectionRequired(value: Int): String {
        return if (value > 1) (PosApp.instance.getString(R.string.selections_required)) else (PosApp.instance.getString(
            R.string.selection_required
        ))
    }

    fun isApplyEntireOrder(): Boolean {
        return (condition is CustomerBuys && ((condition as CustomerBuys).ApplyTo == CustomerDiscApplyTo.ENTIRE_ORDER.value))
                || (condition is CustomerGets && ((condition as CustomerGets).ApplyTo == CustomerDiscApplyTo.ENTIRE_ORDER.value))
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