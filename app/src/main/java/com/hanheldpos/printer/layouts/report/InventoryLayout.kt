package com.hanheldpos.printer.layouts.report

import com.hanheldpos.printer.printer_setup.printer_manager.BasePrinterManager
import com.hanheldpos.printer.wagu.Block
import com.hanheldpos.printer.wagu.WaguUtils
import com.hanheldpos.printer.wagu.WrapType
import com.hanheldpos.data.api.pojo.report.ProductInventory
import com.hanheldpos.data.api.pojo.report.ReportSalesResp
import com.hanheldpos.model.report.SaleReportFilter
import com.hanheldpos.printer.printer_setup.PrintConfig


class InventoryLayout(
    printer: BasePrinterManager,
    printConfig: PrintConfig,
    reportSalesModel: ReportSalesResp?,
    filterOptionReportSale: SaleReportFilter?,
    private val inventories: Map<ProductInventory, List<ProductInventory>> = reportSalesModel?.mapInventorySaleReport() ?: emptyMap()
) : BaseLayoutReport(
    printer, printConfig,
    title = "Item Sales Report",
    reportSalesModel,
    filterOptionReportSale
) {

    private val inventoryColumnSize = mutableListOf(
        charPerLineNormal - 6 - 7 - 6, 6, 7, 6
    )

    override fun print() {
        printHeader()
        printSectionHeader()
        printCategories()
        feedLines(2)
        printOverview()

        cutPaper()
    }

    private fun printSectionHeader() {
        device.drawText(
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
                columnSize = inventoryColumnSize
            ), bold = true
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

        device.drawText(
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
            reportSalesModel?.getInventoryOverview(inventories),
            header = "Total",
            printInventoryColumns = true
        )
    }


    /// endregion
}