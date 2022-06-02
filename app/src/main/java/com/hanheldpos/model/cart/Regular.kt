package com.hanheldpos.model.cart

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.api.pojo.fee.Fee
import com.hanheldpos.data.api.pojo.order.settings.DiningOption
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.model.OrderHelper
import com.hanheldpos.model.cart.fee.FeeApplyToType
import com.hanheldpos.model.cart.fee.FeeType
import com.hanheldpos.model.order.OrderDiningOption
import com.hanheldpos.model.order.ProductChosen
import com.hanheldpos.model.product.ProductType
import kotlinx.parcelize.Parcelize

@Parcelize
class Regular() : BaseProductInCart(), Parcelable, Cloneable {

    constructor(
        productItem: Product,
        diningOptionItem: DiningOption,
        quantity: Int?,
        sku: String?,
        variants: String?,
        fees: List<Fee>?
    ) : this() {
        this.proOriginal = productItem
        this.diningOption = diningOptionItem
        this.quantity = quantity
        this.sku = sku
        this.variants = variants
        this.fees = fees
    }

    init {
        this.productType = ProductType.REGULAR
    }

    override fun getProductName(): String? {
        return proOriginal?.Name
    }

    override fun getFeeString(): String {
        return ""
    }

    fun updateVariant(sku: String, variants: String) {
        this.sku = sku
        this.variants = variants
    }

    fun addModifier(modifierAdd: ModifierCart) {
        val modMatching =
            modifierList.firstOrNull { mod -> mod.modifierId == modifierAdd.modifierId }
        if (modMatching == null) {
            modifierList.add(modifierAdd)
        } else {
            modMatching.quantity = modifierAdd.quantity
        }
    }

    fun removeModifier(modifierRemove: ModifierCart) {
        val modMatching =
            modifierList.firstOrNull { mod -> mod.modifierId == modifierRemove.modifierId }
        if (modMatching != null) {
            modifierList.remove(modMatching)
        }
    }

    override fun totalFee(): Double {
        return totalFee(subTotal(), totalDiscount())
    }

    override fun subTotal(): Double {
        return subTotal(proOriginal!!)
    }

    override fun totalDiscount(): Double {
        return totalDiscount(proOriginal!!)
    }

    override fun total(): Double {
        return total(proOriginal!!)
    }

    override fun totalComp(): Double {
        return totalComp(proOriginal!!)
    }

    override fun totalPriceUsed(discount: DiscountResp): Double {
        val totalPrice = this.totalPrice()
        val totalModifierPrice = this.totalModifier(this.proOriginal!!)
        return this.discountServersList?.filter { disc -> disc._id == discount._id }!!.toList()
            .sumOf { disc ->
                disc.total(
                    totalPrice,
                    totalModifierPrice,
                    this.proOriginal?._id,
                    this.quantity
                )!!
            }
    }

    override fun totalQtyUsed(discount: DiscountResp, product_id: String): Int {
        return if (this.proOriginal?._id != product_id) 0 else this.discountServersList!!.filter { disc -> disc._id == discount._id }
            .sumOf { disc -> disc.quantityUsed ?: 0 }
    }

    override fun compareValue(productDiscList: List<Product>): Double {
        val totalMod = modSubTotal(proOriginal!!)
        val isApplyToMod =
            productDiscList.firstOrNull { p -> p._id == proOriginal?._id }?.ApplyToModifier == 1
        return if (isApplyToMod) (priceOverride + totalMod) else priceOverride
    }

    override fun isCompleted(): Boolean {
        return true
    }

    override fun isMatching(productItem: Product): Boolean {
        return proOriginal?._id.equals(productItem._id) && proOriginal?.VariantsGroup == null
    }

    override fun clone(): Regular {
        val cloneValue = Regular(
            this.proOriginal!!,
            this.diningOption!!,
            this.quantity,
            this.sku,
            this.variants,
            this.fees,
        )
        cloneValue.variantList = this.variantList?.map { it.copy() }?.toMutableList()
        cloneValue.modifierList.addAll(this.modifierList.map { it.copy() }.toMutableList())
        cloneValue.compReason = this.compReason
        cloneValue.discountUsersList = this.discountUsersList?.map { it.copy() }?.toMutableList()
        cloneValue.discountServersList =
            this.discountServersList?.map { it.copy() }?.toMutableList()
        cloneValue.note = this.note

        return cloneValue
    }

    private fun totalTemp(productPricing: Product): Double {
        val subtotal = subTotal(productPricing)
        val totalDiscPrice = totalDiscount(productPricing)
        val totalFeePrice = totalFee(subtotal, totalDiscPrice)
        var total = subtotal - totalDiscPrice + totalFeePrice
        total = if (total < 0) 0.0 else total
        return total
    }

    fun totalComp(productPricing: Product): Double {
        val totalTemp = totalTemp(productPricing)
        val totalComp = compReason?.total(totalTemp) ?: 0.0
        return totalComp
    }

    private fun totalComp(totalTemp: Double): Double {
        val telcomp = compReason?.total(totalTemp) ?: 0.0
        return telcomp
    }

    private fun totalDiscount(productPricing: Product): Double {
        val totalPrice = totalPrice()
        val totalModifierPrice = totalModifier(productPricing)

        val subtotal = totalPrice + totalModifierPrice
        val totalDiscUser = discountUsersList?.sumOf { disc -> disc.total(subtotal) } ?: 0.0
        val totalDiscServer = discountServersList?.sumOf { disc ->
            disc.total(
                totalPrice,
                totalModifierPrice,
                proOriginal?._id,
                quantity
            ) ?: 0.0
        } ?: 0.0

        return totalDiscUser + totalDiscServer
    }

    fun modSubTotal(productPricing: Product): Double {
        val mobSubtotal = modifierList.sumOf {
            it.subTotal(productPricing)
        }
        return mobSubtotal
    }

    fun totalModifier(productPricing: Product): Double {
        val total = modSubTotal(productPricing) * (quantity ?: 0)
        return total
    }

    private fun proModSubTotal(productPricing: Product): Double {
        val modSubtotal = modSubTotal(productPricing)
        val proModSubtotal = modSubtotal + priceOverride
        return proModSubtotal
    }

    fun totalPrice(): Double {
        return priceOverride.times(quantity ?: 0)
    }

    fun subTotal(productPricing: Product): Double {
        val subtotal = proModSubTotal(productPricing) * (quantity ?: 0)
        return subtotal
    }

    fun total(productPricing: Product): Double {
        val totalTemp = totalTemp(productPricing)
        val totalComp = totalComp(totalTemp)
        val lineTotal = totalTemp - totalComp
        return lineTotal
    }

    fun totalFee(subtotal: Double, totalDisc: Double): Double {
        return fees?.filter { it.Id != FeeApplyToType.Included.value }
            ?.sumOf { it.price(subtotal, totalDisc) } ?: 0.0
    }

    fun groupPrice(group: GroupBundle, productBundle: Product): Double {
        val groupPrice =
            productBundle.GroupPrices?.firstOrNull { g_price -> g_price.GroupGuid == group.comboInfo.ComboGuid }?.Product?.firstOrNull { p_price -> p_price.ProductGuid == proOriginal?._id }
        return if (proOriginal?.VariantsGroup != null) {
            val groupPriceVariant =
                groupPrice?.Variants?.firstOrNull { gr_price -> gr_price.GroupSKU == sku }
            groupPriceVariant?.GroupAmount ?: 0.0
        } else {
            groupPrice?.ProductAmount ?: 0.0
        }
    }

    fun totalGroupPrice(group: GroupBundle, productBundle: Product): Double {
        val totalGroupPrice = groupPrice(group, productBundle) * (quantity ?: 0)
        return totalGroupPrice
    }


    fun total(group: GroupBundle, productBundle: Product): Double {
        val totalGroupPrice = totalGroupPrice(group, productBundle)
        val totalModifier = totalModifier(productBundle)
        val total = totalGroupPrice + totalModifier
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
        quantity: Int,
        parentName: String? = null,
        parentIndex: Int = 0,
    ): ProductChosen {
        val modSubtotal = modSubTotal(proOriginal!!)
        val totalModifier = modSubtotal * quantity
        val proModSubtotal = priceOverride + modSubtotal
        val subtotal = subTotal(proOriginal!!)
        val lineTotal = total(proOriginal!!)
        val totalPrice = totalPrice()


        val totalDiscount = totalDiscount(proOriginal!!)
        val totalCompVoid = totalComp(proOriginal!!)
        val totalService =
            fees?.firstOrNull { fee -> FeeType.fromInt(fee.FeeTypeId) == FeeType.ServiceFee }
                ?.price(subtotal, totalDiscount)
        val totalSurcharge =
            fees?.firstOrNull { fee -> FeeType.fromInt(fee.FeeTypeId) == FeeType.SurchargeFee }
                ?.price(subtotal, totalDiscount)
        val totalTax =
            fees?.firstOrNull { fee -> FeeType.fromInt(fee.FeeTypeId) == FeeType.TaxFee }
                ?.price(subtotal, totalDiscount)

        val totalFee = totalFee(subtotal, totalDiscount)
        val grossPrice = grossPrice(subtotal, totalFee)

        val modifierList =
            toModifierList(modifierList, proOriginal!!)
        val compVoidList = toCompVoidList(compReason, totalCompVoid)
        val discountList = toOrderDiscountList(
            discountServersList,
            discountUsersList,
            totalPrice,
            totalModifier,
            proOriginal!!._id,
            quantity
        )
        val surchargeFeeList = toOrderFeeList(
            fees ?: mutableListOf(),
            FeeType.SurchargeFee,
            subtotal,
            totalDiscount
        )
        val serviceFeeList = toOrderFeeList(
            fees ?: mutableListOf(),
            FeeType.ServiceFee,
            subtotal,
            totalDiscount
        )
        val taxesFeeList = toOrderFeeList(
            fees ?: mutableListOf(),
            FeeType.TaxFee,
            subtotal,
            totalDiscount
        )

        return ProductChosen(
            OrderDetailId = indexOfList,
            _id = proOriginal!!._id,
            Name1 = proOriginal!!.Name ?: "",
            Name2 = proOriginal!!.Name3,
            Sku = sku,
            Price = priceOverride,
            Quantity = quantity,
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
            ModSubtotal = modSubtotal,
            ProdModSubtotal = proModSubtotal,
            LineTotal = lineTotal,
            GrossPrice = grossPrice,
            ProductTypeId = productType.value,
            Variant = variants,
            Url = null,
            ModifierList = modifierList,
            CompVoidList = compVoidList,
            DiscountList = discountList,
            ServiceFeeList = serviceFeeList,
            SurchargeFeeList = surchargeFeeList,
            TaxFeeList = taxesFeeList,
            OtherFee = totalFee,
            Category_id = proOriginal!!.CategoryGuid,
            Parent_id = null,
            ParentName = parentName,
            ParentIndex = parentIndex
        )

    }

    fun toProductChosen(
        proOriginalCombo: Product,
        group: GroupBundle,
        index: Int,
        orderDetailId: Int
    ): ProductChosen {

        val parentName = OrderHelper.findGroupNameOrderMenu(group.comboInfo.ComboGuid!!)
        val modSubtotal = modSubTotal(proOriginalCombo)
        val groupPrice = groupPrice(group, proOriginalCombo)
        val totalPrice = groupPrice * quantity!!
        val modifierList = toModifierList(modifierList, proOriginalCombo)
        val totalModifier = modSubtotal * quantity!!
        val subtotal = totalPrice + totalModifier
        val proModSubtotal = groupPrice + modSubtotal

        return ProductChosen(
            OrderDetailId = orderDetailId,
            _id = proOriginal!!._id,
            Name1 = proOriginal!!.Name ?: "",
            Name2 = proOriginal!!.Name3,
            Sku = sku,
            Price = groupPrice,
            Quantity = quantity!!,
            Note = note,

            DiningOption = if (diningOption == null) null else OrderDiningOption(
                Id = diningOption?.Id ?: 0,
                TypeId = diningOption?.TypeId ?: 0,
                Title = diningOption?.Title,
                Acronymn = diningOption?.Acronymn
            ),

            VariantList = variantList,
            DiscountTotalPrice = 0.0,
            ServiceTotalPrice = 0.0,
            SurchargeTotalPrice = 0.0,
            TaxTotalPrice = 0.0,
            ModifierTotalPrice = totalModifier,
            PricingMethodType = null,
            Subtotal = subtotal,
            ModSubtotal = modSubtotal,
            ProdModSubtotal = proModSubtotal,
            LineTotal = subtotal,
            GrossPrice = null,
            ProductTypeId = productType.value,
            Variant = variants,
            Url = proOriginal!!.Url,
            ModifierList = modifierList,
            CompVoidList = null,
            DiscountList = null,
            ServiceFeeList = null,
            SurchargeFeeList = null,
            TaxFeeList = null,
            OtherFee = 0.0,
            Category_id = proOriginal!!.CategoryGuid,
            Parent_id = group.comboInfo.ComboGuid,
            ParentName = parentName,
            ParentIndex = index + 1
        )
    }
}
