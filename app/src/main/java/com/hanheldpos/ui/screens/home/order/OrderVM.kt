package com.hanheldpos.ui.screens.home.order

import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.data.api.pojo.CategoryItem
import com.hanheldpos.data.api.pojo.ModelItem
import com.hanheldpos.data.api.pojo.OrderMenuResp
import com.hanheldpos.data.api.pojo.ProductItem
import com.hanheldpos.data.api.pojo.order.getCategoryList
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.screens.notifyValueChange
import kotlinx.coroutines.*

class OrderVM : BaseUiViewModel<OrderUV>() {

    // Value
    var dropDownCategory = MutableLiveData<Boolean>(false);

    var mLastTimeClick: Long = 0

    init {

    }

    fun initLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this);

        dropDownCategory.observe(owner, {
            uiCallback?.showDropdownCategories(it)
        })

    }

    fun changeViewDropdown() {
        if (SystemClock.elapsedRealtime() - mLastTimeClick > 500) {
            mLastTimeClick = SystemClock.elapsedRealtime();
            dropDownCategory.value = !dropDownCategory.value!!;
        }
    }

    fun categoryItemSelected(adapterPosition: Int, item: CategoryItem) {
        Log.d("OrderVM", "Category : Select Item");
        changeViewDropdown();
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                uiCallback?.productListObserve(mutableListOf(item));
            }
        }


    }

    // Product
    fun productItemSelected(adapterPosition: Int, item: ProductItem) {
        Log.d("ProductItemModel", "ProductModel : ok");
        uiCallback?.showProductSelected(item);
    }


}