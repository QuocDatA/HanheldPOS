package com.hanheldpos.printer.layouts

import com.hanheldpos.printer.printer_setup.PrintConfig
import com.hanheldpos.printer.printer_setup.printer_manager.BasePrinterManager
import com.hanheldpos.printer.wagu.Block

abstract class BaseLayoutPrinter(
    protected val device: BasePrinterManager,
    protected val printConfig: PrintConfig
) {
    /// region printer - layout values
    protected val charPerLineLarge = printConfig.deviceInfo.charsPerLineLarge()
    protected val charPerLineNormal = printConfig.deviceInfo.charsPerLineNormal()
    protected val columnOrderDetailAlign = mutableListOf(
        Block.DATA_MIDDLE_LEFT,
        Block.DATA_MIDDLE_LEFT,
        Block.DATA_MIDDLE_RIGHT
    )
    protected open val leftColumn = printConfig.deviceInfo.leftColumnWidth()
    protected open val rightColumn = printConfig.deviceInfo.rightColumnWidth()
    protected open val centerColumn = printConfig.deviceInfo.centerColumnWidth()

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
        device.setupPage(printConfig.deviceInfo.paperWidth(), -1f)
    }

    protected fun printDivider() {
        device.drawLine(charPerLineNormal)
    }

    protected fun feedLines(line: Int) {
        device.feedLines(line)
    }

    protected fun cutPaper() {
        device.cutPaper()
    }
}
