package com.hanheldpos.ui.screens.home.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hanheldpos.data.api.pojo.order.*
import com.hanheldpos.data.api.pojo.order.ModelItem
import com.hanheldpos.data.api.pojo.order.getCategoryList
import com.hanheldpos.data.api.pojo.order.getProductWithCategoryGuid
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.order.OrderRepo
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.home.order.product.ProductModeViewType
import com.hanheldpos.model.product.ProductCompleteModel
import com.hanheldpos.ui.base.viewmodel.BaseRepoViewModel
import com.hanheldpos.ui.base.viewmodel.BaseViewModel

class OrderDataVM : BaseRepoViewModel<OrderRepo, OrderUV>() {
    private var orderMenuResp: OrderMenuResp? = null;
    val categoryList = MutableLiveData<MutableList<CategoryItem?>>(mutableListOf());
    val categorySelected = MutableLiveData<CategoryItem>();
    val productInCartLD = MutableLiveData<MutableList<ProductCompleteModel>?>(mutableListOf())
    val productQuantityInCartLD = Transformations.map(productInCartLD) {
        return@map productInCartLD.value?.sumOf {
            it.quantity
        } ?: 0
    }
    val productTotalPriceLD = Transformations.map(productInCartLD) {
        return@map productInCartLD.value?.sumOf {
            it.getPriceTotal()
        } ?: 0.0
    }

    fun initData() {


        initMenus();
        initCategories();

        // Init First Page
        categorySelected.value = categoryList.value?.first();
    }

    // Menu
    private fun initMenus() {

        /*val categorys = mutableListOf(
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
                    categoryGuid = it.id,
                    variants = "[{\"Option\":[{\"OptionName\":\"Choose Your Size\",\"OptionValue\":\"S,M,L\"},{\"OptionName\":\"Choose Your Color\",\"OptionValue\":\"Red,Blue,Yellow\"}]," +
                            "\"Group\":[" +
                            "{\"GroupId\":1,\"GroupName\":\"S•Red\",\"Price\":10000,\"ComparePrice\":0,\"CostPerItem\":0,\"Quantity\":0,\"Sku\":\"1\",\"Barcode\":\"\",\"Inventory\":\"0\",\"OrderNo\":0,\"Visible\":1}," +
                            "{\"GroupId\":1,\"GroupName\":\"S•Blue\",\"Price\":10000,\"ComparePrice\":0,\"CostPerItem\":0,\"Quantity\":0,\"Sku\":\"1\",\"Barcode\":\"\",\"Inventory\":\"0\",\"OrderNo\":0,\"Visible\":1}," +
                            "{\"GroupId\":1,\"GroupName\":\"S•Yellow\",\"Price\":10000,\"ComparePrice\":0,\"CostPerItem\":0,\"Quantity\":0,\"Sku\":\"1\",\"Barcode\":\"\",\"Inventory\":\"0\",\"OrderNo\":0,\"Visible\":1}," +
                            "{\"GroupId\":1,\"GroupName\":\"M•Red\",\"Price\":10000,\"ComparePrice\":0,\"CostPerItem\":0,\"Quantity\":0,\"Sku\":\"1\",\"Barcode\":\"\",\"Inventory\":\"0\",\"OrderNo\":0,\"Visible\":1}," +
                            "{\"GroupId\":1,\"GroupName\":\"M•Blue\",\"Price\":10000,\"ComparePrice\":0,\"CostPerItem\":0,\"Quantity\":0,\"Sku\":\"1\",\"Barcode\":\"\",\"Inventory\":\"0\",\"OrderNo\":0,\"Visible\":1}," +
                            "{\"GroupId\":1,\"GroupName\":\"M•Yellow\",\"Price\":10000,\"ComparePrice\":0,\"CostPerItem\":0,\"Quantity\":0,\"Sku\":\"1\",\"Barcode\":\"\",\"Inventory\":\"0\",\"OrderNo\":0,\"Visible\":1}," +
                            "{\"GroupId\":1,\"GroupName\":\"L•Red\",\"Price\":10000,\"ComparePrice\":0,\"CostPerItem\":0,\"Quantity\":0,\"Sku\":\"1\",\"Barcode\":\"\",\"Inventory\":\"0\",\"OrderNo\":0,\"Visible\":1}," +
                            "{\"GroupId\":1,\"GroupName\":\"L•Blue\",\"Price\":10000,\"ComparePrice\":0,\"CostPerItem\":0,\"Quantity\":0,\"Sku\":\"1\",\"Barcode\":\"\",\"Inventory\":\"0\",\"OrderNo\":0,\"Visible\":1}," +
                            "{\"GroupId\":1,\"GroupName\":\"L•Yellow\",\"Price\":10000,\"ComparePrice\":0,\"CostPerItem\":0,\"Quantity\":0,\"Sku\":\"1\",\"Barcode\":\"\",\"Inventory\":\"0\",\"OrderNo\":0,\"Visible\":1}," +
                            "]}]",
                    modifier = "[{\"ModifierGuid\":\"Modifier/438214200\",\"ModifierName\":\"Att Pho (không giá)\"},{\"ModifierGuid\":\"Modifier/438214201\",\"ModifierName\":\"Att Pho (có giá)\"}]"
                )
            })
        }

        var modifierItem = mutableListOf<ModifierItemItem>(
            ModifierItemItem(
                id = "ModifierItem/438214701",
                modifierGuid = "Modifier/438214200",
                modifier = "Khong hanh",
                price = 0.0,
                modifierItemId = 500
            ),
            ModifierItemItem(
                id = "ModifierItem/438214702",
                modifierGuid = "Modifier/438214200",
                modifier = "Khong gia",
                price = 0.0,
                modifierItemId = 500
            ),
            ModifierItemItem(
                id = "ModifierItem/438214703",
                modifierGuid = "Modifier/438214200",
                modifier = "It nuoc beo",
                price = 0.0,
                modifierItemId = 500
            ),
            ModifierItemItem(
                id = "ModifierItem/438214704",
                modifierGuid = "Modifier/438214200",
                modifier = "Nhieu nuoc beo",
                price = 0.0,
                modifierItemId = 500
            ),
            ModifierItemItem(
                id = "ModifierItem/438214705",
                modifierGuid = "Modifier/438214200",
                modifier = "Dau hanh trung",
                price = 0.0,
                modifierItemId = 500
            ),
            ModifierItemItem(
                id = "ModifierItem/438214706",
                modifierGuid = "Modifier/438214200",
                modifier = "Gia trung",
                price = 0.0,
                modifierItemId = 500
            ),
            ModifierItemItem(
                id = "ModifierItem/438214707",
                modifierGuid = "Modifier/438214200",
                modifier = "Khong tieu",
                price = 0.0,
                modifierItemId = 500
            ),
            ModifierItemItem(
                id = "ModifierItem/438214708",
                modifierGuid = "Modifier/438214200",
                modifier = "It banh pho",
                price = 0.0,
                modifierItemId = 500
            ),

            ModifierItemItem(
                id = "ModifierItem/438214709",
                modifierGuid = "Modifier/438214201",
                modifier = "Banh them",
                price = 5000.0,
                modifierItemId = 500
            ),
            ModifierItemItem(
                id = "ModifierItem/438214710",
                modifierGuid = "Modifier/438214201",
                modifier = "Chen nuoc leo bo",
                price = 10000.0,
                modifierItemId = 500
            ),
            ModifierItemItem(
                id = "ModifierItem/438214711",
                modifierGuid = "Modifier/438214201",
                modifier = "Trung them",
                price = 10000.0,
                modifierItemId = 500
            ),
        )*/
        // Fake data
        orderMenuResp = DataHelper.orderMenuResp;

    }

    fun getOrderMenu(): OrderMenuResp? {
        return this.orderMenuResp;
    }

    // Category
    private fun initCategories() {
        categoryList.value = orderMenuResp?.getCategoryList()?.toMutableList();
    }

    fun getProductByCategory(categoryItem: CategoryItem): List<ProductItem?>? {
        return orderMenuResp?.getProductWithCategoryGuid(categoryGuid = categoryItem.id);
    }

    // Cart
    fun addProductCompleteToCart(item: ProductCompleteModel) {
        productInCartLD.value?.add(item);
        productInCartLD.notifyValueChange();
    }

    fun deleteAllProductCart() {
        productInCartLD.value?.clear();
        productInCartLD.notifyValueChange();
    }

    override fun createRepo(): OrderRepo {
        return OrderRepo();
    }

}