package com.hanheldpos.ui.screens.home.table.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.model.home.TableModel
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class TableAdapter(
    listener: BaseItemClickListener<TableModel>
) : BaseBindingListAdapter<TableModel>(DiffCallBack(),listener) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_table;
    }

    private class DiffCallBack : DiffUtil.ItemCallback<TableModel>(){
        override fun areItemsTheSame(oldItem: TableModel, newItem: TableModel): Boolean {
            return oldItem == newItem;
        }

        override fun areContentsTheSame(oldItem: TableModel, newItem: TableModel): Boolean {
            return oldItem == newItem;
        }

    }
}