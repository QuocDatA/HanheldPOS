package com.hanheldpos.ui.screens.menu.report

import android.content.Context
import androidx.lifecycle.ViewModel
import com.hanheldpos.R
import com.hanheldpos.model.menu_nav_opt.NavBarOptionType
import com.hanheldpos.model.menu_nav_opt.ReportOptionType
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.screens.menu.adapter.ItemOptionNav

class ReportVM : BaseUiViewModel<ReportUV>() {

    fun initReportItemList(context : Context) : List<ItemOptionNav> {
        return mutableListOf(
            ItemOptionNav(
                type = ReportOptionType.CURRENT_DRAWER,
                name = getNameMenu(ReportOptionType.CURRENT_DRAWER, context)
            ),
            ItemOptionNav(
                type = ReportOptionType.SALES_REPORT,
                name = getNameMenu(ReportOptionType.SALES_REPORT, context)
            ),
        )
    }

    fun backPress() {
        uiCallback?.getBack()
    }

    private fun getNameMenu(type : ReportOptionType, context: Context): String {
        return when(type){
            ReportOptionType.CURRENT_DRAWER -> context.getString(R.string.current_drawer);
            ReportOptionType.SALES_REPORT -> context.getString(R.string.sales_report);
        }
    }
}