package com.hanheldpos.ui.screens.product

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.data.api.pojo.product.*
import com.hanheldpos.data.api.pojo.product.ProductDetailResp
import com.hanheldpos.data.api.pojo.product.ProductOption
import com.hanheldpos.data.api.pojo.product.ProductOptionExtra
import com.hanheldpos.ui.base.viewmodel.BaseViewModel

class ProductDetailDataVM : BaseViewModel() {
    private var productDetailResp: ProductDetailResp? = null;
    val sizeOptionsList = MutableLiveData<MutableList<ProductOption?>>(mutableListOf());
    val cookOptionsList = MutableLiveData<MutableList<ProductOption?>>(mutableListOf());
    val sauceOptionsList = MutableLiveData<MutableList<ProductOption?>>(mutableListOf());
    val extraOptionsList = MutableLiveData<MutableList<ProductOptionExtra?>>(mutableListOf());

    fun initData() {
        initMenus();
        initOptions();
    }

    // Menu
    private fun initMenus() {
        productDetailResp = ProductDetailResp(
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
        sizeOptionsList.value = productDetailResp?.getSizeOptionsList()?.toMutableList();
        cookOptionsList.value = productDetailResp?.getCookOptionsList()?.toMutableList();
        sauceOptionsList.value = productDetailResp?.getSauceOptionsList()?.toMutableList();
        extraOptionsList.value = productDetailResp?.getExtraOptionsList()?.toMutableList();
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

}