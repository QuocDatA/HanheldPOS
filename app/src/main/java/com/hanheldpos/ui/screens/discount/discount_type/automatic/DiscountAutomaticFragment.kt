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
import com.hanheldpos.model.discount.DiscApplyTo
import com.hanheldpos.model.discount.DiscountTypeFor
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.discount.DiscountFragment
import com.hanheldpos.ui.screens.discount.discount_detail.DiscountDetailFragment
import com.hanheldpos.ui.screens.discount.discount_type.discount_code.adapter.DiscountCodeAdapter


class DiscountAutomaticFragment(
    private val isAlreadyExistDiscountSelect: Boolean = false,
    private val applyToType: DiscApplyTo,
    private val cart: CartModel?,
    private val product: BaseProductInCart?,
    private val listener: DiscountFragment.DiscountTypeListener
) :
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
        viewModel.isAlreadyExistDiscountSelect.observe(this) {
            binding.btnClearDiscount.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (it) R.color.color_0 else R.color.color_8
                )
            )
        }

        discountCodeAdapter =
            DiscountCodeAdapter(listener = object : DiscountCodeAdapter.DiscountItemCallBack {
                override fun onViewDetailClick(item: DiscountResp) {
                    navigator.goTo(DiscountDetailFragment(item, onApplyDiscountAuto = { discount ->
                        viewModel.onApplyDiscountAuto(discount)
                    }))
                }

                override fun onItemClick(item: DiscountResp) {
                    viewModel.onApplyDiscountAuto(item)
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
        viewModel.isAlreadyExistDiscountSelect.postValue(isAlreadyExistDiscountSelect)
        viewModel.loadDiscountAutomatic()
    }

    override fun initAction() {
        binding.btnClearDiscount.setOnClickListener {
            if (isAlreadyExistDiscountSelect) {
                listener.clearAllDiscountCoupon()
                viewModel.isAlreadyExistDiscountSelect.postValue(false)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun loadDataDiscountCode(list: List<DiscountResp>) {
        discountCodeAdapter.submitList(list);
        discountCodeAdapter.notifyDataSetChanged();
    }

    override fun onApplyDiscountForOrder(discount: DiscountResp) {
        listener.discountServerChoose(discount,DiscApplyTo.ORDER)
    }

    override fun onApplyDiscountForItem(discount: DiscountResp) {
        listener.discountServerChoose(discount,DiscApplyTo.ITEM)
    }


    override fun onResume() {
        super.onResume()
        requireActivity().supportFragmentManager.setFragmentResultListener(
            "saveDiscount",
            this
        ) { _, bundle ->
            if (bundle.getSerializable("DiscountTypeFor") == DiscountTypeFor.AUTOMATIC) {

            }
        }
    }

}