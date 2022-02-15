package com.hanheldpos.ui.screens.home.table

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.data.api.pojo.floor.Floor
import com.hanheldpos.data.api.pojo.floor.FloorResp
import com.hanheldpos.data.api.pojo.floor.FloorTable
import com.hanheldpos.model.DataHelper
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class TableVM : BaseUiViewModel<TableUV>() {

    var mLastTimeClick: Long = 0

    fun initLifecycle(owner: LifecycleOwner){
        owner.lifecycle.addObserver(this);
    }

    // Value
    private var floorResp: FloorResp? = null;
    private val floorList = MutableLiveData<MutableList<Floor>>(mutableListOf());
    val floorItemSelected = MutableLiveData<Floor>();
    val floorTableList = MutableLiveData<MutableList<FloorTable>>(mutableListOf());


    fun initData() {
        floorResp = DataHelper.floorLocalStorage
        initFloor();
        /*floorItemSelected.value = floorList.value?.firstOrNull();*/
    }


    private fun initFloor() {
        floorList.value = floorResp?.Floor?.toMutableList();
    }


    fun getTableListByFloor(floorItem : Floor) : List<FloorTable>?{
        return floorResp?.FloorTable?.filter { it.FloorGuid == floorItem._Id };
    }

    fun getTableList() :List<FloorTable>?{
        return floorResp?.FloorTable;
    }

    fun onTableClick(item : FloorTable){

    }
}