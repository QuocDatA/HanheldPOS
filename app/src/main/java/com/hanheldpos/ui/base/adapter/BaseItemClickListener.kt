package com.hanheldpos.ui.base.adapter

interface BaseItemClickListener<T> {
    fun onItemClick(adapterPosition: Int, item: T)
}