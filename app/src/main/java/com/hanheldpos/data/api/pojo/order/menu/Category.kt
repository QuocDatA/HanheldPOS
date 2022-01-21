package com.hanheldpos.data.api.pojo.order.menu

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.product.Product
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val Ansi: String,
    val CategoryId: Int,
    val Color: String,
    val CreateDate: String,
    val Description: String,
    val Guid: String,
    val Handle: String,
    val OrderNo: Int,
    val ProductList: List<Product>,
    val ReferenceId: String,
    val Title: String,
    val Url: String?,
    val Visible: Int,
    val _Id: String,
    val _key: Int,
    val _rev: String
) : Parcelable