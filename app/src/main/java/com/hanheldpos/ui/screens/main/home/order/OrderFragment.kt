package com.hanheldpos.ui.screens.main.home.order

import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentOrderBinding
import com.hanheldpos.model.home.order.CategoryModel
import com.hanheldpos.model.home.order.MenuModel
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.main.home.order.adapter.OrderCategoryAdapter

class OrderFragment : BaseFragment<FragmentOrderBinding, OrderVM>(), OrderUV {
    override fun layoutRes() = R.layout.fragment_order;


    // Adapter
    private lateinit var categoryAdapter: OrderCategoryAdapter;

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

        // product adapter vs lítener

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
        if(isShowed){
            binding.layoutCategory.animate().apply {
                duration = 200;
                alpha(1f)
                translationYBy(100f)

            }.start();

        }
        else{
            binding.layoutCategory.animate().apply {
                duration = 0;
                alpha(0.5f)
                translationYBy(-100f)
            }.start();
        }
    }


}