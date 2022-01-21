package com.hanheldpos.model.cart

import com.hanheldpos.data.api.pojo.fee.Fee
import com.hanheldpos.data.api.pojo.order.settings.Reason
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.UserHelper
import com.hanheldpos.model.cart.fee.FeeType
import com.hanheldpos.model.cart.payment.PaymentOrder
import com.hanheldpos.model.discount.DiscountServer
import com.hanheldpos.model.discount.DiscountUser
import com.hanheldpos.model.order.*
import com.hanheldpos.model.product.BaseProductInCart
import com.hanheldpos.model.product.ProductType
import com.hanheldpos.utils.time.DateTimeHelper
import java.util.*

object CartConverter {

    fun toOrder(cart: CartModel, orderStatus: Int, paymentStatus: Int): OrderModel {
        val subTotal = cart.getSubTotal();
        val total = cart.total();
        val totalCompVoid = cart.totalComp();
        val totalDisc = cart.totalDiscount(subTotal);
        val totalService = cart.fees.firstOrNull { fee -> fee.FeeTypeId == FeeType.ServiceFee }
            ?.price(subTotal, totalDisc);
        val totalSurcharge = cart.fees.firstOrNull { fee -> fee.FeeTypeId == FeeType.SurchargeFee }
            ?.price(subTotal, totalDisc);
        val totalTaxes = cart.fees.firstOrNull { fee -> fee.FeeTypeId == FeeType.TaxFee }
            ?.price(subTotal, totalDisc);

        val totalFees = cart.totalFee(subTotal, totalDisc);
        val grossPrice = cart.totalGross(subTotal, totalDisc);

        val description =
            cart.productsList.map { baseProductInCart -> baseProductInCart.name }.joinToString(",");

        // TODO : save temp order to local
        cart.createDate =
            DateTimeHelper.dateToString(Date(), DateTimeHelper.Format.FULL_DATE_UTC_TIMEZONE);

        return OrderModel(
            Order = Order(
                OrderStatusId = orderStatus,
                PaymentStatusId = paymentStatus,
                UserGuid = UserHelper.getUserGui(),
                EmployeeGuid = UserHelper.getEmployeeGuid(),
                LocationGuid = UserHelper.getLocationGui(),
                DeviceGuid = UserHelper.getDeviceGui(),
                DiningOptionId = cart.diningOption.Id,
                CreateDate = cart.createDate,
                Code = cart.orderCode,
                MenuLocationGuid = cart.menuLocationGuid,
                CurrencySymbol = DataHelper.getCurrencySymbol()!!,
                CashDrawer_id = DataHelper.currentDrawerId,
            ),
            OrderDetail = OrderDetail(
                DiningOption = OrderDiningOption(
                    Id = cart.diningOption.Id ?: 0,
                    TypeId = cart.diningOption.TypeId ?: 0,
                    Title = cart.diningOption.Title,
                    Acronymn = cart.diningOption.Acronymn
                ),
                DeliveryTime = cart.deliveryTime,
                TableList = mutableListOf(cart.table),
                Billing = cart.customer,
                Shipping = cart.shipping,

                DiscountList = toOrderDiscountList(
                    cart.discountServerList,
                    cart.discountUserList,
                    subTotal,
                    0.0
                ),
                ServiceFeeList = toOrderFeeList(
                    cart.fees,
                    FeeType.ServiceFee,
                    subTotal,
                    totalDisc
                ),
                SurchargeFeeList = toOrderFeeList(
                    cart.fees,
                    FeeType.SurchargeFee,
                    subTotal,
                    totalDisc
                ),
                TaxFeeList = toOrderFeeList(
                    cart.fees,
                    FeeType.TaxFee,
                    subTotal,
                    totalDisc
                ),
                CompVoidList = toOrderCompVoidList(cart.compReason, totalCompVoid),
                OrderProducts = toProductBuyList(cart.productsList),
                PaymentList = cart.paymentsList,
                Order = calOrderSummary(
                    total = total,
                    grossPrice = grossPrice,
                    subTotal = subTotal,
                    totalDisc = totalDisc,
                    totalService = totalService,
                    totalSurcharge = totalSurcharge,
                    totalTaxe = totalTaxes,
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
                DiningOptionId = cart.diningOption.Id,
                DiningOptionName = cart.diningOption.Title,
                TableName = cart.table.TableName,
                CreateDate = cart.createDate!!
            )
        );
    }

    private fun toProductBuyList(products: List<BaseProductInCart>): List<ProductBuy> {
        val proOrderList = mutableListOf<ProductBuy>();

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
                    val bundle = baseProductInCart as Combo;
                    proOrderList.add(OrderMapper.mappingProductBuy(bundle, index, null));
                }
                ProductType.NOT_FOUND -> TODO()
                ProductType.GROUP_SKU -> TODO()
                ProductType.BUYX_GETY_DISC -> TODO()
                ProductType.COMBO_DISC -> TODO()
            }
        }
        return proOrderList;
    }

    private fun calOrderSummary(
        total: Double,
        grossPrice: Double,
        subTotal: Double,
        totalDisc: Double,
        totalService: Double?,
        totalSurcharge: Double?,
        totalTaxe: Double?,
        note: String?,
        otherFee: Double,
        paymentList: List<PaymentOrder>
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
            Tax = totalTaxe,
            Note = note,
            OtherFee = otherFee,
            Balance = if (balance < 0.0) 0.0 else balance,
            Received = received,
            Change = change,
            PaymentAmount = totalPaid
        )
    }

    private fun toOrderDiscountList(
        discountServers: List<DiscountServer>?,
        discountUsers: List<DiscountUser>?,
        proSubtotal: Double,
        modSubtotal: Double,
        productOriginal_id: String? = null,
        quantity: Int? = 1
    ): List<DiscountOrder> {
        val discountOrders = mutableListOf<DiscountOrder>();

        // Mapping discount form client.
        val disUser = discountUsers?.map { disc ->
            DiscountOrder(
                disc,
                proSubtotal,
                modSubtotal,
                productOriginal_id ?: "",
                quantity ?: 1
            );
        }
        if (disUser != null) {
            discountOrders.addAll(disUser);
        }
        return discountOrders;
    }

    private fun toOrderFeeList(
        fees: List<Fee>,
        feeType: FeeType,
        subtotal: Double?,
        totalDiscounts: Double?
    ): List<OrderFee> {
        return fees.filter { f -> f.FeeTypeId == feeType }
            .map { fee -> OrderFee(fee, subtotal ?: 0.0, totalDiscounts ?: 0.0) }
    }

    private fun toOrderCompVoidList(reason: Reason?, totalPrice: Double?): List<CompVoid> {
        val compVoids = mutableListOf<CompVoid>();
        reason ?: return compVoids;
        val parentId = DataHelper.getVoidInfo()?.Id;
        val compVoid = CompVoid(reason, parentId, totalPrice);
        compVoids.add(compVoid);
        return compVoids;
    }
}