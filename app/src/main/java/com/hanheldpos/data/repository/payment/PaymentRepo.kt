package com.hanheldpos.data.repository.payment

import com.hanheldpos.data.api.pojo.payment.GiftCardResp
import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp
import com.hanheldpos.data.api.pojo.payment.WalletCardResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepo
import com.hanheldpos.data.repository.base.BaseRepoCallback
import kotlinx.parcelize.RawValue
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentRepo : BaseRepo() {
    fun getPaymentMethods(
        userGuid: String?,
        callback: BaseRepoCallback<BaseResponse<List<PaymentMethodResp>>>
    ) {
        callback.apiRequesting(true)
        paymentService.getPaymentMethods(userGuid = userGuid).enqueue(object :
            Callback<BaseResponse<List<PaymentMethodResp>>> {
            override fun onResponse(
                call: Call<BaseResponse<List<PaymentMethodResp>>>,
                response: Response<BaseResponse<List<PaymentMethodResp>>>
            ) {
                callback.apiRequesting(false)
                callback.apiResponse(getBodyResponse(response))
            }

            override fun onFailure(
                call: Call<BaseResponse<List<PaymentMethodResp>>>,
                t: Throwable
            ) {
                callback.apiRequesting(false)
                t.printStackTrace()
                callback.showMessage(t.message)
            }

        })
    }

    fun getValidGiftCard(
        userGuid: String?,
        codeCard: String?,
        callback: BaseRepoCallback<BaseResponse<GiftCardResp>>
    ) {
        callback.apiRequesting(true)
        paymentService.getValidGiftCard(userGuid = userGuid, codeCard = codeCard).enqueue(object :
            Callback<BaseResponse<GiftCardResp>> {
            override fun onResponse(
                call: Call<BaseResponse<GiftCardResp>>,
                response: Response<BaseResponse<GiftCardResp>>
            ) {
                callback.apiRequesting(false)
                callback.apiResponse(getBodyResponse(response))
            }

            override fun onFailure(call: Call<BaseResponse<GiftCardResp>>, t: Throwable) {
                callback.apiRequesting(false)
                t.printStackTrace()
                callback.showMessage(t.message)
            }

        })
    }

    fun getValidWallet(keyWallet : String,callback: BaseRepoCallback<BaseResponse<WalletCardResp>> ) {
        callback.apiRequesting(true)
        paymentService.getValidWallet(keyWallet).enqueue(object :
            Callback<BaseResponse<WalletCardResp>> {
            override fun onResponse(
                call: Call<BaseResponse<WalletCardResp>>,
                response: Response<BaseResponse<WalletCardResp>>
            ) {
                callback.apiRequesting(false)
                callback.apiResponse(getBodyResponse(response))
            }

            override fun onFailure(call: Call<BaseResponse<WalletCardResp>>, t: Throwable) {
                callback.apiRequesting(false)
                t.printStackTrace()
                callback.showMessage(t.message)
            }

        })
    }

    fun syncPaymentCard(body: String?, callback: BaseRepoCallback<BaseResponse<@RawValue Any>>) {
        callback.apiRequesting(true)
        paymentService.syncPayment(body).enqueue(object :
            Callback<BaseResponse<@RawValue Any>> {
            override fun onResponse(
                call: Call<BaseResponse<@RawValue Any>>,
                response: Response<BaseResponse<@RawValue Any>>
            ) {
                callback.apiRequesting(false)
                callback.apiResponse(getBodyResponse(response))
            }

            override fun onFailure(call: Call<BaseResponse<@RawValue Any>>, t: Throwable) {
                callback.apiRequesting(false)
                t.printStackTrace()
                callback.showMessage(t.message)
            }

        })
    }


}