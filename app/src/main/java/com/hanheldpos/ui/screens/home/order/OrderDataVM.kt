package com.hanheldpos.ui.screens.home.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hanheldpos.data.api.pojo.order.*
import com.hanheldpos.data.api.pojo.order.ModelItem
import com.hanheldpos.data.api.pojo.order.getCategoryList
import com.hanheldpos.data.api.pojo.order.getProductWithCategoryGuid
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.home.order.product.ProductModeViewType
import com.hanheldpos.model.product.ProductCompleteModel
import com.hanheldpos.ui.base.viewmodel.BaseViewModel

class OrderDataVM : BaseViewModel() {
    private var orderMenuResp: OrderMenuResp? = null;
    val categoryList = MutableLiveData<MutableList<CategoryItem?>>(mutableListOf());
    val categorySelected = MutableLiveData<CategoryItem>();
    val productInCartLD = MutableLiveData<MutableList<ProductCompleteModel>?>(mutableListOf())
    val productQuantityInCartLD = Transformations.map(productInCartLD) {
        return@map productInCartLD.value?.sumBy {
            it.quantity
        } ?: 0
    }
    val productTotalPriceLD = Transformations.map(productInCartLD) {
        return@map productInCartLD.value?.sumByDouble {

            it.productItem.price?.times(it.quantity)!!


        }?:0.0
    }

    fun initData() {
        initMenus();
        initCategories();

        // Init First Page
        categorySelected.value = categoryList.value?.first();
    }

    // Menu
    private fun initMenus() {

        val categorys = mutableListOf(
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
                id = "Category/430214304",
                categoryId = 32,
                title = "COM",
                handle = "pho-cb",
                description = "",
                orderNo = 0,
                visible = 1,
                color = "#3166FF"
            ),
            CategoryItem(
                id = "Category/430214305",
                categoryId = 33,
                title = "BUN",
                handle = "pho-cb",
                description = "",
                orderNo = 0,
                visible = 1,
                color = "#3166FF"
            ), CategoryItem(
                id = "Category/430214309",
                categoryId = 37,
                title = "COMBO PHO",
                handle = "pho-cb",
                description = "",
                orderNo = 0,
                visible = 1,
                color = "#A61CD7"
            ), CategoryItem(
                id = "Category/430214310",
                categoryId = 38,
                title = "COMBO COM",
                handle = "pho-cb",
                description = "",
                orderNo = 0,
                visible = 1,
                color = "#A61CD7"
            ), CategoryItem(
                id = "Category/430214311",
                categoryId = 39,
                title = "COMBO BUN",
                handle = "pho-cb",
                description = "",
                orderNo = 0,
                visible = 1,
                color = "#A61CD7"
            ), CategoryItem(
                id = "Category/430214315",
                categoryId = 43,
                title = "MENU SANG",
                handle = "pho-cb",
                description = "",
                orderNo = 0,
                visible = 1,
                color = "#2A9C0E"
            ), CategoryItem(
                id = "Category/430214316",
                categoryId = 44,
                title = "PHO BOGO",
                handle = "pho-cb",
                description = "",
                orderNo = 0,
                visible = 1,
                color = "#2A9C0E"
            ), CategoryItem(
                id = "Category/430214317",
                categoryId = 45,
                title = "PHO GOI",
                handle = "pho-cb",
                description = "",
                orderNo = 0,
                visible = 1,
                color = "#2A9C0E"
            ),
            CategoryItem(
                id = "Category/430214306",
                categoryId = 34,
                title = "ALL DRINKS",
                handle = "pho-cb",
                description = "",
                orderNo = 0,
                visible = 1,
                color = "#2989A8"
            ), CategoryItem(
                id = "Category/430214307",
                categoryId = 35,
                title = "ALL STARTERS",
                handle = "pho-cb",
                description = "",
                orderNo = 0,
                visible = 1,
                color = "#2989A8"
            ), CategoryItem(
                id = "Category/430214308",
                categoryId = 36,
                title = "ALL DESSERT",
                handle = "pho-cb",
                description = "",
                orderNo = 0,
                visible = 1,
                color = "#2989A8"
            ), CategoryItem(
                id = "Category/430214312",
                categoryId = 40,
                title = "EXTRA",
                handle = "pho-cb",
                description = "",
                orderNo = 0,
                visible = 1,
                color = "#B58200"
            ),
            CategoryItem(
                id = "Category/430214313",
                categoryId = 41,
                title = "ALL OTHER",
                handle = "pho-cb",
                description = "",
                orderNo = 0,
                visible = 1,
                color = "#B58200"
            ), CategoryItem(
                id = "Category/430214314",
                categoryId = 42,
                title = "ALL ON",
                handle = "pho-cb",
                description = "",
                orderNo = 0,
                visible = 1,
                color = "#B58200"
            ), CategoryItem(
                id = "Category/430214318",
                categoryId = 46,
                title = "PHO",
                handle = "pho-cb",
                description = "",
                orderNo = 0,
                visible = 1,
                color = "#3166FF"
            ), CategoryItem(
                id = "Category/430214319",
                categoryId = 47,
                title = "PHO",
                handle = "pho-cb",
                description = "",
                orderNo = 0,
                visible = 1,
                color = "#3166FF"
            ),
            CategoryItem(
                id = "Category/430214320",
                categoryId = 48,
                title = "COM",
                handle = "pho-cb",
                description = "",
                orderNo = 0,
                visible = 1,
                color = "#3166FF"
            )
        );
        val products = mutableListOf<ProductItem?>()
        categorys.forEach {
            products.addAll(MutableList((10..40).random()) { it1 ->
                ProductItem(
                    id = "Product/" + (1000000..9000000).random(),
                    name = it.title,
                    description = "Nước dùng thơm ngon nấu từ 100% xương ống bò trong 8 giờ",
                    price = 39000.0,
                    name3 = "500g/Tô",
                    categoryGuid = it.id
                )
            })
        }

        // Fake data
        orderMenuResp = OrderMenuResp(
            model = mutableListOf(
                ModelItem(
                    category = categorys,
                    listProduct =
                    mutableListOf(
                        ListProductItem(
                            product = products
                        )
                    )
                )
            )
        );

    }

    // Category
    private fun initCategories() {
        categoryList.value = orderMenuResp?.getCategoryList()?.toMutableList();
    }

    fun getProductByCategory(categoryItem: CategoryItem): List<ProductItem?>? {
        return orderMenuResp?.getProductWithCategoryGuid(categoryGuid = categoryItem.id);
    }

    // Cart
    fun addProductCompleteToCart(item : ProductCompleteModel){
        productInCartLD.value?.add(item);
        productInCartLD.notifyValueChange();
    }
    fun deleteAllProductCart(){
        productInCartLD.value?.clear();
        productInCartLD.notifyValueChange();
    }

}