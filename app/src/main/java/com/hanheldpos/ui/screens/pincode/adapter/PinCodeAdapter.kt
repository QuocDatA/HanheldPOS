package com.hanheldpos.ui.screens.pincode.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemPinCodeBinding
import com.hanheldpos.ui.screens.pincode.PinCodeRecyclerElement

class PinCodeAdapter(
    private val elements: List<PinCodeRecyclerElement>,
    private val listener: ItemClickListener?
) : RecyclerView.Adapter<PinCodeAdapter.PinCodeVH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PinCodeVH {
        val dataBinding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_pin_code,
            parent,
            false
        )
        return PinCodeVH(dataBinding)
    }



    override fun onBindViewHolder(holder: PinCodeVH, position: Int) {
        val item = elements[position]
        holder.bind(item)

    }

    override fun getItemCount(): Int {
        return elements.size
    }

    inner class PinCodeVH(var dataBinding: ViewDataBinding) : RecyclerView.ViewHolder(dataBinding.root) {
        private val binding = dataBinding as ItemPinCodeBinding
        fun bind(item: PinCodeRecyclerElement) {
            binding.item = item
            binding.btnPinItem.setOnClickListener {
                listener?.onClick(item)
            }
        }
    }

    interface ItemClickListener {
        fun onClick(item: PinCodeRecyclerElement?)
    }
}
