package com.hanheldpos.data.api.pojo.order.status

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Fullfillments(
    val Id: Int,
    val ListDinningOptions: List<ListDinningOptions>?,
    val Title_en: String
) : Parcelable {
    fun findTextOrderStatus(diningOptionId : Int,orderStatusId : Int) : String? {
        return ListDinningOptions?.firstOrNull{ option -> option.Id == diningOptionId }?.ListOrderSettingStatus?.firstOrNull()?.ListDicOrderStatus?.firstOrNull{status -> status.OrderStatusId == orderStatusId}?.Title_en;
    }

    fun findColorOrderStatus(diningOptionId : Int,orderStatusId : Int) : String? {
        return ListDinningOptions?.firstOrNull{option -> option.Id == diningOptionId}?.ListOrderSettingStatus?.firstOrNull()?.ListDicOrderStatus?.firstOrNull{status -> status.OrderStatusId == orderStatusId}?.Color;
    }
}