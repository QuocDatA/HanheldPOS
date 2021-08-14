package com.hanheldpos.ui.screens.home.order

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.data.api.pojo.CategoryItem
import com.hanheldpos.data.api.pojo.ModelItem
import com.hanheldpos.data.api.pojo.OrderMenuResp
import com.hanheldpos.data.api.pojo.ProductItem
import com.hanheldpos.data.api.pojo.order.getCategoryList
import com.hanheldpos.ui.base.viewmodel.BaseViewModel
import com.hanheldpos.utils.screens.notifyValueChange

class OrderDataVM : BaseViewModel() {
    private var orderMenuResp: OrderMenuResp? = null;
    val categoryList = MutableLiveData<MutableList<CategoryItem>>(mutableListOf());
    val productList = MutableLiveData<MutableList<ProductItem>>(mutableListOf());

    fun initData() {
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
                        ), CategoryItem(
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
                        ), CategoryItem(
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

}