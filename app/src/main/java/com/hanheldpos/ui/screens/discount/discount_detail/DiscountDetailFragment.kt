package com.hanheldpos.ui.screens.discount.discount_detail

import android.text.Html
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.databinding.FragmentDiscountDetailBinding
import com.hanheldpos.model.DataHelper
import com.hanheldpos.ui.base.fragment.BaseFragment

class DiscountDetailFragment(
    private val discountResp: DiscountResp,
    private val onApplyDiscountAuto: (discount: DiscountResp) -> Unit
) : BaseFragment<FragmentDiscountDetailBinding, DiscountDetailVM>(), DiscountDetailUV {
    override fun layoutRes(): Int = R.layout.fragment_discount_detail

    override fun viewModelClass(): Class<DiscountDetailVM> {
        return DiscountDetailVM::class.java
    }

    override fun initViewModel(viewModel: DiscountDetailVM) {
        viewModel.run {
            init(this@DiscountDetailFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {
        binding.discountName.text = discountResp.DiscountName
    }

    override fun initData() {
        val discountDetailResp =
            DataHelper.discountDetailsLocalStorage?.find { discountDetailResp ->
                discountDetailResp._id == discountResp._id
            }
        discountDetailResp?.let {
            viewModel.discountDetail = it
            binding.discountDetail = it
            binding.discountDescription.text = removeHtml(it.Description)
            binding.discountTermCondition.text = removeHtml(it.TermsCondition)
        }
    }

    private fun removeHtml(html: String): String {
        var result = html
        result = result.replace("<(.*?)>".toRegex(), " ")
        result = result.replace("<(.*?)\n".toRegex(), " ")
        result = result.replaceFirst("(.*?)>".toRegex(), " ")
        result = result.replace("&nbsp;".toRegex(), " ")
        result = result.replace("&amp;".toRegex(), " ")
        return result
    }

    override fun initAction() {
        binding.btnApplyDiscount.setOnClickListener {
            onApplyDiscountAuto(discountResp)
        }

        binding.btnShowListAppliesItem.setOnClickListener {
            viewModel.showListApplies(viewModel.discountDetail?.RequirementProductList)
        }

        binding.btnShowListRewardItem.setOnClickListener {
            viewModel.showListRewards(viewModel.discountDetail?.RewardProductList)
        }

        binding.schedule.setOnClickListener {
            viewModel.showListSchedules(viewModel.discountDetail?.ScheduleList)
        }
    }

    override fun getBack() {
        onFragmentBackPressed()
    }

    override fun showReqProduct(title: String, list: List<Any>) {
        navigator.goTo(DiscReqProductFragment(title,list))
    }
}