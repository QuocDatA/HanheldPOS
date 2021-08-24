package com.hanheldpos.data.api.pojo.product

import com.google.gson.annotations.SerializedName

class SliderData(url: String) {

    @field:SerializedName("imgUrl")
    private var imgUrl: String? = url

    // Getter method
    fun getImgUrl(): String? {
        return imgUrl
    }

    // Setter method
    fun setImgUrl(imgUrl: String?) {
        this.imgUrl = imgUrl
    }
}