package com.hanheldpos.ui.screens.common

import android.content.Context
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemDropdownSpinnerBinding
import com.hanheldpos.ui.base.adapter.BaseSpinnerAdapter

class StringSpinnerAdapter(context : Context) : BaseSpinnerAdapter<String,ItemDropdownSpinnerBinding>(
    context,
    R.layout.item_dropdown_spinner
) {
    override fun binding(binding: ItemDropdownSpinnerBinding?, item: String?) {
        if(item == null) return;

        binding?.text = item;
    }
}