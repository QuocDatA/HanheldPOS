package com.hanheldpos.model.product

import com.hanheldpos.data.api.pojo.product.ProductModifiers

data class GroupExtra(
    val modifierExtra: ProductModifiers,
    val modifierList: List<ItemExtra> = mutableListOf()
)