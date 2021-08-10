package com.hanheldpos.model.home.order.type

enum class OrderMenuModeViewType(
    val value: Int
) {
    TextColor(1),
    TextImage(2);


    companion object {
        fun fromInt(value: Int?): OrderMenuModeViewType {

            return when(value){
                TextColor.value -> TextColor;
                else -> {
                    TextImage;
                }
            }
        }
    }
}