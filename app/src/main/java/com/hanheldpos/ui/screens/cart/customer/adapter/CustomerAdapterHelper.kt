package com.hanheldpos.ui.screens.cart.customer.adapter

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.data.api.pojo.customer.CustomerResp

class CustomerAdapterHelper(private val listener : SearchCallBack ) {
    private val listCustomer : MutableList<CustomerResp?> = mutableListOf();
    private var isLoadingMore = false;
    private var currentPage = 1;
    private var currentKeyword = "";
    private fun init() {
        listCustomer.clear();
        currentPage = 1;
    }

    fun nextPage() {
        if (isLoadingMore || currentKeyword.trim().isBlank()) return;
        isLoadingMore = true;
        currentPage = currentPage.plus(1);
        listener.onSearch(currentKeyword,currentPage);
        listCustomer.add(null);
        listener.onLoadingNextPage(listCustomer);
    }

    fun search(keyword : String) {
        init();
        currentKeyword = keyword;
        listener.onSearch(keyword);
    }

    fun loadedSuccess(customers : List<CustomerResp>){
        if (listCustomer.isNotEmpty())
        listCustomer.removeAt(listCustomer.size-1);
        listener.onLoadedNextPage(listCustomer.size, listCustomer.apply { addAll(customers) })
        isLoadingMore = false;
    }

    fun loadedFail(){
        currentPage = currentPage.minus(1);
        isLoadingMore = false;
    }

    interface SearchCallBack {
        fun onSearch(keyword : String,pageNo : Int? = 1);
        fun onLoadingNextPage(customers : List<CustomerResp?>);
        fun onLoadedNextPage(startIndex : Int,customers : List<CustomerResp?>);
    }
}