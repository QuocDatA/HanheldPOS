package com.hanheldpos.data.api.pojo.product


fun ProductDetailResp.getPriceByExtra() : Double {
    return this.getPriceByVariant() + this.getPriceByModifier();
}

private fun ProductDetailResp.getPriceByVariant(): Double {
    var price =  0.0;
    return price
}

private fun ProductDetailResp.getPriceByModifier(): Double {
    var totalModifier = 0.0
    this.selectedModifierGroup?.forEach { it ->
        it.value?.forEach {
            val modifierItemPrice = it.realItem?.price?.times(it.quantity) ?: 0.0
            totalModifier += modifierItemPrice
        }
    }
    return totalModifier;
}