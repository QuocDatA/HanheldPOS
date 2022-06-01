package com.hanheldpos.model.order

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.api.pojo.fee.Fee
import com.hanheldpos.data.api.pojo.fee.FeeAssignToProductItem
import com.hanheldpos.data.api.pojo.order.settings.DiningOption
import com.hanheldpos.data.api.pojo.order.settings.Reason
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.OrderHelper
import com.hanheldpos.model.cart.*
import com.hanheldpos.model.cart.fee.FeeType
import com.hanheldpos.model.discount.DiscountUser
import com.hanheldpos.model.product.ProductComboItem
import com.hanheldpos.model.product.ProductType

object OrderConverter {

    fun toCart(orderModel: OrderModel, orderGuid: String?): CartModel {
        val orderPayment = orderModel.OrderDetail.PaymentList;
        val orderData = orderModel.OrderDetail;
        val order = orderModel.Order;

        return CartModel(
            order = orderModel.Order,
            compReason = toReasonComp(orderData.CompVoidList),
            paymentsList = orderPayment?.toMutableList(),
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
            discountServerList = toDiscountsServer(orderData.DiscountList).toMutableList(),
            discountUserList = toDiscountsUser(orderData.DiscountList).toMutableList(),
            productsList = toProductList(orderData.OrderProducts, order.MenuLocationGuid!!),
            note = orderData.Order.Note,
        )
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
        productList: List<ProductChosen>,
        menuLocationId: String
    ): MutableList<BaseProductInCart> {
        val baseProductList: MutableList<BaseProductInCart> = mutableListOf()
        productList.forEachIndexed { _, productBuy ->
            val diningOption = OrderHelper.getDiningOptionItem(productBuy.DiningOption?.Id ?: 0)!!

            val compReason = toReasonComp(productBuy.CompVoidList!!)
            val discountServerList = toDiscountsServer(productBuy.DiscountList!!)
            val discountUserList = toDiscountsUser(productBuy.DiscountList)
            val feeList = toFeeList(
                serviceFeeList = productBuy.ServiceFeeList,
                surchargeFeeList = productBuy.SurchargeFeeList,
                shippingFeeList = productBuy.ShippingFeeList,
                taxesFeeList = productBuy.TaxFeeList
            )

            when (ProductType.values()[productBuy.ProductTypeId]) {
                ProductType.REGULAR -> run {
                    val proOriginal = findProduct(productBuy._id!!) ?: return@run
                    proOriginal.AsignTo = OrderType.REGULAR.value
                    baseProductList.add(
                        toRegular(
                            productBuy = productBuy,
                            proOriginal = proOriginal,
                            diningOption = diningOption,
                            compReason = compReason,
                            discountUserList = discountUserList,
                            discountServerList = discountServerList,
                            feeList = feeList
                        )
                    )
                }
                ProductType.BUNDLE -> run {
                    val proOriginal = findProduct(productBuy._id!!)
                    val comboList: List<ProductComboItem> = Gson().fromJson(
                        proOriginal?.Combo,
                        object : TypeToken<List<ProductComboItem>>() {}.type
                    )
                    if (comboList.isNullOrEmpty())
                        return@run
                    val groupBundleList = mutableListOf<GroupBundle>()
                    comboList.forEachIndexed { index, comboInfo ->
                        val listProduct: MutableList<Regular> = mutableListOf()
                        productBuy.ProductChoosedList?.filter { it.Parent_id.equals(comboInfo.ComboGuid) }
                            ?.toList()?.forEach { productChosen ->
                                val productChosenOriginal = findProduct(productChosen._id!!)
                                listProduct.add(
                                    toRegular(
                                        productBuy = productChosen,
                                        proOriginal = productChosenOriginal!!,
                                        diningOption = diningOption,
                                        compReason = null,
                                        discountUserList = emptyList(),
                                        discountServerList = emptyList(),
                                        feeList = emptyList()
                                    )
                                )
                            }
                        groupBundleList.add(
                            GroupBundle(
                                comboInfo = comboInfo,
                                productList = listProduct
                            )
                        )
                    }
                    baseProductList.add(
                        toBundle(
                            productBuy = productBuy,
                            proOriginal = proOriginal!!,
                            groupBundleList = groupBundleList,
                            diningOption = diningOption,
                            compReason = compReason,
                            discountUserList = discountUserList,
                            discountServerList = discountServerList,
                            feeList = feeList
                        )
                    )
                }

                else -> {}
            }
        }
        return baseProductList
    }


    private fun findProduct(productGuid: String): Product? {
        return DataHelper.menuLocalStorage?.ProductList?.firstOrNull {
            it._id == productGuid
        }
    }

    private fun toRegular(
        productBuy: ProductChosen,
        proOriginal: Product,
        diningOption: DiningOption,
        compReason: Reason?,
        discountUserList: List<DiscountUser>,
        discountServerList: List<DiscountResp>,
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
            this.discountServersList = discountServerList.toMutableList()
            this.discountUsersList = discountUserList.toMutableList()
            this.productType = ProductType.REGULAR
            this.modifierList = toModifierCartList(productBuy.ModifierList, proOriginal.Modifier!!)
            this.note = productBuy.Note
        }
    }

    private fun toBundle(
        productBuy: ProductChosen,
        proOriginal: Product,
        groupBundleList: List<GroupBundle>,
        diningOption: DiningOption,
        compReason: Reason?,
        discountUserList: List<DiscountUser>,
        discountServerList: List<DiscountResp>,
        feeList: List<Fee>,
    ): Combo {
        return Combo(
            proOriginal,
            groupBundleList,
            diningOption,
            productBuy.Quantity,
            productBuy.Sku,
            productBuy.Variant,
            feeList
        ).apply {
            this.compReason = compReason
            this.productType = ProductType.BUNDLE
            this.discountServersList = discountServerList.toMutableList()
            this.discountUsersList = discountUserList.toMutableList()
            this.note = productBuy.Note
        }
    }

    private fun toDiscountsServer(orderDiscounts: List<DiscountOrder>): List<DiscountResp> {
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
                FeeTypeId = FeeType.ServiceFee.value,
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
                FeeTypeId = FeeType.SurchargeFee.value,
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
                FeeTypeId = FeeType.TaxFee.value,
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
                FeeTypeId = FeeType.ShippingFee.value,
                Value = fee.FeeValue,
                Name = fee.FeeName,
                Total = fee.TotalPrice,
                AssignToProductList = assignToProductList
            )
        } ?: mutableListOf())

        return fees
    }

    private fun toModifierCartList(
        listLineExtra: List<OrderModifier>?,
        modifierString: String
    ): MutableList<ModifierCart> {
        if (listLineExtra == null) {
            return mutableListOf()
        }

        return listLineExtra.map { extra ->
            ModifierCart(
                modifierId = extra.ModifierItemGuid,
                name = extra.Name,
                price = extra.Price,
                quantity = extra.ModifierQuantity ?: 1
            )
        }.toMutableList()
    }
}