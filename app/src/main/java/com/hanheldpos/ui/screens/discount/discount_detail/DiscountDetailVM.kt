package com.hanheldpos.ui.screens.discount.discount_detail

import com.hanheldpos.data.api.pojo.discount.CouponResp
import com.hanheldpos.data.api.pojo.discount.ListScheduleItem
import com.hanheldpos.data.api.pojo.discount.RequirementProduct
import com.hanheldpos.data.api.pojo.discount.RewardProduct
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class DiscountDetailVM : BaseUiViewModel<DiscountDetailUV>() {

    var discountDetail: CouponResp? = null

    fun backPress() {
        uiCallback?.getBack()
    }

    fun showListApplies(list: List<RequirementProduct>?) {
        list ?: return
        uiCallback?.showReqProduct("Requirements", list.map { it.Name1 })
    }

    fun showListRewards(list: List<RewardProduct>?) {
        list ?: return
        uiCallback?.showReqProduct("Rewards", list.map { it.Name1 })
    }

    fun showListSchedules(list : List<ListScheduleItem>?) {
        if (list?.isEmpty() == true) return
        uiCallback?.showReqProduct("Schedule", list!!.map { Pair(it.Date,it.listTimeString) })
    }
}