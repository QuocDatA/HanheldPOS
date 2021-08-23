package com.hanheldpos.ui.screens.product.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.hanheldpos.data.api.pojo.product.SliderData
import com.hanheldpos.ui.screens.home.order.OrderFragment
import com.hanheldpos.ui.screens.product.ProductDetailFragment
import com.smarteist.autoimageslider.SliderViewAdapter


internal class SliderAdapter(sliderDataArrayList: ArrayList<SliderData>) :
    SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder>() {

    // list for storing urls of images.
    private val mSliderItems: List<SliderData>

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterViewHolder {
        val inflate: View =
            LayoutInflater.from(parent.context).inflate(com.hanheldpos.R.layout.item_slider_layout, null)
        return SliderAdapterViewHolder(inflate)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterViewHolder, position: Int) {
        val sliderItem = mSliderItems[position]

        // Glide is use to load image
        // from url in your imageview.
        Glide.with(viewHolder.itemView)
            .load(sliderItem.getImgUrl())
            .fitCenter()
            .into(viewHolder.imageViewBackground)
    }

    override fun getCount(): Int {
        return mSliderItems.size
    }

    internal class SliderAdapterViewHolder(itemView: View) : ViewHolder(itemView) {
        var itemViewer: View = itemView
        var imageViewBackground: ImageView = itemView.findViewById(com.hanheldpos.R.id.myimage)

    }

    // Constructor
    init {
        mSliderItems = sliderDataArrayList
    }
}
