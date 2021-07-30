package com.hanheldpos.ui.screens.home.order

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.hanheldpos.model.home.MenuModel
import com.hanheldpos.model.home.ProductModel
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.base.viewmodel.BaseViewModel

class OrderVM : BaseUiViewModel<OrderUV>() {

    // Value
    val menuSelected = MutableLiveData<MenuModel>()
    private val menuList = MutableLiveData<MutableList<MenuModel>>(mutableListOf());
    private val productSelectd = MutableLiveData<ProductModel>();
    private val productList = MutableLiveData<MutableList<ProductModel>>(mutableListOf());



    init {
        initMenuList();
        initProductList();
        menuSelected.value = menuList.value?.first();
    }

    fun initLifecycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this);

        menuSelected.observe(owner, {
            notifyMenuChange();
        })

        productList.observe(owner,{
            notyfyProductListChange();
        });

    }

    // Menu
    private fun initMenuList() {
        val rs: MutableList<MenuModel> = mutableListOf();
        rs.add(MenuModel("pizza", "Pizza"));
        rs.add(MenuModel("pasta", "Pasta"));
        rs.add(MenuModel("appetizer", "Appetizer"));
        rs.add(MenuModel("main_course", "Main Course"));
        rs.add(MenuModel("sandwich", "Sandwich"));
        menuList.value = rs;
    }

    fun getListOrderMenu(): List<MenuModel>? {
        return menuList.value;
    }

    fun menuItemSelected(adapterPosition: Int, item: MenuModel) {
        Log.d("MenuItemModel", "MenuModel : ok");
        menuSelected.value = item;
    }

    private fun notifyMenuChange() {
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

    // Product

    private fun initProductList(){
        val rs: MutableList<ProductModel> = mutableListOf();
        rs.add(ProductModel("", "Pizza",10000.0,"Tomato sauce, Fresh mozzarella,\nsun dried tomato and basil."));
        rs.add(ProductModel("", "Pizza",10000.0,"Tomato sauce, Fresh mozzarella,\nsun dried tomato and basil."));
        rs.add(ProductModel("", "Pizza",10000.0,"Tomato sauce, Fresh mozzarella,\nsun dried tomato and basil."));
        rs.add(ProductModel("", "Pizza",10000.0,"Tomato sauce, Fresh mozzarella,\nsun dried tomato and basil."));
        rs.add(ProductModel("", "Pizza",10000.0,"Tomato sauce, Fresh mozzarella,\nsun dried tomato and basil."));
        productList.value = rs;
    }

    fun getProductList() : List<ProductModel>?{
        return productList.value;
    }

    fun productItemSelected(adapterPosition: Int, item: ProductModel) {
        Log.d("ProductItemModel", "ProductModel : ok");
        productSelectd.value = item;
    }

    private fun notyfyProductListChange(){
        uiCallback?.productListObserve();
    }


}