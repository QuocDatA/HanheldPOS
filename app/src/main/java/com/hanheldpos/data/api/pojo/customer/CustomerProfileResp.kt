package com.hanheldpos.data.api.pojo.customer


import com.google.gson.annotations.SerializedName

data class CustomerProfileResp(
    @SerializedName("Acronymn")
    val acronymn: String?,
    @SerializedName("Avatar")
    val avatar: String?,
    @SerializedName("FirstName")
    val firstName: String?,
    @SerializedName("FullName")
    val fullName: String?,
    @SerializedName("FullName_Ansi")
    val fullNameAnsi: String?,
    @SerializedName("GiftCard")
    val giftCard: Int?,
    @SerializedName("GiftCardText")
    val giftCardText: String?,
    @SerializedName("_id")
    val _id: String?,
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("LastName")
    val lastName: String?,
    @SerializedName("Logo")
    val logo: String?,
    @SerializedName("MemberSince")
    val memberSince: String?,
    @SerializedName("MemberText")
    val memberText: String?,
    @SerializedName("MemberType")
    val memberType: Int?,
    @SerializedName("Phone")
    val phone: String?,
    @SerializedName("Points")
    val points: Int?,
    @SerializedName("PointsText")
    val pointsText: String?,
    @SerializedName("WalletBalance")
    val walletBalance: String?,
    @SerializedName("WalletBalanceText")
    val walletBalanceText: String?
)