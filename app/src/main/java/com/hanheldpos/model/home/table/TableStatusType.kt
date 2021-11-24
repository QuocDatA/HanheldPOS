package com.hanheldpos.model.home.table

enum class TableStatusType(
    val value: Int
) {
    Available(1),
    Pending(2),
    Unavailable(3);

    companion object {
        fun fromInt(value: Int?): TableStatusType? {
            return values().find {
                it.value == value
            }
        }
    }
}