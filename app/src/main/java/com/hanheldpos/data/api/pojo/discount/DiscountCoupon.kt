package com.hanheldpos.data.api.pojo.discount

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.hanheldpos.model.DataHelper
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiscountCoupon(
 @SerializedName("_id")
 val Id: String,

 @SerializedName("DiscountName")
 val DiscountName: String,

 @SerializedName("DiscountCode")
 val DiscountCode: String,

 @SerializedName("DiscountAutomatic")
 val DiscountAutomatic: Boolean,

 @SerializedName("OnlyApplyDiscountOncePerOrder")
 val OnlyApplyDiscountOncePerOrder: Int?,

 @SerializedName("DiscountTotalPrice")
 val DiscountTotalPrice: Double?,

 @SerializedName("DiscountQuantity")
 val DiscountQuantity: Int?,

 @SerializedName("DiscountLineTotalPrice")
 val DiscountLineTotalPrice: Double?,

 @SerializedName("DiscountType")
 val DiscountType: Int?,

 @SerializedName("DiscountValue")
 val DiscountValue: Double?,

 @SerializedName("DiscountLineTotalPriceFormatter")
 val DiscountLineTotalPriceFormatter: String,

 @SerializedName("DiscountApplyTo")
 val DiscountApplyTo: Int?,

 @SerializedName("OrderDetailId")
 val OrderDetailId: Int?,

 @SerializedName("Product_id")
 val ProductId: String,
) : Parcelable {

 fun toDiscount(): DiscountResp? {
  val discApply = DataHelper.findDiscount(Id)
  if (discApply != null) {
   discApply.maxAmountUsed = DiscountLineTotalPrice
   discApply.quantityUsed = DiscountQuantity
   discApply.DiscountCode = DiscountCode
  }
  return discApply
 }
}