package com.hanheldpos.printer.layouts.report

import com.handheld.printer.printer_setup.PrintOptions
import com.handheld.printer.printer_setup.printer_manager.BasePrinterManager
import com.handheld.printer.wagu.Block
import com.handheld.printer.wagu.WaguUtils
import com.hanheldpos.data.api.pojo.report.InventoryReport


class InventoryLayout(
    printer: BasePrinterManager,
    printOptions: PrintOptions,
    startDate: String,
    endDate: String,
    inventories:  Map<InventoryReport,List<InventoryReport>>
) : BaseLayoutReport(printer, printOptions, startDate, endDate) {

    override fun print() {
        printLogo()
        feedLines(1)
        printTitle()
        printDate()
        feedLines(1)
        printDivider()
        printBillTime()
        printDivider()
        printHeader()
    }

    /// region print methods
    private fun printLogo() {
        printer.drawText(
            WaguUtils.columnListDataBlock(
                charPerLineNormal,
                mutableListOf(
                    mutableListOf(
                        "<logo>"
                    )
                ),
                columnSize = mutableListOf(6),
                aligns = mutableListOf(Block.DATA_MIDDLE_LEFT)
            )
        )
    }

    private fun printTitle() {
        printer.drawText(
            WaguUtils.columnListDataBlock(
                charPerLineLarge,
                mutableListOf(
                    mutableListOf(
                        "Item Sales Report",
                    )
                ),
                aligns = mutableListOf(Block.DATA_CENTER)
            ),
            bold = true,
            BasePrinterManager.FontSize.Large
        )
    }

    private fun printBillTime() {
        printer.drawText(
            WaguUtils.columnListDataBlock(
                charPerLineNormal,
                mutableListOf(
                    mutableListOf(
                        "Bill Time",
                        "All Day"
                    )
                ),
                aligns = mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT),
            )
        )
    }

    private fun printHeader() {
        printer.drawText(
            WaguUtils.columnListDataBlock(
                charPerLineNormal,
                mutableListOf(
                    mutableListOf(
                        "Name",
                        "Gross",
                        "Refund",
                        "Net"
                    )
                ),
                aligns = mutableListOf(
                    Block.DATA_MIDDLE_LEFT,
                    Block.DATA_MIDDLE_RIGHT,
                    Block.DATA_MIDDLE_RIGHT,
                    Block.DATA_MIDDLE_RIGHT
                ),
                columnSize = mutableListOf(
                    charPerLineNormal - 6 - 8 - 4, 6, 8, 4
                )
            ), bold = true
        )
    }


    /// endregion
}