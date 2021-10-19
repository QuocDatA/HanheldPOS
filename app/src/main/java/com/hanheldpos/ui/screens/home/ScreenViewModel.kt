package com.hanheldpos.ui.screens.home

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.table.FloorItem
import com.hanheldpos.ui.base.viewmodel.BaseViewModel

class ScreenViewModel : BaseViewModel() {
    data class ScreenEvent(
        val screen: HomeFragment.HomePage,
        val data: Parcelable? = null
    )

    val screenEvent: MutableLiveData<ScreenEvent> = MutableLiveData()

    fun showOrderPage(data: Parcelable? = null) {
        screenEvent.value = ScreenEvent(HomeFragment.HomePage.Order, data)
    }

    fun showTablePage(data: Parcelable? = null) {
        screenEvent.value = ScreenEvent(HomeFragment.HomePage.Table, data)
    }

    //Sub Spinner Sort
    val dropDownSelected = MutableLiveData<DropDownItem>();
    fun onChangeDropdown(item : DropDownItem){
        dropDownSelected.value = item;
    }
}