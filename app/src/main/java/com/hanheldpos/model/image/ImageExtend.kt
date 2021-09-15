package com.hanheldpos.model.image

import com.hanheldpos.data.api.ApiConst
import com.hanheldpos.data.api.pojo.order.*
import com.hanheldpos.data.api.pojo.order.getDomainImage
import com.hanheldpos.data.api.pojo.order.getProductImageListWithId
import com.hanheldpos.extension.isHTTP

internal fun GroupsItem.getImageUrl(orderMenuResp: OrderMenuResp, groupGuid: String?): String? {
    if (groupGuid == null) return null

    val imageListById = orderMenuResp.getGroupImageListWithId(groupGuid)

    imageListById?.forEach {
        if (it?.styleId == ImageStyleType.Group.value) {

            if (!it.url.isNullOrEmpty()) {

                it.url.isHTTP {
                    return it.url
                }
            }

            return orderMenuResp.getDomainImage() + ApiConst.PHOTO_DIR + it.url
        }
    }
    return null
}

internal fun CategoryItem.getImageUrl(orderMenuResp: OrderMenuResp, groupGuid: String?): String? {
    if (groupGuid == null) return null

    val imageByListId = orderMenuResp.getCategoryImageListWithId(groupGuid)

    imageByListId?.forEach {
        if (it?.styleId == ImageStyleType.Category.value) {

            if (!it.url.isNullOrEmpty()) {

                it.url.isHTTP {
                    return it.url
                }
            }

            return orderMenuResp.getDomainImage() + ApiConst.PHOTO_DIR + it.url
        }
    }
    return null
}

internal fun getImageUrl(orderMenuResp: OrderMenuResp, productGuid: String?): String? {
    if (productGuid == null) return null

    val productImageList = orderMenuResp.getProductImageListWithId(productGuid)

    productImageList?.forEach {
        it?.let {
            if (!it.url.isNullOrEmpty()) {
                var rs = it.url
                rs = rs.replace("600_600", "252_252")

                rs.isHTTP {
                    return rs
                }

                return orderMenuResp.getDomainImage() + ApiConst.PHOTO_DIR + rs
            }
        }
    }
    return null
}