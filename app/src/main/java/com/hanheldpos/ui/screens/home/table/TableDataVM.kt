package com.hanheldpos.ui.screens.home.table

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.data.api.pojo.table.*
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.home.table.TableModeViewType
import com.hanheldpos.ui.base.viewmodel.BaseViewModel
import com.hanheldpos.ui.screens.home.order.OrderDataVM

class TableDataVM : BaseViewModel() {

    // Value
    val curTableLD: MutableLiveData<FloorTableItem?> = MutableLiveData()
    private var tableResp: TableResp? = null;
    private val floorList = MutableLiveData<MutableList<FloorItem?>>(mutableListOf());
    val floorItemSelected = MutableLiveData<FloorItem>();
    val floorTableList = MutableLiveData<MutableList<FloorTableItem?>>(mutableListOf());


    fun initData() {
        tableResp = DataHelper.tableResp
        initFloor();
        /*floorItemSelected.value = floorList.value?.firstOrNull();*/
    }


    private fun initFloor() {
        floorList.value = tableResp?.getFloorList()?.toMutableList();
    }


    fun getTableListByFloor(floorItem : FloorItem ) : List<FloorTableItem?>?{
        return tableResp?.getTableWithFloorGuid(floorGuid = floorItem.id);
    }

    fun getTableList() :List<FloorTableItem?>?{
        return tableResp?.getFloorTableList();
    }

    fun onTableClick(item : FloorTableItem){

    }
}