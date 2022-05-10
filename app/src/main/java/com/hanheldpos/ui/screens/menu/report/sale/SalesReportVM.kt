package com.hanheldpos.ui.screens.menu.report.sale

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.report.ReportSalesResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.order.OrderAsyncRepo
import com.hanheldpos.data.repository.report.ReportRepo
import com.hanheldpos.data.repository.setting.SettingRepo
import com.hanheldpos.database.DatabaseMapper
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.DatabaseHelper
import com.hanheldpos.model.OrderHelper
import com.hanheldpos.model.UserHelper
import com.hanheldpos.model.order.OrderSubmitResp
import com.hanheldpos.model.report.SaleReportCustomData
import com.hanheldpos.model.setting.SettingDevicePut
import com.hanheldpos.ui.base.dialog.AppAlertDialog
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.screens.menu.report.sale.adapter.NumberDayReportItem
import com.hanheldpos.utils.DateTimeUtils
import com.hanheldpos.utils.GSonUtils
import com.hanheldpos.utils.StringUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

class SalesReportVM : BaseUiViewModel<SalesReportUV>() {


    fun initNumberDaySelected(): MutableList<NumberDayReportItem> {
        return mutableListOf(
            NumberDayReportItem("1D", 1),
            NumberDayReportItem("2D", 2),
            NumberDayReportItem("3D", 3),
            NumberDayReportItem("5D", 5),
            NumberDayReportItem("1W", 7),
        )
    }

    fun onOpenCustomizeReport() {
        uiCallback?.onOpenCustomizeReport();
    }

    fun backPress() {
        uiCallback?.backPress();
    }
}