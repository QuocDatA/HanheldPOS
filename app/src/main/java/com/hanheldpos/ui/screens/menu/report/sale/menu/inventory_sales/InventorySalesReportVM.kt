package com.hanheldpos.ui.screens.menu.report.sale.menu.inventory_sales

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.report.InventoryReport
import com.hanheldpos.data.api.pojo.report.SectionReport
import com.hanheldpos.model.menu.report.ReportItem
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.PriceUtils

class InventorySalesReportVM : BaseUiViewModel<InventorySalesReportUV>() {
    val isShowDetail = MutableLiveData(false)

    fun getInventorySummary(context: Context, inventories: List<InventoryReport>?): MutableList<Any> {
        var totalGrossQty = 0
        var totalNetQty = 0
        inventories?.forEach { inventory ->
            totalGrossQty += inventory.Qty
            totalNetQty += inventory.NetQty
        }
        return mutableListOf(totalGrossQty,totalNetQty)
    }


    fun getInventoryHeaders(context: Context): Collection<String> {
        return mutableListOf(
            context.getString(R.string.name),
            context.getString(R.string.gross_qty),
            context.getString(R.string.returns),
            context.getString(R.string.net_qty)
        )
    }

    fun getInventoryRows(context: Context, inventories: List<InventoryReport>?): Collection<Collection<String>> {
        var totalGrossQty = 0
        var totalReturns = 0
        var totalNetQty = 0
        val rows = mutableListOf<Collection<String>>()

        inventories?.forEach { inventory ->
            totalGrossQty += inventory.Qty
            totalNetQty += inventory.NetQty
            totalReturns += inventory.RefundQty
            rows.add(
                mutableListOf(
                    inventory.CategoryName,
                    inventory.Qty.toString(),
                    inventory.RefundQty.toString(),
                    inventory.NetQty.toString()
                )
            )
            rows.addAll(
                inventory.Product.map { pro->
                    mutableListOf(
                        "        ${pro.Name}",
                        pro.GrossQuantity.toString(),
                        pro.RefundQuantity.toString(),
                        pro.NetQuantity.toString()
                    )
                }
            )
        }

        rows.add(
            mutableListOf(
                context.getString(R.string.total),
                totalGrossQty.toString(),
                totalReturns.toString(),
                totalNetQty.toString()
            )
        )
        return rows
    }
}