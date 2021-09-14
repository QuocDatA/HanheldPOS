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