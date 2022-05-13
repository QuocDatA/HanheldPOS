package com.hanheldpos.ui.screens.buy_x_get_y.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.hanheldpos.databinding.ItemComboPickedBinding
import com.hanheldpos.databinding.ItemComboRegularBinding
import com.hanheldpos.databinding.ItemOrderProductBinding
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.home.order.menu.ProductMenuItem

class ProductTypePagerAdapter(private val context: Context, private val itemList: List<Regular>) :
    PagerAdapter() {

    override fun getCount(): Int {
        return itemList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding =
            ItemComboRegularBinding.inflate(LayoutInflater.from(context), container, false)
        binding.item = itemList[position]
        container.addView(binding.root)
        return binding.root
    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }
}