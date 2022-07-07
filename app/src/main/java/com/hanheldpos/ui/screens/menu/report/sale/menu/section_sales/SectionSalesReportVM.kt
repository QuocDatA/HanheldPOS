package com.hanheldpos.ui.screens.menu.report.sale.menu.section_sales

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.report.SectionReport
import com.hanheldpos.model.menu.report.ReportItem
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.widgets.TableLayoutFixedHeader
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
        return mutableListOf(totalGrossSales, totalNetSales)
    }


    fun getSectionSalesHeaders(context: Context): List<TableLayoutFixedHeader.Title> {
        return mutableListOf(
            TableLayoutFixedHeader.Title(context.getString(R.string.sections)),
            TableLayoutFixedHeader.Title(context.getString(R.string.bill)),
            TableLayoutFixedHeader.Title(context.getString(R.string.covers)),
            TableLayoutFixedHeader.Title(context.getString(R.string.gross_sales)),
            TableLayoutFixedHeader.Title(context.getString(R.string.net_sales)),
            TableLayoutFixedHeader.Title(context.getString(R.string.tips))
        )
    }

    fun getSectionSalesRows(
        context: Context,
        sections: List<SectionReport>?
    ): List<TableLayoutFixedHeader.Row> {
        var totalBill = 0
        var totalCovers = 0
        var totalGrossSales = 0.0
        var totalNetSales = 0.0
        var totalTips = 0.0
        val rows = mutableListOf<TableLayoutFixedHeader.Row>()

        sections?.forEach { section ->
            totalBill += section.Bill.toInt()
            totalCovers += section.Covers.toInt()
            totalGrossSales += section.GrossSales
            totalNetSales += section.NetSale
            totalTips += section.Tips

            rows.add(
                TableLayoutFixedHeader.Row(
                    mutableListOf(
                        TableLayoutFixedHeader.Title(
                            section.SectionName
                        ),
                        TableLayoutFixedHeader.Title(section.Bill.toInt().toString()),
                        TableLayoutFixedHeader.Title(section.Covers.toInt().toString()),
                        TableLayoutFixedHeader.Title(PriceUtils.formatStringPrice(section.GrossSales)),
                        TableLayoutFixedHeader.Title(PriceUtils.formatStringPrice(section.NetSale)),
                        TableLayoutFixedHeader.Title(PriceUtils.formatStringPrice(section.Tips))
                    )
                )

            )
            rows.addAll(
                section.FloorTable.map { floorTable ->
                    TableLayoutFixedHeader.Row(
                        mutableListOf(
                            TableLayoutFixedHeader.Title("\t${floorTable.Name}"),
                            TableLayoutFixedHeader.Title(floorTable.Bill.toInt().toString()),
                            TableLayoutFixedHeader.Title(floorTable.Covers.toInt().toString()),
                            TableLayoutFixedHeader.Title(PriceUtils.formatStringPrice(floorTable.GrossSales)),
                            TableLayoutFixedHeader.Title(PriceUtils.formatStringPrice(floorTable.NetSale)),
                            TableLayoutFixedHeader.Title(PriceUtils.formatStringPrice(floorTable.Tips))
                        )
                    )

                }
            )
        }

        rows.add(
            TableLayoutFixedHeader.Row(
                mutableListOf(
                    TableLayoutFixedHeader.Title(
                        context.getString(R.string.total)
                    ),
                    TableLayoutFixedHeader.Title(totalBill.toString()),
                    TableLayoutFixedHeader.Title(totalCovers.toString()),
                    TableLayoutFixedHeader.Title(PriceUtils.formatStringPrice(totalGrossSales)),
                    TableLayoutFixedHeader.Title(PriceUtils.formatStringPrice(totalNetSales)),
                    TableLayoutFixedHeader.Title(PriceUtils.formatStringPrice(totalTips))
                )
            )
        )
        return rows
    }
}