package com.hanheldpos.data.api.pojo.order.menu

internal fun OrderMenuResp.getModel() = this.model?.firstOrNull()

internal fun OrderMenuResp.getListMenuGroupItemTree() = getModel()?.listMenusGroupItemTree

internal fun OrderMenuResp.getHierarchyList() = this.getListMenuGroupItemTree()?.listToHierarchy

internal fun OrderMenuResp.getDomainImage() = getModel()?.domainImages

//region Group

internal fun OrderMenuResp.getGroupList() = this.getModel()?.groups

internal fun OrderMenuResp.getGroupItem(groupGuid: String?) =
    this.getGroupList()?.find { it?.id.equals(groupGuid) }

internal fun OrderMenuResp.getGroupImage() = getModel()?.groupsImages

internal fun OrderMenuResp.getGroupImageListWithId(groupGuid: String?) =
    this.getGroupImage()?.filter { it?.groupGuid.equals(groupGuid) }

//endregion

//region Category

internal fun OrderMenuResp.getCategoryList() = this.getModel()?.category

internal fun OrderMenuResp.getCategoryItem(id: String?) =
    this.getCategoryList()?.find { it?.id.equals(id) }

internal fun OrderMenuResp.getCategoryImage() = getModel()?.categoryImages

internal fun OrderMenuResp.getCategoryImageListWithId(groupGuid: String?) =
    this.getCategoryImage()?.filter { it?.categoryGuid.equals(groupGuid) }

//endregion

//region Menus

internal fun OrderMenuResp.getMenuList() = getModel()?.menus

//endregion

//region Menu Group Item

internal fun OrderMenuResp.getMenuGroupItemList() = getModel()?.menusGroupItem

internal fun OrderMenuResp.getMenuGroupItemListWithGroupId(groupGuid: String?) =
    this.getMenuGroupItemList()?.filter { it?.groupGuid == groupGuid }

internal fun OrderMenuResp.getMenuGroupItemListWithItemId(itemGuid: String?) =
    this.getMenuGroupItemList()?.filter { it?.itemGuid == itemGuid }

//endregion

//region Product

internal fun OrderMenuResp.getListProduct() = this.getModel()?.listProduct?.firstOrNull()

internal fun OrderMenuResp.getProductByListProduct() = this.getListProduct()?.product

internal fun OrderMenuResp.getProductModifierByListProduct() =
    this.getListProduct()?.productModifier

internal fun OrderMenuResp.getModifierItemByListProduct() = this.getListProduct()?.modifierItem

internal fun OrderMenuResp.getProductWithItemGuid(itemGuid: String?) =
    this.getProductByListProduct()?.filter {
        it?.id == itemGuid
    }

internal fun OrderMenuResp.getProductWithCategoryGuid(categoryGuid: String?) =
    this.getProductByListProduct()?.filter {
        it?.categoryGuid == categoryGuid
    }


internal fun OrderMenuResp.getProductImage() = this.getListProduct()?.productImages

internal fun OrderMenuResp.getProductImageListWithId(productGuid: String?) =
    this.getProductImage()?.filter { it?.productGuid.equals(productGuid) }

internal fun OrderMenuResp.getUnitList() = this.getListProduct()?.units
//endregion