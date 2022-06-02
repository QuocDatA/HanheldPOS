package com.hanheldpos.printer.layouts.order


import com.handheld.printer.printer_setup.PrintOptions
import com.handheld.printer.printer_setup.printer_manager.BasePrinterManager
import com.handheld.printer.wagu.Block
import com.handheld.printer.wagu.WaguUtils
import com.handheld.printer.wagu.WrapType
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
        val orderCode = mutableListOf("Order #:", order.Order.Code.toString())
        val employee =
            mutableListOf(
                "Employee:",
                DataHelper.deviceCodeLocalStorage?.Employees?.find { it._id == order.Order.EmployeeGuid }?.FullName.toString()
            )
        val dateCreate = mutableListOf(
            "Create Date:", DateTimeUtils.dateToString(
                DateTimeUtils.strToDate(
                    order.Order.CreateDate,
                    DateTimeUtils.Format.FULL_DATE_UTC_TIMEZONE
                ), DateTimeUtils.Format.DD_MM_YYYY_HH_MM
            )
        )
        val content = WaguUtils.columnListDataBlock(
            charPerLineNormal, mutableListOf(orderCode, employee, dateCreate),
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