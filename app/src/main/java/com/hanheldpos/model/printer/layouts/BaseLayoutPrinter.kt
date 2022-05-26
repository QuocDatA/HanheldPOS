package com.hanheldpos.model.printer.layouts

import android.content.Context
import com.handheld.printer.printer_manager.BasePrinterManager
import com.handheld.printer.wagu.Block
import com.handheld.printer.wagu.WaguUtils
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.order.OrderReq
import com.hanheldpos.model.printer.BillPrinterManager
import com.hanheldpos.utils.DateTimeUtils
import com.hanheldpos.utils.StringUtils

abstract class BaseLayoutPrinter(
    protected val context: Context,
    protected val order: OrderReq,
    protected val printer: BasePrinterManager,
    protected val printOptions: BillPrinterManager.PrintOptions
) {
    enum class LayoutType {
        Cashier,
        Kitchen,
        Reprint,
    }

    /// region printer - layout values
    protected val charPerLineLarge = printOptions.deviceInfo.charsPerLineLarge()
    protected val charPerLineNormal = printOptions.deviceInfo.charsPerLineNormal()
    protected val columnOrderDetailAlign = mutableListOf(
        Block.DATA_MIDDLE_LEFT,
        Block.DATA_MIDDLE_LEFT,
        Block.DATA_MIDDLE_RIGHT
    )
    protected open val leftColumn = printOptions.deviceInfo.leftColumnWidth()
    protected open val rightColumn = printOptions.deviceInfo.rightColumnWidth()
    protected open val centerColumn = printOptions.deviceInfo.centerColumnWidth()

    protected open fun columnSize() = mutableListOf(leftColumn, centerColumn, rightColumn)
    protected open fun columnExtraSize() = mutableListOf(2, centerColumn - 2)
    protected open fun columnGroupBundle() = mutableListOf(4, centerColumn - 4)
    protected open fun columnGroupBundleExtra() = mutableListOf(2, centerColumn - 6)

    /// endregion

    init {
        setUpPage()
    }

    abstract fun print()

    private fun setUpPage() {
        printer.setupPage(printOptions.deviceInfo.paperWidth(), -1f)
    }

    /// region print methods

    protected open fun printBillStatus(isReprint: Boolean) {

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
                    aligns = statusColumns,
                )
            ).toString(),
            false,
            BasePrinterManager.FontSize.Medium
        )
    }

    protected fun printOrderInfo() {
        val orderCode = mutableListOf("Order #:", order.Order.Code.toString())
        val employee =
            mutableListOf(
                "Employee :",
                DataHelper.deviceCodeLocalStorage?.Employees?.find { it._id == order.Order.EmployeeGuid }?.FullName.toString()
            )
        val dateCreate = mutableListOf(
            "Create Date :", DateTimeUtils.dateToString(
                DateTimeUtils.strToDate(
                    order.Order.CreateDate,
                    DateTimeUtils.Format.FULL_DATE_UTC_TIMEZONE
                ), DateTimeUtils.Format.DD_MM_YYYY_HH_MM
            )
        )
        val content = WaguUtils.columnListDataBlock(
            charPerLineNormal, mutableListOf(orderCode, employee, dateCreate),
            mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT), isWrapWord = true
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

    protected fun printDivider() {
        printer.drawLine(charPerLineNormal)
    }

    protected fun feedLines(line: Int) {
        printer.feedLines(line)
    }

    protected fun cutPaper() {
        printer.cutPaper()
    }
}
