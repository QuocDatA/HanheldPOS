package com.hanheldpos.data.api.pojo.fee

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.hanheldpos.model.cart.fee.FeeApplyToType
import kotlinx.parcelize.Parcelize


@Parcelize
data class Fee(
    @field:SerializedName("_id")
    val _id: String,

    @field:SerializedName("Id")
    val Id: Int,

    @field:SerializedName("Name")
    val Name: String,

    @field:SerializedName("Value")
    val Value: Double = 0.0,

    @field:SerializedName("FeeTypeId")
    val FeeTypeId: Int,

    @field:SerializedName("AssignToProductList")
    val AssignToProductList: List<FeeAssignToProductItem>,

    val Total: Double,

    ) : Parcelable {

    fun price(subtotal : Double, totalDisc : Double) : Double {

        var subIncDisc = totalDisc.let { subtotal.minus(it) }

        subIncDisc = if (subIncDisc < 0.0) 0.0 else subIncDisc

        return when(FeeApplyToType.fromInt(Id)) {
            FeeApplyToType.NotIncluded-> {
                subIncDisc * (Value / 100)
            }
            FeeApplyToType.Included -> {
                subIncDisc - (subIncDisc/((Value + 100)/100))
            }
            FeeApplyToType.Order -> {
                subIncDisc * (Value / 100)
            }
            else-> 0.0
        }

    }
}

@Parcelize
data class FeeAssignToProductItem(
    @field:SerializedName("ProductGuid")
    val ProductGuid: String,

    @field:SerializedName("Visible")
    val Visible: Int,
) : Parcelable
