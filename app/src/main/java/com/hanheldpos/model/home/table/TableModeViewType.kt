package com.hanheldpos.model.home.table

enum class TableModeViewType(val value : Int,var pos : Int? = 0) {
    Table(1),
    PrevButton(2),
    NextButton(3),
    Empty(4);
}