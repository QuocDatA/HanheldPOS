package com.hanheldpos.model.order

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hanheldpos.data.api.pojo.fee.Fee
import com.hanheldpos.data.api.pojo.fee.FeeAssignToProductItem
import com.hanheldpos.data.api.pojo.order.settings.DiningOption
import com.hanheldpos.data.api.pojo.order.settings.Reason
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.OrderHelper
import com.hanheldpos.model.cart.CartModel
import com.hanheldpos.model.cart.Combo
import com.hanheldpos.model.cart.GroupBundle
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.cart.fee.FeeType
import com.hanheldpos.model.discount.DiscountServer
import com.hanheldpos.model.discount.DiscountUser
import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.model.product.ProductComboItem
import com.hanheldpos.model.product.ProductType

object OrderConverter {

    public fun toCart(orderModel: OrderReq, orderGuid: String): CartModel {
        val orderPayment = orderModel.OrderDetail.PaymentList;
        val orderData = orderModel.OrderDetail;
        val order = orderModel.Order;

        val model = CartModel(
            order = orderModel.Order,
            compReason = toReasonComp(orderData.CompVoidList),
            paymentsList = orderPayment.toMutableList(),
            customer = orderData.Billing,
            shipping = orderData.Shipping,
            table = orderData.TableList.firstOrNull()!!,
            deliveryTime = orderData.DeliveryTime,
            diningOption = OrderHelper.getDiningOptionItem(order.DiningOptionId ?: 0)!!,
            orderGuid = orderGuid,
            createDate = order.CreateDate,
            orderCode = order.Code,
            menuLocationGuid = order.MenuLocationGuid,
            fees = toFeeList(
                serviceFeeList = orderData.ServiceFeeList,
                shippingFeeList = orderData.ShippingFeeList,
                surchargeFeeList = orderData.SurchargeFeeList,
                taxesFeeList = orderData.TaxFeeList,
            ),
            discountServerList = toDiscountsServer(orderData.DiscountList) as MutableList<DiscountServer>,
            discountUserList = toDiscountsUser(orderData.DiscountList) as MutableList<DiscountUser>,
            productsList = toProductList(orderData.OrderProducts, order.MenuLocationGuid!!),
        )
        return model
    }

    private fun toReasonComp(compVoids: List<CompVoid>): Reason? {
        return compVoids.map { comp ->
            Reason(
                CompVoidGuid = comp.CompVoidGuid,
                CompVoidValue = comp.CompVoidValue,
                Visible = 1,
                Title = comp.CompVoidTitle,
                Id = comp.CompVoidGroupId,
            )
        }.firstOrNull()
    }

    private fun toDiscountsUser(orderDiscounts: List<DiscountOrder>): List<DiscountUser> {
        return orderDiscounts.filter { it._id.isEmpty() }.toList()
            .map { dc ->
                DiscountUser(
                    DiscountGuid = dc._id,
                    DiscountType = dc.DiscountType,
                    DiscountName = dc.DiscountName,
                    DiscountValue = dc.DiscountValue
                )
            }
    }

    private fun toProductList(
        orderProducts: List<ProductBuy>,
        menuLocation_id: String
    ): MutableList<BaseProductInCart> {
        val productList: MutableList<BaseProductInCart> = mutableListOf()
        orderProducts.forEachIndexed { _, productOrder ->
            val diningOption = OrderHelper.getDiningOptionItem(productOrder.DiningOption?.Id ?: 0)!!

            val compReason = toReasonComp(productOrder.CompVoidList!!)
            val discountServerList = toDiscountsServer(productOrder.DiscountList!!)
            val discountUserList = toDiscountsUser(productOrder.DiscountList)
            val feeList = toFeeList(
                serviceFeeList = productOrder.ServiceFeeList,
                surchargeFeeList = productOrder.SurchargeFeeList,
                shippingFeeList = productOrder.ShippingFeeList,
                taxesFeeList = productOrder.TaxFeeList
            )

            when (ProductType.values()[productOrder.ProductTypeId]) {
                ProductType.REGULAR -> run {
                    val proOriginal = findProduct(productOrder._id) ?: return@run
                    productList.add(
                        toRegular(
                            productBuy = productOrder,
                            proOriginal = proOriginal,
                            diningOption = diningOption,
                            compReason = compReason!!,
                            discountUserList = discountUserList,
                            discountServerList = discountServerList,
                            feeList = feeList
                        )
                    )
                }
                ProductType.BUNDLE -> run {
                    val productBundleOriginal = findProduct(productOrder._id)
                    val comboList: List<ProductComboItem> = Gson().fromJson(
                        productBundleOriginal?.Combo,
                        object : TypeToken<List<ProductComboItem>>() {}.type
                    )
                    if (!comboList.any())
                        return@run
                    val groupBundleList: List<GroupBundle> = listOf()
                    comboList.forEachIndexed { index, comboInfo ->

                        val listProduct: MutableList<Regular> = mutableListOf()
                        productOrder.ProductChoosedList?.filter { it.Parent_id.equals(comboInfo.ComboGuid) }
                            ?.toList()?.forEach { productChoosed ->
                                val productChoosedOriginal = findProduct(productChoosed._id)
                                listProduct.add(
                                    toRegular(
                                        productBuy = productChoosed,
                                        proOriginal = productChoosedOriginal!!,
                                        diningOption = diningOption,
                                        compReason = null,
                                        discountUserList = emptyList(),
                                        discountServerList = emptyList(),
                                        feeList = emptyList()
                                    )
                                )
                            }
                    }
                    productList.add(
                        Combo(
                            productBundleOriginal!!,
                            groupBundleList,
                            diningOption,
                            productOrder.Quantity,
                            productOrder.Sku,
                            productOrder.Variant,
                            feeList
                        ).apply {
                            this.compReason = compReason
                            this.discountServersList = discountServerList as MutableList<DiscountServer>
                            this.discountUsersList = discountUserList as MutableList<DiscountUser>; }
                    )
                }

                else -> {}
            }
        }
        return productList
    }


    private fun findProduct(productGuid: String): Product? {
        return DataHelper.menuLocalStorage?.ProductList?.firstOrNull {
            it._id == productGuid
        }
    }

    private fun toRegular(
        productBuy: ProductBuy,
        proOriginal: Product,
        diningOption: DiningOption,
        compReason: Reason?,
        discountUserList: List<DiscountUser>,
        discountServerList: List<DiscountServer>,
        feeList: List<Fee>,
    ): Regular {
        return Regular(
            proOriginal,
            diningOption,
            productBuy.Quantity,
            productBuy.Sku,
            productBuy.Variant,
            feeList
        ).apply {
            this.compReason = compReason
            this.discountServersList = discountServerList as MutableList<DiscountServer>
            this.discountUsersList = discountUserList as MutableList<DiscountUser>
        }


    }

    private fun toDiscountsServer(orderDiscounts: List<DiscountOrder>): List<DiscountServer> {
        return listOf()
    }

    private fun toFeeList(
        serviceFeeList: List<OrderFee>?,
        surchargeFeeList: List<OrderFee>?,
        taxesFeeList: List<OrderFee>?,
        shippingFeeList: List<OrderFee>?
    ): List<Fee> {
        val fees: MutableList<Fee> = mutableListOf()
        val assignToProductList: List<FeeAssignToProductItem> = listOf()

        fees.addAll(serviceFeeList?.map { fee ->
            Fee(
                _id = fee._id,
                Id = fee.FeeType,
                FeeTypeId = FeeType.ServiceFee,
                Value = fee.FeeValue,
                Name = fee.FeeName,
                Total = fee.TotalPrice,
                AssignToProductList = assignToProductList
            )
        } ?: mutableListOf())

        fees.addAll(surchargeFeeList?.map { fee ->
            Fee(
                _id = fee._id,
                Id = fee.FeeType,
                FeeTypeId = FeeType.SurchargeFee,
                Value = fee.FeeValue,
                Name = fee.FeeName,
                Total = fee.TotalPrice,
                AssignToProductList = assignToProductList
            )
        } ?: mutableListOf())

        fees.addAll(taxesFeeList?.map { fee ->
            Fee(
                _id = fee._id,
                Id = fee.FeeType,
                FeeTypeId = FeeType.TaxFee,
                Value = fee.FeeValue,
                Name = fee.FeeName,
                Total = fee.TotalPrice,
                AssignToProductList = assignToProductList
            )
        } ?: mutableListOf())

        fees.addAll(shippingFeeList?.map { fee ->
            Fee(
                _id = fee._id,
                Id = fee.FeeType,
                FeeTypeId = FeeType.ShippingFee,
                Value = fee.FeeValue,
                Name = fee.FeeName,
                Total = fee.TotalPrice,
                AssignToProductList = assignToProductList
            )
        } ?: mutableListOf())

        return fees
    }
}