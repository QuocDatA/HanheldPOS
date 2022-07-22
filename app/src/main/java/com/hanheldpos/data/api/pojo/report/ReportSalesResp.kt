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
    val ListInventory: List<InventoryReport>,
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
) : Parcelable {
    fun mapInventorySaleReport(): Map<ProductInventory, List<ProductInventory>> {
        val result = mutableMapOf<ProductInventory, List<ProductInventory>>()
        ListInventory.forEach { item ->
            result[ProductInventory(
                Name = item.CategoryName,
                GrossQuantity = item.Product.sumOf { product -> product.GrossQuantity },
                RefundQuantity = item.Product.sumOf { product -> product.RefundQuantity },
                NetQuantity = item.Product.sumOf { product -> product.NetQuantity },
                ProductGuid = item.CategoryGuid
            )] = item.Product
        }
        return result
    }
    fun getInventoryOverview(inventories: Map<ProductInventory, List<ProductInventory>>) : ProductInventory {
        return ProductInventory(
            GrossQuantity = inventories.keys.sumOf { inventory -> inventory.GrossQuantity },
            RefundQuantity = inventories.keys.sumOf { inventory ->
                inventory.RefundQuantity
            },
            NetQuantity = inventories.keys.sumOf { inventory -> inventory.NetQuantity },
            ProductGuid = "",
            Name = ""
        )
    }
}