package com.hanheldpos.model.home.order.menu

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.model.home.order.ProductModeViewType

data class OrderMenuItem(
    var id: String? = null,
    var text: String? = null,
    var menuType: MenusType? = null,
    var description: String? = null,
    var img: String? = null,
    var mappedItem: Parcelable? = null, // data that mapping from the node
    var nodeItem: Parcelable? = null,
    var color: String? = null,
    var childList: MutableList<ProductMenuItem>? = null,
    ) {
    var uiType: MenuModeViewType? = MenuModeViewType.Menu;
}

data class ProductMenuItem(
    val proOriginal : Product? =null,
) {
    var isChosen : Boolean = false;
    var uiType = ProductModeViewType.Product;
}
