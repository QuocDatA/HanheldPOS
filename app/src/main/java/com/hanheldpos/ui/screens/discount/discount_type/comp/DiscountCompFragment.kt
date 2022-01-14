package com.hanheldpos.ui.screens.discount.discount_type.comp

import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.order.settings.Reason
import com.hanheldpos.databinding.FragmentDiscountCompBinding
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.discount.DiscountTypeFor
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.discount.discount_type.DiscountTypeFragment
import com.hanheldpos.ui.screens.discount.discount_type.comp.adapter.DiscountReasonAdapter


class DiscountCompFragment(
    private val comp: Reason?,
    private val listener: DiscountTypeFragment.DiscountTypeListener,
) : BaseFragment<FragmentDiscountCompBinding, DiscountCompVM>(), DiscountCompUV {

    private lateinit var adapter: DiscountReasonAdapter;

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


        viewModel.reasonChosen.observe(this, { reason ->
            binding.removeComp.apply {
                reason?.run { setTextColor(resources.getColor(R.color.color_0)); }
                setOnClickListener {
                    reason?.run {
                        listener.compRemoveAll();
                    }
                }
            }
        });

        adapter = DiscountReasonAdapter(
            comp,
            listener = object : BaseItemClickListener<Reason> {
                override fun onItemClick(adapterPosition: Int, item: Reason) {
                    // Dealing with item selected;
                    viewModel.reasonChosen.postValue(item)
                }
            })
        binding.reasonContainer.adapter = adapter;


    }

    override fun initData() {
        viewModel.reasonChosen.postValue(comp);

        val list = DataHelper.getCompList();
        if (list != null) adapter.submitList(list as MutableList<Reason>);
    }

    override fun initAction() {
        viewModel.reasonChosen.observe(this, {
            listener.validDiscount(it != comp);
        });
    }

    override fun onResume() {
        super.onResume()
        requireActivity().supportFragmentManager.setFragmentResultListener("saveDiscount",this) { _, bundle ->
            if (bundle.getSerializable("DiscountTypeFor") == DiscountTypeFor.COMP) {
                listener.compReasonChoose(viewModel.reasonChosen.value!!);
            }

        }

        listener.validDiscount(viewModel.reasonChosen.value != comp);
    }

}