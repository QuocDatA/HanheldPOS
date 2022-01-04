package com.hanheldpos.model.discount

enum class CtmEligibilityType(val value: Int) {
    EVERYONE(1),
    SPECIFIC_GROUP_CUSTOMERS(2),
    SPECIFIC_CUSTOMER(3);
    companion object {
        fun fromInt(value: Int): CtmEligibilityType? {
            CtmEligibilityType.values().forEach {
                if (it.value == value) {
                    return it
                }
            }
            return null
        }
    }
}