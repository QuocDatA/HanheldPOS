package com.hanheldpos.model.order

enum class OrderStatus(val value : Int) {
    DRAFT (1),
    EMAIL (2),
    PHONE (3),
    ORDER (4),
    COMFIRMED (5),
    KITCHENT  ( 6),
    PACKING (7),
    SHIPPED (8),
    PREPAID (9),
    COMPLETED (10),
    CANCEL (11),
    PICKING (12),
    REJECT (13);
    companion object {
        fun fromInt(value: Int): OrderStatus? {
            values().forEach {
                if (it.value == value) {
                    return it
                }
            }
            return null
        }
    }
}