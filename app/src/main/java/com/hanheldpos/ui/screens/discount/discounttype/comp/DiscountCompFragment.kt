package com.hanheldpos.ui.screens.discount.discounttype.comp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.order.settings.ListReasonsItem
import com.hanheldpos.databinding.FragmentDiscountCompBinding
import com.hanheldpos.model.DataHelper
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.discount.discounttype.comp.adapter.DiscountReasonAdapter


class DiscountCompFragment : BaseFragment<FragmentDiscountCompBinding,DiscountCompVM>(), DiscountCompUV {

    private lateinit var adapter : DiscountReasonAdapter;

    override fun layoutRes(): Int = R.layout.fragment_discount_comp

    override fun viewModelClass(): Class<DiscountCompVM> {
        return DiscountCompVM::class.java;
    }

    override fun initViewModel(viewModel: DiscountCompVM) {
        viewModel.run {
            init(this@DiscountCompFragment);
            binding.viewModel = this;
        }
    }

    override fun initView() {
        adapter = DiscountReasonAdapter(listener = object : BaseItemClickListener<ListReasonsItem>{
            override fun onItemClick(adapterPosition: Int, item: ListReasonsItem) {
                // Dealing with item selected;
                viewModel.reasonChosen.postValue(item);
            }
        })
        binding.reasonContainer.adapter = adapter;
    }

    override fun initData() {
        val list = DataHelper.getCompList();
        adapter.submitList(list);
    }

    override fun initAction() {

    }

}