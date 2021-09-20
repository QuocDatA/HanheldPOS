package com.hanheldpos.ui.screens.product.adapter.variant
import com.hanheldpos.data.api.pojo.order.menu.OptionItem

data class VariantHeader(
    var name: String? = null,
    var realItem: OptionItem? = null,
    var childList: List<VariantLayoutItem>? = null,

    var headerPosition: Int = 0
) {
    var expanded: Boolean = false
}