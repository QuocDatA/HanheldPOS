package com.hanheldpos.data.api.pojo.receipt

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReceiptCashier(
    val AdditionalText_CustomText: String,
    val AdditionalText_ReturnPolicy: String,
    val Contact_Address: String,
    val Contact_Facebook: String,
    val Contact_Instagram: String,
    val Contact_Phone: String,
    val Contact_Twitter: String,
    val Contact_Website: String,
    val DigitalReceipt: String,
    val LocationBusinessName: String,
    val LogoDigital: String,
    val LogoPrinted: String,
    val ShowItemDescription: Int,
    val ShowLocation: Int,
    val ShowReferralBanner: Int
) : Parcelable