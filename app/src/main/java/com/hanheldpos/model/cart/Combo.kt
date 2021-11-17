package com.hanheldpos.model.cart

import com.hanheldpos.data.api.pojo.fee.Fee
import com.hanheldpos.data.api.pojo.order.settings.DiningOptionItem
import com.hanheldpos.data.api.pojo.product.ProductItem
import com.hanheldpos.model.product.BaseProductInCart
import com.hanheldpos.model.product.PricingMethodType
import com.hanheldpos.model.product.ProductType


class Combo() : BaseProductInCart() {

    var groupList: MutableList<GroupBundle> = mutableListOf()

    constructor(
        productItem: ProductItem,
        groupProducts: List<GroupBundle>,
        diningOptionItem: DiningOptionItem,
        quantity: Int?,
        sku: String?,
        variants: String?,
        priceOverride: Double?,
        fees: List<Fee>?
    ) : this() {
        this.proOriginal = productItem
        this.groupList = groupProducts.toMutableList();
        this.diningOption = diningOptionItem
        this.quantity = quantity
        this.sku = sku
        this.variants = variants
        this.priceOverride = priceOverride;
        this.fees = fees;
    }

    init {
        this.productType = ProductType.BUNDLE
    }

    override fun getProductName(): String? {
        return proOriginal?.name;
    }

    override fun getFeeString(): String {
        return ""
    }

    override fun subTotal(): Double {
        val proModSubtotal = proModSubTotal() * (quantity?:0);
        return proModSubtotal;
    }

    override fun totalDiscount(): Double {
        return 0.0
    }

    override fun total(): Double {
        val totalTemp = totalTemp();
        val lineTotal = totalTemp - totalComp(totalTemp);
        return lineTotal;
    }

    override fun totalComp(): Double {
        val totalTemp = totalTemp();
        val totalcomp = compReason?.total(totalTemp)?:0.0
        return totalcomp;
    }

    fun totalComp(totalTemp: Double): Double {
        return compReason?.total(totalTemp) ?: 0.0;
    }

    override fun isCompleted(): Boolean {
        return groupList.firstOrNull { gr -> !gr.isComplete() } == null;
    }

    override fun isMatching(productItem: ProductItem): Boolean {
        return false;
    }

    override fun clone(): Combo {
        return Combo(
            proOriginal!!,
            groupList.toMutableList().map { it.copy() },
            diningOption!!,
            quantity,
            sku,
            variants,
            priceOverride,
            fees
        );
    }

    fun modSubTotal() : Double {
        val modSubtotal = groupList.sumOf { group -> group.productList.sumOf { regular-> regular.totalModifier(proOriginal!!) } }
        return modSubtotal;
    }

    fun groupSubTotal() : Double{
        var groupSubtotal = 0.0
        groupList.forEach { group ->
            group.productList.forEach { regular->
                groupSubtotal+= regular.totalGroupPrice(group,proOriginal!!);
            }
        }
        return groupSubtotal;
    }

    fun proModSubTotal() : Double {
        val pricingMethodType = proOriginal?.pricingMethodType ?: 1;

        val modSubtotal = modSubTotal();
        val proSubtotal = priceOverride ?: 0.0;

        return when(PricingMethodType.fromInt(pricingMethodType)){
            PricingMethodType.BasePrice -> proSubtotal.plus(modSubtotal);
            PricingMethodType.GroupPrice -> {
                val groupSubtotal = groupSubTotal();
                return proSubtotal + modSubtotal + groupSubtotal;
            }
            else-> proSubtotal.plus(modSubtotal);
        }
    }

    private fun totalTemp(): Double {
        val subtotal = subTotal();
        val totalDiscPrice = 0.0;
        val totalFeePrice = 0.0;

        var total = subtotal - totalDiscPrice + totalFeePrice;
        total = if (total < 0) 0.0 else total;
        return total;
    }


}