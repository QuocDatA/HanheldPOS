package com.hanheldpos.model.cart

import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.api.pojo.fee.Fee
import com.hanheldpos.data.api.pojo.order.settings.DiningOption
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.model.cart.fee.FeeApplyToType
import com.hanheldpos.model.cart.fee.FeeType
import com.hanheldpos.model.order.OrderDiningOption
import com.hanheldpos.model.order.ProductChosen
import com.hanheldpos.model.product.PricingMethodType
import com.hanheldpos.model.product.ProductType


class Combo() : BaseProductInCart() {

    var groupList: MutableList<GroupBundle> = mutableListOf()
    var isShowDetail: Boolean = false

    constructor(
        productItem: Product,
        groupProducts: List<GroupBundle>,
        diningOptionItem: DiningOption,
        quantity: Int?,
        sku: String?,
        variants: String?,
        fees: List<Fee>?
    ) : this() {
        this.proOriginal = productItem
        this.groupList = groupProducts.toMutableList()
        this.diningOption = diningOptionItem
        this.quantity = quantity
        this.sku = sku
        this.variants = variants
        this.fees = fees
    }

    init {
        this.productType = ProductType.BUNDLE
    }

    override fun getProductName(): String? {
        return proOriginal?.Name
    }

    override fun getFeeString(): String {
        return ""
    }

    override fun totalFee(): Double {
        return totalFee(subTotal(), totalDiscount())
    }

    override fun subTotal(): Double {
        val proModSubtotal = proModSubTotal() * (quantity ?: 0)
        return proModSubtotal
    }

    override fun totalDiscount(): Double {
        val subtotal = subTotal()
        val totalModifierPrice = totalModifier()

        var totalPrice = subtotal - totalModifierPrice

        val totalDiscUser = discountUsersList?.sumOf { disc -> disc.total(subtotal) } ?: 0.0
        // TODO : Discount server
        //  var totalDiscServer = discountServersList?.sumOf { disc -> disc.total(totalPrice, totalModifierPrice, proOriginal?.id, quantity) } ?: 0.0

        val total = totalDiscUser// + totalDiscServer
        return total
    }

    override fun total(): Double {
        val totalTemp = totalTemp()
        val lineTotal = totalTemp - totalComp(totalTemp)
        return lineTotal
    }

    override fun totalComp(): Double {
        val totalTemp = totalTemp()
        val totalcomp = compReason?.total(totalTemp) ?: 0.0
        return totalcomp
    }

    override fun totalPriceUsed(discount: DiscountResp): Double {
        val subtotal = this.subTotal()
        val totalModifierPrice = this.totalModifier()
        val totalPrice = subtotal - totalModifierPrice
        if (this.discountServersList.isNullOrEmpty()) return 0.0
        return this.discountServersList!!.filter { disc ->
            disc._id == discount._id
        }.toList().sumOf { disc ->
            disc.total(
                totalPrice,
                totalModifierPrice,
                this.proOriginal?._id,
                this.quantity
            ) ?: 0.0
        }
    }

    override fun totalQtyUsed(discount: DiscountResp, product_id: String): Int {
        return if (this.proOriginal?._id != product_id) 0
        else this.discountServersList!!.filter { disc ->
            disc._id == discount._id
        }.sumOf { disc -> disc.quantityUsed ?: 0 }
    }

    override fun compareValue(productDiscList: List<Product>): Double {
        val modSubTotal = this.modSubTotal()
        val proModSubTotal = this.proModSubTotal()
        val isApplyToMod = productDiscList.firstOrNull { p ->
            p._id == this.proOriginal?._id
        }?.ApplyToModifier == 1
        return if (isApplyToMod) proModSubTotal else (proModSubTotal - modSubTotal)
    }

    fun totalModifier(): Double {
        val totalModifier = modSubTotal() * quantity!!
        return totalModifier
    }

    fun totalComp(totalTemp: Double): Double {
        return compReason?.total(totalTemp) ?: 0.0
    }

    override fun isCompleted(): Boolean {
        return groupList.firstOrNull { gr -> !gr.isComplete() } == null
    }

    override fun isMatching(productItem: Product): Boolean {
        return false
    }

    override fun clone(): Combo {
        val cloneValue = Combo(
            proOriginal!!,
            groupList.toMutableList().map { it.clone() },
            diningOption!!,
            quantity,
            sku,
            variants,
            fees
        )

        cloneValue.compReason = this.compReason
        cloneValue.discountUsersList = this.discountUsersList?.map { it.copy() }?.toMutableList()
        cloneValue.discountServersList =
            this.discountServersList?.map { it.copy() }?.toMutableList()
        cloneValue.note = this.note

        return cloneValue
    }

    fun modSubTotal(): Double {
        val modSubtotal = groupList.sumOf { group ->
            group.productList.sumOf { regular ->
                regular.totalModifier(proOriginal!!)
            }
        }
        return modSubtotal
    }

    fun groupSubTotal(): Double {
        var groupSubtotal = 0.0
        groupList.forEach { group ->
            group.productList.forEach { regular ->
                groupSubtotal += regular.totalGroupPrice(group, proOriginal!!)
            }
        }
        return groupSubtotal
    }

    fun proModSubTotal(): Double {
        val pricingMethodType = proOriginal?.PricingMethodType ?: 1

        val modSubtotal = modSubTotal()
        val proSubtotal = priceOverride ?: 0.0

        return when (PricingMethodType.fromInt(pricingMethodType)) {
            PricingMethodType.BasePrice -> proSubtotal.plus(modSubtotal)
            PricingMethodType.GroupPrice -> {
                val groupSubtotal = groupSubTotal()
                return proSubtotal + modSubtotal + groupSubtotal
            }
            else -> proSubtotal.plus(modSubtotal)
        }
    }

    fun totalFee(subtotal: Double, totalDisc: Double): Double {
        return fees?.filter { it.Id != FeeApplyToType.Included.value }
            ?.sumOf { it.price(subtotal, totalDisc) } ?: 0.0
    }

    private fun totalTemp(): Double {
        val subtotal = subTotal()
        val totalDiscPrice = totalDiscount()
        val totalFeePrice = totalFee(subtotal, totalDiscPrice)

        var total = subtotal - totalDiscPrice + totalFeePrice
        total = if (total < 0) 0.0 else total
        return total
    }

    fun plusOrderQuantity(num: Int) {
        quantity = (quantity ?: 0).plus(num)
    }

    fun minusOrderQuantity(num: Int) {
        quantity = if (quantity!! > 0) quantity!!.minus(num) else 0
    }

    fun toProductChosen(
        indexOfList: Int,
        parentName: String? = null,
        parentIndex: Int = 0
    ): ProductChosen {
        val proChooseList = mutableListOf<ProductChosen>()
        val totalModifier = totalModifier()
        val subtotal = subTotal()
        val lineTotal = total()
        val totalCompVoid = totalComp()

        var orderDetailId = 0

        groupList.forEachIndexed { index, groupBundle ->
            groupBundle.productList.forEachIndexed { _, regular ->
                val proChoose = regular.toProductChosen(
                    proOriginal!!,
                    groupBundle,
                    index,
                    orderDetailId
                )
                proChooseList.add(proChoose)
                orderDetailId++
            }
        }

        val totalProductChosenList = proChooseList.sumOf { pro -> pro.LineTotal ?: 0.0 }
        val proModSubtotal = priceOverride + totalProductChosenList
        val totalPrice = proModSubtotal * quantity!!

        val totalDiscount = totalDiscount()
        val totalService =
            fees?.firstOrNull { fee -> FeeType.fromInt(fee.FeeTypeId) == FeeType.ServiceFee }
                ?.price(subtotal, totalDiscount)
        val totalSurcharge =
            fees?.firstOrNull { fee -> FeeType.fromInt(fee.FeeTypeId) == FeeType.SurchargeFee }
                ?.price(subtotal, totalDiscount)
        val totalTax = fees?.firstOrNull { fee -> FeeType.fromInt(fee.FeeTypeId) == FeeType.TaxFee }
            ?.price(subtotal, totalDiscount)
        val totalFee = totalFee(subtotal, totalDiscount)
        val grossPrice = grossPrice(subtotal, totalFee)

        val discountList = toOrderDiscountList(
            discountServersList,
            discountUsersList,
            totalPrice, totalModifier, proOriginal!!._id, quantity
        )
        val compVoidList = toCompVoidList(compReason, totalCompVoid)
        val serviceFeeList =
            toOrderFeeList(
                fees ?: mutableListOf(),
                FeeType.ServiceFee,
                subtotal,
                totalDiscount
            )
        val taxesFeeList =
            toOrderFeeList(
                fees ?: mutableListOf(),
                FeeType.TaxFee,
                subtotal,
                totalDiscount
            )
        val surchargeFeeList = toOrderFeeList(
            fees ?: mutableListOf(),
            FeeType.SurchargeFee,
            subtotal,
            totalDiscount
        )

        return ProductChosen(
            OrderDetailId = indexOfList,
            _id = proOriginal!!._id,
            Name1 = proOriginal!!.Name,
            Name2 = proOriginal!!.Name3,
            Sku = sku,
            Price = priceOverride,
            Quantity = quantity!!,
            Note = note,
            DiningOption = if (diningOption == null) null else OrderDiningOption(
                Id = diningOption?.Id ?: 0,
                TypeId = diningOption?.TypeId ?: 0,
                Title = diningOption?.Title,
                Acronymn = diningOption?.Acronymn
            ),
            VariantList = variantList,
            DiscountTotalPrice = totalDiscount,
            ServiceTotalPrice = totalService,
            SurchargeTotalPrice = totalSurcharge,
            TaxTotalPrice = totalTax,
            ModifierTotalPrice = totalModifier,
            PricingMethodType = proOriginal!!.PricingMethodType,
            Subtotal = subtotal,
            ModSubtotal = 0.0,
            ProdModSubtotal = proModSubtotal,
            LineTotal = lineTotal,
            GrossPrice = grossPrice,
            ProductTypeId = productType.value,
            Variant = variants,
            Url = proOriginal?.Url,
            ModifierList = null,
            CompVoidList = compVoidList,
            DiscountList = discountList,
            ServiceFeeList = serviceFeeList,
            SurchargeFeeList = surchargeFeeList,
            TaxFeeList = taxesFeeList,
            OtherFee = totalFee,
            Category_id = proOriginal!!.CategoryGuid,
            Parent_id = null,
            ParentName = parentName,
            ParentIndex = parentIndex,
            ProductChoosedList = proChooseList,
        )

    }

}