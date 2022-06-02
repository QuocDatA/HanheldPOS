package com.hanheldpos.model.order

data class GroupProductChosen(
    val index : Int,
    val title : String,
    val productChosenList : List<ProductChosen>
)