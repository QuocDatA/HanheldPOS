package com.hanheldpos.ui.screens.home.order

import android.view.View
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentOrderBinding
import com.hanheldpos.model.home.order.CategoryModel
import com.hanheldpos.model.home.order.MenuModel
import com.hanheldpos.model.home.order.ProductModel
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.main.adapter.TabSpinnerAdapter
import com.hanheldpos.ui.screens.home.HomeFragment
import com.hanheldpos.ui.screens.home.order.adapter.OrderCategoryAdapter
import com.hanheldpos.ui.screens.home.order.adapter.OrderGPProductAdapter

class OrderFragment : BaseFragment<FragmentOrderBinding, OrderVM>(), OrderUV {
    override fun layoutRes() = R.layout.fragment_order;


    // Sub spinner
    private val priceMap : MutableMap<SortPrice,Double> = mutableMapOf();
    enum class SortPrice(pos: Int,name: String){
        PriceList(0,"Price List");
    }

    // Adapter
    private lateinit var categoryAdapter: OrderCategoryAdapter;
    private lateinit var productGroupAdapter : OrderGPProductAdapter;

    override fun viewModelClass(): Class<OrderVM> {
        return OrderVM::class.java;
    }

    override fun initViewModel(viewModel: OrderVM) {
        viewModel.run {
            init(this@OrderFragment);
            initLifecycle(this@OrderFragment);
            binding.viewModel = this;

        }
    }

    override fun initView() {
        // price map
        priceMap[SortPrice.PriceList] = 0.0;

        // product group adapter vs lítener
        productGroupAdapter = OrderGPProductAdapter(
            itemClickListener = object : BaseItemClickListener<ProductModel> {
                override fun onItemClick(adapterPosition: Int, item: ProductModel) {
                    viewModel.productItemSelected(adapterPosition, item);
                }

            }
        )
        binding.orderProductGroupList.adapter = productGroupAdapter;
        // menu adapter vs listener

        // category adapter vs listener
        categoryAdapter = OrderCategoryAdapter(
            listener = object : BaseItemClickListener<CategoryModel> {
                override fun onItemClick(adapterPosition: Int, item: CategoryModel) {
                    viewModel.categoryItemSelected(adapterPosition,item);
                }

            }
        )

        binding.listCategories.adapter = categoryAdapter;

    }

    override fun initData() {

    }

    override fun initAction() {

    }

    override fun categoryListObserve(categoryList: List<CategoryModel>) {
        categoryAdapter.submitList(categoryList);
        categoryAdapter.notifyDataSetChanged();
    }

    override fun showDropdownCategories(isShowed: Boolean) {
        viewModel.isDropDownCategory = true;
        if(isShowed){

            binding.layoutCategory.animate().apply {
                binding.layoutCategory.visibility = View.VISIBLE;
                binding.backgroundDropdown.visibility = View.VISIBLE;
                duration = 300;
                alpha(1f)
                translationYBy(100f)

            }.withEndAction {
                viewModel.isDropDownCategory = false;
            }.start();
        }
        else{
            binding.layoutCategory.animate().apply {
                duration = 300;
                alpha(0.5f)
                translationYBy(-100f)
            }.withEndAction {
                binding.layoutCategory.visibility = View.GONE;
                binding.backgroundDropdown.visibility = View.GONE;
                viewModel.isDropDownCategory = false;
            }.start();
        }
    }

    override fun productListObserve(categoryListSelected: List<CategoryModel>) {
        productGroupAdapter.submitList(categoryListSelected);
        productGroupAdapter.notifyDataSetChanged();
    }


}