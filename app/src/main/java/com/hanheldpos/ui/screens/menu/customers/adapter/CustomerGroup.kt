package com.hanheldpos.ui.screens.menu.customers.adapter

import com.hanheldpos.data.api.pojo.customer.CustomerResp

data class CustomerGroup(
    val groupTitle : String,
    val customers : List<CustomerResp>
)
