package com.hanheldpos.data.api.pojo.cashdrawer.pay_in_out

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class PayInOutResp(
    val PayInOutGuid : String,
    val Code : String,
    val CashDrawerGuid :String,
    val CreateDate : String,
) : Parcelable
