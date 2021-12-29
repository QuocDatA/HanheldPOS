package com.hanheldpos.ui.screens.discount.discount_type.discount_code

import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.discount.DiscountItem
import com.hanheldpos.databinding.FragmentDiscountCodeBinding
import com.hanheldpos.model.discount.DiscountApplyToType
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.discount.discount_type.discount_code.adapter.DiscountCodeAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class DiscountCodeFragment(private val applyToType: DiscountApplyToType) :
    BaseFragment<FragmentDiscountCodeBinding, DiscountCodeVM>(), DiscountCodeUV {
    override fun layoutRes(): Int = R.layout.fragment_discount_code

    private lateinit var discountCodeAdapter: DiscountCodeAdapter;

    override fun viewModelClass(): Class<DiscountCodeVM> {
        return DiscountCodeVM::class.java;
    }

    override fun initViewModel(viewModel: DiscountCodeVM) {
        viewModel.run {
            init(this@DiscountCodeFragment);
            binding.viewModel = this;
        }

    }

    override fun initView() {
        discountCodeAdapter =
            DiscountCodeAdapter(listener = object : BaseItemClickListener<DiscountItem> {
                override fun onItemClick(adapterPosition: Int, item: DiscountItem) {

                }
            });
        binding.listDiscountCode.adapter = discountCodeAdapter;
    }

    override fun initData() {
        if (applyToType == DiscountApplyToType.ORDER_DISCOUNT_APPLY_TO)
            GlobalScope.launch(Dispatchers.IO) {
                viewModel.initData();
            }

    }

    override fun initAction() {

    }

    override fun loadDataDiscountCode(list: List<DiscountItem>) {
        GlobalScope.launch (Dispatchers.Main) {
            discountCodeAdapter.submitList(list);
            discountCodeAdapter.notifyDataSetChanged();
        }

    }

}