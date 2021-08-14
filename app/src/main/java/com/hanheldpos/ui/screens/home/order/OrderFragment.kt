package com.hanheldpos.ui.screens.home.order

import android.os.SystemClock
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.view.setPadding
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.CategoryItem
import com.hanheldpos.data.api.pojo.ProductItem
import com.hanheldpos.databinding.FragmentOrderBinding
import com.hanheldpos.model.home.order.type.OrderMenuModeViewType
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.home.order.adapter.OrderCategoryAdapter
import com.hanheldpos.ui.screens.home.order.adapter.OrderGPProductAdapter
import com.hanheldpos.ui.screens.product.ProductDetailFragment
import kotlinx.coroutines.*

class OrderFragment : BaseFragment<FragmentOrderBinding, OrderVM>(), OrderUV {
    override fun layoutRes() = R.layout.fragment_order;

    // Temp style
    /*
    * - 1 : Color Style
    * - 2 : Image Style
    */
    private val mainView = OrderMenuModeViewType.fromInt(2);

    // ViewModel
    private val dataVM by activityViewModels<OrderDataVM>()


    // Adapter
    private lateinit var categoryAdapter: OrderCategoryAdapter;
    private lateinit var productGroupAdapter: OrderGPProductAdapter;


    override fun viewModelClass(): Class<OrderVM> {
        return OrderVM::class.java;
    }

    override fun initViewModel(viewModel: OrderVM) {
        viewModel.run {
            init(this@OrderFragment);
            initLifeCycle(this@OrderFragment);
            binding.viewModel = this;

        }
    }

    override fun initView() {

        // product group adapter vs lítener
        productGroupAdapter = OrderGPProductAdapter(
            itemClickListener = object : BaseItemClickListener<ProductItem> {
                override fun onItemClick(adapterPosition: Int, item: ProductItem) {
                    /*if (SystemClock.elapsedRealtime() - viewModel.mLastTimeClick >= 1000) {
                        viewModel.mLastTimeClick = SystemClock.elapsedRealtime();
                        viewModel.productItemSelected(adapterPosition, item);
                    }*/
                }

            }
        ).also {
            binding.orderProductGroupList.adapter = it;
        }

        // category adapter vs listener
        categoryAdapter = OrderCategoryAdapter(
            mainType = mainView,
            listener = object : BaseItemClickListener<CategoryItem> {
                override fun onItemClick(adapterPosition: Int, item: CategoryItem) {
                    if (SystemClock.elapsedRealtime() - viewModel.mLastTimeClick > 300) {
                        viewModel.categoryItemSelected(adapterPosition, item);
                    }
                }

            }
        ).also {
            binding.listCategories.apply {
                layoutManager = when (mainView) {
                    OrderMenuModeViewType.TextColor -> {
                        setPadding(12);
                        GridLayoutManager(context, 2);
                    }
                    OrderMenuModeViewType.TextImage -> {
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    }
                }
                adapter = it;
            }
        }


    }

    override fun initData() {

    }

    override fun initAction() {
        dataVM.categoryList.observe(this,{
            categoryListObserve(it);
        })

        // Wait for ui draw
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (binding.root.height > 0) {
                    binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this);
                    dataVM.initData();
                    // Load All Data
                    dataVM.categoryList.value?.let { productListObserve(it) };
                }
            }
        })
    }

    override fun categoryListObserve(categoryList: List<CategoryItem>) {
        categoryAdapter.submitList(categoryList);
        categoryAdapter.notifyDataSetChanged();
    }

    override fun showDropdownCategories(isShowed: Boolean) {
        if (isShowed) {
            binding.layoutCategory.animate().apply {
                binding.listCategories.smoothScrollToPosition(0);
                binding.layoutCategory.visibility = View.VISIBLE;
                binding.backgroundDropdown.visibility = View.VISIBLE;
                duration = 300;
                alpha(1f)
                translationYBy(80f)
            }.withEndAction {

            }.start();
        } else {
            binding.layoutCategory.animate().apply {
                duration = 300;
                alpha(0.5f)
                translationYBy(-80f)
            }.withEndAction {
                binding.layoutCategory.visibility = View.GONE;
                binding.backgroundDropdown.visibility = View.GONE;

            }.start();
        }
    }

    override fun productListObserve(categoryListSelected: List<CategoryItem>) {
        productGroupAdapter.submitList(categoryListSelected);
        productGroupAdapter.notifyDataSetChanged();
    }

    override fun showProductSelected(productSelected: ProductItem) {
        navigator.goTo(ProductDetailFragment());
    }


}