package com.hanheldpos.model.home.order.menu


import com.hanheldpos.data.api.pojo.order.menu.*
import com.hanheldpos.data.api.pojo.product.toProductOrderItem
import com.hanheldpos.model.product.ProductOrderItem

object OrderMenuDataMapper {
    lateinit var orderMenuResp: OrderMenuResp


    /**
     *  Get Menu Child List of a OrderMenuItem by find in the OrderMenuResp
     *  Note: this should call when an item is click in the OrderMenu list
     */
    fun OrderMenuItemModel.getChildList(): MutableList<ProductOrderItem?> {
        var rs = mutableListOf<ProductOrderItem?>()

        if (this.nodeItem is ListToHierarchyItem) {
            val listToHierarchyItem = this.nodeItem as ListToHierarchyItem
            when (listToHierarchyItem.menusType) {
                1 -> { // is GroupItem
                    // Find all Product with sGroupItem
                    rs.addAll(
                        getProductOrderItemListByGroupGuid(listToHierarchyItem.groupGuid)
                    )
                }
                2 -> { // is Category
                    // Find all product in category
                    rs.addAll(
                        getProductOrderItemListByCategoryGuid(listToHierarchyItem.groupGuid)
                    )
                }
            }
        } else {
            if (this.mappedItem is GroupsItem) {
                val groupGuid = (this.mappedItem as GroupsItem).id
                rs.addAll(
                    getProductOrderItemListByGroupGuid(groupGuid)
                )

            } else if (this.mappedItem is CategoryItem) {
                val categoryGuid = (this.mappedItem as CategoryItem).id
                rs.addAll(
                    getProductOrderItemListByCategoryGuid(categoryGuid)
                )
            }
        }
        return rs
    }


    /**
     * Find all product by @categoryGuid and transforms to OrderMenuItem List
     */
    private fun getProductOrderItemListByCategoryGuid(categoryGuid: String?): MutableList<ProductOrderItem?> {
        val rs: MutableList<ProductOrderItem?> = mutableListOf()

        orderMenuResp.getProductWithCategoryGuid(categoryGuid)?.forEach {
            rs.add(it?.toProductOrderItem(orderMenuResp))
        };
        return rs
    }

    /**
     * Find all product by @groupGuid and transforms to OrderMenuItem List
     */
    private fun getProductOrderItemListByGroupGuid(groupGuid: String?): MutableList<ProductOrderItem?> {
        val rs: MutableList<ProductOrderItem?> = mutableListOf()

        //todo(GroupItem): set Group type for product is here
        orderMenuResp.getMenuGroupItemListWithGroupId(groupGuid)?.forEach {
            it?.let { it1 ->
                orderMenuResp.getProductWithItemGuid(it1.itemGuid)?.toMutableList()?.forEach { it2 ->
                    rs.add(it2?.toProductOrderItem(orderMenuResp))
                }
            }
        }
        return rs
    }

    //region ### Get ProductItem by ListToHierarchyItem

    fun getLevel_1(menuIndex: Int): List<OrderMenuItemModel> {
        val rs = mutableListOf<OrderMenuItemModel>()

        orderMenuResp.getHierarchyList()?.forEach {

            it?.let {
                val menuIdInMenuList = orderMenuResp.getMenuList()?.get(menuIndex)?.id
                if (it.menusGuid == menuIdInMenuList) {

                    val orderMenuItemList = getOrderMenuItemList(it, null)

                    if (!orderMenuItemList.isNullOrEmpty()) {
                        rs.addAll(orderMenuItemList)
                    }
                }
            }
        }

        rs.forEach {
            it.childList = it.getChildList();
        }

        return rs
    }


    /**
     * Second level
     *//*
    fun getLevel_2(
        item: ListToHierarchyItem,
        parent: OrderMenuItemModel?
    ): MutableList<OrderMenuItemModel> {
        val rs = mutableListOf<OrderMenuItemModel>()
        item.children?.forEach {
            it?.let {
                val orderMenuItemList = getOrderMenuItemList(it, parent)
                if (!orderMenuItemList.isNullOrEmpty()) {
                    rs.addAll(orderMenuItemList)
                }
            }
        }
        return rs
    }*/


    /**
     * Get OrderMenuItem by @listToHierarchyItem
     */
    private fun getOrderMenuItemList(
        listToHierarchyItem: ListToHierarchyItem,
        parent: OrderMenuItemModel?
    ): List<OrderMenuItemModel>? {
        val rs: MutableList<OrderMenuItemModel> = mutableListOf()
        val orderItem = OrderMenuItemModel()

        orderItem.nodeItem = listToHierarchyItem

        val groupGuid = listToHierarchyItem.groupGuid ?: return null
        when (listToHierarchyItem.menusType) {
            0 -> { // SystemGroupItem
                // If it's a SystemGroupItem then get all the child inside an return a list OrderMenuItem
                // with the same level as the current checking level
                // If the SystemGroupItem has no child then return null

                listToHierarchyItem.children?.forEach {
                    it?.let {
                        return getOrderMenuItemList(it, parent)
                    }
                }
                return null
            }
            1 -> { // Groups
                // TODO: the flow of the group is not clear specially when get the group child
                orderItem.setGroupOrderItem(orderMenuResp, groupGuid)
            }
            2 -> { // Category
                // groupGuid --> in this case is CategoryGuid
                orderItem.setCategoryOrderItem(orderMenuResp, groupGuid)
            }
        }
        rs.add(orderItem)
        return rs
    }

    //endregion
}