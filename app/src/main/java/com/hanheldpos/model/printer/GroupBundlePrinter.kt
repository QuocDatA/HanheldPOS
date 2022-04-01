package com.hanheldpos.model.printer

import com.hanheldpos.model.order.ProductChosen

data class GroupBundlePrinter(
    var productList: MutableList<ProductChosen>
) {
}