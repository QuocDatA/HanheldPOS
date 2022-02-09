package com.hanheldpos.ui.screens.welcome

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.DatabaseHelper
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WelcomeVM : BaseUiViewModel<WelcomeUV>() {

    fun initLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
    }

    fun openDeviceCode(){
        Log.d("Test Click","is Clicked");
        uiCallback?.openDeviceCode();
    }

    fun checkDeviceCode(){
        viewModelScope.launch(Dispatchers.IO) {
            DatabaseHelper.deviceCodeLocalRepo?.getAll().let {
                launch(Dispatchers.Default) {
                    if(!it.isNullOrEmpty()) uiCallback?.openPinCode();
                }
            }
        }

    }

}