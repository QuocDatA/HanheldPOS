package com.hanheldpos.ui.screens.product

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.data.api.pojo.product.*
import com.hanheldpos.data.api.pojo.product.getSizeOptionsList
import com.hanheldpos.model.product.ProductCompleteModel
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class ProductDetailVM : BaseUiViewModel<ProductDetailUV>() {


    val isToolbarExpand = MutableLiveData<Boolean>(true)
    // Value
    var productDetailResp = MutableLiveData<ProductDetailResp>();
    var productCompleteLD = MutableLiveData<ProductCompleteModel>();
    var numberQuantityLD = MutableLiveData<Int>(1);
    var quantityCanChoose = MutableLiveData<Int>(1);

    val sizeOptionsList = MutableLiveData<MutableList<ProductOption?>>(mutableListOf());
    val cookOptionsList = MutableLiveData<MutableList<ProductOption?>>(mutableListOf());
    val sauceOptionsList = MutableLiveData<MutableList<ProductOption?>>(mutableListOf());
    val extraOptionsList = MutableLiveData<MutableList<ProductOptionExtra?>>(mutableListOf());


    //Placeholder value
    var url = "https://i.imgur.com/LhdRp3a.jpg"


    fun initLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
    }

    fun initData() {
        initMenus();
        initOptions();
    }


    // Menu
    private fun initMenus() {
        productDetailResp.value = ProductDetailResp(
            model = mutableListOf(
                ModelItem(
                    listSizeOptions = mutableListOf(
                        ProductOption(
                            name = "S",
                            checked = true
                        ),
                        ProductOption(
                            name = "M",
                            checked = false
                        ),
                        ProductOption(
                            name = "L",
                            checked = false
                        ),
                        ProductOption(
                            name = "L",
                            checked = false
                        ),
                        ProductOption(
                            name = "L",
                            checked = false
                        ),
                    ),
                    listCookOptions = mutableListOf(
                        ProductOption(
                            name = "Rare",
                            checked = true
                        ),
                        ProductOption(
                            name = "Medium",
                            checked = false
                        )
                    ),
                    listSauceOptions = mutableListOf(
                        ProductOption(
                            name = "Mushroom sauce",
                            checked = true
                        ),
                        ProductOption(
                            name = "Pepper sauce",
                            checked = false
                        )
                    ),
                    listExtraOptions = mutableListOf(
                        ProductOptionExtra(
                            name = "Thit bo them",
                            price = "20.000",
                            quantity = 1
                        ),
                        ProductOptionExtra(
                            name = "Thit bo them",
                            price = "20.000",
                            quantity = 1
                        ),
                    )
                )
            )
        )
    }

    private fun initOptions() {
        sizeOptionsList.value = productDetailResp.value?.getSizeOptionsList()?.toMutableList();
        cookOptionsList.value = productDetailResp.value?.getCookOptionsList()?.toMutableList();
        sauceOptionsList.value = productDetailResp.value?.getSauceOptionsList()?.toMutableList();
        extraOptionsList.value = productDetailResp.value?.getExtraOptionsList()?.toMutableList();
    }

    fun getOptionList(str: String): List<ProductOption> {
        when (str) {
            "Size" -> {
                sizeOptionsList.value?.let { it1 ->
                    if (it1.size != 0) {
                        return it1.toList() as List<ProductOption>;
                    }

                };
            }
            "Cook" -> {
                cookOptionsList.value?.let { it1 ->
                    if (it1.size != 0) {
                        return it1.toList() as List<ProductOption>;
                    }

                };
            }
            "Sauce" -> {
                sauceOptionsList.value?.let { it1 ->
                    if (it1.size != 0) {
                        return it1.toList() as List<ProductOption>;
                    }

                };
            }
        }
        return mutableListOf();
    }

    fun getExtraOptionList(): List<ProductOptionExtra> {
        extraOptionsList.value?.let { it1 ->
            if (it1.size != 0) {
                return it1.toList() as List<ProductOptionExtra>;
            }
        };
        return mutableListOf();
    }

    fun goBack() {
        uiCallback?.goBack();
    }

    fun addProductToCart(){
        uiCallback?.onAddCart();
    }
}