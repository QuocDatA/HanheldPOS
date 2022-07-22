package com.hanheldpos.ui.screens.menu.settings.adapter

import android.content.Context
import android.os.Parcelable
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemDropdownSpinnerAlignLeftBinding
import com.hanheldpos.ui.base.adapter.BaseSpinnerAdapter
import com.hanheldpos.ui.screens.home.DropDownItem

class BillLanguageOptionSpinner(context: Context) :
    BaseSpinnerAdapter<Parcelable, ItemDropdownSpinnerAlignLeftBinding>(
        context,
        R.layout.item_dropdown_spinner_align_left
    ) {
    override fun binding(binding: ItemDropdownSpinnerAlignLeftBinding?, item: Parcelable?) {
        if (item == null) return
        if (item is DropDownItem) {
            binding?.text = item.name
        }
    }
}