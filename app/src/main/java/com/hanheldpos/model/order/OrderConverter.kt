package com.hanheldpos.model.order

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hanheldpos.data.api.pojo.fee.Fee
import com.hanheldpos.data.api.pojo.fee.FeeAssignToProductItem
import com.hanheldpos.data.api.pojo.order.menu.getProductByListProduct
import com.hanheldpos.data.api.pojo.order.menu.getProductImage
import com.hanheldpos.data.api.pojo.order.settings.DiningOptionItem
import com.hanheldpos.data.api.pojo.order.settings.Reason
import com.hanheldpos.data.api.pojo.product.ProductItem
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.cart.CartModel
import com.hanheldpos.model.cart.Combo
import com.hanheldpos.model.cart.GroupBundle
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.cart.fee.FeeType
import com.hanheldpos.model.discount.DiscountServer
import com.hanheldpos.model.discount.DiscountUser
import com.hanheldpos.model.product.BaseProductInCart
import com.hanheldpos.model.product.ProductComboItem
import com.hanheldpos.model.product.ProductType
import java.net.URI

object OrderConverter {
    public fun toCart(orderModel: OrderModel, orderGuid: String): CartModel {
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
            diningOption = DataHelper.getDiningOptionItem(order.DiningOptionId ?: 0)!!,
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
                compVoidGuid = comp.CompVoidGuid,
                compVoidValue = comp.CompVoidValue,
                visible = 1,
                title = comp.CompVoidTitle,
                id = comp.CompVoidGroupId,
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
        orderProducts.forEachIndexed { index, productOrder ->
            val productBuy = productOrder
            val diningOption = DataHelper.getDiningOptionItem(productBuy.DiningOption?.Id ?: 0)!!

            val compReason = toReasonComp(productBuy.CompVoidList!!)
            val discountServerList = toDiscountsServer(productBuy.DiscountList!!)
            val discountUserList = toDiscountsUser(productBuy.DiscountList!!)
            val feeList = toFeeList(
                serviceFeeList = productBuy.ServiceFeeList,
                surchargeFeeList = productBuy.SurchargeFeeList,
                shippingFeeList = productBuy.ShippingFeeList,
                taxesFeeList = productBuy.TaxFeeList
            )

            when (ProductType.values()[productBuy.ProductTypeId]) {
                ProductType.REGULAR -> run {
                    val proOriginal = findProduct(productBuy._id)
                    if (proOriginal == null)
                        return@run
                    proOriginal.url = findUrlProduct(proOriginal.id).toString()
                    productList.add(
                        toRegular(
                            productBuy = productBuy,
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
                    val productBundleOriginal = findProduct(productBuy._id)
                    val comboList: List<ProductComboItem> = Gson().fromJson(
                        productBundleOriginal?.combo,
                        object : TypeToken<List<ProductComboItem>>() {}.type
                    )
                    if (!comboList.any())
                        return@run
                    val groupBundleList: List<GroupBundle> = listOf()
                    comboList.forEachIndexed { index, comboInfo ->

                        val listProduct: MutableList<Regular> = mutableListOf()
                        productOrder.ProductChoosedList?.filter { it.Parent_id.equals(comboInfo.comboGuid) }
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
                    val priceOverride = productBundleOriginal?.priceOverride(
                        menuLocation_id,
                        productBuy.Sku,
                        productBundleOriginal.price
                    )
                    productList.add(
                        Combo(
                            productBundleOriginal!!,
                            groupBundleList,
                            diningOption,
                            productBuy.Quantity,
                            productBuy.Sku,
                            productBuy.Variant,
                            priceOverride,
                            feeList
                        ).apply {
                            this.compReason = compReason
                            this.discountServersList = discountServerList as MutableList<DiscountServer>
                            this.discountUsersList = discountUserList as MutableList<DiscountUser>; }
                    )
                }

            }
        }
        return productList
    }

    private fun findUrlProduct(productId: String): URI {
        // TODO: check this fun logic
        return URI.create(DataHelper.orderMenuResp?.getProductImage()?.firstOrNull {
            it?.id == productId
        }?.url)
    }

    private fun findProduct(productGuid: String): ProductItem? {
        return DataHelper.orderMenuResp?.getProductByListProduct()?.firstOrNull {
            it.id == productGuid
        }
    }

    private fun toRegular(
        productBuy: ProductBuy,
        proOriginal: ProductItem,
        diningOption: DiningOptionItem,
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
            productBuy.Price,
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
                feeApplyToType = fee.FeeType,
                feeType = FeeType.ServiceFee,
                value = fee.FeeValue,
                name = fee.FeeName,
                total = fee.TotalPrice,
                assignToProducts = assignToProductList
            )
        } ?: mutableListOf())

        fees.addAll(surchargeFeeList?.map { fee ->
            Fee(
                _id = fee._id,
                feeApplyToType = fee.FeeType,
                feeType = FeeType.SurchargeFee,
                value = fee.FeeValue,
                name = fee.FeeName,
                total = fee.TotalPrice,
                assignToProducts = assignToProductList
            )
        } ?: mutableListOf())

        fees.addAll(taxesFeeList?.map { fee ->
            Fee(
                _id = fee._id,
                feeApplyToType = fee.FeeType,
                feeType = FeeType.TaxFee,
                value = fee.FeeValue,
                name = fee.FeeName,
                total = fee.TotalPrice,
                assignToProducts = assignToProductList
            )
        } ?: mutableListOf())

        fees.addAll(shippingFeeList?.map { fee ->
            Fee(
                _id = fee._id,
                feeApplyToType = fee.FeeType,
                feeType = FeeType.ShippingFee,
                value = fee.FeeValue,
                name = fee.FeeName,
                total = fee.TotalPrice,
                assignToProducts = assignToProductList
            )
        } ?: mutableListOf())

        return fees
    }
}