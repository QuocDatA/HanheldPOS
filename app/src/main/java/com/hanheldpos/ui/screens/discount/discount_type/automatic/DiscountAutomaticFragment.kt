package com.hanheldpos.ui.screens.discount.discount_type.automatic

import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.discount.DiscountItem
import com.hanheldpos.databinding.FragmentDiscountAutomaticBinding
import com.hanheldpos.model.cart.CartModel
import com.hanheldpos.model.discount.DiscountTypeFor
import com.hanheldpos.model.product.BaseProductInCart
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.discount.discount_type.discount_code.adapter.DiscountCodeAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class DiscountAutomaticFragment(private val cart: CartModel?) :
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
        setFragmentResultListener("saveDiscount") { _, bundle ->
            if (bundle.getSerializable("DiscountTypeFor") == DiscountTypeFor.AUTOMATIC) {

            }

        }
        discountCodeAdapter =
            DiscountCodeAdapter(listener = object : BaseItemClickListener<DiscountItem> {
                override fun onItemClick(adapterPosition: Int, item: DiscountItem) {

                }
            });
        binding.listDiscountCode.adapter = discountCodeAdapter;
    }

    override fun initData() {
        itemSelected.observe(this, {
            GlobalScope.launch(Dispatchers.IO) {
                viewModel.loadDiscountAutomatic(itemSelected.value, cart);
            }
        });

    }

    override fun initAction() {

    }

    override fun loadDataDiscountCode(list: List<DiscountItem>) {
        discountCodeAdapter.submitList(list);
        discountCodeAdapter.notifyDataSetChanged();
    }

    fun onItemSelectChange(item: BaseProductInCart?) {
        itemSelected.postValue(item);
    }

}