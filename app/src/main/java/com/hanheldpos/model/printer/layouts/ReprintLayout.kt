package com.hanheldpos.model.printer.layouts

import com.handheld.printer.printer_manager.BasePrinterManager
import com.hanheldpos.model.order.OrderModel
import com.hanheldpos.model.printer.BillPrinterManager

class ReprintLayout(

    order: OrderModel,
    printer: BasePrinterManager,
    printOptions: BillPrinterManager.PrintOptions
) : KitchenLayout(
     order, printer, printOptions
) {
    override fun printBillStatus(isReprint: Boolean) {
        super.printBillStatus(true)
    }
}