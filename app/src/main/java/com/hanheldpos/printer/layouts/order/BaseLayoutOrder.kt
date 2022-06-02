package com.example.pos2.printer.layouts.order

import com.example.pos2.model.DataHelper
import com.example.pos2.model.order.OrderModel
import com.example.pos2.printer.layouts.BaseLayoutPrinter
import com.example.pos2.printer_setup.PrintOptions
import com.example.pos2.printer_setup.printer_manager.BasePrinterManager
import com.example.pos2.utils.StringHelper
import com.example.pos2.utils.time.DateTimeHelper
import com.example.pos2.wagu.Block
import com.example.pos2.wagu.WaguUtils

abstract class BaseLayoutOrder(
    protected val order: OrderModel,
    printer: BasePrinterManager,
    printOptions: PrintOptions,
    private val isReprint: Boolean,
) : BaseLayoutPrinter(printer, printOptions) {
    /// region print methods

    protected open fun printBillStatus() {
        val status = mutableListOf<String>()
        val statusColumns = mutableListOf<Int>()
        status.add(order.orderDetail.tableList?.firstOrNull()?.tableName.toString())
        statusColumns.add(Block.DATA_MIDDLE_LEFT)
        if (isReprint) {
            status.add("COPY")
            statusColumns.add(Block.DATA_CENTER)
        }
        status.add(order.orderDetail.diningOption?.acronymn.toString())
        statusColumns.add(Block.DATA_MIDDLE_RIGHT)


        printer.drawText(
            StringHelper.removeAccent(
                WaguUtils.columnListDataBlock(
                    charPerLineNormal,
                    list = mutableListOf(status),
                    statusColumns,
                )
            ).toString(),
            false,
            BasePrinterManager.FontSize.Large
        )
    }

    protected fun printOrderInfo() {
        val orderCode = mutableListOf("Order #:", order.order.code)
        val employee =
            mutableListOf(
                "Employee:",
                DataHelper.getEmployeeListByDeviceCode()
                    ?.find { it?.id == order.order.employeeGuid }?.fullName.toString()
            )
        val dateCreate = mutableListOf(
            "Create Date:", DateTimeHelper.dateToString(
                DateTimeHelper.strToDate(
                    order.order.createDate,
                    DateTimeHelper.Format.FULL_DATE_UTC
                ), DateTimeHelper.Format.DD_MM_YYYY_HH_MM
            )
        )
        val content = WaguUtils.columnListDataBlock(
            charPerLineNormal, mutableListOf(orderCode, employee, dateCreate),
            mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT), isWrapWord = true
        )
        printer.drawText(content)
    }

    protected fun printNote(drawBottomLine: Boolean = true) {
        order.orderDetail.order?.note?.takeIf { it.isNotEmpty() }?.let {
            printer.drawText(StringHelper.removeAccent("Note: $it"))
            if (drawBottomLine)
                printer.drawLine(charPerLineNormal)
        }
    }

    /// endregion
}