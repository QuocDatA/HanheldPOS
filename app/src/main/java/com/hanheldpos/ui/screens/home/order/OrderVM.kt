package com.hanheldpos.ui.screens.home.order

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
import kotlinx.coroutines.*
import okhttp3.internal.wait

class OrderVM : BaseUiViewModel<OrderUV>() {

    // Value
    var dropDownCategory = MutableLiveData<Boolean>(false);
    var isDropDownCategory : Boolean = false;
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

        categoryList.value?.get(0)?.childList = mutableListOf(
            ProductModel("","Com",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Com",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Com",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Com",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Com",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Com",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Com",70000.0,"Pho ga (L) & Thuc uong"),
        );

        categoryList.value?.add(CategoryModel("AccountGroup/159","PHO",null,"",0,"#3166FF",null,0,1,"Customer/438227813"))

        categoryList.value?.get(1)?.childList = mutableListOf(
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
        );
        categoryList.value?.add(CategoryModel("AccountGroup/159","BUN",null,"",0,"#3166FF",null,0,1,"Customer/438227813"))

        categoryList.value?.get(2)?.childList = mutableListOf(
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
        );
        categoryList.value?.add(CategoryModel("AccountGroup/159","COMBO PHO",null,"",0,"#3166FF",null,0,1,"Customer/438227813"))

        categoryList.value?.get(3)?.childList = mutableListOf(
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
        );
        categoryList.value?.add(CategoryModel("AccountGroup/159","COMBO COM",null,"",0,"#f9ad2e",null,0,1,"Customer/438227813"))
        categoryList.value?.get(4)?.childList = mutableListOf(
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
        );
        categoryList.value?.add(CategoryModel("AccountGroup/159","COMBO BUN",null,"",0,"#f9ad2e",null,0,1,"Customer/438227813"))
        categoryList.value?.get(5)?.childList = mutableListOf(
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
        );
        categoryList.value?.add(CategoryModel("AccountGroup/159","Com",null,"",0,"#f9ad2e",null,0,1,"Customer/438227813"))
        categoryList.value?.get(6)?.childList = mutableListOf(
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
        );
        categoryList.value?.add(CategoryModel("AccountGroup/159","Com",null,"",0,"#f9ad2e",null,0,1,"Customer/438227813"))
        categoryList.value?.get(7)?.childList = mutableListOf(
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
        );
        categoryList.value?.add(CategoryModel("AccountGroup/159","Com",null,"",0,"#2989A8",null,0,1,"Customer/438227813"))
        categoryList.value?.get(8)?.childList = mutableListOf(
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
        );
        categoryList.value?.add(CategoryModel("AccountGroup/159","Com",null,"",0,"#2989A8",null,0,1,"Customer/438227813"))
        categoryList.value?.get(9)?.childList = mutableListOf(
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
        );
        categoryList.value?.add(CategoryModel("AccountGroup/159","Com",null,"",0,"#007b80",null,0,1,"Customer/438227813"))
        categoryList.value?.get(10)?.childList = mutableListOf(
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
        );
        categoryList.value?.add(CategoryModel("AccountGroup/159","Com",null,"",0,"#007b80",null,0,1,"Customer/438227813"))
        categoryList.value?.get(11)?.childList = mutableListOf(
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
            ProductModel("","Combo Pho Ga L",70000.0,"Pho ga (L) & Thuc uong"),
        );
        categoryList.notifyValueChange();
    }

    fun changeViewDropdown(){
        if(isDropDownCategory) return;
        dropDownCategory.value = !dropDownCategory.value!!;
    }

    fun categoryItemSelected(adapterPosition: Int, item: CategoryModel){
        if(isDropDownCategory) return;
        Log.d("OrderVM","Category : Select Item ${item.id}");
        changeViewDropdown();

        CoroutineScope(Dispatchers.IO).launch {
            while (isDropDownCategory){
                delay(500);
            } ;
            withContext(Dispatchers.Main){
                uiCallback?.productListObserve(mutableListOf(item));
            }
        }


    }



    // Product
    fun productItemSelected(adapterPosition: Int, item: ProductModel) {
        Log.d("ProductItemModel", "ProductModel : ok");

    }


}