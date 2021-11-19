package com.hanheldpos.model.cart

import android.os.Parcel
import android.os.Parcelable
import com.hanheldpos.data.api.pojo.fee.Fee
import com.hanheldpos.data.api.pojo.order.settings.DiningOptionItem
import com.hanheldpos.data.api.pojo.product.ProductItem
import com.hanheldpos.model.cart.fee.FeeApplyToType
import com.hanheldpos.model.product.BaseProductInCart
import com.hanheldpos.model.product.ProductType
import kotlinx.parcelize.Parcelize

@Parcelize
class Regular() : BaseProductInCart(), Parcelable, Cloneable {

    constructor(
        productItem: ProductItem,
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
        return proOriginal?.name;
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
        return 0.0
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

    override fun isMatching(productItem: ProductItem): Boolean {
        return proOriginal?.id.equals(productItem.id) && proOriginal?.variantsGroup == null
    }
    override fun clone(): Regular {
        val cloneValue = Regular(
            this.proOriginal!!,
            this.diningOption!!,
            this.quantity,
            this.sku,
            this.variants,
            this.priceOverride,
            this.fees
        )
        cloneValue.variantList = this.variantList?.map { it.copy() }?.toMutableList()
        cloneValue.modifierList.addAll(this.modifierList.map { it.copy() }.toMutableList())
        return cloneValue
    }

    private fun totalTemp(productPricing: ProductItem): Double {
        val subtotal = subTotal(productPricing);
        val totalDiscPrice = 0.0;
        val totalFeePrice = totalFee(subtotal, totalDiscPrice);
        var total = subtotal - totalDiscPrice + totalFeePrice;
        total = if (total < 0) 0.0 else total;
        return total
    }

    fun totalComp(productPricing: ProductItem): Double {
        val totalTemp = totalTemp(productPricing);
        val totalComp = compReason?.total(totalTemp) ?: 0.0;
        return totalComp;
    }

    fun totalComp(totalTemp: Double): Double {
        val telcomp = compReason?.total(totalTemp) ?: 0.0;
        return telcomp;
    }

    fun modSubTotal(productPricing: ProductItem): Double {
        val mobSubtotal = modifierList.sumOf {
            it.subTotal(productPricing) ?: 0.0
        }
        return mobSubtotal;
    }

    fun totalModifier(productPricing: ProductItem): Double {
        val total = modSubTotal(productPricing) * (quantity ?: 0);
        return total;
    }

    fun proModSubTotal(productPricing: ProductItem): Double {
        val modSubtotal = modSubTotal(productPricing);
        val proModsubtotal = modSubtotal + (priceOverride ?: 0.0);
        return proModsubtotal;
    }

    fun subTotal(productPricing: ProductItem): Double {
        val subtotal = proModSubTotal(productPricing) * quantity!!;
        return subtotal;
    }

    fun total(productPricing: ProductItem): Double {
        val totalTemp = totalTemp(productPricing);
        val totalComp = totalComp(totalTemp);
        val linetotal = totalTemp - totalComp;
        return linetotal;
    }

    fun totalFee(subtotal: Double, totalDisc: Double): Double {
        return fees?.filter { it.feeApplyToType != FeeApplyToType.Included.value }
            ?.sumOf { it.price(subtotal, totalDisc) } ?: 0.0
    }

    fun groupPrice(group: GroupBundle, productBundle: ProductItem): Double {
        val groupPrice =
            productBundle.groupPrices?.firstOrNull { g_price -> g_price.groupGUID == group.comboInfo.comboGuid }?.product?.firstOrNull { p_price -> p_price.productGUID == proOriginal?.id }
        return if (proOriginal?.variantsGroup != null) {
            val groupPriceVariant =
                groupPrice?.variants?.firstOrNull { gr_price -> gr_price.groupSKU == sku }
            groupPriceVariant?.groupAmount ?: 0.0
        } else {
            groupPrice?.productAmount ?: 0.0
        }
    }

    fun totalGroupPrice(group: GroupBundle, productBundle: ProductItem): Double {
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
