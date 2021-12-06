package com.hanheldpos.model.cart

import android.os.Build
import androidx.annotation.RequiresApi
import com.hanheldpos.data.api.pojo.discount.DiningOptionDiscount
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.UserHelper
import com.hanheldpos.model.cart.fee.FeeType
import com.hanheldpos.model.cart.payment.PaymentOrder
import com.hanheldpos.model.order.*
import com.hanheldpos.model.product.BaseProductInCart
import com.hanheldpos.model.product.ProductType
import com.hanheldpos.utils.time.DateTimeHelper
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*

object CartConverter {

    fun toOrder(cart: CartModel, orderStatus: Int, paymentStatus: Int): OrderModel {
        var subTotal = cart.getSubTotal();
        var total = cart.total();
        var totalCompVoid = cart.totalComp();
        var totalDisc = cart.totalDiscount(subTotal);
        var totalService = cart.fees.firstOrNull { fee -> fee.feeType == FeeType.ServiceFee }
            ?.price(subTotal, totalDisc);
        var totalSurcharge = cart.fees.firstOrNull { fee -> fee.feeType == FeeType.SurchargeFee }
            ?.price(subTotal, totalDisc);
        var totalTaxe = cart.fees.firstOrNull { fee -> fee.feeType == FeeType.TaxFee }
            ?.price(subTotal, totalDisc);

        var totalFees = cart.totalFee(subTotal, totalDisc);
        var grossPrice = cart.totalGross(subTotal, totalDisc);

        var description =
            cart.productsList.map { baseProductInCart -> baseProductInCart.name }.joinToString(",");

        // TODO : save temp order to local
        cart.createDate = DateTimeHelper.dateToString(Date(),DateTimeHelper.Format.FULL_DATE_UTC_Z);

        return OrderModel(
            Order = Order(
                OrderStatusId = orderStatus,
                PaymentStatusId = paymentStatus,
                UserGuid = UserHelper.getUserGui(),
                EmployeeGuid = UserHelper.getEmployeeGuid(),
                LocationGuid = UserHelper.getLocationGui(),
                DeviceGuid = UserHelper.getDeviceGui(),
                DiningOptionId = cart.diningOption.id,
                CreateDate = cart.createDate,
                Code = cart.orderCode,
                MenuLocationGuid = cart.menuLocationGuid,
                CurrencySymbol = DataHelper.getCurrencySymbol()!!
            ),
            OrderDetail = OrderDetail(
                DiningOption = OrderDiningOption(
                    Id = cart.diningOption.id ?: 0,
                    TypeId = cart.diningOption.typeId ?: 0,
                    Title = cart.diningOption.title,
                    Acronymn = cart.diningOption.acronymn
                ),
                DeliveryTime = cart.deliveryTime,
                TableList = if (cart.table == null) mutableListOf() else mutableListOf(cart.table),
                Billing = cart.customer,
                Shipping = cart.shipping,

                DiscountList = OrderMapper.mappingDiscountList(
                    cart.discountServerList,
                    cart.discountUserList,
                    subTotal,
                    0.0
                ),
                ServiceFeeList = OrderMapper.mappingFeeList(
                    cart.fees,
                    FeeType.ServiceFee,
                    subTotal,
                    totalDisc
                ),
                SurchargeFeeList = OrderMapper.mappingFeeList(
                    cart.fees,
                    FeeType.SurchargeFee,
                    subTotal,
                    totalDisc
                ),
                TaxFeeList = OrderMapper.mappingFeeList(
                    cart.fees,
                    FeeType.TaxFee,
                    subTotal,
                    totalDisc
                ),
                CompVoidList = OrderMapper.mappingCompVoidList(cart.compReason, totalCompVoid),
                OrderProducts = listProduct(cart.productsList),
                PaymentList = cart.paymentsList,
                Order = calOrderSumary(
                    total = total,
                    grossPrice = grossPrice,
                    subTotal = subTotal,
                    totalDisc = totalDisc,
                    totalService = totalService,
                    totalSurcharge = totalSurcharge,
                    totalTaxe = totalTaxe,
                    note = cart.note,
                    otherFee = totalFees,
                    paymentList = cart.paymentsList
                )
            ),
            OrderSummary = OrderSummaryPrimary(
                OrderCode = cart.orderCode!!,
                OrderStatusId = orderStatus,
                PaymentStatusId = paymentStatus,
                Description = description,
                GrandTotal = total,
                TableId = cart.table._id,
                DiningOptionId = cart.diningOption.id,
                DiningOptionName = cart.diningOption.title,
                TableName = cart.table.TableName,
                CreateDate = cart.createDate!!
            )
        );
    }

    private fun listProduct(products: List<BaseProductInCart>): List<ProductBuy> {
        var proOrderList = mutableListOf<ProductBuy>();

        products.forEachIndexed { index, baseProductInCart ->

            when (baseProductInCart.productType) {
                ProductType.REGULAR -> {
                    val regular = baseProductInCart as Regular;
                    proOrderList.add(
                        OrderMapper.mappingProductBuy(
                            regular,
                            index,
                            regular.quantity!!
                        )
                    )
                }
                ProductType.BUNDLE -> {
                    var bundle = baseProductInCart as Combo;
                    proOrderList.add(OrderMapper.mappingProductBuy(bundle, index, null));
                }
            }
        }
        return proOrderList;
    }

    private fun calOrderSumary(
        total: Double,
        grossPrice: Double,
        subTotal: Double,
        totalDisc: Double,
        totalService: Double?,
        totalSurcharge: Double?,
        totalTaxe: Double?,
        note  : String?,
        otherFee : Double,
        paymentList : List<PaymentOrder>
    ): OrderSummary {
        val totalPaid = paymentList.sumOf { it.Payable }
        val totalGrand = total;
        val balance = totalGrand - totalPaid
        val received = totalPaid
        val change = received - totalGrand;

        return OrderSummary(
            Grandtotal = total,
            GrossPrice = grossPrice,
            Subtotal = subTotal,
            Discount = totalDisc,
            Service = totalService,
            Surcharge = totalSurcharge,
            Tax = totalTaxe ,
            Note = note,
            OtherFee = otherFee,
            Balance = if (balance < 0.0) 0.0 else balance,
            Received = received,
            Change = change,
            PaymentAmount = totalPaid
        )
    }


}