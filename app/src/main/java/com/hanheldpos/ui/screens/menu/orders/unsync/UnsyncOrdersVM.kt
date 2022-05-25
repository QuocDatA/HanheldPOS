package com.hanheldpos.ui.screens.menu.orders.unsync

import android.util.Log
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hanheldpos.R
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.database.DatabaseMapper
import com.hanheldpos.database.entities.OrderCompletedEntity
import com.hanheldpos.model.DatabaseHelper
import com.hanheldpos.model.OrderHelper
import com.hanheldpos.model.menu.orders.OrderMenuGroupItem
import com.hanheldpos.model.order.OrderSubmitResp
import com.hanheldpos.model.order.OrderSummaryPrimary
import com.hanheldpos.ui.base.dialog.AppAlertDialog
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.DateTimeUtils
import com.hanheldpos.utils.GSonUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class UnsyncOrdersVM : BaseUiViewModel<UnsyncOrdersUV>() {


    fun groupUnsyncOrders(orders: List<OrderCompletedEntity>): List<OrderMenuGroupItem> {
        orders.filter { OrderHelper.isValidOrderPush(it) }.let { listNeedPush ->
            return listNeedPush.map { item -> DatabaseMapper.mappingOrderReqFromEntity(item).OrderSummary }
                .groupBy { item ->
                    DateTimeUtils.strToStr(
                        item.OrderCreateDate,
                        DateTimeUtils.Format.FULL_DATE_UTC_TIMEZONE,
                        DateTimeUtils.Format.EEEE_dd_MMM_yyyy
                    )
                }.map {
                    OrderMenuGroupItem(
                        createDate = it.key,
                        orders = it.value
                    )
                }
        }
    }

    fun onFragmentBackPressed() {
        uiCallback?.onFragmentBackPressed()
    }
}