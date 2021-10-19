package com.hanheldpos.ui.screens.product.adapter.variant

import java.lang.ref.WeakReference

data class VariantLayoutItem(
    var name: String,
    val header: WeakReference<VariantHeader>,
    var isSelected : Boolean = false
)