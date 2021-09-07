package com.hanheldpos.data.api.pojo.order

import com.diadiem.pos_config.utils.Const

fun OptionItem.splitListOptionValue(): List<String>? {
    return this.optionValue?.split(',')
}

fun GroupItem.splitListGroupNameValue(): List<String>? {
    return this.groupName?.split(Const.SymBol.VariantSeparator)
}