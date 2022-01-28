package com.hanheldpos.ui.screens.cart.customer.add_customer

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.data.api.pojo.customer.CustomerSearchResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.customer.CustomerRepo
import com.hanheldpos.model.DataHelper
import com.hanheldpos.ui.base.viewmodel.BaseRepoViewModel

class AddCustomerVM : BaseRepoViewModel<CustomerRepo, AddCustomerUV>() {

    val isLoading = MutableLiveData<Boolean>(false);

    override fun createRepo(): CustomerRepo {
        return CustomerRepo();
    }

    fun searchCustomer(keyword: String?, pageNo: Int? = 1) {
        val userGuid = DataHelper.getUserGuidByDeviceCode()
        repo?.getCustomersFromSearch(
            userGuid = userGuid,
            keyword = keyword,
            pageNo = pageNo,
            object : BaseRepoCallback<BaseResponse<List<CustomerSearchResp>>?> {
                override fun apiRequesting(showLoading: Boolean) {
                    if (pageNo == 1)
                        isLoading.postValue(showLoading)
                }

                override fun apiResponse(data: BaseResponse<List<CustomerSearchResp>>?) {
                    if (data == null || data.DidError) {
                        uiCallback?.loadCustomer(mutableListOf(),false);
                    } else {
                        data.Model?.firstOrNull()?.List?.let { uiCallback?.loadCustomer(it,true) }
                    }
                }

                override fun showMessage(message: String?) {
                    uiCallback?.loadCustomer(mutableListOf(),false);
                }
            })
    }

    fun backPress() {
        uiCallback?.getBack();
    }

    fun onAddNewCustomer() {
        uiCallback?.onAddNewCustomer()
    }
}