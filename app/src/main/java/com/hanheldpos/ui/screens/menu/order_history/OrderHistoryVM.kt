package com.hanheldpos.ui.screens.menu.order_history

import android.content.Context
import com.hanheldpos.R
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.order.OrderRepo
import com.hanheldpos.database.DatabaseMapper
import com.hanheldpos.database.entities.OrderCompletedEntity
import com.hanheldpos.model.OrderHelper
import com.hanheldpos.model.UserHelper
import com.hanheldpos.model.menu.orders.OrderMenuGroupItem
import com.hanheldpos.model.order.OrderSummaryPrimary
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.DateTimeUtils
import kotlin.random.Random

class OrderHistoryVM : BaseUiViewModel<OrderHistoryUV>() {
    private var isRequest : Boolean = false
    private val listOrderHistory : MutableList<OrderSummaryPrimary> = mutableListOf()
    private val orderRepo =  OrderRepo()
    fun getOrderHistory(context : Context) {
        if (isRequest) return
        isRequest = true
        showLoading(true)
        val listOrderId = listOrderHistory.lastOrNull()?._id
        orderRepo.getOrderHistory(UserHelper.getUserGuid(),UserHelper.getLocationGuid(),UserHelper.getDeviceGuid(),10,listOrderId,object  : BaseRepoCallback<BaseResponse<List<OrderSummaryPrimary>>?> {
            override fun apiResponse(data: BaseResponse<List<OrderSummaryPrimary>>?) {
                showLoading(false)
                isRequest = false
                if (data?.DidError == true) {
                    showError(data.ErrorMessage ?: data.Message ?: context.getString(R.string.failed_to_load_data))
                }
                else {

                    listOrderHistory.addAll(data?.Model?: mutableListOf())
                    uiCallback?.onLoadOrderHistory(groupOrderHistory(listOrderHistory))
                }
            }

            override fun showMessage(message: String?) {
                isRequest = false
                showLoading(false)
                showError(message)
            }
        })
    }

    private fun groupOrderHistory(orders: List<OrderSummaryPrimary>): List<OrderMenuGroupItem> {
        return orders
            .groupBy { item ->
                var title = DateTimeUtils.strToStr(
                    item.OrderCreateDate,
                    DateTimeUtils.Format.YYYY_MM_DD_HH_MM_SS,
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
}