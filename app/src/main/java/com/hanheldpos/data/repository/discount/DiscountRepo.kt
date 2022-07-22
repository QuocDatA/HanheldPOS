package com.hanheldpos.data.repository.discount

import com.hanheldpos.data.api.pojo.discount.CouponResp
import com.hanheldpos.data.api.pojo.discount.DiscountCoupon
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepo
import com.hanheldpos.data.repository.base.BaseRepoCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiscountRepo : BaseRepo() {
    fun getDiscountList(
        userGuid: String?,
        locationGuid: String?,
         callback: BaseRepoCallback<BaseResponse<List<DiscountResp>>>
    ) {
        callback.apiRequesting(true)
        discountService.getDiscounts(userGuid,locationGuid).enqueue(object :
            Callback<BaseResponse<List<DiscountResp>>> {
            override fun onResponse(
                call: Call<BaseResponse<List<DiscountResp>>>,
                response: Response<BaseResponse<List<DiscountResp>>>
            ) {
                callback.apiRequesting(false)
                callback.apiResponse(getBodyResponse(response))
            }

            override fun onFailure(call: Call<BaseResponse<List<DiscountResp>>>, t: Throwable) {
                callback.apiRequesting(false)
                t.printStackTrace()
                callback.showMessage(t.message)
            }
        })
    }

    fun getDiscountDetailList(
        userGuid: String?,
        locationGuid: String?,
        callback: BaseRepoCallback<BaseResponse<List<CouponResp>>>
    ) {
        callback.apiRequesting(true)
        discountService.getDiscountDetails(userGuid,locationGuid).enqueue(object :
            Callback<BaseResponse<List<CouponResp>>> {
            override fun onResponse(
                call: Call<BaseResponse<List<CouponResp>>>,
                response: Response<BaseResponse<List<CouponResp>>>
            ) {
                callback.apiRequesting(false)
                callback.apiResponse(getBodyResponse(response))
            }

            override fun onFailure(call: Call<BaseResponse<List<CouponResp>>>, t: Throwable) {
                callback.apiRequesting(false)
                t.printStackTrace()
                callback.showMessage(t.message)
            }
        })
    }

    fun postDiscountCoupon(
        body: String,
        callback: BaseRepoCallback<BaseResponse<List<DiscountCoupon>>?>
    ) {
        callback.apiRequesting(true)
        discountService.postDiscountCoupon(body).enqueue(object :
            Callback<BaseResponse<List<DiscountCoupon>>?> {
            override fun onResponse(
                call: Call<BaseResponse<List<DiscountCoupon>>?>,
                response: Response<BaseResponse<List<DiscountCoupon>>?>
            ) {
                callback.apiRequesting(false)
                callback.apiResponse(getBodyResponse(response))
            }

            override fun onFailure(
                call: Call<BaseResponse<List<DiscountCoupon>>?>,
                t: Throwable
            ) {
                callback.apiRequesting(false)
                t.printStackTrace()
                callback.showMessage(t.message)
            }

        })
    }
}