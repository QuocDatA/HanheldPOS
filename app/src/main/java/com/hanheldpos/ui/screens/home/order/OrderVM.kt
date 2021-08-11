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
    private var orderMenuResp: OrderMenuResp? = null;
    private val categoryList = MutableLiveData<MutableList<CategoryItem>>(mutableListOf());
    private val productList = MutableLiveData<MutableList<ProductItem>>(mutableListOf());
    var mLastTimeClick: Long = 0

    init {
        orderMenuResp = OrderMenuResp(
            model = mutableListOf(
                ModelItem(
                    category = mutableListOf(
                        CategoryItem(
                            id = "Category/430214303",
                            categoryId = 31,
                            title = "PHO",
                            handle = "pho-cb",
                            description = "",
                            orderNo = 0,
                            visible = 1,
                            color = "#3166FF"
                        ),
                        CategoryItem(
                            id = "Category/430214303",
                            categoryId = 31,
                            title = "ALL DRINKS",
                            handle = "pho-cb",
                            description = "",
                            orderNo = 0,
                            visible = 1,
                            color = "#2989A8"
                        ), CategoryItem(
                            id = "Category/430214303",
                            categoryId = 31,
                            title = "COM",
                            handle = "pho-cb",
                            description = "",
                            orderNo = 0,
                            visible = 1,
                            color = "#3166FF"
                        ), CategoryItem(
                            id = "Category/430214303",
                            categoryId = 31,
                            title = "ALL STARTERS",
                            handle = "pho-cb",
                            description = "",
                            orderNo = 0,
                            visible = 1,
                            color = "#2989A8"
                        ),CategoryItem(
                            id = "Category/430214303",
                            categoryId = 31,
                            title = "BUN",
                            handle = "pho-cb",
                            description = "",
                            orderNo = 0,
                            visible = 1,
                            color = "#3166FF"
                        ), CategoryItem(
                            id = "Category/430214303",
                            categoryId = 31,
                            title = "ALL DESSERT",
                            handle = "pho-cb",
                            description = "",
                            orderNo = 0,
                            visible = 1,
                            color = "#2989A8"
                        )
                        , CategoryItem(
                            id = "Category/430214303",
                            categoryId = 31,
                            title = "COMBO PHO",
                            handle = "pho-cb",
                            description = "",
                            orderNo = 0,
                            visible = 1,
                            color = "#A61CD7"
                        ), CategoryItem(
                            id = "Category/430214303",
                            categoryId = 31,
                            title = "EXTRA",
                            handle = "pho-cb",
                            description = "",
                            orderNo = 0,
                            visible = 1,
                            color = "#B58200"
                        ), CategoryItem(
                            id = "Category/430214303",
                            categoryId = 31,
                            title = "COMBO COM",
                            handle = "pho-cb",
                            description = "",
                            orderNo = 0,
                            visible = 1,
                            color = "#A61CD7"
                        ), CategoryItem(
                            id = "Category/430214303",
                            categoryId = 31,
                            title = "ALL OTHER",
                            handle = "pho-cb",
                            description = "",
                            orderNo = 0,
                            visible = 1,
                            color = "#B58200"
                        ), CategoryItem(
                            id = "Category/430214303",
                            categoryId = 31,
                            title = "COMBO BUN",
                            handle = "pho-cb",
                            description = "",
                            orderNo = 0,
                            visible = 1,
                            color = "#A61CD7"
                        ), CategoryItem(
                            id = "Category/430214303",
                            categoryId = 31,
                            title = "ALL ON",
                            handle = "pho-cb",
                            description = "",
                            orderNo = 0,
                            visible = 1,
                            color = "#B58200"
                        ), CategoryItem(
                            id = "Category/430214303",
                            categoryId = 31,
                            title = "MENU SANG",
                            handle = "pho-cb",
                            description = "",
                            orderNo = 0,
                            visible = 1,
                            color = "#2A9C0E"
                        ), CategoryItem(
                            id = "Category/430214303",
                            categoryId = 31,
                            title = "PHO",
                            handle = "pho-cb",
                            description = "",
                            orderNo = 0,
                            visible = 1,
                            color = "#3166FF"
                        ), CategoryItem(
                            id = "Category/430214303",
                            categoryId = 31,
                            title = "PHO BOGO",
                            handle = "pho-cb",
                            description = "",
                            orderNo = 0,
                            visible = 1,
                            color = "#2A9C0E"
                        ), CategoryItem(
                            id = "Category/430214303",
                            categoryId = 31,
                            title = "PHO",
                            handle = "pho-cb",
                            description = "",
                            orderNo = 0,
                            visible = 1,
                            color = "#3166FF"
                        ),
                        CategoryItem(
                            id = "Category/430214303",
                            categoryId = 31,
                            title = "PHO GOI",
                            handle = "pho-cb",
                            description = "",
                            orderNo = 0,
                            visible = 1,
                            color = "#2A9C0E"
                        ), CategoryItem(
                            id = "Category/430214303",
                            categoryId = 31,
                            title = "PHO",
                            handle = "pho-cb",
                            description = "",
                            orderNo = 0,
                            visible = 1,
                            color = "#3166FF"
                        )

                    )
                )
            )
        );

        initCategories();


    }

    fun initLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this);

        categoryList.observe(owner, {
            uiCallback?.categoryListObserve(it);
        });

        dropDownCategory.observe(owner, {
            uiCallback?.showDropdownCategories(it)
        })

    }

    // Category
    private fun initCategories() {
        orderMenuResp?.getCategoryList()?.forEach {
            if (it != null) {
                it.productList = mutableListOf(
                    ProductItem(
                        name = "PHO",

                        description = "Nước dùng thơm ngon nấu từ 100% xương ống bò trong 8 giờ",
                        price = 39000.0,
                        name3 = "500g/Tô"
                    ),
                    ProductItem(
                        name = "PHO",
                        description = "Nước dùng thơm ngon nấu từ 100% xương ống bò trong 8 giờ",
                        price = 39000.0,
                        name3 = "500g/Tô"
                    ),
                    ProductItem(
                        name = "PHO",
                        description = "Nước dùng thơm ngon nấu từ 100% xương ống bò trong 8 giờ",
                        price = 39000.0,
                        name3 = "500g/Tô"
                    ),
                    ProductItem(
                        name = "PHO",
                        description = "Nước dùng thơm ngon nấu từ 100% xương ống bò trong 8 giờ",
                        price = 39000.0,
                        name3 = "500g/Tô"
                    ),
                );
                categoryList.value?.add(it)
            };
        }

        categoryList.notifyValueChange();
    }


    fun initDataProductGroup() {
        categoryList.value.let {
            it?.let { it1 -> uiCallback?.productListObserve(it1) };
        }
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