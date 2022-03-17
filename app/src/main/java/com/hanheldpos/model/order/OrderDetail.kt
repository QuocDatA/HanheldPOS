package com.hanheldpos.model.order

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.model.payment.PaymentOrder
import com.hanheldpos.model.home.table.TableSummary
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderDetail(
    var IsCompletedOrder: Boolean = true,
    var Order: OrderSummary,
    var DiningOption: OrderDiningOption,
    var DeliveryTime: DeliveryTime?,
    var Shipping: Shipping?,
    var Billing: CustomerResp?,
    var TableList: List<TableSummary>,
    var PaymentList: List<PaymentOrder>,
    var DiscountList: List<DiscountOrder>,
    var ServiceFeeList: List<OrderFee>,
    var SurchargeFeeList: List<OrderFee>,
    var ShippingFeeList: List<OrderFee>?= null,
    var TaxFeeList: List<OrderFee>,
    var CompVoidList: List<CompVoid>,
    var OrderProducts: List<ProductChosen>,
    // comment => already has class
) : Parcelable {
}