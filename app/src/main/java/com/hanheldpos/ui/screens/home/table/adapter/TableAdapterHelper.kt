package com.hanheldpos.ui.screens.home.table.adapter


import com.hanheldpos.data.api.pojo.floor.FloorTable
import com.hanheldpos.database.DatabaseMapper
import com.hanheldpos.model.DatabaseHelper
import com.hanheldpos.model.home.table.TableModeViewType
import com.hanheldpos.model.home.table.TableStatusType
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking


class TableAdapterHelper(private val callback: AdapterCallBack) {

    interface AdapterCallBack {
        fun onListSplitCallBack(list: List<FloorTable>)
    }

    private var currentIndex: Int = 1;

    private var list: MutableList<FloorTable> = mutableListOf();
    private var listOfTablePage: MutableList<List<FloorTable>> = mutableListOf();

    fun submitList(list: MutableList<FloorTable>) {

        // Processing order in table

        this.list = runBlocking {
            processingOrderInTable(list.onEach {
                it.uiType = TableModeViewType.Table
                it.tableStatus = TableStatusType.Available
            }).toMutableList()
        }

        currentIndex = 1
        listOfTablePage.clear()
        var sizeOfMainList: Int = this.list.size
        var currentListIndex = 1 // page currentIndex

        if (sizeOfMainList > 0) {
            while (sizeOfMainList > 0) {
                val tablePage = split(currentListIndex)
                listOfTablePage.add(tablePage)
                sizeOfMainList -= (tablePage.size - 2) // reduce list size except for 2 dirButton
                currentListIndex++
            }
        } else if (sizeOfMainList == 0) {
            val tempList = split(currentListIndex)
            listOfTablePage.add(tempList)
        }

        callback.onListSplitCallBack(listOfTablePage[currentIndex - 1])
    }

    fun next() {
        if ((currentIndex * maxItemViewProduct) < list.size) {
            currentIndex = currentIndex.plus(1);
            callback.onListSplitCallBack(listOfTablePage[currentIndex - 1])
        }

    }

    fun previous() {
        if (currentIndex > 1) {
            currentIndex = currentIndex.minus(1);
            callback.onListSplitCallBack(listOfTablePage[currentIndex - 1])
        }

    }

    private fun split(pagePosition: Int): MutableList<FloorTable> {
        val rs: MutableList<FloorTable> = mutableListOf();
        list.let {
            if (it.size != 0) {
                val start: Int = (pagePosition - 1) * maxItemViewProduct;
                val end: Int =
                    if (it.size > maxItemViewProduct * pagePosition) maxItemViewProduct * pagePosition else it.size;
                rs.addAll(it.toList().subList(start, end) as List<FloorTable>);
            }
        }

        // Add Empty
        for (i in rs.size until maxItemViewProduct) {
            rs.add(FloorTable("", "", 0.0, 0.0, 0, 0, "", "", 0, 0.0, "", 0, 0.0, "", 0, "").apply {
                uiType = TableModeViewType.Empty;
            })
        }

        // set state for Direction Button to add to list
        var nextButtonType = TableModeViewType.NextButtonDisable
        var prevButtonType = TableModeViewType.PrevButtonDisable
        if (pagePosition > 1) {
            prevButtonType = TableModeViewType.PrevButtonEnable
        }
        if ((pagePosition * maxItemViewProduct) < list.size) {
            nextButtonType = TableModeViewType.NextButtonEnable
        }

        // Add Direction Button
        rs.addAll(
            listOf(
                /*
                * 1 : Enable
                * 2 : Disable
                * */
                FloorTable("", "", 0.0, 0.0, 0, 0, "", "", 0, 0.0, "", 0, 0.0, "", 0, "").apply {
                    uiType = prevButtonType
                },
                FloorTable("", "", 0.0, 0.0, 0, 0, "", "", 0, 0.0, "", 0, 0.0, "", 0, "").apply {
                    uiType = nextButtonType
                })
        )
        return rs
    }

    private suspend fun processingOrderInTable(list: List<FloorTable>): List<FloorTable> {

        DatabaseHelper.tableStatuses.getAll().take(1).collectLatest { listEntity ->
            listEntity.map { DatabaseMapper.mappingTableFromEntity(it) }.forEach {
                list.find { floorTable -> floorTable._Id == it._Id }?.let { tableFind ->
                    tableFind.tableStatus = it.tableStatus
                    tableFind.orderSummary = it.orderSummary
                }
            }
        }

        return list
    }

    companion object {
        @JvmStatic
        val maxItemViewProduct: Int = 13;
    }
}