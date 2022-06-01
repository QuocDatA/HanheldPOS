package com.hanheldpos.model.printer.layouts

import com.handheld.printer.printer_manager.BasePrinterManager
import com.hanheldpos.model.order.OrderModel
import com.hanheldpos.model.printer.BillPrinterManager


object BaseLayoutPrinterFactory {
    fun getLayout(
        order: OrderModel,
        printer: BasePrinterManager,
        printOptions: BillPrinterManager.PrintOptions,
        layoutType: BaseLayoutPrinter.LayoutType
    ): BaseLayoutPrinter {
        return when (layoutType) {
            BaseLayoutPrinter.LayoutType.Cashier -> CashierLayout(
                order,
                printer,
                printOptions
            )
            BaseLayoutPrinter.LayoutType.Kitchen -> KitchenLayout(
                order,
                printer,
                printOptions
            )
            BaseLayoutPrinter.LayoutType.Reprint -> ReprintLayout(
                order,
                printer,
                printOptions
            )
        }
    }
}

