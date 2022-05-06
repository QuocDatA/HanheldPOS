package com.hanheldpos.ui.screens.buy_x_get_y

import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.databinding.FragmentBuyXGetYBinding
import com.hanheldpos.model.buy_x_get_y.ItemBuyXGetYGroup
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.buy_x_get_y.adapter.BuyXGetYGroupAdapter

class BuyXGetYFragment(private val discount: DiscountResp) :
    BaseFragment<FragmentBuyXGetYBinding, BuyXGetYVM>(), BuyXGetYUV {
    override fun layoutRes(): Int = R.layout.fragment_buy_x_get_y

    private lateinit var buyXGetYGroupAdapter: BuyXGetYGroupAdapter

    override fun viewModelClass(): Class<BuyXGetYVM> {
        return BuyXGetYVM::class.java
    }

    override fun initViewModel(viewModel: BuyXGetYVM) {
        viewModel.run {
            init(this@BuyXGetYFragment)
            binding.viewModel = this
            binding.discount = discount
        }
    }

    override fun initView() {
        buyXGetYGroupAdapter = BuyXGetYGroupAdapter()
        binding.buyXGetYGroupAdapter.adapter = buyXGetYGroupAdapter
    }

    override fun initData() {
        val buyXGetY = viewModel.initDefaultList(discount)
        buyXGetY.groupList?.map { groupBuyXGetY ->
            ItemBuyXGetYGroup(
                groupBuyXGetY
            )
        }.let {
            buyXGetYGroupAdapter.submitList(it?.toMutableList())
        }

    }

    override fun initAction() {
    }

}