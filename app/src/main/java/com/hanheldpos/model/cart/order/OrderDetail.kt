package com.hanheldpos.model.cart.order

import android.os.Parcelable
import com.hanheldpos.model.cart.payment.PaymentOrder
import com.hanheldpos.model.customer.Customer
import com.hanheldpos.model.home.table.TableSummary
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderDetai(
    var IsCompletedOrder: Boolean,
    var Order: OrderSummary,
    //var DiningOption: OrderDiningOption,
    var DeliveryTime: DeliveryTime,
    var Shipping: Shipping,
    //var Billing: Customer,
    //var TableList: List<TableSummary>,
    var PaymentList: List<PaymentOrder>,
    //var DiscountList: List<DiscountOrder>,
    var ServiceFeeList: List<OrderFee>,
    var SurchargeFeeList: List<OrderFee>,
    var ShippingFeeList: List<OrderFee>,
    var TaxFeeList: List<OrderFee>,
    //var CompVoidList: List<CompVoid>,
    //var OrderProducts: List<ProductBuy>,
    // comment => already has class
) : Parcelable {
}