package com.hanheldpos.ui.screens.menu.customers

import android.media.MediaDrm
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.data.api.pojo.customer.CustomerSearchResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.customer.CustomerRepo
import com.hanheldpos.model.DataHelper
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.screens.menu.customers.adapter.CustomerGroup

class CustomerMenuVM : BaseUiViewModel<CustomerMenuUV>() {
    private val customerRepo = CustomerRepo()
    val customerSearchString = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>(false)
    val customerGroups: MutableList<CustomerRepo> = mutableListOf()
    fun onFragmentBackPressed() {
        uiCallback?.onFragmentBackPressed()
    }

    fun fetchDataCustomer(keyword: String?, pageNo: Int? = 1, keyRequest: Int) {
        customerRepo.getCustomersFromSearch(
            DataHelper.userGuid(),
            keyword,
            pageNo,
            object : BaseRepoCallback<BaseResponse<List<CustomerSearchResp>>?> {
                override fun apiRequesting(showLoading: Boolean) {
                    isLoading.postValue(showLoading)
                }

                override fun apiResponse(data: BaseResponse<List<CustomerSearchResp>>?) {
                    if (data == null || data.DidError) {
                        uiCallback?.onLoadedCustomers(mutableListOf(), false, keyRequest)
                    } else {
                        data.Model?.firstOrNull()?.List?.let {
                            uiCallback?.onLoadedCustomers(
                                it,
                                true,
                                keyRequest
                            )
                        }
                    }
                }

                override fun showMessage(message: String?) {
                    uiCallback?.onLoadedCustomers(mutableListOf(), false, keyRequest)
                }
            })
    }

    fun groupingCustomers(list: List<CustomerResp>?): List<CustomerGroup> {
        list ?: return emptyList()
        return list.groupBy { it.NameAcronymn }.map {
            CustomerGroup(it.key, it.value)
        }
    }
}