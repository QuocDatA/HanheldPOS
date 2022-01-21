package com.hanheldpos.model.home.order.menu

import com.hanheldpos.data.api.pojo.order.menu.*

/**
 * Get Category Info
 */
internal fun OrderMenuItem.setCategoryOrderItem(
    menuResp: MenuResp,
    categoryGuid: String
) {
    val categoryItem = menuResp.CategoryList?.find { it._Id == categoryGuid }

    categoryItem?.let {
        this.setCategoryOrderItem(menuResp, it)
    }
}

internal fun OrderMenuItem.setCategoryOrderItem(
    menuResp: MenuResp,
    categoryItem: Category
) {
    this.color = categoryItem.Color
    this.id = categoryItem._Id
    this.text = categoryItem.Title
    this.description = categoryItem.Description

    this.menuType = MenusType.Category
    this.mappedItem = categoryItem
}

/**
 * Get Groups Info
 */
internal fun OrderMenuItem.setGroupOrderItem(menuResp: MenuResp, groupGuid: String) {

    val groupItem = menuResp.GroupList?.find { it._Id == groupGuid };

    groupItem?.let {
        this.setGroupOrderItem(menuResp, it)
    }
}

internal fun OrderMenuItem.setGroupOrderItem(
    menuResp: MenuResp,
    groupItem: Group
) {
    this.id = groupItem._Id

    this.text = groupItem.GroupName
    this.color = groupItem.Color

    this.menuType = MenusType.Group
    this.mappedItem = groupItem
}