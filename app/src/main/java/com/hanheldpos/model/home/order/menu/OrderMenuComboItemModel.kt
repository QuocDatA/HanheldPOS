package com.hanheldpos.model.home.order.menu

import android.os.Parcelable
import com.hanheldpos.model.cart.order.OrderItemModel
import com.hanheldpos.model.product.ExtraDoneModel
import com.hanheldpos.model.product.ProductComboItem
import com.hanheldpos.model.product.ProductOrderItem
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import kotlinx.parcelize.IgnoredOnParcel
import kotlin.math.max

@Parcelize
data class OrderMenuComboItemModel(
    /**
     *  List name of selected products in combo list
     */
    var listItemsByGroup: MutableList<ItemComboGroupManager?>? = null,

    var comboAdapter: @RawValue Any? = null,

    var comboParentId: String? = null,
    /**
     *  if this value is <0, mean its list is null
     */
    var modifiedGroup: Int = -1,

) : Parcelable

/**
 *  Hold essential info of items in combo separated by group
 */
@Parcelize
data class ItemComboGroupManager(
    var productComboItem: ProductComboItem? = null,

    var comboDetailAdapter: @RawValue Any? = null,

    /**
     * Show list of ComboPickedItem has been add to combo
     */
    var comboItemSelectedAdapter: @RawValue Any? = null,

    var listSelectedComboItems: MutableList<ComboPickedItemViewModel?> = mutableListOf(),

    var modifiedItem: Int = -1,

    /**
     * only show combo list when item in cart is focused by user
     */
    var isFocused: Boolean = false,
) : Parcelable {

    private fun getAllItemQuantity(): Int {
        var total = 0
        listSelectedComboItems.map {
            total += it?.selectedComboItem!!.quantity;
        }
        return total
    }

    fun requireQuantity() = productComboItem?.quantity?.minus(getAllItemQuantity()) ?: 0

    fun isMaxItemSelected() = getAllItemQuantity() == productComboItem?.quantity

    fun getGroupName() =
        OrderMenuDataMapper.getGroupNameFromGroupGuid(productComboItem?.comboGuid)
}

@Parcelize
data class ComboPickedItemViewModel(
    var comboParentId: String? = null,
    /**
     * Selected products in combo list of this group
     */
    var selectedComboItem: OrderItemModel? = null,
) : Parcelable, Cloneable {
    @IgnoredOnParcel
    var isChosen: Boolean = false;

    public override fun clone(): ComboPickedItemViewModel {
        return copy(
            selectedComboItem = selectedComboItem?.clone()
        )
    }
}



