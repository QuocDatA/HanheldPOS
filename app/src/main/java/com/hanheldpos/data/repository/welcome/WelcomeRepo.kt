package com.hanheldpos.data.repository.welcome

import com.hanheldpos.data.api.pojo.welcome.WelcomeRespModel
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepo
import com.hanheldpos.data.repository.base.BaseRepoCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WelcomeRepo : BaseRepo() {
    fun getWelcomeModel(callback: BaseRepoCallback<BaseResponse<List<WelcomeRespModel>>>) {
        callback.apiRequesting(true);
        welcomeService.getWelcomeModel().enqueue(object :
            Callback<BaseResponse<List< WelcomeRespModel>>> {

            override fun onResponse(
                call: Call<BaseResponse<List<WelcomeRespModel>>>,
                response: Response<BaseResponse<List<WelcomeRespModel>>>
            ) {
                callback.apiRequesting(false);
                callback.apiResponse(getBodyResponse(response));
            }

            override fun onFailure(call: Call<BaseResponse<List<WelcomeRespModel>>>, t: Throwable) {
                callback.apiRequesting(false);
                t.printStackTrace();
                callback.showMessage(t.message);
            }

        })
    }
}