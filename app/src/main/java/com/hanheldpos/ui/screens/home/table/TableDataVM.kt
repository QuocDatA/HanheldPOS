package com.hanheldpos.ui.screens.home.table

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.data.api.pojo.table.*
import com.hanheldpos.model.home.table.TableModeViewType
import com.hanheldpos.ui.base.viewmodel.BaseViewModel
import com.hanheldpos.ui.screens.home.order.OrderDataVM

class TableDataVM : BaseViewModel() {

    // Value
    private var tableResp: TableResp? = null;
    private val floorList = MutableLiveData<MutableList<FloorItem?>>(mutableListOf());
    val floorItemSelected = MutableLiveData<FloorItem>();
    val floorTableList = MutableLiveData<MutableList<FloorTableItem?>>(mutableListOf());


    fun initData() {
        // Fake Data
        val floorListT = MutableList(1){
            FloorItem(
                id = "Floor/90",
                rev = "_cto8ANq---",
                key = 90,
                floorId = 11,
                floorCode = "00000b",

            )
        }
        val tableList = MutableList(30) {
            FloorTableItem(
                id = "FloorTable/" + (6824 + it).toString(),
                floorGuid = "Floor/90",
                tableTypeId = it,
                tableName = "B$it",
                userGuid = "Customer/438227813"
            )
        }

        tableResp = TableResp(
            model = mutableListOf(
                ModelItem(
                    floorTable = tableList,
                    floor = floorListT
                ),
            ),
        )
        initFloor();
        floorItemSelected.value = floorList.value?.firstOrNull();
    }



    private fun initFloor() {
        floorList.value = tableResp?.getFloorList()?.toMutableList();
    }


    fun getTableListByFloor(floorItem : FloorItem ) : List<FloorTableItem?>?{
        return tableResp?.getTableWithFloorGuid(floorGuid = floorItem.id);
    }

    fun onTableClick(item : FloorTableItem){

    }
}