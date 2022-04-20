package com.hanheldpos.model.discount

enum class DiscountTriggerType(val value: Int) {
    ALL(0),
    ON_PAGE_LOAD(1),
    ON_CLICK (2),
    IN_CART (3),
    IN_ITEM_DETAIL (4),
    ENTER_CODE (5)
}