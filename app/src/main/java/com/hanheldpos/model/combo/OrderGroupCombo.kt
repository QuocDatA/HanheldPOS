package com.hanheldpos.model.combo

import com.hanheldpos.model.cart.GroupBundle
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.home.order.menu.ProductMenuItem

/**
 *  Hold essential info of items in combo separated by group
 */

data class ItemComboGroup(
    var groupBundle: GroupBundle,
    //Show list of ComboPickedItem has been add to combo
    var productsForChoose: List<Regular> = mutableListOf(),
    // only show combo list when item in cart is focused by user
    var isFocused: Boolean = false,
) {

    fun requireQuantity(): Int = groupBundle.requireQuantity ?: 0

    fun isMaxItemSelected(): Boolean = groupBundle.isComplete()

    fun getGroupName() = groupBundle.groupName
}



