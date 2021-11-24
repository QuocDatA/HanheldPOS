package com.hanheldpos.model.home.order.menu

import com.hanheldpos.data.api.pojo.order.menu.getCategoryItem
import com.hanheldpos.data.api.pojo.order.menu.getGroupItem
import com.hanheldpos.data.api.pojo.order.menu.*
import com.hanheldpos.model.image.getImageUrl

/**
 * Get Category Info
 */
internal fun OrderMenuItem.setCategoryOrderItem(
    orderMenuResp: OrderMenuResp,
    categoryGuid: String
) {

    val categoryItem = orderMenuResp.getCategoryItem(categoryGuid)

    categoryItem?.let {
        this.setCategoryOrderItem(orderMenuResp, it)
    }
}

internal fun OrderMenuItem.setCategoryOrderItem(
    orderMenuResp: OrderMenuResp,
    categoryItem: CategoryItem
) {
    this.color = categoryItem.color
    this.id = categoryItem.id
    this.text = categoryItem.title
    this.description = categoryItem.description

    this.menuType = MenuType.Category
    this.mappedItem = categoryItem

    this.img = categoryItem.getImageUrl(orderMenuResp, categoryItem.id)
}

/**
 * Get Groups Info
 */
internal fun OrderMenuItem.setGroupOrderItem(orderMenuResp: OrderMenuResp, groupGuid: String) {

    val groupItem = orderMenuResp.getGroupItem(groupGuid)

    groupItem?.let {
        this.setGroupOrderItem(orderMenuResp, it)
    }
}

internal fun OrderMenuItem.setGroupOrderItem(
    orderMenuResp: OrderMenuResp,
    groupItem: GroupsItem
) {
    this.id = groupItem.id

    this.text = groupItem.groupName
    this.color = groupItem.color

    this.menuType = MenuType.Group
    this.mappedItem = groupItem

    this.img = groupItem.getImageUrl(orderMenuResp, groupItem.id)
}