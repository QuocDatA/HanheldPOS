package com.hanheldpos.ui.screens.main.home.order

import android.annotation.SuppressLint
import android.util.Log
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hanheldpos.model.home.order.CategoryModel
import com.hanheldpos.model.home.order.MenuModel
import com.hanheldpos.model.home.order.ProductModel
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.screens.notifyValueChange

class OrderVM : BaseUiViewModel<OrderUV>() {

    // Value
    var dropDownCategory = MutableLiveData<Boolean>(false);
    private val categoryList  = MutableLiveData<MutableList<CategoryModel>>(mutableListOf());


    init {
        initCategories();
    }

    fun initLifecycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this);

        categoryList.observe(owner,{
            uiCallback?.categoryListObserve(it);
        });

        dropDownCategory.observe(owner,{
            uiCallback?.showDropdownCategories(it)
        })
    }

    // Category
    private fun initCategories(){
        categoryList.value?.add(CategoryModel("AccountGroup/159","Com",null,"",0,"#3166FF",null,0,1,"Customer/438227813"))
        categoryList.value?.add(CategoryModel("AccountGroup/159","Com",null,"",0,"#3166FF",null,0,1,"Customer/438227813"))
        categoryList.value?.add(CategoryModel("AccountGroup/159","Com",null,"",0,"#f9ad2e",null,0,1,"Customer/438227813"))
        categoryList.value?.add(CategoryModel("AccountGroup/159","Com",null,"",0,"#f9ad2e",null,0,1,"Customer/438227813"))
        categoryList.value?.add(CategoryModel("AccountGroup/159","Com",null,"",0,"#2989A8",null,0,1,"Customer/438227813"))
        categoryList.value?.add(CategoryModel("AccountGroup/159","Com",null,"",0,"#2989A8",null,0,1,"Customer/438227813"))
        categoryList.value?.add(CategoryModel("AccountGroup/159","Com",null,"",0,"#007b80",null,0,1,"Customer/438227813"))
        categoryList.value?.add(CategoryModel("AccountGroup/159","Com",null,"",0,"#007b80",null,0,1,"Customer/438227813"))
        categoryList.notifyValueChange();
    }

    fun changeViewDropdown(){
        dropDownCategory.value = !dropDownCategory.value!!;
    }

    fun categoryItemSelected(adapterPosition: Int, item: CategoryModel){
        Log.d("OrderVM","Category : Select Item ${item.id}");
        changeViewDropdown();
    }



    // Product

    fun productItemSelected(adapterPosition: Int, item: ProductModel) {
        Log.d("ProductItemModel", "ProductModel : ok");

    }


}