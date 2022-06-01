
package com.hanheldpos.ui.screens.menu.order_detail

import android.content.Context
import com.hanheldpos.R
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.order.OrderRepo
import com.hanheldpos.model.order.OrderModel
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class OrderDetailViewVM : BaseUiViewModel<OrderDetailViewUV>() {
    private val orderRepo = OrderRepo()

    fun getOrderDetail(context : Context, orderId : String) {
        showLoading(true)
        orderRepo.getOrderDetail(orderId, callback = object : BaseRepoCallback<BaseResponse<OrderModel>?> {
            override fun apiResponse(data: BaseResponse<OrderModel>?) {
                showLoading(false)
                if (data?.DidError == true) {
                    showError(data.ErrorMessage ?: data.Message ?: context.getString(R.string.failed_to_load_data))
                }
                else {
                    data?.Model?.let { uiCallback?.onShowOrderDetail(it) }
                }
            }

            override fun showMessage(message: String?) {
                showLoading(false)
                showError(message)
            }
        })
    }

    fun onFragmentBackPressed() {
        uiCallback?.onFragmentBackPressed()
    }
}