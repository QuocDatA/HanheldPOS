package com.hanheldpos.model.home.table

enum class TableStatusType(
    val value: Int
) {
    Available(1),
    Held(2),
    Unavailable(3);

    companion object {
        fun fromInt(value: Int?): TableStatusType? {
            return values().find {
                it.value == value
            }
        }
    }
}