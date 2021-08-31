package com.hanheldpos.ui.screens.home

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.R
import com.hanheldpos.ui.base.viewmodel.BaseViewModel

class ScreenViewModel : BaseViewModel() {
    data class ScreenEvent(
        val screen: HomeFragment.HomePage,
        val data: Parcelable? = null
    )

    val screenEvent: MutableLiveData<ScreenEvent> = MutableLiveData()

    fun showOrderPage(data: Parcelable? = null) {
        screenEvent.value = ScreenEvent(HomeFragment.HomePage.Menu, data)
    }

    fun showTablePage(data: Parcelable? = null) {
        screenEvent.value = ScreenEvent(HomeFragment.HomePage.Table, data)
    }
}