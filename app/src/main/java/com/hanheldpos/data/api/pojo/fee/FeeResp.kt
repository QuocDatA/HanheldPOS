package com.hanheldpos.data.api.pojo.fee

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
import com.hanheldpos.model.cart.fee.FeeApplyToType
import com.hanheldpos.model.cart.fee.FeeType
import kotlinx.parcelize.Parcelize


@Parcelize
data class FeeResp(

    @SerializedName("Message") var message: String,
    @SerializedName("DidError") var didError: Boolean,
    @SerializedName("ErrorMessage") var errorMessage: String,
    @SerializedName("Model") var feeModel: FeeModel

) : Parcelable

/*
data class DiscountList (

    @SerializedName("_id") var Id : String,
    @SerializedName("DiscountName") var DiscountName : String,
    @SerializedName("DiscountType") var DiscountType : Int,
    @SerializedName("DiscountAutomatic") var DiscountAutomatic : Boolean,
    @SerializedName("Acronymn") var Acronymn : String,
    @SerializedName("Color") var Color : String,
    @SerializedName("AssignTo") var AssignTo : Int,
    @SerializedName("DiscountCode") var DiscountCode : String,
    @SerializedName("OnlyApplyDiscountProductOncePerOrder") var OnlyApplyDiscountProductOncePerOrder : Int,
    @SerializedName("OnlyApplyDiscountOncePerOrder") var OnlyApplyDiscountOncePerOrder : Int,
    @SerializedName("MinimumRequiredType") var MinimumRequiredType : Int,
    @SerializedName("MinimumRequired") var MinimumRequired : String,
    @SerializedName("CustomerEligibility") var CustomerEligibility : Int,
    @SerializedName("ListCustomerEligibility") var ListCustomerEligibility : String,
    @SerializedName("Excluding") var Excluding : Int,
    @SerializedName("UsageLimits") var UsageLimits : String,
    @SerializedName("SetSchedules") var SetSchedules : Int,
    @SerializedName("ListSchedules") var ListSchedules : String,
    @SerializedName("DateRange") var DateRange : Int,
    @SerializedName("DateOn") var DateOn : String,
    @SerializedName("DateOff") var DateOff : String,
    @SerializedName("IsVisiblePOS") var IsVisiblePOS : Int,
    @SerializedName("ApplyToPriceProduct") var ApplyToPriceProduct : Int,
    @SerializedName("DiningOption") var diningOptionIds : List<Map<String,Int>>,
    @SerializedName("DiscountApplyToOrder") var DiscountApplyToOrder : Int,
    @SerializedName("Condition") var Condition : Condition,
    @SerializedName("DiscountsApplyToItem") var DiscountsApplyToItem : DiscountsApplyToItem,
    @SerializedName("ListUrl") var ListUrl : List<String>

)
*/

@Parcelize
data class FeeModel(
    @SerializedName("FeesType")
    val feesType: Map<String, String>,

    @SerializedName("Fees") val fees: List<Fee>,

//    @SerializedName("DiscountList") val discounts : List<DiscountList>

) : Parcelable

@Parcelize
data class Fee(
    @field:SerializedName("_id")
    val _id: String,

    @field:SerializedName("Id")
    val feeApplyToType: Int,

    @field:SerializedName("Name")
    val name: String,

    @field:SerializedName("Value")
    val value: Double = 0.0,

    @field:SerializedName("FeeTypeId")
    val feeType: FeeType,

    @field:SerializedName("AssignToProductList")
    val assignToProducts: List<FeeAssignToProductItem>,

    ) : Parcelable {
        fun price(subtotal : Double , totalDisc : Double) : Double {
            var subIncDisc = totalDisc.let { subtotal.minus(it) };
            subIncDisc = if (subIncDisc < 0.0) 0.0 else subIncDisc;
            when(FeeApplyToType.fromInt(feeApplyToType)) {
                FeeApplyToType.NotIncluded-> {
                    return subIncDisc * (value / 100);
                }
                FeeApplyToType.Included -> {
                    return subIncDisc - (subIncDisc/((value + 100)/100))
                }
                FeeApplyToType.Order -> {
                    return subIncDisc * (value / 100)
                }
                else-> return  0.0;
            }

        }
    }

@Parcelize
data class FeeAssignToProductItem(
    @field:SerializedName("ProductGuid")
    val productId: String,

    @field:SerializedName("Visible")
    val visible: Int,
) : Parcelable