package com.hanheldpos.ui.screens.home.table

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.model.home.table.TableModel
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.screens.notifyValueChange

class TableVM : BaseUiViewModel<TableUV>() {
    // value
    val tableList = MutableLiveData<MutableList<TableModel>>(mutableListOf());

    init {
        initTableLlist();

    }

    fun initLifecycle(owner: LifecycleOwner){
        owner.lifecycle.addObserver(this);

        tableList.observe(owner, {
            uiCallback?.tableListObserve();
        })
    }

    private fun initTableLlist(){
        val rs : MutableList<TableModel> = mutableListOf();
        // Fake Data
        rs.add(TableModel("B1"));
        rs.add(TableModel("B2"));
        rs.add(TableModel("B3"));
        rs.add(TableModel("B4"));
        rs.add(TableModel("B5"));
        rs.add(TableModel("B6"));
        rs.add(TableModel("B7"));
        rs.add(TableModel("B8"));
        rs.add(TableModel("B9"));
        rs.add(TableModel("B10"));
        rs.add(TableModel("B11"));
        rs.add(TableModel("B12"));
        rs.add(TableModel("B13"));
        rs.add(TableModel("B14"));
        rs.add(TableModel("B15"));
        tableList.value = rs;
        tableList.notifyValueChange();
    }

    fun getTableList() : List<TableModel>?{
        return tableList.value;
    }

}