package com.example.pos2.printer.layouts

enum class LayoutType {
    ;

    enum class Order {
        Cashier,
        Kitchen,
    }

    enum class Report {
        Overview,
        Inventory,
    }
}