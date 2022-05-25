package com.hanheldpos.extension

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


fun RecyclerView.addItemDecorationWithoutLastDivider() {

    addItemDecoration(object :
        DividerItemDecoration(context, (layoutManager as LinearLayoutManager).orientation) {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position: Int =
                parent.getChildAdapterPosition(
                    view
                )
            if (position == state.itemCount - 1) {
                outRect.setEmpty()
            } else {
                super.getItemOffsets(
                    outRect,
                    view,
                    parent,
                    state
                )
            }
        }
    })
}