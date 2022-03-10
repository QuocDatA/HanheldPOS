package com.hanheldpos.ui.screens.cart.customer.add_customer.adapter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.data.api.pojo.customer.CustomerResp

class CustomerAdapterHelper(private val listener: SearchCallBack) {
    private val listCustomer: MutableList<CustomerResp?> = mutableListOf()
    private var isLoadingMore = false
    private var currentPage = 1
    private var currentKeyword = ""
    private fun init() {
        listCustomer.clear()
        currentPage = 1
    }

    fun nextPage() {
        if (isLoadingMore || currentKeyword.trim().isBlank()) return
        isLoadingMore = true
        currentPage = currentPage.plus(1)
        listener.onSearch(currentKeyword, currentPage)
        listCustomer.add(null)
        listener.onLoadingNextPage(listCustomer)
    }

    fun search(keyword: String) {
        init()
        currentKeyword = keyword
        listener.onSearch(keyword)
    }


    fun loaded(customers: List<CustomerResp>, isSuccess: Boolean) {
        listCustomer.removeAll { it == null }
        if (isSuccess)
            listener.onLoadedNextPage(if (listCustomer.isEmpty()) 0 else listCustomer.size-1 , listCustomer.apply { addAll(customers) })
        else {
            currentPage = currentPage.minus(1)
            listener.onLoadedNextPage(if (listCustomer.isEmpty()) 0 else listCustomer.size-1, listCustomer)
        }
        isLoadingMore = false
    }

    interface SearchCallBack {
        fun onSearch(keyword: String, pageNo: Int? = 1)
        fun onLoadingNextPage(customers: List<CustomerResp?>)
        fun onLoadedNextPage(startIndex: Int, customers: List<CustomerResp?>)
    }
}