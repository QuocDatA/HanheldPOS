package com.hanheldpos.ui.screens.menu.report.sale.menu.section_sales

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.report.SectionReport
import com.hanheldpos.model.menu.report.ReportItem
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.PriceUtils

class SectionSalesReportVM : BaseUiViewModel<SectionSalesReportUV>() {
    val isShowDetail = MutableLiveData(false)

    fun getSectionSalesSummary(context: Context, sections: List<SectionReport>?): MutableList<Any> {
        var totalGrossSales = 0.0
        var totalNetSales = 0.0
        sections?.forEach { section ->
            totalGrossSales += section.GrossSales
            totalNetSales += section.NetSale
        }
        return mutableListOf(totalGrossSales,totalNetSales)
    }


    fun getSectionSalesHeaders(context: Context): Collection<String> {
        return mutableListOf(
            context.getString(R.string.sections),
            context.getString(R.string.bill),
            context.getString(R.string.covers),
            context.getString(R.string.gross_sales),
            context.getString(R.string.net_sales),
            context.getString(R.string.tips)
        )
    }

    fun getSectionSalesRows(context: Context, sections: List<SectionReport>?): Collection<Collection<String>> {
        var totalBill = 0
        var totalCovers = 0
        var totalGrossSales = 0.0
        var totalNetSales = 0.0
        var totalTips = 0.0
        val rows = mutableListOf<Collection<String>>()

        sections?.forEach { section ->
            totalBill += section.Bill.toInt()
            totalCovers += section.Covers.toInt()
            totalGrossSales += section.GrossSales
            totalNetSales += section.NetSale
            totalTips += section.Tips

            rows.add(
                mutableListOf(
                    section.SectionName,
                    section.Bill.toInt().toString(),
                    section.Covers.toInt().toString(),
                    PriceUtils.formatStringPrice(section.GrossSales),
                    PriceUtils.formatStringPrice(section.NetSale),
                    PriceUtils.formatStringPrice(section.Tips)
                )
            )
            rows.addAll(
                section.FloorTable.map { floorTable->
                    mutableListOf(
                        "        ${floorTable.Name}",
                        floorTable.Bill.toInt().toString(),
                        floorTable.Covers.toInt().toString(),
                        PriceUtils.formatStringPrice(floorTable.GrossSales),
                        PriceUtils.formatStringPrice(floorTable.NetSale),
                        PriceUtils.formatStringPrice(floorTable.Tips)
                    )
                }
            )
        }

        rows.add(
            mutableListOf(
                context.getString(R.string.total),
                totalBill.toString(),
                totalCovers.toString(),
                PriceUtils.formatStringPrice(totalGrossSales),
                PriceUtils.formatStringPrice(totalNetSales),
                PriceUtils.formatStringPrice(totalTips)
            )
        )
        return rows
    }
}