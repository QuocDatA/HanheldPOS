package com.hanheldpos.model.home.order.menu

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.product.ProductItem
import com.hanheldpos.model.home.order.ProductModeViewType
import com.hanheldpos.model.home.order.menu.MenuType
import com.hanheldpos.model.product.BaseProductInCart
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

data class OrderMenuItem(
    var id: String? = null,
    var text: String? = null,
    var menuType: MenuType? = null,
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
    val proOriginal : ProductItem? =null,
) {
    var isChosen : Boolean = false;
    var uiType = ProductModeViewType.Product;
}
