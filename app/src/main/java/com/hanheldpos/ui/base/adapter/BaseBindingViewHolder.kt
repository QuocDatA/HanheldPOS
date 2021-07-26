package com.hanheldpos.ui.base.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class BaseBindingViewHolder<T>(
    val binding: ViewDataBinding,
    private val itemClickListener: BaseItemClickListener<T>? = null
) : RecyclerView.ViewHolder(binding.root) {

    private var item: T? = null

    fun bindItem(item: T?) {
        item?.let {
            this.item = it
            binding.apply {
                //TODO enable it when using recycler item
//                setVariable(BR.item, it)
                executePendingBindings()
            }
        }
        initActions()
    }

    private fun initActions() {
        binding.root.setOnClickListener {
            itemClickListener?.onItemClick(absoluteAdapterPosition, item!!)
        }
    }
}