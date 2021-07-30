package com.hanheldpos.ui.base.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.hanheldpos.ui.screens.home.HomeFragment

abstract class BaseSpinnerAdapter <T, V : ViewDataBinding>(
    context: Context,
    private val resId: Int
) : ArrayAdapter<T>(context, resId) {

    abstract fun binding(binding: V?, item: T?)

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View? {
        return getCustomView(position, convertView, parent)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    override fun addAll(collection: Collection<T?>) {
        super.addAll(collection)
    }

    open fun submitList(collection: Collection<T?>) {
        clear()
        super.addAll(collection)
    }

    private fun getCustomView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutBinding: V = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            resId,
            null,
            true
        )
        val item = getItem(position)
        binding(layoutBinding, item)

        return layoutBinding.root
    }

}