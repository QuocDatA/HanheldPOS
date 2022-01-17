package com.hanheldpos.model.cart

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.fee.Fee
import com.hanheldpos.data.api.pojo.order.settings.DiningOptionItem
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.model.cart.fee.FeeApplyToType
import com.hanheldpos.model.product.BaseProductInCart
import com.hanheldpos.model.product.ProductType
import kotlinx.parcelize.Parcelize

@Parcelize
class Regular() : BaseProductInCart(), Parcelable, Cloneable {

    constructor(
        productItem: Product,
        diningOptionItem: DiningOptionItem,
        quantity: Int?,
        sku: String?,
        variants: String?,
        priceOverride: Double?,
        fees: List<Fee>?
    ) : this() {
        this.proOriginal = productItem
        this.diningOption = diningOptionItem
        this.quantity = quantity
        this.sku = sku
        this.variants = variants
        this.priceOverride = priceOverride;
        this.fees = fees;
    }

    init {
        this.productType = ProductType.REGULAR
    }

    override fun getProductName(): String? {
        return proOriginal?.Name;
    }

    override fun getFeeString(): String {
        return ""
    }

    override fun totalFee(): Double {
        return totalFee(subTotal(),totalDiscount());
    }

    override fun subTotal(): Double {
        return subTotal(proOriginal!!);
    }

    override fun totalDiscount(): Double {
        return totalDiscount(proOriginal!!);
    }

    override fun total(): Double {
        return total(proOriginal!!);
    }

    override fun totalComp(): Double {
        return totalComp(proOriginal!!);
    }

    override fun isCompleted(): Boolean {
        return true;
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
            this.priceOverride,
            this.fees,
        )
        cloneValue.variantList = this.variantList?.map { it.copy() }?.toMutableList()
        cloneValue.modifierList.addAll(this.modifierList.map { it.copy() }.toMutableList())
        cloneValue.compReason = this.compReason;
        cloneValue.discountUsersList = this.discountUsersList?.map { it.copy() }?.toMutableList()
        cloneValue.discountServersList = this.discountServersList?.map { it.copy() }?.toMutableList()

        return cloneValue
    }

    private fun totalTemp(productPricing: Product): Double {
        val subtotal = subTotal(productPricing);
        val totalDiscPrice = totalDiscount(productPricing);
        val totalFeePrice = totalFee(subtotal, totalDiscPrice);
        var total = subtotal - totalDiscPrice + totalFeePrice;
        total = if (total < 0) 0.0 else total;
        return total
    }

    fun totalComp(productPricing: Product): Double {
        val totalTemp = totalTemp(productPricing);
        val totalComp = compReason?.total(totalTemp) ?: 0.0;
        return totalComp;
    }

    fun totalComp(totalTemp: Double): Double {
        val telcomp = compReason?.total(totalTemp) ?: 0.0;
        return telcomp;
    }

    fun totalDiscount(productPricing: Product) : Double {
        var totalPrice = totalPrice();
        var totalModifierPrice = totalModifier(productPricing);

        var subtotal = totalPrice + totalModifierPrice;
        var totalDiscUser = discountUsersList?.sumOf { disc -> disc.total(subtotal) }?: 0.0;
        // TODO : un comment when apply discount server
        /*var totalDiscServer = discountServersList?.Sum(disc => disc.Total(totalPrice, totalModifierPrice, ProOriginal?.Id, Quantity)) ?? 0;*/

        var total = totalDiscUser //+ totalDiscServer;
        return total;
    }

    fun modSubTotal(productPricing: Product): Double {
        val mobSubtotal = modifierList.sumOf {
            it.subTotal(productPricing) ?: 0.0
        }
        return mobSubtotal;
    }

    fun totalModifier(productPricing: Product): Double {
        val total = modSubTotal(productPricing) * (quantity ?: 0);
        return total;
    }

    fun proModSubTotal(productPricing: Product): Double {
        val modSubtotal = modSubTotal(productPricing);
        val proModsubtotal = modSubtotal + (priceOverride ?: 0.0);
        return proModsubtotal;
    }

    fun totalPrice() :Double {
        return priceOverride?.times(quantity!!)?: 0.0
    }

    fun subTotal(productPricing: Product): Double {
        val subtotal = proModSubTotal(productPricing) * quantity!!;
        return subtotal;
    }

    fun total(productPricing: Product): Double {
        val totalTemp = totalTemp(productPricing);
        val totalComp = totalComp(totalTemp);
        val linetotal = totalTemp - totalComp;
        return linetotal;
    }

    fun totalFee(subtotal: Double, totalDisc: Double): Double {
        return fees?.filter { it.feeApplyToType != FeeApplyToType.Included.value }
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
        val totalGroupPrice = groupPrice(group, productBundle) * (quantity ?: 0);
        return totalGroupPrice;
    }

    fun plusOrderQuantity(num: Int) {
        quantity = (quantity ?: 0).plus(num);
    }

    fun minusOrderQuantity(num: Int) {
        quantity = if (quantity!! > 0) quantity!!.minus(num) else 0
    }


}
