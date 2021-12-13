package com.hanheldpos.data.repository.discount

import com.hanheldpos.data.api.pojo.discount.CouponResp
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.api.pojo.employee.EmployeeResp
import com.hanheldpos.data.repository.GDataResp
import com.hanheldpos.data.repository.base.BaseRepo
import com.hanheldpos.data.repository.base.BaseRepoCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiscountRepo : BaseRepo() {
    fun getDiscountList(
        userGuid: String?,
        locationGuid: String?,
         callback: BaseRepoCallback<DiscountResp>
    ) {
        callback.apiRequesting(true);
        discountService.getDiscounts(userGuid,locationGuid).enqueue(object :
            Callback<DiscountResp> {
            override fun onResponse(
                call: Call<DiscountResp>,
                response: Response<DiscountResp>
            ) {
                callback.apiRequesting(false);
                callback.apiResponse(getBodyResponse(response));
            }

            override fun onFailure(call: Call<DiscountResp>, t: Throwable) {
                callback.apiRequesting(false);
                t.printStackTrace();
                callback.showMessage(t.message);
            }
        });
    }

    fun getDiscountDetailList(
        userGuid: String?,
        locationGuid: String?,
        callback: BaseRepoCallback<CouponResp>
    ) {
        callback.apiRequesting(true);
        discountService.getDiscountDetails(userGuid,locationGuid).enqueue(object :
            Callback<CouponResp> {
            override fun onResponse(
                call: Call<CouponResp>,
                response: Response<CouponResp>
            ) {
                callback.apiRequesting(false);
                callback.apiResponse(getBodyResponse(response));
            }

            override fun onFailure(call: Call<CouponResp>, t: Throwable) {
                callback.apiRequesting(false);
                t.printStackTrace();
                callback.showMessage(t.message);
            }
        });
    }
}