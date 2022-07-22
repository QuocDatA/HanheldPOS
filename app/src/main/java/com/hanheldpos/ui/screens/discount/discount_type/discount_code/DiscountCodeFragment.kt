package com.hanheldpos.ui.screens.discount.discount_type.discount_code

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.discount.DiscountCoupon
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.databinding.FragmentDiscountCodeBinding
import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.model.discount.DiscApplyTo
import com.hanheldpos.model.discount.DiscountTriggerType
import com.hanheldpos.model.discount.DiscountTypeFor
import com.hanheldpos.model.product.buy_x_get_y.BuyXGetY
import com.hanheldpos.model.product.combo.ItemActionType
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.discount.DiscountFragment
import com.hanheldpos.ui.screens.discount.discount_detail.DiscountDetailFragment
import com.hanheldpos.ui.screens.discount.discount_type.adapter.DiscountServerAdapter
import com.hanheldpos.ui.screens.product.buy_x_get_y.BuyXGetYFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiscountCodeFragment(
    private val applyToType: DiscApplyTo,
    private val listener: DiscountFragment.DiscountTypeListener
) : BaseFragment<FragmentDiscountCodeBinding, DiscountCodeVM>(), DiscountCodeUV {
    override fun layoutRes(): Int = R.layout.fragment_discount_code

    private lateinit var discountCodeAdapter: DiscountServerAdapter

    override fun viewModelClass(): Class<DiscountCodeVM> {
        return DiscountCodeVM::class.java
    }

    override fun initViewModel(viewModel: DiscountCodeVM) {
        viewModel.run {
            init(this@DiscountCodeFragment)
            binding.viewModel = this
        }

    }

    override fun initView() {

        discountCodeAdapter =
            DiscountServerAdapter(listener = object : DiscountServerAdapter.DiscountItemCallBack {
                override fun onViewDetailClick(item: DiscountResp) {
                    navigator.goTo(
                        DiscountDetailFragment(
                            item,
                            onApplyDiscountAuto = {},
                            onApplyDiscountCode = { discount -> viewModel.onApplyDiscount(discount) })
                    )
                }

                override fun onItemClick(item : DiscountResp) {
                    if (item.isExistsTrigger(DiscountTriggerType.ON_CLICK))
                        viewModel.onApplyDiscount(item)
                }

            })
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
        }
        binding.listDiscountCode.adapter = discountCodeAdapter

    }

    override fun initData() {
        if (applyToType == DiscApplyTo.ORDER)
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.initData()
            }

    }

    override fun openBuyXGetY(discount: DiscountResp) {
        navigator.goTo(
            BuyXGetYFragment(
                buyXGetY = viewModel.initDefaultBuyXGetY(discount),
                discount = discount,
                actionType = ItemActionType.Add,
                listener = object : BuyXGetYCallBack {
                    override fun onCartAdded(item: BaseProductInCart, action: ItemActionType) {
                        listener.addDiscountBuyXGetY(discount, item as BuyXGetY)
                    }

                    override fun onDiscountBuyXGetYEntireOrder(discount: DiscountResp) {
                        listener.discountServerChoose(discount, DiscApplyTo.ORDER, true)
                    }
                }
            )
        )
    }

    override fun initAction() {
        binding.discountCodeInput.doAfterTextChanged {
            listener.validDiscount(it.toString().isNotEmpty())
            viewModel.searchDiscountCode(it.toString())
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun loadDataDiscountCode(list: List<DiscountResp>) {
        lifecycleScope.launch(Dispatchers.Main) {
            discountCodeAdapter.submitList(list)
            discountCodeAdapter.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().supportFragmentManager.setFragmentResultListener(
            "saveDiscount",
            this
        ) { _, bundle ->
            if (bundle.getSerializable("DiscountTypeFor") == DiscountTypeFor.DISCOUNT_CODE) {
                val discountSelect = discountCodeAdapter.currentList.find {
                    it.DiscountCode.uppercase() == binding.discountCodeInput.text.toString()
                }
                if (discountSelect != null) {
                    viewModel.onApplyDiscount(discountSelect)
                }
                else showMessage(getString(R.string.code_doesnt_exist))
            }
        }
        listener.validDiscount(binding.discountCodeInput.text.toString().isNotEmpty())
    }

    override fun updateDiscountCouponCode(discount: List<DiscountCoupon>?) {
        listener.discountCodeChoose(discount)
    }

    interface BuyXGetYCallBack {
        fun onCartAdded(item: BaseProductInCart, action: ItemActionType)
        fun onDiscountBuyXGetYEntireOrder(discount: DiscountResp)
    }
}