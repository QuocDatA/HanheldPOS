package com.hanheldpos.printer.layouts.order


import com.hanheldpos.printer.printer_setup.PrintOptions
import com.hanheldpos.printer.printer_setup.printer_manager.BasePrinterManager
import com.hanheldpos.printer.wagu.Block
import com.hanheldpos.printer.wagu.WaguUtils
import com.hanheldpos.printer.wagu.WrapType
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.order.OrderModel
import com.hanheldpos.printer.layouts.BaseLayoutPrinter
import com.hanheldpos.utils.DateTimeUtils
import com.hanheldpos.utils.StringUtils

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
        status.add(order.OrderDetail.TableList.firstOrNull()?.TableName.toString())
        statusColumns.add(Block.DATA_MIDDLE_LEFT)
        if (isReprint) {
            status.add("COPY")
            statusColumns.add(Block.DATA_CENTER)
        }
        status.add(order.OrderDetail.DiningOption.Acronymn.toString())
        statusColumns.add(Block.DATA_MIDDLE_RIGHT)


        printer.drawText(
            StringUtils.removeAccent(
                WaguUtils.columnListDataBlock(
                    charPerLineLarge,
                    list = mutableListOf(status),
                    statusColumns,
                )
            ).toString(),
            false,
            BasePrinterManager.FontSize.Large
        )
    }

    protected fun printOrderInfo() {
        val listPrintInfo = mutableListOf<MutableList<String>>()
        val orderCode = mutableListOf("Order #:", order.Order.Code.toString())
        listPrintInfo.add(orderCode)
        DataHelper.receiptCashierLocalStorage?.let {
            listPrintInfo.add(mutableListOf("Location:",it.LocationBusinessName))
        }
        val employee =
            mutableListOf(
                "Employee:",
                DataHelper.deviceCodeLocalStorage?.Employees?.find { it._id == order.Order.EmployeeGuid }?.FullName.toString()
            )
        listPrintInfo.add(employee)
        val dateCreate = mutableListOf(
            "Create Date:", DateTimeUtils.dateToString(
                DateTimeUtils.strToDate(
                    order.Order.CreateDate,
                    DateTimeUtils.Format.YYYY_MM_DD_HH_MM_SS
                ), DateTimeUtils.Format.DD_MM_YYYY_HH_MM
            )
        )
        listPrintInfo.add(dateCreate)
        val content = WaguUtils.columnListDataBlock(
            charPerLineNormal, listPrintInfo,
            mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT), wrapType = WrapType.SOFT_WRAP
        )
        printer.drawText(content)
    }

    protected fun printNote(drawBottomLine: Boolean = true) {
        order.OrderDetail.Order.Note?.takeIf { it.isNotEmpty() }?.let {
            printer.drawText(StringUtils.removeAccent("Note: $it"))
            if (drawBottomLine)
                printer.drawLine(charPerLineNormal)
        }
    }

    /// endregion
}