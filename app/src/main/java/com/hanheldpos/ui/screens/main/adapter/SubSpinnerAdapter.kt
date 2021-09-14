package com.hanheldpos.ui.screens.main.adapter

import android.content.Context
import android.os.Parcelable
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemDropdownSpinnerBinding
import com.hanheldpos.ui.base.adapter.BaseSpinnerAdapter
import com.hanheldpos.ui.screens.home.DropDownItem

class SubSpinnerAdapter(context: Context) :
    BaseSpinnerAdapter<Parcelable, ItemDropdownSpinnerBinding>(
        context,
        R.layout.item_dropdown_spinner
    ) {
    override fun binding(binding: ItemDropdownSpinnerBinding?, item: Parcelable?) {
        if(item == null) return;
        if(item is DropDownItem){
            binding?.text = item.name;
        }
    }
}