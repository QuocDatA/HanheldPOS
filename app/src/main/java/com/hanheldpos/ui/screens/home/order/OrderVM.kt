package com.hanheldpos.ui.screens.home.order

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.hanheldpos.model.home.MenuModel
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.base.viewmodel.BaseViewModel

class OrderVM : BaseUiViewModel<OrderUV>() {

    val menuSelected = MutableLiveData<MenuModel>()

    val menuList = MutableLiveData<MutableList<MenuModel>>(mutableListOf());

    init {
        initListMenu();
        menuSelected.value = menuList.value?.first();
    }

    fun initLifecycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this);
        menuSelected.observe(owner,{
            notifyMenuChange();
        })
    }

    private fun initListMenu(){
        val rs : MutableList<MenuModel> = mutableListOf();
        rs.add(MenuModel("pizza","Pizza"));
        rs.add(MenuModel("pasta","Pasta"));
        rs.add(MenuModel("appetizer","Appetizer"));
        rs.add(MenuModel("main_course","Main Course"));
        rs.add(MenuModel("sandwich","Sandwich"));
        menuList.value = rs;
    }

    fun getListOrderMenu() : List<MenuModel>? {
        return menuList.value;
    }

    fun menuItemSelected(adapterPosition: Int, item: MenuModel){
        Log.d("MenuItemModel","MenuModel : ok");
        menuSelected.value = item;
    }

    private fun notifyMenuChange(){

        menuList.value?.map {
            it.selected = false;
        }

        menuList.value?.find {
            it.id == menuSelected.value?.id
        }?.apply {
            this.selected = true;
        }
        uiCallback?.menuListObserve();
    }


}