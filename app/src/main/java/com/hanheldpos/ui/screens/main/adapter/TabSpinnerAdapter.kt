package com.hanheldpos.ui.screens.main.adapter

import android.content.Context
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemDropdownSpinnerBinding
import com.hanheldpos.ui.base.adapter.BaseSpinnerAdapter
import com.hanheldpos.ui.screens.home.HomeFragment

class TabSpinnerAdapter(context: Context) :
    BaseSpinnerAdapter<HomeFragment.HomePage, ItemDropdownSpinnerBinding>(
        context,
        R.layout.item_dropdown_spinner
    ) {
    override fun binding(binding: ItemDropdownSpinnerBinding?, item: HomeFragment.HomePage?) {
        if (item == null) return;

        binding?.text = context.getString(item.textId)
    }


}