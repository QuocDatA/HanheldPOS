package com.hanheldpos.model.home.order.menu


import com.hanheldpos.data.api.pojo.order.menu.*
import com.hanheldpos.data.api.pojo.product.ProductItem

object OrderMenuDataMapper {
    lateinit var menuDB: OrderMenuResp

    /**
     *  Get Menu Child List of a OrderMenuItem by find in the OrderMenuResp
     *  Note: this should call when an item is click in the OrderMenu list
     */
    private fun OrderMenuItem.getChildList(): MutableList<ProductMenuItem> {
        val rs = mutableListOf<ProductMenuItem>()

        if (this.nodeItem is ListToHierarchyItem) {
            val listToHierarchyItem = this.nodeItem as ListToHierarchyItem
            when (listToHierarchyItem.menusType) {
                1 -> { // is GroupItem
                    // Find all Product with sGroupItem
                    rs.addAll(
                        getProductItemListByGroupGuid(listToHierarchyItem.groupGuid)
                    )
                }
                2 -> { // is Category
                    // Find all product in category
                    rs.addAll(
                        getProductItemListByCategoryGuid(listToHierarchyItem.groupGuid)
                    )
                }
            }
        } else {
            if (this.mappedItem is GroupsItem) {
                val groupGuid = (this.mappedItem as GroupsItem).id
                rs.addAll(
                    getProductItemListByGroupGuid(groupGuid)
                )

            } else if (this.mappedItem is CategoryItem) {
                val categoryGuid = (this.mappedItem as CategoryItem).id
                rs.addAll(
                    getProductItemListByCategoryGuid(categoryGuid)
                )
            }
        }
        return rs
    }

    /**
     *  Get Combo List of a Selected Combo by find in the OrderMenuResp
     *  groupTab = id of group to get, if =0 means get all
     *  Note: this should call when an item is click in the User selected Combo Type in Combo Group
     */
//    fun ProductItem.getComboList(groupTab: Int): MutableList<ProductItem> {
//        val rs = mutableListOf<ProductItem>()
//        val productComboList = getProductComboList(combo)
//        if (groupTab == 0) {
//            this.productComboList?.map {
//                rs.addAll(getProductItemListByComboGuid(it.comboGuid))
//            }
//        } else {
//            this.productComboList?.map {
//                if (it.id == groupTab) {
//                    rs.addAll(getProductItemListByComboGuid(it.comboGuid))
//                    return@map
//                }
//            }
//        }
//        return rs
//    }

    /**
     * Find product list in groups by using @ComboGuid in combo
     */
    fun getProductItemListByComboGuid(comboGuid: String?): MutableList<ProductItem> {
        val rs: MutableList<ProductItem> = mutableListOf()
        menuDB.getMenuGroupItemListWithGroupId(comboGuid)?.forEach { it ->
            //Get product id that enabled in combo hold in menu group
            it?.itemGuid!!.let {
                menuDB.getProductWithItemGuid(it)?.forEach { product ->
                    rs.add(product)
                }
            }
        }
        return rs
    }

    fun getGroupNameFromGroupGuid(groupGuid: String?) =
        menuDB.getGroupItem(groupGuid)?.groupName

    fun getMenuGroupItemListFromItemGuid(itemGuid: String?) =
        menuDB.getMenuGroupItemListWithItemId(itemGuid)

    /**
     * Find all product by @categoryGuid and transforms to OrderMenuItem List
     */
    private fun getProductItemListByCategoryGuid(categoryGuid: String?): MutableList<ProductMenuItem> {
        val rs: MutableList<ProductMenuItem> = mutableListOf()

        menuDB.getProductWithCategoryGuid(categoryGuid)?.forEach {
            rs.add(ProductMenuItem(it))
        };
        return rs
    }

    /**
     * Find all product by @groupGuid and transforms to OrderMenuItem List
     */
    private fun getProductItemListByGroupGuid(groupGuid: String?): MutableList<ProductMenuItem> {
        val rs: MutableList<ProductMenuItem> = mutableListOf()

        //todo(GroupItem): set Group type for product is here
        menuDB.getMenuGroupItemListWithGroupId(groupGuid)?.forEach {
            it?.let { it1 ->
                menuDB.getProductWithItemGuid(it1.itemGuid)?.toMutableList()?.forEach { it2 ->
                    rs.add(ProductMenuItem(it2))
                }
            }
        }
        return rs
    }

    //region ### Get ProductItem by ListToHierarchyItem

    fun getMenuByBranch(branchIndex: Int): List<OrderMenuItem> {
        val rs = mutableListOf<OrderMenuItem>()
        menuDB.getHierarchyList()?.forEach {
            it.let {
                val menuIdInMenuList = menuDB.getMenuList()?.get(branchIndex)?.id
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
     * Get OrderMenuItem by @listToHierarchyItem
     */
    private fun getOrderMenuItemList(
        listToHierarchyItem: ListToHierarchyItem,
        parent: OrderMenuItem?
    ): List<OrderMenuItem>? {
        val rs: MutableList<OrderMenuItem> = mutableListOf()
        val orderItem = OrderMenuItem()

        orderItem.nodeItem = listToHierarchyItem

        val groupGuid = listToHierarchyItem.groupGuid ?: return null
        when (listToHierarchyItem.menusType) {
            0 -> { // SystemGroupItem
                // If it's a SystemGroupItem then get all the child inside an return a list OrderMenuItem
                // with the same level as the current checking level
                // If the SystemGroupItem has no child then return null

//                listToHierarchyItem.children?.forEach {
//                    it?.let {
//                        return getOrderMenuItemList(it, parent)
//                    }
//                }
                return null
            }
            1 -> { // Groups
                // TODO: the flow of the group is not clear specially when get the group child
                orderItem.setGroupOrderItem(menuDB, groupGuid)
            }
            2 -> { // Category
                // groupGuid --> in this case is CategoryGuid
                orderItem.setCategoryOrderItem(menuDB, groupGuid)
            }
        }
        rs.add(orderItem)
        return rs
    }

    //endregion
}