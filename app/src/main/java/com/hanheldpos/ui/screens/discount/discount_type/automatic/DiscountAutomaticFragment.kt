package com.hanheldpos.ui.screens.discount.discount_type.automatic

import android.annotation.SuppressLint
import android.text.InputFilter
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.databinding.FragmentDiscountAutomaticBinding
import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.model.cart.CartModel
import com.hanheldpos.model.discount.DiscApplyTo
import com.hanheldpos.model.discount.DiscountTriggerType
import com.hanheldpos.model.discount.DiscountTypeFor
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.discount.DiscountFragment
import com.hanheldpos.ui.screens.discount.discount_detail.DiscountDetailFragment
import com.hanheldpos.ui.screens.discount.discount_type.adapter.DiscountServerAdapter


class DiscountAutomaticFragment(
    private val applyToType: DiscApplyTo,
    private val cart: CartModel?,
    private val product: BaseProductInCart?,
    private val listener: DiscountFragment.DiscountTypeListener
) :
    BaseFragment<FragmentDiscountAutomaticBinding, DiscountAutomaticVM>(), DiscountAutomaticUV {

    override fun layoutRes(): Int {
        return R.layout.fragment_discount_automatic
    }

    private lateinit var discountAutoAdapter: DiscountServerAdapter;

    override fun viewModelClass(): Class<DiscountAutomaticVM> {
        return DiscountAutomaticVM::class.java
    }

    override fun initViewModel(viewModel: DiscountAutomaticVM) {
        viewModel.run {
            init(this@DiscountAutomaticFragment)
        }
        binding.viewModel = viewModel;
    }

    override fun initView() {

        discountAutoAdapter =
            DiscountServerAdapter(listener = object : DiscountServerAdapter.DiscountItemCallBack {
                override fun onViewDetailClick(item: DiscountResp) {
                    navigator.goTo(DiscountDetailFragment(item, onApplyDiscountAuto = { discount ->
                        viewModel.onApplyDiscountAuto(discount)
                    }, onApplyDiscountCode = {}))
                }

                override fun onItemClick(item: DiscountResp) {
                    if (item.isExistsTrigger(DiscountTriggerType.ON_CLICK))
                        viewModel.onApplyDiscountAuto(item)
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
        binding.listDiscountCode.adapter = discountAutoAdapter

    }

    override fun initData() {
        viewModel.initData()
    }

    override fun initAction() {

        binding.discountAutomaticInput.doAfterTextChanged {
            listener.validDiscount(it.toString().isNotEmpty())
            viewModel.loadDiscountAutomatic(it.toString())
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun loadDataDiscountCode(list: List<DiscountResp>) {
        discountAutoAdapter.submitList(list);
        discountAutoAdapter.notifyDataSetChanged();
    }

    override fun onApplyDiscountForOrder(discount: DiscountResp) {
        listener.discountServerChoose(discount, DiscApplyTo.ORDER)
    }

    override fun onApplyDiscountForItem(discount: DiscountResp) {
        listener.discountServerChoose(discount, DiscApplyTo.ITEM)
    }


    override fun onResume() {
        super.onResume()
        requireActivity().supportFragmentManager.setFragmentResultListener(
            "saveDiscount",
            this
        ) { _, bundle ->
            if (bundle.getSerializable("DiscountTypeFor") == DiscountTypeFor.AUTOMATIC) {
                val discountSelect = discountAutoAdapter.currentList.find {
                    it.DiscountCode.uppercase() == binding.discountAutomaticInput.text.toString()
                }
                if (discountSelect != null) {
                    viewModel.onApplyDiscountAuto(discountSelect)
                } else showMessage(getString(R.string.code_doesnt_exist))

            }
        }
        listener.validDiscount(binding.discountAutomaticInput.text.toString().isNotEmpty())
    }

}