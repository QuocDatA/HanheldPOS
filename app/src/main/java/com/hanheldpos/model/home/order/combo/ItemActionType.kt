package com.hanheldpos.model.home.order.combo

enum class ItemActionType(
    val value: Int
) {
    Add(1),
    Modify(2),
    Remove(3);

    companion object {
        fun fromInt(value: Int): ItemActionType? {
            values().forEach {
                if (it.value == value) {
                    return it
                }
            }
            return null
        }
    }
}