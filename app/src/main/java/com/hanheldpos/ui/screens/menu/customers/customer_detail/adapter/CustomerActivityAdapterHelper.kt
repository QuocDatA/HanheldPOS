package com.hanheldpos.ui.screens.menu.customers.customer_detail.adapter

import com.hanheldpos.model.order.OrderSummaryPrimary
import kotlin.random.Random

class CustomerActivityAdapterHelper(private val listener: ActivityCallBack) {
    private val activities: MutableList<OrderSummaryPrimary> = mutableListOf()
    private var keyRequest: Int = 0
    private var isLoadingMore = false
    private var currentPage = 1
    private var customerId : String? = null;
    private fun init() {
        activities.clear()
        currentPage = 1
    }

    fun nextPage() {
        if (isLoadingMore) return
        keyRequest = Random.nextInt(100000, 999999)
        isLoadingMore = true
        currentPage = currentPage.plus(1)
        listener.onGetActivities(customerId,currentPage, keyRequest)
        distinctList()
        listener.onLoadingNextPage(activities)
    }

    fun fetch(customerId: String?) {
        init()
        keyRequest = Random.nextInt(100000, 999999)
        this.customerId = customerId
        listener.onGetActivities(customerId, keyRequest = keyRequest)
    }


    fun loaded(activities: List<OrderSummaryPrimary>, isSuccess: Boolean, keyRequest: Int) {
        if (keyRequest != this.keyRequest) return
        if (isSuccess) {
            this.activities.apply { addAll(activities) }
        } else {
            currentPage = currentPage.minus(1)
        }
        distinctList()
        listener.onLoadedNextPage(
            if (this.activities.isEmpty()) 0 else this.activities.size - 1,
            this.activities
        )
        isLoadingMore = false
    }

    interface ActivityCallBack {
        fun onGetActivities(customerId: String?, pageNo: Int? = 1, keyRequest: Int)
        fun onLoadingNextPage(activities: List<OrderSummaryPrimary>)
        fun onLoadedNextPage(startIndex: Int, activities: List<OrderSummaryPrimary>)
    }

    private fun distinctList() {
        val list = activities.distinctBy { it._id }
        activities.clear()
        activities.addAll(list)
    }
}