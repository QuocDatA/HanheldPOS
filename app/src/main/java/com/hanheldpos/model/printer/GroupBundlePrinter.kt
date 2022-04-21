package com.hanheldpos.model.printer

import com.hanheldpos.model.order.ProductChosen

data class GroupBundlePrinter(
    val parentName : String?,
    var productList: MutableList<ProductChosen>
) {
}