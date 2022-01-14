package com.hanheldpos.ui.screens.home.order.menu

import android.annotation.SuppressLint
import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentCategoryMenuBinding
import com.hanheldpos.model.home.order.menu.OrderMenuItem
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.adapter.GridSpacingItemDecoration
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.home.order.OrderDataVM
import com.hanheldpos.ui.screens.home.order.adapter.OrderMenuAdapter
import com.hanheldpos.ui.screens.home.order.adapter.OrderMenuAdapterHelper

class CategoryMenuFragment(
    private val isBackToTable: Boolean? = false,
    private val listener: CategoryMenuCallBack,
    private val listMenuCategory: MutableList<OrderMenuItem?>
) :
    BaseFragment<FragmentCategoryMenuBinding, CategoryMenuVM>(),
    CategoryMenuUV {

    override fun layoutRes() = R.layout.fragment_category_menu;

    //Adapter
    private lateinit var menuAdapter: OrderMenuAdapter;
    private lateinit var menuAdapHelper: OrderMenuAdapterHelper;

    //View Model
    private val dataVM by activityViewModels<OrderDataVM>()


    override fun viewModelClass(): Class<CategoryMenuVM> {
        return CategoryMenuVM::class.java;
    }

    override fun initViewModel(viewModel: CategoryMenuVM) {
        viewModel.run {
            init(this@CategoryMenuFragment)
            binding.viewModel = this;
        }
    }

    override fun initView() {
        // category adapter vs listener
        menuAdapHelper = OrderMenuAdapterHelper(callBack = object :
            OrderMenuAdapterHelper.AdapterCallBack {
            @SuppressLint("NotifyDataSetChanged")
            override fun onListSplitCallBack(list: List<OrderMenuItem?>) {
                menuAdapter.submitList(list);
                menuAdapter.notifyDataSetChanged();
            }
        });

        menuAdapter = OrderMenuAdapter(
            listener = object : BaseItemClickListener<OrderMenuItem> {
                override fun onItemClick(adapterPosition: Int, item: OrderMenuItem) {
                    menuItemSelected(item);
                    getBack()
                }

            },
        ).also {
            binding.categoryList.adapter = it
            binding.categoryList.addItemDecoration(
                GridSpacingItemDecoration(spanCount = 2,includeEdge = true, spacing = resources.getDimensionPixelSize(R.dimen._13sdp))
            )
        }
    }

    override fun initData() {
        menuAdapHelper.submitList(listMenuCategory)
    }

    override fun initAction() {
    }

    override fun getBack(isSelected: Boolean) {
        if (isBackToTable == true && !isSelected) {
            onFragmentBackPressed()
            listener.onMenuClose()
        } else {
            onFragmentBackPressed()
        }
    }

    private fun menuItemSelected(menuItem: OrderMenuItem) {
        dataVM.selectedMenu.value = menuItem
    }

    interface CategoryMenuCallBack {
        fun onMenuClose();
    }
}