package com.hanheldpos.data.api.pojo.discount

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hanheldpos.data.api.pojo.customer.CustomerGroup
import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.data.api.pojo.fee.CustomerGets
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.model.cart.CartModel
import com.hanheldpos.model.discount.*
import com.hanheldpos.ui.screens.cart.CurCartData
import com.hanheldpos.utils.DateTimeUtils
import kotlinx.parcelize.Parcelize
import java.util.*


@Parcelize
data class DiscountResp(
    val Acronymn: String,
    val ApplyToDiningOptionText: String,
    val ApplyToPriceProduct: Int,
    val AssignTo: Int,
    val Color: String,
    val Condition: Condition,
    val CustomerEligibility: Int,
    val CustomerEligibilityList: List<CustomerEligibility>,
    val DateOff: String,
    val DateOn: String,
    val DateRange: Int,
    val Description: String,
    val DiningOption: List<DiningOptionDiscount>,
    val DiscountApplyTo: Int,
    val DiscountAutomatic: Boolean,
    val DiscountAutomaticText: String,
    val DiscountCode: String,
    val DiscountName: String,
    val DiscountText: String,
    val DiscountType: Int,
    val DiscountTypeText: String,
    val DiscountValueText: String,
    val DiscountsApplyToItem: DiscountsApplyToItem,
    val Excluding: Int,
    val FromToDiscount: String,
    val IsFullBanner: Boolean,
    val IsVisiblePOS: Int,
    val MaximumDiscount: Double,
    val MaximumDiscountFormatter: String,
    val MaximumNumberOfUsedPerOrder: Boolean,
    val MaximumNumberOfUsedPerOrderValue: Int,
    val MinimumOrder: Double,
    val MinimumOrderFormatter: String,
    val MinimumOrderId: Int,
    val MinimumRequiredType: Int,
    val MinimumRequiredValue: Double,
    val OnlyApplyDiscountOncePerOrder: Int,
    val OnlyApplyDiscountProductOncePerOrder: Int,
    val OrderNo: Int,
    val ScheduleList: List<ListScheduleItem>,
    val SetSchedules: Int,
    val Trigger: List<Trigger>,
    val Url: String,
    val UsageLimits: String,
    val UseForOrder: Int,
    val _id: String,
    val jsaction: String
) : Parcelable, Cloneable {

    var quantityUsed: Int? = null
    var maxAmountUsed: Double? = null

    val getQuantityUsed: Int
        get() = if (Condition.CustomerBuys.IsMaxQuantity == 1) quantityUsed ?: 0 else 1

    fun getAmountUsed(productId: String?): Double? {
        return if (Condition?.CustomerBuys?.IsMaxAmount == 1) Condition?.CustomerBuys?.ListApplyTo?.firstOrNull { p -> p._id == productId }?.MaxAmount
            ?: 0.0 else 0.0
    }

    fun isValid(cart: CartModel, curDateTime: Date): Boolean {
        val subTotal = cart.getSubTotal();
        val diningOptionList =
            cart.productsList.map { baseProductInCart -> baseProductInCart.diningOption?.Id ?: 0 }
                .distinct().toList();
        return isValidDiningOption(diningOptionList) &&
                (DateRange == 0 || isValidDate(curDateTime)) &&
                isValidMinRequired(
                    subTotal, cart?.getTotalQuantity() ?: 0
                ) && isValidCtmEligibility(cart.customer)
    }

    fun isValid(
        subtotal: Double,
        baseProduct: BaseProductInCart,
        customer: CustomerResp?,
        curDateTime: Date
    ): Boolean {
        return isValidDiningOption(
            mutableListOf(
                baseProduct?.diningOption?.Id ?: 0
            )
        ) && (DateRange == 0 || isValidDate(curDateTime)) &&
                isValidProduct(baseProduct) &&
                isValidMinRequired(
                    subtotal,
                    baseProduct.quantity!!
                ) && isValidCtmEligibility(customer)

    }

    private fun isValidProduct(baseProduct: BaseProductInCart): Boolean {
        val productApply =
            Condition.CustomerBuys.ListApplyTo.firstOrNull { p -> p._id == baseProduct.proOriginal?._id };

        return if (Excluding == 1) productApply == null
        else productApply != null && (productApply.VariantsGroup?.isExistsVariant(baseProduct.variantList)
            ?: false);
    }

    private fun isValidCtmEligibility(customer: CustomerResp?): Boolean {
        return when (CtmEligibilityType.fromInt(CustomerEligibility)) {
            CtmEligibilityType.EVERYONE ->
                true;

            CtmEligibilityType.SPECIFIC_GROUP_CUSTOMERS ->
                ((Gson().fromJson(
                    customer?.ListGroups,
                    object : TypeToken<List<CustomerGroup>?>() {}.type
                )) as List<CustomerGroup>?)?.map { gr ->
                    gr.CustomerGuestGroupGuid
                }?.intersect(CustomerEligibilityList.map { cus -> cus._id }.toSet())
                    ?.isNotEmpty() == true

            CtmEligibilityType.SPECIFIC_CUSTOMER ->
                CustomerEligibilityList.firstOrNull { c -> c._id == customer?._Id } != null;

            else ->
                false;
        }
    }

    private fun isValidDiningOption(diningOptionIdList: List<Int>): Boolean {
        val isValidDiningOption =
            diningOptionIdList.intersect(DiningOption?.map { d -> d.Id }.toSet())?.size > 0;
        return isValidDiningOption;
    }

    private fun isValidDate(curDateTime: Date): Boolean {
        try {
            if (DateOn?.isNullOrEmpty() && DateOff?.isNullOrEmpty()) {
                return true;
            }

            if (curDateTime <=
                DateTimeUtils.strToDate(
                    DateOff,
                    DateTimeUtils.Format.FULL_DATE_UTC_Z
                )
                && curDateTime >=
                DateTimeUtils.strToDate(
                    DateOn,
                    DateTimeUtils.Format.FULL_DATE_UTC_Z
                )
            ) {
                return isValidSchedule(curDateTime);
            }
            return false;
        } catch (error: Exception) {
            return false;
        }
    }

    private fun isValidSchedule(curDateTime: Date): Boolean {
        if (!(ScheduleList?.any() ?: false)) {
            return true;
        }
        val c = Calendar.getInstance()
        c.time = curDateTime;
        return isValidTime(
            ScheduleList?.first { schedule -> schedule.Id == c.get(Calendar.DAY_OF_WEEK) },
            curDateTime
        );
    }

    private fun isValidTime(schedule: ListScheduleItem, curDateTime: Date): Boolean {
        return schedule.ListSetTime.firstOrNull() { time ->
            isValidTime(
                time.TimeOff,
                time.TimeOn,
                curDateTime
            )
        } != null;
    }

    private fun isValidTime(
        timeOffString: String,
        timeOnString: String,
        curDateTime: Date
    ): Boolean {
        try {
            val timeOff = DateTimeUtils.strToDate(timeOffString, DateTimeUtils.Format.HH_mm);
            val timeOn = DateTimeUtils.strToDate(timeOnString, DateTimeUtils.Format.HH_mm);

            return curDateTime.compareTo(timeOff) <= 0 && curDateTime.compareTo(timeOn) >= 0;
        } catch (error: Exception) {
            return false; }
    }

    private fun isValidMinRequired(subtotal: Double, quantity: Int): Boolean {
        return when (DiscMinRequiredType.fromInt(MinimumRequiredType)) {

            DiscMinRequiredType.NONE ->
                true;

            DiscMinRequiredType.AMOUNT ->
                MinimumRequiredValue <= subtotal;

            DiscMinRequiredType.QUANTITY ->
                MinimumRequiredValue <= quantity;

            else ->
                false;

        }
    }

    fun total(
        totalPrice: Double?,
        totalModifier: Double?,
        productOriginal_id: String? = null,
        quantity: Int? = 1
    ): Double? {
        val subtotal =
            if (Condition?.CustomerBuys.isApplyModifier(productOriginal_id ?: "")) totalPrice?.plus(
                totalModifier ?: 0.0
            ) else totalPrice

        val discountValue = Condition.DiscountValue ?: 0.0
        val discountType = DiscountTypeEnum.fromInt(DiscountType)

        when (discountType) {
            DiscountTypeEnum.PERCENT -> return total(
                subtotal,
                quantity,
                discountType,
                discountValue,
                productOriginal_id
            )
            DiscountTypeEnum.AMOUNT
            -> return total(subtotal, quantity, discountType, discountValue, productOriginal_id)
            DiscountTypeEnum.BUYX_GETY -> {
                val discValue = Condition?.CustomerGets?.DiscountValue ?: 0.0
                return when (DiscountEntireType.fromInt(Condition?.CustomerGets?.DiscountValueType)) {
                    DiscountEntireType.FREE -> subtotal
                    DiscountEntireType.SPECIFIC -> subtotal?.minus(
                        quantity?.times(discValue) ?: 0.0
                    )
                    DiscountEntireType.AMOUNT -> discValue
                    DiscountEntireType.PERCENT -> subtotal?.times(discValue.div(100))
                    else -> {
                        0.0
                    }
                }
            }
            else -> return 0.0
        }
    }

    private fun total(
        subtotal: Double?,
        quantity: Int?,
        discountType: DiscountTypeEnum,
        discountValue: Double?,
        productOriginal_id: String?
    ): Double? {
        var quantity = quantity ?: 1
        val proModSubtotal = subtotal?.div(quantity)
        var discPrice = if (discountType == DiscountTypeEnum.PERCENT) proModSubtotal?.times(
            discountValue?.div(100) ?: 1.0
        ) else
            (if (proModSubtotal ?: 0.0 > discountValue ?: 0.0) discountValue else proModSubtotal)

        // Quantity applied on the product must be smaller or equals to "MaxQuantity"
        if (Condition?.CustomerBuys?.IsMaxQuantity == 1) quantity = quantityUsed ?: 1
        // Amount reduced on the product must be smaller or equals to "MaxAmount"
        if (Condition?.CustomerBuys?.IsMaxAmount == 1) {
            discPrice = Condition?.CustomerBuys?.getMaxAmount(discPrice ?: 0.0, productOriginal_id);
        }
        // OnlyApplyDiscountApplyTo is 1: Discount is only allowed to apply the maximum for a product.
        discPrice =
            if (OnlyApplyDiscountProductOncePerOrder == 0) (discPrice?.times(quantity)) else discPrice;

        if (Condition?.CustomerBuys?.IsDiscountLimit == 1) {
            return if (maxAmountUsed ?: 0.0 > discPrice ?: 0.0) discPrice else maxAmountUsed ?: 0.0;
        }
        return discPrice;
    }

    fun isAutoOnClick(): Boolean {
        return (this.DiscountAutomatic) && isExistsTrigger(DiscountTriggerType.ON_CLICK)
    }

    fun isAutoInCart(): Boolean {
        return (this.DiscountAutomatic) && isExistsTrigger(DiscountTriggerType.IN_CART)
    }

    fun isCoupon(): Boolean {
        return !this.DiscountAutomatic
    }

    fun isBuyXGetY(): Boolean {
        return this.DiscountType == DiscountTypeEnum.BUYX_GETY.value
    }

    fun isExistsTrigger(triggerType: DiscountTriggerType): Boolean {
        return this.Trigger.firstOrNull { trigger ->
            trigger.Id == triggerType.value
        } != null || triggerType == DiscountTriggerType.ALL
    }

    fun isMaxNumberOfUsedPerOrder(): Boolean {
        var totalQtyDiscUsed = 0;
        when (DiscApplyTo.fromInt(DiscountApplyTo ?: 0)) {
            DiscApplyTo.ITEM ->
                totalQtyDiscUsed = CurCartData.cartModel?.productsList?.sumOf { pro ->
                    pro.totalQtyDiscUsed(this._id)
                } ?: 0;

            DiscApplyTo.ORDER ->
                totalQtyDiscUsed = CurCartData.cartModel?.totalQtyDiscUsed(this._id) ?: 0;
        }

        return MaximumNumberOfUsedPerOrder && totalQtyDiscUsed >= MaximumNumberOfUsedPerOrderValue;
    }

    public override fun clone(): DiscountResp {
        return copy()
    }
}

@Parcelize
data class Condition(
    val CustomerBuys: CustomerBuys,
    val CustomerGets: CustomerGets,
    val DiscountValue: Double
) : Parcelable

@Parcelize
data class DiningOptionDiscount(
    val Id: Int
) : Parcelable

@Parcelize
data class CustomerEligibility(
    val _id: String
) : Parcelable

@Parcelize
data class CustomerBuys(
    val ApplyTo: Int,
    val IsDiscountLimit: Int,
    val IsMaxAmount: Int,
    val IsMaxQuantity: Int,
    val ListApplyTo: List<Product>,
    val MaximumDiscount: Double
) : Parcelable {
    fun getMaxAmount(amountUsed: Double, productId: String?): Double {
        val productApply = ListApplyTo.firstOrNull { p -> p._id == productId }
        if (productApply != null) {
            return if (amountUsed <= productApply.MaxAmount) amountUsed else productApply.MaxAmount
        }
        return amountUsed
    }

    fun getMaxQuantity(quantityUsed: Int, quantity: Int, product_id: String): Int? {
        val productApply = this.ListApplyTo.firstOrNull { product -> product._id == product_id }
        if (productApply != null) {
            if (productApply.MaxQuantity == 0) {
                return quantity
            }
            val maxQuantity = productApply.MaxQuantity - quantityUsed
            return if (maxQuantity > 0) {
                if (quantity >= maxQuantity) maxQuantity
                else quantity
            } else 0
        }
        return 0
    }

    fun isApplyModifier(productId: String?): Boolean {
        return ListApplyTo.firstOrNull { p -> p._id == productId }?.ApplyToModifier == 1;
    }
}

@Parcelize
data class DiscountsApplyToItem(
    val ApplyTo: Int,
    val DiscountAutomatic: Boolean,
    val DiscountGuid: String,
    val DiscountType: Int,
    val ListCombo: String,
    val ListParents: String,
    val ListProducts: String,
    val UserGuid: String
) : Parcelable


@Parcelize
data class Trigger(
    val Id: Int,
    val Name: String,
    val RefreshTimer: Int
) : Parcelable

@Parcelize
data class ListScheduleItem(
    val Id: Int,
    val Date: String,
    val ListSetTime: List<ListSetTimeItem>,
    val Active: Boolean,
) : Parcelable {
    val listTimeString: String
        get() {
            if (!Active || ListSetTime?.isEmpty())
                return "--:--"
            return ListSetTime.joinToString("\n") { time -> "${time.TimeOn} to ${time.TimeOff}" }
        }
}

@Parcelize

data class ListSetTimeItem(
    val TimeOn: String,
    val TimeOff: String,
    val OrderNo: Long,
) : Parcelable