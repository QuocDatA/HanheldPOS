package com.hanheldpos.model.cart

import com.hanheldpos.data.api.pojo.discount.*
import com.hanheldpos.data.api.pojo.fee.Fee
import com.hanheldpos.data.api.pojo.order.settings.Reason
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.OrderHelper
import com.hanheldpos.model.UserHelper
import com.hanheldpos.model.cart.fee.FeeType
import com.hanheldpos.model.discount.DiscountUser
import com.hanheldpos.model.order.*
import com.hanheldpos.model.payment.PaymentOrder
import com.hanheldpos.model.product.ProductType

object CartConverter {

    fun toOrder(cart: CartModel, orderStatus: Int, paymentStatus: Int): OrderReq {
        val subTotal = cart.getSubTotal()
        val total = cart.total()
        val totalCompVoid = cart.totalComp()
        val totalDisc = cart.totalDiscount(subTotal)
        val totalService =
            cart.fees.firstOrNull { fee -> FeeType.fromInt(fee.FeeTypeId) == FeeType.ServiceFee }
                ?.price(subTotal, totalDisc)
        val totalSurcharge =
            cart.fees.firstOrNull { fee -> FeeType.fromInt(fee.FeeTypeId) == FeeType.SurchargeFee }
                ?.price(subTotal, totalDisc)
        val totalTaxes =
            cart.fees.firstOrNull { fee -> FeeType.fromInt(fee.FeeTypeId) == FeeType.TaxFee }
                ?.price(subTotal, totalDisc)

        val totalFees = cart.totalFee(subTotal, totalDisc)
        val grossPrice = cart.totalGross(subTotal, totalDisc)

        val description =
            cart.productsList.map { baseProductInCart -> baseProductInCart.name }.joinToString(",")

        return OrderReq(
            Order = Order(
                OrderStatusId = orderStatus,
                PaymentStatusId = paymentStatus,
                UserGuid = UserHelper.getUserGuid(),
                EmployeeGuid = UserHelper.getEmployeeGuid(),
                LocationGuid = UserHelper.getLocationGuid(),
                DeviceGuid = UserHelper.getDeviceGuid(),
                DiningOptionId = cart.diningOption.Id,
                CreateDate = cart.createDate,
                Code = cart.orderCode,
                MenuLocationGuid = cart.menuLocationGuid,
                CurrencySymbol = OrderHelper.getCurrencySymbol()!!,
                CashDrawer_id = DataHelper.currentDrawerId,
                CustomerGuestGuid = cart.customer?._Id
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
        )
    }

    private fun toProductBuyList(products: List<BaseProductInCart>): List<ProductChosen> {
        val proOrderList = mutableListOf<ProductChosen>()

        products.forEachIndexed { index, baseProductInCart ->

            when (baseProductInCart.productType) {
                ProductType.REGULAR -> {
                    val regular = baseProductInCart as Regular
                    proOrderList.add(
                        regular.toProductChosen(
                            index,
                            regular.quantity!!
                        )
                    )
                }
                ProductType.BUNDLE -> {
                    val bundle = baseProductInCart as Combo
                    proOrderList.add(bundle.toProductChosen(index, null))
                }
                ProductType.NOT_FOUND -> TODO()
                ProductType.GROUP_SKU -> TODO()
                ProductType.BUYX_GETY_DISC -> TODO()
                ProductType.COMBO_DISC -> TODO()
            }
        }
        return proOrderList
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
        paymentList: List<PaymentOrder>?
    ): OrderSummary {
        val totalPaid = paymentList?.sumOf { it.OverPay!! }
        val totalGrand = total
        val balance = if (totalPaid != null) totalGrand - totalPaid else null
        val received = totalPaid
        val change = received?.minus(totalGrand)

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
            Balance = if (balance != null)
                if (balance < 0.0) 0.0
                else balance
            else null,
            Received = received,
            Change = if (change != null)
                if (change < 0.0) 0.0
                else change
            else null,
            PaymentAmount = totalPaid
        )
    }

    private fun toOrderDiscountList(
        discountServers: List<DiscountResp>?,
        discountUsers: List<DiscountUser>?,
        proSubtotal: Double,
        modSubtotal: Double,
        productOriginal_id: String? = null,
        quantity: Int? = 1
    ): List<DiscountOrder> {
        val discountOrders = mutableListOf<DiscountOrder>()

        // Mapping discount form client.
        val disUser = discountUsers?.map { disc ->
            DiscountOrder(
                disc,
                proSubtotal,
                modSubtotal,
                productOriginal_id ?: "",
                quantity ?: 1
            )
        }
        if (disUser != null) {
            discountOrders.addAll(disUser)
        }

        // Mapping discount form client.
        val disServers = discountServers?.map { disc ->
            DiscountOrder(
                disc,
                proSubtotal,
                modSubtotal,
                productOriginal_id ?: "",
                quantity ?: 1
            )
        }
        if (disServers != null) {
            discountOrders.addAll(disServers)
        }
        return discountOrders
    }

    private fun toOrderFeeList(
        fees: List<Fee>,
        feeType: FeeType,
        subtotal: Double?,
        totalDiscounts: Double?
    ): List<OrderFee> {
        return fees.filter { f -> FeeType.fromInt(f.FeeTypeId) == feeType }
            .map { fee -> OrderFee(fee, subtotal ?: 0.0, totalDiscounts ?: 0.0) }
    }

    private fun toOrderCompVoidList(reason: Reason?, totalPrice: Double?): List<CompVoid> {
        val compVoids = mutableListOf<CompVoid>()
        reason ?: return compVoids
        val parentId = DataHelper.orderSettingLocalStorage?.ListVoid?.firstOrNull()?.Id
        val compVoid = CompVoid(reason, parentId, totalPrice)
        compVoids.add(compVoid)
        return compVoids
    }

    fun toOrderCoupon(cart: CartModel, coupon_code: String): CouponDiscountReq {
        return CouponDiscountReq(
            CouponCode = coupon_code,
            UserGuid = DataHelper.deviceCodeLocalStorage?.Device?.firstOrNull()?.UserGuid ?: "",
            LocationGuid = DataHelper.deviceCodeLocalStorage?.Device?.firstOrNull()?.Location!!,
            DeviceGuid = DataHelper.deviceCodeLocalStorage?.Device?.firstOrNull()?._Id!!,
            DeliveryTypeId = cart.diningOption.Id,
            CustomerGuestGuid = cart.customer?._Id,
            DiscountUsed = toDiscountUsedList(cart.discountServerList),
            OrderDetail = toOrderDetailCoupon(cart.productsList),
        )
    }

    private fun toDiscountUsedList(discServerList: List<DiscountResp>): List<DiscountUsed> {
        val discUsedList: MutableList<DiscountUsed> = mutableListOf()
        discServerList.filter { disc -> !disc.DiscountAutomatic }.toList().forEach { coupon ->
            discUsedList.add(
                DiscountUsed(DiscountGuid = coupon._id, DiscountCode = coupon.DiscountCode)
            )
        }
        return discUsedList.toList()
    }

    private fun toOrderDetailCoupon(productList: List<BaseProductInCart>): List<OrderDetailPostCoupon> {
        val detailList: MutableList<OrderDetailPostCoupon> = mutableListOf()
        productList
            .forEachIndexed { index,baseProduct ->
                var baseExtraGuid = "Extra/${index + 1}"
                when (baseProduct.productType) {
                    ProductType.BUNDLE -> {
                        val bundle = baseProduct as Combo
                        detailList.add(
                            OrderDetailPostCoupon(
                                ProductsBuy = toProductBuyCouponList(
                                    bundle,
                                    baseExtraGuid,
                                    bundle.productType
                                )
                            )
                        )
                    }
                    ProductType.REGULAR -> {
                        val regular = baseProduct as Regular
                        baseExtraGuid =
                            if (regular.modifierList.isNullOrEmpty() || !regular.modifierList.any()) "" else baseExtraGuid
                        detailList.add(
                            OrderDetailPostCoupon(
                                ListLineExtra = toExtraList(
                                    regular.modifierList,
                                    baseExtraGuid
                                ),
                                ProductsBuy = toProductBuyCouponList(
                                    regular,
                                    baseExtraGuid,
                                    regular.productType
                                )
                            )
                        )

                    }
                    else -> {}
                }
            }
        return detailList
    }

    private fun toExtraList(
        modifier: List<ModifierCart>,
        extraGuid: String
    ): List<ListLineExtraPostCoupon> {
        return modifier.map { v ->
            ListLineExtraPostCoupon(
                ModifierGuid = v.modifierId,
                Quantity = v.quantity,
                ExtraId = extraGuid
            )
        }.toList()
    }

    private fun toProductBuyCouponList(
        baseProduct: BaseProductInCart,
        extraGuid: String,
        type: ProductType
    ): List<ProductsBuyPostCoupon> {
        return listOf(toProductCoupon(baseProduct, extraGuid, type))
    }

    private fun toProductCoupon(
        baseProduct: BaseProductInCart,
        extraGuid: String,
        type: ProductType
    ): ProductsBuyPostCoupon {
        return ProductsBuyPostCoupon(
            ExtraId = extraGuid,
            Active = 0,
            ProductGuid = baseProduct.proOriginal!!._id,
            Note = baseProduct.note,
            Quantity = baseProduct.quantity,
            OrderDetailType = type.value,
            DiscountUsed = codesUsedDiscountCoupon(baseProduct.discountServersList),
            Variants = baseProduct.variants!!,
        )
    }

    private fun codesUsedDiscountCoupon(discountServer: List<DiscountResp>?): List<DiscountUsed> {
        return discountServer?.filter { disc -> !disc.DiscountAutomatic }?.toList()
            ?.map { discCoupon ->
                DiscountUsed(
                    DiscountGuid = discCoupon._id,
                    DiscountCode = discCoupon.DiscountCode!!
                )
            }?.toList() ?: listOf()
    }
}