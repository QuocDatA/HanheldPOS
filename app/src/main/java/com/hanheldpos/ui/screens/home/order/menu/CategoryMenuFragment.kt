package com.hanheldpos.ui.screens.home.order.menu

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentCategoryMenuBinding
import com.hanheldpos.model.home.order.menu.MenuModeViewType
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

    override fun layoutRes() = R.layout.fragment_category_menu

    //Adapter
    private lateinit var menuAdapter: OrderMenuAdapter
    private lateinit var menuAdapHelper: OrderMenuAdapterHelper

    //View Model
    private val dataVM by activityViewModels<OrderDataVM>()


    override fun viewModelClass(): Class<CategoryMenuVM> {
        return CategoryMenuVM::class.java
    }

    override fun initViewModel(viewModel: CategoryMenuVM) {
        viewModel.run {
            init(this@CategoryMenuFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {

        // category adapter vs listener
        menuAdapHelper = OrderMenuAdapterHelper(callBack = object :
            OrderMenuAdapterHelper.AdapterCallBack {
            @SuppressLint("NotifyDataSetChanged")
            override fun onListSplitCallBack(list: List<OrderMenuItem?>) {
                menuAdapter.submitList(list)
                menuAdapter.notifyDataSetChanged()
            }
        })

        menuAdapter = OrderMenuAdapter(
            space = resources.getDimension(R.dimen._12sdp).toInt(),
            listener = object : OrderMenuAdapter.OrderMenuCallBack<OrderMenuItem> {

                override fun onBtnPrevClick() {
                    menuAdapHelper.previous()
                }

                override fun onBtnNextClick() {
                    menuAdapHelper.next()
                }

            }, baseListener = object : BaseItemClickListener<OrderMenuItem> {
                override fun onItemClick(adapterPosition: Int, item: OrderMenuItem) {
                    when (item.uiType) {
                        MenuModeViewType.Menu -> {
                            menuItemSelected(item)
                            getBack()
                        }
                        else -> {}
                    }
                }
            }).also {
            binding.categoryList.apply {
                adapter = it
                itemAnimator = null
                addItemDecoration(GridSpacingItemDecoration(2,resources.getDimension(R.dimen._12sdp).toInt(),true))
            }
        }
        binding.categoryList.layoutManager = object: GridLayoutManager(this.requireContext(), 2) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
    }

    override fun initData() {
        menuAdapHelper.submitList(listMenuCategory)
    }

    override fun initAction() {
    }

    override fun getBack(isSelected: Boolean) {
        if (isBackToTable == true && !isSelected) {
            listener.onMenuClose()
        }
        onFragmentBackPressed()

    }

    private fun menuItemSelected(menuItem: OrderMenuItem) {
        dataVM.selectedMenu.postValue(menuItem)
    }

    private fun getScreenHeight(context: Context): Int {
        var height: Int = 0
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val displayMetrics = DisplayMetrics()
            val display = context.display
            display!!.getRealMetrics(displayMetrics)
            displayMetrics.heightPixels
        } else {
            val displayMetrics = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
            height = displayMetrics.heightPixels
            height
        }
    }

    interface CategoryMenuCallBack {
        fun onMenuClose()
    }
}