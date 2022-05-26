package com.hanheldpos.ui.screens.menu.orders.synced

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.order.filter.OrderFilterResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.order.OrderRepo
import com.hanheldpos.database.DatabaseMapper
import com.hanheldpos.database.entities.OrderCompletedEntity
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.OrderHelper
import com.hanheldpos.model.UserHelper
import com.hanheldpos.model.menu.orders.OrderMenuGroupItem
import com.hanheldpos.model.menu.orders.SyncedOrdersFilterData
import com.hanheldpos.model.order.OrderSummaryPrimary
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.DateTimeUtils

class SyncedOrdersVM : BaseUiViewModel<SyncedOrdersUV>() {
    private val orderRepo = OrderRepo()
    val filterData = MutableLiveData(
        SyncedOrdersFilterData(
            startDay = DateTimeUtils.curDate,
            endDay = DateTimeUtils.curDate,
            isAllDay = true,
            startTime = null,
            endTime = null,
            diningOption = null,
        )
    )
    val orderCodeSearch = MutableLiveData<String>()

    fun getOrderFilter(data: SyncedOrdersFilterData, page: Int? = 1) {
        showLoading(true)
        orderRepo.getOrderFilter(
            UserHelper.getUserGuid(),
            UserHelper.getLocationGuid(),
            UserHelper.getDeviceGuid(),
            data.diningOption?.Id,
            pageNo = page,
            pageSize = 10,
            startDate = DateTimeUtils.dateToString(
                data.startDay,
                DateTimeUtils.Format.YYYY_MM_DD_HH_MM
            ),
            endDate = DateTimeUtils.dateToString(
                data.endDay,
                DateTimeUtils.Format.YYYY_MM_DD_HH_MM
            ),
            orderCode = orderCodeSearch.value,
            callback = object : BaseRepoCallback<BaseResponse<OrderFilterResp>?> {
                override fun apiResponse(data: BaseResponse<OrderFilterResp>?) {
                    showLoading(false)
                    if (data == null || data.DidError) {
                        uiCallback?.onLoadOrderFilter(mutableListOf())
                        showError(data?.Message ?: data?.ErrorMessage)
                    } else {
                        uiCallback?.onLoadOrderFilter(data.Model?.OrderList)
                    }
                }

                override fun showMessage(message: String?) {
                    uiCallback?.onLoadOrderFilter(mutableListOf())
                    showLoading(false)
                    showError(message)
                }

            }
        )
    }

    fun groupSyncOrders(orders: List<OrderSummaryPrimary>): List<OrderMenuGroupItem> {
        return orders
            .groupBy { item ->
                var title = DateTimeUtils.strToStr(
                    item.OrderCreateDate,
                    DateTimeUtils.Format.FULL_DATE_UTC_TIMEZONE,
                    DateTimeUtils.Format.EEEE_dd_MMM_yyyy
                )
                if (title.isEmpty())
                    title = DateTimeUtils.strToStr(
                        item.OrderCreateDate,
                        DateTimeUtils.Format.YYYY_MM_DD_HH_MM_SS,
                        DateTimeUtils.Format.EEEE_dd_MMM_yyyy
                    )
                title
            }.map {
                OrderMenuGroupItem(
                    createDate = it.key,
                    orders = it.value
                )
            }

    }

    fun onFragmentBackPressed() {
        uiCallback?.onFragmentBackPressed()
    }

    fun showFilter() {
        uiCallback?.onShowFilter()
    }
}