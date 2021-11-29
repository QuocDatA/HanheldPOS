package com.hanheldpos.model.cart.order

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.data.api.pojo.order.settings.DiningOptionItem
import com.hanheldpos.model.cart.payment.PaymentOrder
import com.hanheldpos.model.home.table.TableSummary
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderDetail(
    var IsCompletedOrder: Boolean,
    var Order: OrderSummary,
    var DiningOption: DiningOptionItem,
    var DeliveryTime: DeliveryTime,
    var Shipping: Shipping,
    var Billing: CustomerResp,
    var TableList: List<TableSummary>,
    var PaymentList: List<PaymentOrder>,
    var DiscountList: List<DiscountOrder>,
    var ServiceFeeList: List<OrderFee>,
    var SurchargeFeeList: List<OrderFee>,
    var ShippingFeeList: List<OrderFee>,
    var TaxFeeList: List<OrderFee>,
    var CompVoidList: List<CompVoid>,
    var OrderProducts: List<ProductBuy>,
    // comment => already has class
) : Parcelable {
}