package com.hanheldpos.ui.screens.discount.discount_type.discount_code

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.databinding.FragmentDiscountCodeBinding
import com.hanheldpos.model.discount.DiscApplyTo
import com.hanheldpos.model.discount.DiscountTypeFor
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.discount.DiscountFragment
import com.hanheldpos.ui.screens.discount.discount_detail.DiscountDetailFragment
import com.hanheldpos.ui.screens.discount.discount_type.adapter.DiscountServerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiscountCodeFragment(
    private val applyToType: DiscApplyTo,
    private val listener: DiscountFragment.DiscountTypeListener
) : BaseFragment<FragmentDiscountCodeBinding, DiscountCodeVM>(), DiscountCodeUV {
    override fun layoutRes(): Int = R.layout.fragment_discount_code

    private lateinit var discountCodeAdapter: DiscountServerAdapter;

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
            DiscountServerAdapter(listener = object : DiscountServerAdapter.DiscountItemCallBack {
                override fun onViewDetailClick(item: DiscountResp) {
                    navigator.goTo(DiscountDetailFragment(item, onApplyDiscountAuto = { discount ->

                    }))
                }

                override fun onItemClick(item : DiscountResp) {
                }

            });
        binding.listDiscountCode.apply {
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    LinearLayoutManager.VERTICAL
                ).apply {
                    setDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.divider_vertical
                        )!!
                    )
                }
            )
        };
        binding.listDiscountCode.adapter = discountCodeAdapter;

    }

    override fun initData() {
        if (applyToType == DiscApplyTo.ORDER)
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.initData();
            }

    }

    override fun initAction() {
        binding.discountCodeInput.doAfterTextChanged {
            listener.validDiscount(it.toString().isNotEmpty())
            viewModel.searchDiscountCode(it.toString())
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun loadDataDiscountCode(list: List<DiscountResp>) {
        CoroutineScope(Dispatchers.Main).launch {
            discountCodeAdapter.submitList(list);
            discountCodeAdapter.notifyDataSetChanged();
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().supportFragmentManager.setFragmentResultListener(
            "saveDiscount",
            this
        ) { _, bundle ->
            if (bundle.getSerializable("DiscountTypeFor") == DiscountTypeFor.DISCOUNT_CODE) {

            }
        }
        listener.validDiscount(binding.discountCodeInput.text.toString().isNotEmpty())
    }
}