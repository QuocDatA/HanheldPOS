package com.hanheldpos.model.product

import com.hanheldpos.data.api.pojo.product.ModifierExtra

data class GroupExtra(
    val modifierExtra: ModifierExtra,
    val modifierList: List<ItemExtra> = mutableListOf()
) {

}