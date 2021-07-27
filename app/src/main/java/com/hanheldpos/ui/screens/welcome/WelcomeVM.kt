package com.hanheldpos.ui.screens.welcome

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class WelcomeVM : BaseUiViewModel<WelcomeUV>() {

    fun initLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
    }


    fun openDeviceCode(){
        Log.d("Test Click","is Clicked");
        uiCallback?.openDeviceCode();
    }

}