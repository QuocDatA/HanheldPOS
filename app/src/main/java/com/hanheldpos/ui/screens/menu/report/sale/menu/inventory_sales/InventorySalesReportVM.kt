package com.hanheldpos.ui.screens.menu.report.sale.menu.inventory_sales

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.report.InventoryReport
import com.hanheldpos.data.api.pojo.report.SectionReport
import com.hanheldpos.model.menu.report.ReportItem
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.widgets.TableLayoutFixedHeader
import com.hanheldpos.utils.PriceUtils

class InventorySalesReportVM : BaseUiViewModel<InventorySalesReportUV>() {
    val isShowDetail = MutableLiveData(false)

    fun getInventorySummary(
        context: Context,
        inventories: List<InventoryReport>?
    ): MutableList<Any> {
        var totalGrossQty = 0
        var totalNetQty = 0
        inventories?.forEach { inventory ->
            totalGrossQty += inventory.Qty
            totalNetQty += inventory.NetQty
        }
        return mutableListOf(totalGrossQty, totalNetQty)
    }


    fun getInventoryHeaders(context: Context): List<TableLayoutFixedHeader.Title> {
        return mutableListOf(
            TableLayoutFixedHeader.Title(context.getString(R.string.name)),
            TableLayoutFixedHeader.Title(context.getString(R.string.gross_qty)),
            TableLayoutFixedHeader.Title(context.getString(R.string.returns)),
            TableLayoutFixedHeader.Title(context.getString(R.string.net_qty))
        )
    }

    fun getInventoryRows(
        context: Context,
        inventories: List<InventoryReport>?
    ): List<TableLayoutFixedHeader.Row> {
        var totalGrossQty = 0
        var totalReturns = 0
        var totalNetQty = 0
        val rows = mutableListOf<TableLayoutFixedHeader.Row>()

        inventories?.forEach { inventory ->
            totalGrossQty += inventory.Qty
            totalNetQty += inventory.NetQty
            totalReturns += inventory.RefundQty
            rows.add(
                TableLayoutFixedHeader.Row(
                    mutableListOf(
                        TableLayoutFixedHeader.Title(inventory.CategoryName),
                        TableLayoutFixedHeader.Title(inventory.Qty.toString()),
                        TableLayoutFixedHeader.Title(inventory.RefundQty.toString()),
                        TableLayoutFixedHeader.Title(inventory.NetQty.toString())
                    )
                )
            )
            rows.addAll(
                inventory.Product.map { pro ->
                    TableLayoutFixedHeader.Row(
                        mutableListOf(
                            TableLayoutFixedHeader.Title("\t${pro.Name}"),
                            TableLayoutFixedHeader.Title(pro.GrossQuantity.toString()),
                            TableLayoutFixedHeader.Title(pro.RefundQuantity.toString()),
                            TableLayoutFixedHeader.Title(pro.NetQuantity.toString())
                        )
                    )
                }
            )
        }

        rows.add(
            TableLayoutFixedHeader.Row(
                mutableListOf(
                    TableLayoutFixedHeader.Title(context.getString(R.string.total)),
                    TableLayoutFixedHeader.Title(totalGrossQty.toString()),
                    TableLayoutFixedHeader.Title(totalReturns.toString()),
                    TableLayoutFixedHeader.Title(totalNetQty.toString())
                )
            )

        )
        return rows
    }
}