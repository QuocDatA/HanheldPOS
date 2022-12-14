package com.hanheldpos.ui.screens.cart.customer.add_customer.adapter

import com.hanheldpos.data.api.pojo.customer.CustomerResp
import kotlin.random.Random

class CustomerAdapterHelper(private val listener: SearchCallBack) {
    private val listCustomer: MutableList<CustomerResp> = mutableListOf()
    private var keyRequest: Int = 0
    private var isLoadingMore = false
    private var currentPage = 1
    private var currentKeyword = ""
    private fun init() {
        listCustomer.clear()
        currentPage = 1
    }

    fun nextPage() {
        if (isLoadingMore || currentKeyword.trim().isBlank()) return
        keyRequest = Random.nextInt(100000, 999999)
        isLoadingMore = true
        currentPage = currentPage.plus(1)
        listener.onSearch(currentKeyword, currentPage, keyRequest)
        distinctList()
        listener.onLoadingNextPage(listCustomer)
    }

    fun search(keyword: String) {
        init()
        keyRequest = Random.nextInt(100000, 999999)
        currentKeyword = keyword
        listener.onSearch(keyword, keyRequest = keyRequest)
    }


    fun loaded(customers: List<CustomerResp>, isSuccess: Boolean, keyRequest: Int) {
        if (keyRequest != this.keyRequest) return
        if (isSuccess) {
            listCustomer.apply { addAll(customers) }
        } else {
            currentPage = currentPage.minus(1)
        }
        distinctList()
        listener.onLoadedNextPage(
            if (listCustomer.isEmpty()) 0 else listCustomer.size - 1,
            listCustomer
        )
        isLoadingMore = false
    }

    interface SearchCallBack {
        fun onSearch(keyword: String, pageNo: Int? = 1, keyRequest: Int)
        fun onLoadingNextPage(customers: List<CustomerResp>)
        fun onLoadedNextPage(startIndex: Int, customers: List<CustomerResp>)
    }

    private fun distinctList() {
        val list = listCustomer.distinctBy { it._id }
        listCustomer.clear()
        listCustomer.addAll(list)
    }
}