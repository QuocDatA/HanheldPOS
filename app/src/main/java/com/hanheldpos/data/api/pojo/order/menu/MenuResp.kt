package com.hanheldpos.data.api.pojo.order.menu

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.product.Product
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuResp(
    val CategoryList: List<Category>?,
    val GroupList: List<Group>?,
    val HierarchyList: List<Hierarchy>?,
    val MenuList: List<Menu>?,
    val ProductList: List<Product>?
) : Parcelable