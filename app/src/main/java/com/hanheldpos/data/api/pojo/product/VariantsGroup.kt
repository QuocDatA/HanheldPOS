package com.hanheldpos.data.api.pojo.product

import android.os.Parcelable
import com.hanheldpos.model.cart.VariantCart
import kotlinx.parcelize.Parcelize

@Parcelize
data class VariantsGroup(
    val DisplayName: String?,
    val IsDisplayName: Boolean?,
    val OptionName: String?,
    val OptionValueList: List<OptionValueVariantsGroup>? = null,
    val VariantOptionType: Int?
) : Parcelable {
    @Parcelize
    data class OptionValueVariantsGroup(
        val Barcode: String,
        val ComparePrice: Double,
        var OptionName: String?,
        val CostPerItem: Double,
        val Discount: Double,
        val GroupValue: String,
        val Id: Int,
        val Inventory: Int,
        val Level: Int,
        val Price: Double,
        val Sku: String,
        val Value: String,
        val Visible: Int,
        val Variant: VariantsGroup?,
    ) : Parcelable {
        fun isExistsVariant(variantList: List<VariantCart>?): Boolean {
            if (Variant == null) {
                return true; }

            val isExists = variantList?.firstOrNull { v -> v.Id == Id } != null
            return if (isExists) Variant.isExistsVariant(variantList) else isExists
        }
    }

    fun subOptionValueList(): List<OptionValueVariantsGroup>? {
        return OptionValueList?.map { option_value ->
            option_value.apply { this.OptionName = this@VariantsGroup.OptionName }
        }
    }

    fun isExistsVariant(variantList: List<VariantCart>?): Boolean {
        if (variantList?.any() != true) {
            return true; }
        val isExists =
            OptionValueList?.firstOrNull { option -> option.isExistsVariant(variantList) } != null
        return isExists
    }
}
