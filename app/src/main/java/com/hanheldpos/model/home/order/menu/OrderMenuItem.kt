package com.hanheldpos.model.home.order.menu

import android.os.Parcelable

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