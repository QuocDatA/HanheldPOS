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
    val floorItemSelected : MutableLiveData<FloorItem>? = null;
    val floorTablePage = MutableLiveData<Int>();
    val floorTableList = MutableLiveData<MutableList<FloorTableItem?>>(mutableListOf());


    fun initData() {
        tableResp = TableResp(
            model = mutableListOf(
                ModelItem(
                    floorTable = MutableList(100) {
                        FloorTableItem(
                            id = "FloorTable/" + (6824 + it).toString(),
                            floorGuid = "Floor/73",
                            tableTypeId = it,
                            tableName = it.toString(),
                            userGuid = "Customer/438227813"
                        )
                    }
                ),
            ),
        )
        initFloor();
        floorItemSelected?.value = floorList.value?.firstOrNull();
    }



    private fun initFloor() {
        floorList.value = tableResp?.getFloorList()?.toMutableList();
    }


    fun getTableListByFloor(floorItem : FloorItem ) : List<FloorTableItem?>?{
        return tableResp?.getTableWithFloorGuid(floorGuid = floorItem.id);
    }

    fun getTableListByPage(pagePosition: Int) : List<FloorTableItem?> {
        val rs : MutableList<FloorTableItem> = mutableListOf();
        floorTableList.value?.let {
            if (it.size != 0){
                val start: Int = (pagePosition - 1) * TableDataVM.maxItemViewTable;
                val end: Int =
                    if (it.size > TableDataVM.maxItemViewTable * pagePosition) TableDataVM.maxItemViewTable * pagePosition else it.size;
            }
        }
        // Add Empty
        for (i in rs.size..TableDataVM.maxItemViewTable){
            rs.add(FloorTableItem().apply {
                uiType = TableModeViewType.Empty;
            })
        }

        // Add Direction Button
        rs.addAll(listOf(
            FloorTableItem().apply {
                uiType = TableModeViewType.PrevButton;
            }, FloorTableItem().apply {
                uiType = TableModeViewType.NextButton;
            }))

        return rs;
    }

    companion object {
        @JvmStatic
        val maxItemViewTable : Int = 13;
    }
}