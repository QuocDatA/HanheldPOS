package com.hanheldpos.data.api.pojo.order.settings

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.hanheldpos.model.cart.fee.FeeType
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderSettingResp(

    @field:SerializedName("Message")
    val message: String? = null,

    @field:SerializedName("Model")
    val model: List<ModelItem?>? = null,

    @field:SerializedName("ErrorMessage")
    val errorMessage: String? = null,

    @field:SerializedName("DidError")
    val didError: Boolean? = null
) : Parcelable

@Parcelize
data class DiningOptionItem(

    @field:SerializedName("Id")
    val id: Int? = null,

    @field:SerializedName("Title")
    val title: String? = null,

    @field:SerializedName("Acronymn")
    val acronymn: String? = null,

    @field:SerializedName("OrderNo")
    val orderNo: Int? = null,

    @field:SerializedName("TypeId")
    val typeId: Int? = null,

    @field:SerializedName("TypeText")
    val typeText: String? = null,

    @field:SerializedName("SubDiningOption")
    val subDiningOptions: List<SubDiningOptionItem>? = null,


    ) : Parcelable

@Parcelize
data class SubDiningOptionItem(

    @field:SerializedName("SubId")
    val subId: Int? = null,

    @field:SerializedName("SubTitle")
    val subTitle: String? = null,

    @field:SerializedName("Location")
    val location: String? = null,

) : Parcelable

@Parcelize
data class ModelItem(

    @field:SerializedName("ListVoid")
    val listVoid: List<ListVoidItem?>? = null,

    @field:SerializedName("ListDiningOptions")
    val diningOptions: List<DiningOptionItem?>? = null,

    @field:SerializedName("ListComp")
    val listComp: List<ListCompItem?>? = null
) : Parcelable

@Parcelize
data class ListReasonsItem(

    @field:SerializedName("Visible")
    val visible: Int? = null,

    @field:SerializedName("Title")
    val title: String? = null,

    @field:SerializedName("Id")
    val id: Int? = null
) : Parcelable

@Parcelize
data class ListVoidItem(

    @field:SerializedName("GroupName")
    val groupName: String? = null,

    @field:SerializedName("Id")
    val id: Int? = null,

    @field:SerializedName("ListReasons")
    val listReasons: List<ListReasonsItem?>? = null
) : Parcelable

@Parcelize
data class ListCompItem(

    @field:SerializedName("GroupName")
    val groupName: String? = null,

    @field:SerializedName("Id")
    val id: Int? = null,

    @field:SerializedName("ListReasons")
    val listReasons: List<ListReasonsItem?>? = null
) : Parcelable

@Parcelize
data class FeeResp (

    @SerializedName("Message") var message : String,
    @SerializedName("DidError") var didError : Boolean,
    @SerializedName("ErrorMessage") var errorMessage : String,
    @SerializedName("Model") var feeModel : FeeModel

):Parcelable

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
    val feesType: Map<String,String>,

    @SerializedName("Fees") val fees : List<Fee>,

//    @SerializedName("DiscountList") val discounts : List<DiscountList>

):Parcelable

@Parcelize
data class Fee(
    @field:SerializedName("_id")
    val _id: String,

    @field:SerializedName("Id")
    val Id: String,

    @field:SerializedName("Name")
    val name: String,

    @field:SerializedName("Value")
    val value: Double = 0.0,

    @field:SerializedName("FeeTypeId")
    val feeType: FeeType,

    @field:SerializedName("AssignToProductList")
    val assignToProducts:List<FeeAssignToProductItem>,

    ):Parcelable

@Parcelize
data class FeeAssignToProductItem(
    @field:SerializedName("ProductGuid")
    val productId: String,

    @field:SerializedName("Visible")
    val visible: Int,
):Parcelable