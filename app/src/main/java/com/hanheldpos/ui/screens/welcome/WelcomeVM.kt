package com.hanheldpos.ui.screens.welcome

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.data.api.pojo.welcome.WelcomeRespModel
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.welcome.WelcomeRepo
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class WelcomeVM : BaseUiViewModel<WelcomeUV>() {
    private var welcomeRepo: WelcomeRepo = WelcomeRepo()

    var isLoading = MutableLiveData(false)

    fun initLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
    }

    fun openDeviceCode() {
        uiCallback?.openDeviceCode()
    }

    fun initUI() {
        isLoading.postValue(true)
        welcomeRepo.getWelcomeModel(callback = object :
            BaseRepoCallback<BaseResponse<List<WelcomeRespModel>>> {
            override fun apiResponse(data: BaseResponse<List<WelcomeRespModel>>?) {
                isLoading.postValue(false)
                if (data != null && !data.DidError) {
                    uiCallback?.updateUI(data.Model?.firstOrNull())
                }
            }

            override fun showMessage(message: String?) {
                isLoading.postValue(false)
            }

        })
    }



}