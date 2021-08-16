package com.hanheldpos.ui.screens.home.order

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.data.api.pojo.*
import com.hanheldpos.data.api.pojo.order.getCategoryList
import com.hanheldpos.data.api.pojo.order.getProductWithCategoryGuid
import com.hanheldpos.model.home.order.product.ProductModeViewType
import com.hanheldpos.ui.base.viewmodel.BaseViewModel
import com.hanheldpos.utils.screens.notifyValueChange

class OrderDataVM : BaseViewModel() {
    private var orderMenuResp: OrderMenuResp? = null;
    val categoryList = MutableLiveData<MutableList<CategoryItem?>>(mutableListOf());
    val categorySelected = MutableLiveData<CategoryItem>();
    val pageCategoryList = MutableLiveData<Int>();
    val productListSl = MutableLiveData<MutableList<ProductItem?>>(mutableListOf());
    val pageProductListSl = MutableLiveData<Int>();

    fun initData() {
        initMenus();
        initCategories();

        // Init First Page

        categorySelected.value = categoryList.value?.first();
        pageCategoryList.value = 1;


    }

    // Menu
    private fun initMenus() {
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
                            title = "COM",
                            handle = "pho-cb",
                            description = "",
                            orderNo = 0,
                            visible = 1,
                            color = "#3166FF"
                        ),
                        CategoryItem(
                            id = "Category/430214303",
                            categoryId = 31,
                            title = "BUN",
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
                            title = "ALL STARTERS",
                            handle = "pho-cb",
                            description = "",
                            orderNo = 0,
                            visible = 1,
                            color = "#2989A8"
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
                            title = "COMBO COM",
                            handle = "pho-cb",
                            description = "",
                            orderNo = 0,
                            visible = 1,
                            color = "#A61CD7"
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
                            title = "EXTRA",
                            handle = "pho-cb",
                            description = "",
                            orderNo = 0,
                            visible = 1,
                            color = "#B58200"
                        ),
                        CategoryItem(
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
                            title = "PHO BOGO",
                            handle = "pho-cb",
                            description = "",
                            orderNo = 0,
                            visible = 1,
                            color = "#2A9C0E"
                        ), CategoryItem(
                            id = "Category/430214303",
                            categoryId = 31,
                            title = "PHO GOI",
                            handle = "pho-cb",
                            description = "",
                            orderNo = 0,
                            visible = 1,
                            color = "#2A9C0E"
                        ), /*CategoryItem(
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
                            title = "PHO",
                            handle = "pho-cb",
                            description = "",
                            orderNo = 0,
                            visible = 1,
                            color = "#3166FF"
                        )*/

                    ),
                    listProduct = mutableListOf(
                        ListProductItem(
                            product = mutableListOf(
                                ProductItem(
                                    id = "Product/438212511",
                                    name = "Pho Bo Tai (S)",
                                    description = "Nước dùng thơm ngon nấu từ 100% xương ống bò trong 8 giờ",
                                    price = 39000.0,
                                    name3 = "500g/Tô",
                                    categoryGuid = "Category/430214303"
                                ),
                                ProductItem(
                                    id = "Product/438212512",
                                    name = "Pho Bo Tai (S)",
                                    description = "Nước dùng thơm ngon nấu từ 100% xương ống bò trong 8 giờ",
                                    price = 39000.0,
                                    name3 = "500g/Tô",
                                    categoryGuid = "Category/430214303"
                                ), ProductItem(
                                    id = "Product/438212513",
                                    name = "Pho Bo Tai (S)",
                                    description = "Nước dùng thơm ngon nấu từ 100% xương ống bò trong 8 giờ",
                                    price = 39000.0,
                                    name3 = "500g/Tô",
                                    categoryGuid = "Category/430214303"
                                ), ProductItem(
                                    id = "Product/438212514",
                                    name = "Pho Bo Tai (S)",
                                    description = "Nước dùng thơm ngon nấu từ 100% xương ống bò trong 8 giờ",
                                    price = 39000.0,
                                    name3 = "500g/Tô",
                                    categoryGuid = "Category/430214303"
                                ), ProductItem(
                                    id = "Product/438212515",
                                    name = "Pho Bo Tai (S)",
                                    description = "Nước dùng thơm ngon nấu từ 100% xương ống bò trong 8 giờ",
                                    price = 39000.0,
                                    name3 = "500g/Tô",
                                    categoryGuid = "Category/430214303"
                                ), ProductItem(
                                    id = "Product/438212516",
                                    name = "Pho Bo Tai (S)",
                                    description = "Nước dùng thơm ngon nấu từ 100% xương ống bò trong 8 giờ",
                                    price = 39000.0,
                                    name3 = "500g/Tô",
                                    categoryGuid = "Category/430214303"
                                ), ProductItem(
                                    id = "Product/438212517",
                                    name = "Pho Bo Tai (S)",
                                    description = "Nước dùng thơm ngon nấu từ 100% xương ống bò trong 8 giờ",
                                    price = 39000.0,
                                    name3 = "500g/Tô",
                                    categoryGuid = "Category/430214303"
                                ), ProductItem(
                                    id = "Product/438212518",
                                    name = "Pho Bo Tai (S)",
                                    description = "Nước dùng thơm ngon nấu từ 100% xương ống bò trong 8 giờ",
                                    price = 39000.0,
                                    name3 = "500g/Tô",
                                    categoryGuid = "Category/430214303"
                                ), ProductItem(
                                    id = "Product/438212519",
                                    name = "Pho Bo Tai (S)",
                                    description = "Nước dùng thơm ngon nấu từ 100% xương ống bò trong 8 giờ",
                                    price = 39000.0,
                                    name3 = "500g/Tô",
                                    categoryGuid = "Category/430214303"
                                ), ProductItem(
                                    id = "Product/438212520",
                                    name = "Pho Bo Tai (S)",
                                    description = "Nước dùng thơm ngon nấu từ 100% xương ống bò trong 8 giờ",
                                    price = 39000.0,
                                    name3 = "500g/Tô",
                                    categoryGuid = "Category/430214303"
                                ), ProductItem(
                                    id = "Product/438212521",
                                    name = "Pho Bo Tai (S)",
                                    description = "Nước dùng thơm ngon nấu từ 100% xương ống bò trong 8 giờ",
                                    price = 39000.0,
                                    name3 = "500g/Tô",
                                    categoryGuid = "Category/430214303"
                                ), ProductItem(
                                    id = "Product/438212522",
                                    name = "Pho Bo Tai (S)",
                                    description = "Nước dùng thơm ngon nấu từ 100% xương ống bò trong 8 giờ",
                                    price = 39000.0,
                                    name3 = "500g/Tô",
                                    categoryGuid = "Category/430214303"
                                ), ProductItem(
                                    id = "Product/438212523",
                                    name = "Pho Bo Tai (S)",
                                    description = "Nước dùng thơm ngon nấu từ 100% xương ống bò trong 8 giờ",
                                    price = 39000.0,
                                    name3 = "500g/Tô",
                                    categoryGuid = "Category/430214303"
                                ), ProductItem(
                                    id = "Product/438212524",
                                    name = "Pho Bo Tai (S)",
                                    description = "Nước dùng thơm ngon nấu từ 100% xương ống bò trong 8 giờ",
                                    price = 39000.0,
                                    name3 = "500g/Tô",
                                    categoryGuid = "Category/430214303"
                                ), ProductItem(
                                    id = "Product/438212525",
                                    name = "Pho Bo Tai (S)",
                                    description = "Nước dùng thơm ngon nấu từ 100% xương ống bò trong 8 giờ",
                                    price = 39000.0,
                                    name3 = "500g/Tô",
                                    categoryGuid = "Category/430214303"
                                ), ProductItem(
                                    id = "Product/438212526",
                                    name = "Pho Bo Tai (S)",
                                    description = "Nước dùng thơm ngon nấu từ 100% xương ống bò trong 8 giờ",
                                    price = 39000.0,
                                    name3 = "500g/Tô",
                                    categoryGuid = "Category/430214303"
                                ), ProductItem(
                                    id = "Product/438212527",
                                    name = "Pho Bo Tai (S)",
                                    description = "Nước dùng thơm ngon nấu từ 100% xương ống bò trong 8 giờ",
                                    price = 39000.0,
                                    name3 = "500g/Tô",
                                    categoryGuid = "Category/430214303"
                                ), ProductItem(
                                    id = "Product/438212528",
                                    name = "Pho Bo Tai (S)",
                                    description = "Nước dùng thơm ngon nấu từ 100% xương ống bò trong 8 giờ",
                                    price = 39000.0,
                                    name3 = "500g/Tô",
                                    categoryGuid = "Category/430214303"
                                ), ProductItem(
                                    id = "Product/438212529",
                                    name = "Pho Bo Tai (S)",
                                    description = "Nước dùng thơm ngon nấu từ 100% xương ống bò trong 8 giờ",
                                    price = 39000.0,
                                    name3 = "500g/Tô",
                                    categoryGuid = "Category/430214303"
                                ), ProductItem(
                                    id = "Product/438212530",
                                    name = "Pho Bo Tai (S)",
                                    description = "Nước dùng thơm ngon nấu từ 100% xương ống bò trong 8 giờ",
                                    price = 39000.0,
                                    name3 = "500g/Tô",
                                    categoryGuid = "Category/430214303"
                                )
                            )
                        )
                    )
                )
            )
        );
    }

    private fun initCategories(){
        categoryList.value = orderMenuResp?.getCategoryList()?.toMutableList();
    }

    fun getCategoryByPage(pagePosition: Int) : List<CategoryItem>{
        categoryList.value?.let { it1 ->
            if (it1.size != 0) {
                val start: Int = (pagePosition - 1) * maxItemViewCate;
                val end: Int =
                    if (it1.size > maxItemViewCate * pagePosition) maxItemViewCate * pagePosition else it1.size;
                return it1.toList().subList(start, end) as List<CategoryItem>;
            }

        };
        return mutableListOf();
    }

    fun getProductByCategory(categoryItem: CategoryItem) : List<ProductItem?>? {
        return orderMenuResp?.getProductWithCategoryGuid(categoryGuid = categoryItem.id);
    }

    fun getProductByPage(pagePosition: Int) : List<ProductItem> {
        val rs : MutableList<ProductItem> = mutableListOf();
        productListSl.value?.let {
            if(it.size != 0){
                val start: Int = (pagePosition - 1) * maxItemViewProduct;
                val end: Int =
                    if (it.size > maxItemViewProduct * pagePosition) maxItemViewProduct * pagePosition else it.size;
                rs.addAll(it.toList().subList(start, end) as List<ProductItem>);
            }
        }

        // Add Empty
        for (i in rs.size..15){
            rs.add(ProductItem().apply {
                uiType = ProductModeViewType.Empty;
            })
        }

        // Add Direction Button
        rs.addAll(listOf(
            ProductItem().apply {
                uiType = ProductModeViewType.PrevButton;
            },ProductItem().apply {
                uiType = ProductModeViewType.NextButton;
            }))

        return rs;
    }

    companion object {
        @JvmStatic
        val maxItemViewCate: Int = 15;

        @JvmStatic
        val maxItemViewProduct: Int = 16;
    }

}