package com.hanheldpos.ui.screens.discount.discount_type.automatic

import android.annotation.SuppressLint
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.databinding.FragmentDiscountAutomaticBinding
import com.hanheldpos.model.cart.CartModel
import com.hanheldpos.model.discount.DiscountTypeFor
import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.model.discount.DiscountApplyToType
import com.hanheldpos.model.discount.DiscountTypeEnum
import com.hanheldpos.model.discount.DiscountUser
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.discount.discount_type.discount_code.adapter.DiscountCodeAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class DiscountAutomaticFragment(private val applyToType: DiscountApplyToType ,private val cart: CartModel? ) :
    BaseFragment<FragmentDiscountAutomaticBinding, DiscountAutomaticVM>(), DiscountAutomaticUV {

    private var itemSelected = MutableLiveData<BaseProductInCart?>();

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
            DiscountCodeAdapter(listener = object : BaseItemClickListener<DiscountResp> {
                override fun onItemClick(adapterPosition: Int, item: DiscountResp) {

                }
            });
        binding.listDiscountCode.adapter = discountCodeAdapter;
    }

    override fun initData() {
        itemSelected.observe(this) {
            CoroutineScope(Dispatchers.IO).launch() {
                viewModel.loadDiscountAutomatic(itemSelected.value, cart);
            }
        };

    }

    override fun initAction() {

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun loadDataDiscountCode(list: List<DiscountResp>) {
        discountCodeAdapter.submitList(list);
        discountCodeAdapter.notifyDataSetChanged();
    }

    fun onItemSelectChange(item: BaseProductInCart?) {
        itemSelected.postValue(item);
    }

    override fun onResume() {
        super.onResume()
        requireActivity().supportFragmentManager.setFragmentResultListener("saveDiscount",this) { _, bundle ->
            if (bundle.getSerializable("DiscountTypeFor") == DiscountTypeFor.AUTOMATIC) {

            }
        }
    }

}