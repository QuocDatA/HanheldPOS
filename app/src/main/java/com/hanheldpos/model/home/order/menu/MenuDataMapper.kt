package com.hanheldpos.model.home.order.menu


import com.hanheldpos.data.api.pojo.order.menu.*

object MenuDataMapper {
    /**
     *  Get Menu Child List of a OrderMenuItem by find in the OrderMenuResp
     *  Note: this should call when an item is click in the OrderMenu list
     */
    private fun OrderMenuItem.getChildList(menuResp: MenuResp): MutableList<ProductMenuItem> {
        val rs = mutableListOf<ProductMenuItem>()

        if (this.nodeItem is Hierarchy) {
            val hierarchyItem = this.nodeItem as Hierarchy
            when (MenusType.fromInt(hierarchyItem.MenusType)) {
                MenusType.Group -> { // is Group
                    // Find all Product with sGroupItem
                    rs.addAll(
                        getProductListByGroupGuid(hierarchyItem.GroupGuid, menuResp)
                    )
                }
                MenusType.Category -> { // is Category
                    // Find all product in category
                    rs.addAll(
                        getProductListByCategoryGuid(hierarchyItem.GroupGuid, menuResp)
                    )
                }
                else -> {}
            }
        } else {
            if (this.mappedItem is Group) {
                val groupGuid = (this.mappedItem as Group)._Id
                rs.addAll(
                    getProductListByGroupGuid(groupGuid,menuResp)
                )

            } else if (this.mappedItem is Category) {
                val categoryGuid = (this.mappedItem as Category)._Id
                rs.addAll(
                    getProductListByCategoryGuid(categoryGuid,menuResp)
                )
            }
        }
        return rs
    }

    fun getGroupNameFromGroupGuid(groupGuid: String?,menuResp: MenuResp) =
        menuResp.GroupList?.find { it._Id == groupGuid }?.GroupName

    /**
     * Find all product by @categoryGuid and transforms to OrderMenuItem List
     */
    fun getProductListByCategoryGuid(
        categoryGuid: String?,
        menuResp: MenuResp
    ): MutableList<ProductMenuItem> {
        val rs: MutableList<ProductMenuItem> = mutableListOf()

        menuResp.CategoryList?.filter { it._Id == categoryGuid }?.forEach {
            val listProductInCategory = it.ProductList.map { product -> product._id };
            menuResp.ProductList?.filter { product -> product._id in listProductInCategory }?.toMutableList()?.map { product -> ProductMenuItem(product) }
                ?.let { it1 -> rs .addAll(it1) }
        };
        return rs
    }

    /**
     * Find all product by @groupGuid and transforms to OrderMenuItem List
     */
    fun getProductListByGroupGuid(
        groupGuid: String?,
        menuResp: MenuResp
    ): MutableList<ProductMenuItem> {
        val rs: MutableList<ProductMenuItem> = mutableListOf()

        //todo(GroupItem): set Group type for product is here
        menuResp.GroupList?.filter { it._Id == groupGuid }?.forEach {
            val listProductInGroup = it.ProductList.map { product -> product._id };
            menuResp.ProductList?.filter { product -> product._id in listProductInGroup }?.toMutableList()?.map { product -> ProductMenuItem(product) }
                ?.let { it1 -> rs .addAll(it1) }
        }
        return rs
    }

    //region ### Get ProductItem by ListToHierarchyItem

    fun getMenuByBranch(branchIndex: Int, menuResp: MenuResp): List<OrderMenuItem> {
        val rs = mutableListOf<OrderMenuItem>()
        menuResp.HierarchyList?.forEach {
            it.let {
                val menuIdInMenuList = menuResp.MenuList?.get(branchIndex)?._Id
                if (it.MenusGuid == menuIdInMenuList) {
                    val orderMenuItemList = getOrderMenuItemList(it,menuResp)

                    if (!orderMenuItemList.isNullOrEmpty()) {
                        rs.addAll(orderMenuItemList)
                    }
                }
            }
        }

        rs.forEach {
            it.childList = it.getChildList(menuResp);
        }

        return rs
    }


    /**
     * Get OrderMenuItem by @listToHierarchyItem
     */
    private fun getOrderMenuItemList(
        hierarchyItem: Hierarchy,
        menuResp: MenuResp,
    ): List<OrderMenuItem>? {
        val rs: MutableList<OrderMenuItem> = mutableListOf()
        val orderItem = OrderMenuItem()

        orderItem.nodeItem = hierarchyItem

        val groupGuid = hierarchyItem.GroupGuid ?: return null
        when (MenusType.fromInt(hierarchyItem.MenusType)) {
            MenusType.SYSTEM_GROUP -> { // SystemGroupItem
                return null
            }
            MenusType.Group -> { // Groups
                // TODO: the flow of the group is not clear specially when get the group child
                orderItem.setGroupOrderItem(menuResp, groupGuid)
            }
            MenusType.Category -> { // Category
                // groupGuid --> in this case is CategoryGuid
                orderItem.setCategoryOrderItem(menuResp, groupGuid)
            }
            else -> {}
        }
        rs.add(orderItem)
        return rs
    }

    //endregion
}