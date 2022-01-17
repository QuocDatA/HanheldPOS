package com.hanheldpos.data.api.pojo.order.menu

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.product.Product
import kotlinx.parcelize.Parcelize

@Parcelize
data class Group(
    val Color: String,
    val CreateDate: String,
    val Description: String,
    val GroupName: String,
    val Handle: String,
    val MenusType: Int,
    val OrderNo: Int,
    val ProductList: List<Product>,
    val Url: String?,
    val UserGuid: String,
    val Visible: Int,
    val _Id: String,
    val _key: Int,
    val _rev: String
) : Parcelable