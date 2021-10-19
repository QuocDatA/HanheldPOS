package com.hanheldpos.ui.screens.welcome

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.hanheldpos.data.repository.settings.SettingRepo
import com.hanheldpos.model.DataHelper
import com.hanheldpos.ui.base.viewmodel.BaseRepoViewModel

class WelcomeVM : BaseRepoViewModel<SettingRepo,WelcomeUV>() {

    fun initLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
    }

    fun openDeviceCode(){
        Log.d("Test Click","is Clicked");
        uiCallback?.openDeviceCode();
    }

    override fun createRepo(): SettingRepo {
        return SettingRepo();
    }

    fun checkDeviceCode(){
        DataHelper.deviceCodeResp?.let {
            uiCallback?.openPinCode();
        }
    }

}