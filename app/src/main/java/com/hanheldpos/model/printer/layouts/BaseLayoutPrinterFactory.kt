package com.hanheldpos.model.printer.layouts

import android.content.Context
import com.handheld.printer.printer_manager.BasePrinterManager
import com.hanheldpos.model.order.OrderReq
import com.hanheldpos.model.printer.BillPrinterManager


object BaseLayoutPrinterFactory {
    fun getLayout(
        context: Context,
        order: OrderReq,
        printer: BasePrinterManager,
        printOptions: BillPrinterManager.PrintOptions,
        layoutType: BaseLayoutPrinter.LayoutType
    ): BaseLayoutPrinter {
        return when (layoutType) {
            BaseLayoutPrinter.LayoutType.Cashier -> CashierLayout(
                context,
                order,
                printer,
                printOptions
            )
            BaseLayoutPrinter.LayoutType.Kitchen -> KitchenLayout(
                context,
                order,
                printer,
                printOptions
            )
            BaseLayoutPrinter.LayoutType.Reprint -> ReprintLayout(
                context,
                order,
                printer,
                printOptions
            )
        }
    }
}

