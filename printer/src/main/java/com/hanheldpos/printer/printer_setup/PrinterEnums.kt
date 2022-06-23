package com.hanheldpos.printer.printer_setup

enum class PrinterConnectionTypes(val value: Int) {
    LAN(1),
    BLUETOOTH(3),
}

enum class PrinterTypes(val value: Int) {
    CASHIER(1),
    EXPO(2),
    PHO(3),
    KITCHEN(4),
}

enum class PrinterRecipeType(val value: Int) {
    CASHIER(1),
    KITCHEN(2),
}