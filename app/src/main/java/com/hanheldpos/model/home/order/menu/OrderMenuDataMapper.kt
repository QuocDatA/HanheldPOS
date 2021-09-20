package com.hanheldpos.model.home.order.menu


import com.hanheldpos.data.api.pojo.order.menu.*

object OrderMenuDataMapper {
    lateinit var orderMenuResp: OrderMenuResp


    /**
     *  Get Menu Child List of a OrderMenuItem by find in the OrderMenuResp
     *  Note: this should call when an item is click in the OrderMenu list
     */
    fun OrderMenuItemModel.getChildList(): MutableList<ProductItem?> {
        var rs = mutableListOf<ProductItem?>()

        if (this.nodeItem is ListToHierarchyItem) {
            val listToHierarchyItem = this.nodeItem as ListToHierarchyItem
            when (listToHierarchyItem.menusType) {
                1 -> { // is GroupItem
                    // Find all Product with sGroupItem
                    rs.addAll(
                        getOrderMenuItemListByGroupGuid(listToHierarchyItem.groupGuid)
                    )
                }
                2 -> { // is Category
                    // Find all product in category
                    rs.addAll(
                        getOrderMenuItemListByCategoryGuid(listToHierarchyItem.groupGuid)
                    )
                }
            }
        } else {
            if (this.mappedItem is GroupsItem) {
                val groupGuid = (this.mappedItem as GroupsItem).id
                rs.addAll(
                    getOrderMenuItemListByGroupGuid(groupGuid)
                )

            } else if (this.mappedItem is CategoryItem) {
                val categoryGuid = (this.mappedItem as CategoryItem).id
                rs.addAll(
                    getOrderMenuItemListByCategoryGuid(categoryGuid)
                )
            }
        }
        return rs
    }


    /**
     * Find all product by @categoryGuid and transforms to OrderMenuItem List
     */
    private fun getOrderMenuItemListByCategoryGuid(categoryGuid: String?): MutableList<ProductItem?> {
        val rs: MutableList<ProductItem?> = mutableListOf()

        orderMenuResp.getProductWithCategoryGuid(categoryGuid)?.let { rs.addAll(it) };
        return rs
    }

    /**
     * Find all product by @groupGuid and transforms to OrderMenuItem List
     */
    private fun getOrderMenuItemListByGroupGuid(groupGuid: String?): MutableList<ProductItem?> {
        val rs: MutableList<ProductItem?> = mutableListOf()

        //todo(GroupItem): set Group type for product is here
        orderMenuResp.getMenuGroupItemListWithGroupId(groupGuid)?.forEach {
            it?.let { it1 ->
                orderMenuResp.getProductWithItemGuid(it1.itemGuid)?.toMutableList()?.let { it2 ->
                    rs.addAll(
                        it2
                    )
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