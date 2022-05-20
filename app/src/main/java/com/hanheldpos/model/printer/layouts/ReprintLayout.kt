package com.hanheldpos.model.printer.layouts

import android.content.Context
import com.handheld.printer.printer_manager.BasePrinterManager
import com.hanheldpos.model.order.OrderReq
import com.hanheldpos.model.printer.BillPrinterManager

class ReprintLayout(
    context: Context,
    order: OrderReq,
    printer: BasePrinterManager,
    printOptions: BillPrinterManager.PrintOptions
) : KitchenLayout(
    context, order, printer, printOptions
) {
    override fun printBillStatus(isReprint: Boolean) {
        super.printBillStatus(true)
    }
}