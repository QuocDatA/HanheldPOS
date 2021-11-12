package com.hanheldpos.model.product

import com.hanheldpos.data.api.pojo.product.ModifierExtra

class GroupExtra(
    val modifierExtra: ModifierExtra,
    val modifierList: List<ItemExtra> = mutableListOf()
) {

}