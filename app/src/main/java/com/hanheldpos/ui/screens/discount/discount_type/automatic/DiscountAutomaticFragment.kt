package com.hanheldpos.ui.screens.discount.discount_type.automatic

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.databinding.FragmentDiscountAutomaticBinding
import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.model.cart.CartModel
import com.hanheldpos.model.discount.DiscountApplyToType
import com.hanheldpos.model.discount.DiscountTypeFor
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.discount.discount_detail.DiscountDetailFragment
import com.hanheldpos.ui.screens.discount.discount_type.discount_code.adapter.DiscountCodeAdapter


class DiscountAutomaticFragment(private val applyToType: DiscountApplyToType ,private val cart: CartModel?, private val product: BaseProductInCart?) :
    BaseFragment<FragmentDiscountAutomaticBinding, DiscountAutomaticVM>(), DiscountAutomaticUV {

    override fun layoutRes(): Int {
        return R.layout.fragment_discount_automatic;
    }

    private lateinit var discountCodeAdapter: DiscountCodeAdapter;

    override fun viewModelClass(): Class<DiscountAutomaticVM> {
        return DiscountAutomaticVM::class.java;
    }

    override fun initViewModel(viewModel: DiscountAutomaticVM) {
        viewModel.run {
            init(this@DiscountAutomaticFragment);
        }
        binding.viewModel = viewModel;
    }

    override fun initView() {

        discountCodeAdapter =
            DiscountCodeAdapter(listener = object : DiscountCodeAdapter.DiscountItemCallBack {
                override fun onViewDetailClick(item: DiscountResp) {
                    navigator.goTo(DiscountDetailFragment(item))
                }

                override fun onItemClick() {

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
        viewModel.loadDiscountAutomatic(product, cart)
    }

    override fun initAction() {

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun loadDataDiscountCode(list: List<DiscountResp>) {
        discountCodeAdapter.submitList(list);
        discountCodeAdapter.notifyDataSetChanged();
    }


    override fun onResume() {
        super.onResume()
        requireActivity().supportFragmentManager.setFragmentResultListener("saveDiscount",this) { _, bundle ->
            if (bundle.getSerializable("DiscountTypeFor") == DiscountTypeFor.AUTOMATIC) {

            }
        }
    }

}