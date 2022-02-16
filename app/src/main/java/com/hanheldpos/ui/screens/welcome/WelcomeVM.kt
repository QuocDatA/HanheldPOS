package com.hanheldpos.ui.screens.welcome

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.welcome.WelcomeRespModel
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.welcome.WelcomeRepo
import com.hanheldpos.model.DataHelper
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class WelcomeVM : BaseUiViewModel<WelcomeUV>() {
    var welcomeRepo: WelcomeRepo = WelcomeRepo()
    var isLoading = MutableLiveData<Boolean>(false)

    fun initLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
    }

    fun openDeviceCode() {
        Log.d("Test Click", "is Clicked");
        uiCallback?.openDeviceCode();
    }

    fun initUI() {
        welcomeRepo.getWelcomeModel(callback = object :
            BaseRepoCallback<BaseResponse<List<WelcomeRespModel>>> {
            override fun apiResponse(data: BaseResponse<List<WelcomeRespModel>>?) {
                if (data == null || data.DidError) {

                } else {
                    welcomeRespModel = data.Model!!
                }
            }

            override fun showMessage(message: String?) {

            }

        })
    }

    fun checkDeviceCode() {
        DataHelper.deviceCodeLocalStorage?.let {
            uiCallback?.openPinCode();
        }
    }

}