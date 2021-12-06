package com.hanheldpos.model.order

enum class OrderStatus(val value : Int) {
    DRAFT (1),
    EMAIL (2),
    PHONE (3),
    ORDER (4),
    KITCHENT (5),
    COMFIRMED ( 6),
    PACKING (7),
    SHIPPED (8),
    PREPAID (9),
    COMPLETED (10),
    CANCEL (11),
    PICKING (12),
    REJECT (13)
}