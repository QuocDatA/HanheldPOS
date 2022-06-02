package com.hanheldpos.printer.layouts.report

import com.handheld.printer.printer_setup.PrintOptions
import com.handheld.printer.printer_setup.printer_manager.BasePrinterManager
import com.handheld.printer.wagu.Block
import com.handheld.printer.wagu.WaguUtils
import com.handheld.printer.wagu.WrapType
import com.hanheldpos.data.api.pojo.report.InventoryReport
import com.hanheldpos.data.api.pojo.report.ProductInventory
import com.hanheldpos.data.api.pojo.report.ReportSalesResp
import com.hanheldpos.model.report.SaleReportFilter
import com.hanheldpos.ui.screens.menu.report.sale.SalesReportVM
import com.hanheldpos.utils.DateTimeUtils
import java.util.*


class InventoryLayout(
    printer: BasePrinterManager,
    printOptions: PrintOptions,
    private val salesReport: ReportSalesResp?,
    private val filterOptionSalesReport: SaleReportFilter?,
    private val inventories: Map<ProductInventory, List<ProductInventory>> = salesReport?.mapInventorySaleReport() ?: emptyMap()
) : BaseLayoutReport(
    printer, printOptions,
    DateTimeUtils.dateToString(
        filterOptionSalesReport?.startDay,
        DateTimeUtils.Format.EEEE_dd_MMM_yyyy
    ),
    DateTimeUtils.dateToString(
        filterOptionSalesReport?.endDay,
        DateTimeUtils.Format.EEEE_dd_MMM_yyyy
    ),
) {

    private val inventoryColumnSize = mutableListOf(
        charPerLineNormal - 6 - 7 - 6, 6, 7, 6
    )

    override fun print() {
        printLogo()
        feedLines(1)
        printTitle()
        printDate()
        feedLines(1)

        printEmployee()
        printStaff()

        printDivider()
        printBillTime()
        printDivider()
        printHeader()
        printCategories()
        feedLines(2)
        printOverview()

        cutPaper()
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
                aligns = mutableListOf(Block.DATA_CENTER),
                wrapType = WrapType.SOFT_WRAP
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
                ),
                wrapType = WrapType.ELLIPSIS

            ), bold = true
        )
    }

    private fun printEmployee() {
        printer.drawText(
            WaguUtils.columnListDataBlock(
                charPerLineNormal,
                mutableListOf(
                    mutableListOf(
                        "Shop: ${salesReport?.Report?.firstOrNull()?.LocationName ?: ""}"
                    )
                ),
                aligns = mutableListOf(Block.DATA_MIDDLE_LEFT)
            ),
            size = BasePrinterManager.FontSize.Small
        )
    }

    private fun printStaff() {
        printer.drawText(
            WaguUtils.columnListDataBlock(
                charPerLineNormal,
                mutableListOf(
                    mutableListOf(
                        "Staff: ${salesReport?.Report?.firstOrNull()?.Employee ?: ""}",
                        DateTimeUtils.dateToString(Date(),DateTimeUtils.Format.DD_MM_YYYY_HH_MM)
                    )
                ),
                aligns = mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT)
            )
        )
    }

    private fun printCategories() {
        inventories.forEach { inventory ->
            printInventory(
                inventory.key,
                header = "Cat: ${inventory.key.Name}",
                printInventoryColumns = false
            )
            inventory.value.forEach {
                printInventory(it, header = it.Name.toString(), printInventoryColumns = true)
            }
            printDivider()
            printInventory(
                inventory.key,
                header = "Cat Total",
                printInventoryColumns = true
            )
            feedLines(1)
        }
    }

    /**
     * @param printInventoryColumns to indicate that this won't need to print value columns
     * @param header to print the header of the row
     * */
    private fun printInventory(
        productInventory: ProductInventory?,
        header: String,
        printInventoryColumns: Boolean
    ) {

        val line = mutableListOf<String>(
            header
        )
        val aligns = mutableListOf(
            Block.DATA_MIDDLE_LEFT
        )

        if (printInventoryColumns) {
            line.add((productInventory?.GrossQuantity ?: 0).toString())
            line.add((productInventory?.RefundQuantity ?: 0).toString())
            line.add((productInventory?.NetQuantity ?: 0).toString())

            aligns.add(Block.DATA_MIDDLE_RIGHT)
            aligns.add(Block.DATA_MIDDLE_RIGHT)
            aligns.add(Block.DATA_MIDDLE_RIGHT)
        }

        printer.drawText(
            WaguUtils.columnListDataBlock(
                width = charPerLineNormal,
                mutableListOf(
                    line
                ),
                aligns = aligns,
                columnSize = inventoryColumnSize
            , wrapType = WrapType.ELLIPSIS
            ),
            size = BasePrinterManager.FontSize.Small,

        )
    }

    private fun printOverview() {
        printInventory(
            salesReport?.getInventoryOverview(inventories),
            header = "Total",
            printInventoryColumns = true
        )
    }


    /// endregion
}