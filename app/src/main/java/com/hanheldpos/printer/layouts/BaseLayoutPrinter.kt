package com.hanheldpos.printer.layouts

import com.handheld.printer.printer_setup.PrintOptions
import com.handheld.printer.printer_setup.printer_manager.BasePrinterManager
import com.handheld.printer.wagu.Block
import com.handheld.printer.wagu.WaguUtils
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.order.OrderModel
import com.hanheldpos.printer.BillPrinterManager
import com.hanheldpos.utils.DateTimeUtils
import com.hanheldpos.utils.StringUtils

abstract class BaseLayoutPrinter(
    protected val printer: BasePrinterManager,
    protected val printOptions: PrintOptions
) {
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
