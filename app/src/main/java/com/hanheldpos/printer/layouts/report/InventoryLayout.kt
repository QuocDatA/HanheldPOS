package com.example.pos2.printer.layouts.report

import com.example.pos2.printer.layouts.BaseLayoutPrinter
import com.example.pos2.printer_setup.PrintOptions
import com.example.pos2.printer_setup.printer_manager.BasePrinterManager
import com.example.pos2.view.left.report.sales.inventory.adapter.ListProductInventoryHeader
import com.example.pos2.wagu.Block
import com.example.pos2.wagu.WaguUtils


class InventoryLayout(
    printer: BasePrinterManager,
    printOptions: PrintOptions,
    startDate: String,
    endDate: String,
    inventories: List<ListProductInventoryHeader>
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