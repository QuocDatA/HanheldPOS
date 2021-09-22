package com.hanheldpos.model.cart.order

import android.os.Parcelable
import com.hanheldpos.data.repository.GenerateId
import com.hanheldpos.model.home.order.menu.OrderMenuComboItemModel
import com.hanheldpos.model.product.ExtraDoneModel
import com.hanheldpos.model.product.ProductOrderItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderItemModel(
    /**
     * @OrderItemId we generate the Id so we can check save compare in the future, ex: when save edited item
     */
    val orderItemId: String = GenerateId.getOrderItemId(),

    var productOrderItem: ProductOrderItem? = null,

    /**
     * If order item has combo list
     */
    var productChoosedList: OrderMenuComboItemModel? = null, //Why choosed it must be chosen
//    var orderMenuAction: OrderMenuAction = OrderMenuAction.Add,

    var type: OrderItemType? = null,

    var extraDone: ExtraDoneModel? = null,

    ) : Parcelable {

}