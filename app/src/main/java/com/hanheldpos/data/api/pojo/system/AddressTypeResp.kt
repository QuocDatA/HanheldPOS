package com.hanheldpos.data.api.pojo.system

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddressTypeResp(
    @SerializedName("AddressTypeId") var AddressTypeId: Int? = null,
    @SerializedName("AddressType_vi") var AddressTypeVi: String? = null,
    @SerializedName("AddressType_en") var AddressTypeEn: String? = null,
    @SerializedName("OrderNo") var OrderNo: Int? = null,
    @SerializedName("Visible") var Visible: Int? = null,
    @SerializedName("Default") var Default: Int? = null
) : Parcelable