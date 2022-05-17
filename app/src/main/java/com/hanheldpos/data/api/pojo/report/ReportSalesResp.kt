package com.hanheldpos.data.api.pojo.report

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReportSalesResp(
    val Cash: List<Cash>,
    val CashVoucher: List<CashVoucherReport>,
    val Category: List<CategoryReport>,
    val Comp: List<CompReport>,
    val Device: List<DeviceReport>,
    val DinningOption: List<DinningOptionReport>,
    val DiscountOrder: List<DiscountOrderReport>,
    val Item: List<ItemSale>,
    val ListCombo: List<ListComboReport>,
    val ListInventory: List<Inventory>,
    val ListSection: List<SectionReport>,
    val ListServiceFee: List<FeeReport>,
    val ListSurchargeFee: List<FeeReport>,
    val ListTaxFee: List<FeeReport>,
    val OrderInfo: List<OrderInfo>,
    val OrderPayment: List<OrderPayment>,
    val Refund: List<RefundReport>,
    val Report: List<Report>,
    val SalesSummary: List<SalesSummary>,
    val Status: Int,
    val TimeSpan: List<TimeSpan>,
    val _id: String,
    val _key: Int
) : Parcelable