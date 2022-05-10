package com.hanheldpos.ui.screens.menu.report.sale

import android.content.Context
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.report.ReportSalesResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.report.ReportRepo
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.UserHelper
import com.hanheldpos.model.menu.report.SaleOptionPage
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.screens.menu.adapter.ItemOptionNav

class SaleReportsMenuVM : BaseUiViewModel<SaleReportsMenuUV>() {

    fun onFragmentBackPressed() {
        uiCallback?.onFragmentBackPressed()
    }

    fun getListOptionPages(context: Context): List<ItemOptionNav> {
        return mutableListOf(
            ItemOptionNav(
                SaleOptionPage.Overview,
                name = context.getString(R.string.overview)
            ),
            ItemOptionNav(
                SaleOptionPage.PaymentSummary,
                name = context.getString(R.string.payment_summary)
            ),
            ItemOptionNav(
                SaleOptionPage.DiningOptions,
                name = context.getString(R.string.dining_options)
            ),
            ItemOptionNav(
                SaleOptionPage.SectionSales,
                name = context.getString(R.string.section_sales)
            ),
            ItemOptionNav(
                SaleOptionPage.CashVoucher,
                name = context.getString(R.string.cash_voucher)
            ),
            ItemOptionNav(
                SaleOptionPage.ItemSales,
                name = context.getString(R.string.item_sales)
            ),
            ItemOptionNav(
                SaleOptionPage.InventorySales,
                name = context.getString(R.string.inventory_sales)
            ),
            ItemOptionNav(
                SaleOptionPage.CategorySales,
                name = context.getString(R.string.overview)
            ),
            ItemOptionNav(
                SaleOptionPage.Discounts,
                name = context.getString(R.string.discounts)
            ),
            ItemOptionNav(
                SaleOptionPage.Comps,
                name = context.getString(R.string.comps),
            ),
            ItemOptionNav(
                SaleOptionPage.Refund,
                name = context.getString(R.string.refund)
            ),
            ItemOptionNav(
                SaleOptionPage.Taxes,
                name = context.getString(R.string.taxes)
            ),
            ItemOptionNav(
                SaleOptionPage.Service,
                name = context.getString(R.string.service)
            ),
            ItemOptionNav(
                SaleOptionPage.Surcharge,
                name = context.getString(R.string.surcharge)
            )
        )
    }
}