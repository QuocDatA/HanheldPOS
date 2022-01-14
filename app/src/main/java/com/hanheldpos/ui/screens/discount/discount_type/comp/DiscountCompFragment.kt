package com.hanheldpos.ui.screens.discount.discount_type.comp

import android.annotation.SuppressLint
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

    @SuppressLint("NotifyDataSetChanged")
    override fun initView() {




        adapter = DiscountReasonAdapter(
            comp,
            listener = object : BaseItemClickListener<Reason> {
                override fun onItemClick(adapterPosition: Int, item: Reason) {
                    // Dealing with item selected;
                    viewModel.reasonChosen.postValue(item)
                }
            })
        binding.reasonContainer.adapter = adapter;

        viewModel.reasonChosen.observe(this, { reason ->
            binding.removeComp.apply {
                if(reason != null) {
                    setTextColor(resources.getColor(R.color.color_0));
                }
                else {
                    setTextColor(resources.getColor(R.color.color_6));
                }

                setOnClickListener {
                    reason?.run {
                        adapter.selectedItem.value = -1;
                        adapter.notifyDataSetChanged()
                        viewModel.reasonChosen.postValue(null)
                        listener.compRemoveAll();
                    }
                }
            }
        });

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
                viewModel.reasonChosen.value?.run {
                    listener.compReasonChoose(viewModel.reasonChosen.value!!);
                }
            }

        }

        listener.validDiscount(viewModel.reasonChosen.value != comp);
    }

}