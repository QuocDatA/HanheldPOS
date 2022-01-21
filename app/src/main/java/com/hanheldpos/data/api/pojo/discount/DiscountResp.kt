package com.hanheldpos.data.api.pojo.discount

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hanheldpos.data.api.pojo.customer.CustomerGroup
import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.model.cart.CartModel
import com.hanheldpos.model.discount.CtmEligibilityType
import com.hanheldpos.model.discount.DiscMinRequiredType
import com.hanheldpos.model.product.BaseProductInCart
import com.hanheldpos.utils.time.DateTimeHelper
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
) : Parcelable {

    fun isValid(cart: CartModel, curDateTime: Date): Boolean {
        val subTotal = cart.getSubTotal();
        val diningOptionList =
            cart.productsList.map { baseProductInCart -> "${baseProductInCart.diningOption?.Id}" }
                .distinct().toList();
        if (isValidDiningOption(diningOptionList)) {
            if (DateRange == 0 || isValidDate(curDateTime)) {
                if (isValidMinRequired(
                        subTotal,
                        cart?.getTotalQuantity() ?: 0
                    ) && isValidCtmEligibility(cart.customer)
                ) {
                    return true;
                }
            }
        }
        return false;
    }

    fun isValid(
        baseProduct: BaseProductInCart,
        customer: CustomerResp,
        curDateTime: Date
    ): Boolean {
        if (isValidDiningOption(mutableListOf(baseProduct?.diningOption?.Id.toString()))) {
            if (DateRange == 0 || isValidDate(curDateTime)) {
                if (isValidProduct(baseProduct)) {
                    if (isValidMinRequired(
                            baseProduct.total(),
                            baseProduct.quantity!!
                        ) && isValidCtmEligibility(customer)
                    ) {
                        return true;
                    }
                }

            }
        }
        return false;
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
                    object : TypeToken<List<CustomerGroup>>() {}.type
                )) as List<CustomerGroup>).map { gr ->
                    gr.CustomerGuestGroupGuid
                }.intersect(CustomerEligibilityList.map { cus -> cus._id }.toSet()).isNotEmpty()

            CtmEligibilityType.SPECIFIC_CUSTOMER ->
                CustomerEligibilityList.firstOrNull { c -> c._id == customer?._Id } != null;

            else ->
                false;
        }
    }

    private fun isValidDiningOption(diningOptionIdList: List<String>): Boolean {
        val isValidDiningOption =
            diningOptionIdList.intersect(DiningOption?.map { d -> d.Id }.toSet())?.size > 0;
        return isValidDiningOption;
    }

    private fun isValidDate(curDateTime: Date): Boolean {
        try {
            if (DateOn.isNullOrEmpty() && DateOff.isNullOrEmpty()) {
                return false;
            }

            if (curDateTime.compareTo(
                    DateTimeHelper.strToDate(
                        DateOff,
                        DateTimeHelper.Format.FULL_DATE_UTC_Z
                    )
                ) <= 0 && curDateTime.compareTo(
                    DateTimeHelper.strToDate(
                        DateOn,
                        DateTimeHelper.Format.FULL_DATE_UTC_Z
                    )
                ) >= 0
            ) {
                return isValidSchedule(curDateTime);
            }
            return false;
        } catch (error: Exception) {
            return false;
        }
    }

    private fun isValidSchedule(curDateTime: Date): Boolean {
        if (!ScheduleList?.any() ?: false) {
            return true; }
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
            val timeOff = DateTimeHelper.strToDate(timeOffString, DateTimeHelper.Format.HH_mm);
            val timeOn = DateTimeHelper.strToDate(timeOnString, DateTimeHelper.Format.HH_mm);

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
}

@Parcelize
data class Condition(
    val CustomerBuys: CustomerBuys,
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
) : Parcelable

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
    val ListSetTime: List<ListSetTimeItem>
) : Parcelable

@Parcelize

data class ListSetTimeItem(
    val TimeOn: String,
    val TimeOff: String,
    val OrderNo: Long,
) : Parcelable