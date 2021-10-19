package com.hanheldpos.model.home.order.menu

import android.os.Parcelable
import com.hanheldpos.model.home.order.menu.MenuType
import com.hanheldpos.model.product.ExtraData
import com.hanheldpos.model.product.ProductOrderItem
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderMenuItemModel(
    var id: String? = null,
    var text: String? = null,
    var menuType: MenuType? = null,
    var description: String? = null,
    var img: String? = null,
    var mappedItem: Parcelable? = null, // data that mapping from the node
    var nodeItem: Parcelable? = null,
    var color: String? = null,

    var childList: MutableList<ProductOrderItem?>? = null,
    ) : Parcelable {
    var uiType: MenuModeViewType? = MenuModeViewType.Menu;
}

