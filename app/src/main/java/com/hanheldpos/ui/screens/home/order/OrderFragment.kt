package com.hanheldpos.ui.screens.home.order

import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.CategoryItem
import com.hanheldpos.data.api.pojo.ProductItem
import com.hanheldpos.databinding.FragmentOrderBinding
import com.hanheldpos.model.home.order.product.ProductModeViewType
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.home.order.adapter.OrderCategoryAdapter
import com.hanheldpos.ui.screens.home.order.adapter.OrderProductAdapter
import com.hanheldpos.ui.screens.product.ProductDetailFragment
import kotlinx.coroutines.*

class OrderFragment : BaseFragment<FragmentOrderBinding, OrderVM>(), OrderUV {
    override fun layoutRes() = R.layout.fragment_order

    // ViewModel
    private val dataVM by activityViewModels<OrderDataVM>()


    // Adapter
    private lateinit var categoryAdapter: OrderCategoryAdapter
    private lateinit var productAdapter: OrderProductAdapter

    override fun viewModelClass(): Class<OrderVM> {
        return OrderVM::class.java
    }

    override fun initViewModel(viewModel: OrderVM) {
        viewModel.run {
            init(this@OrderFragment)
            initLifeCycle(this@OrderFragment)
            binding.viewModel = this
        }
        binding.dataVM = this.dataVM
    }

    override fun initView() {

        // category adapter vs listener
        categoryAdapter = OrderCategoryAdapter(
            listener = object : BaseItemClickListener<CategoryItem> {
                override fun onItemClick(adapterPosition: Int, item: CategoryItem) {
                    if (SystemClock.elapsedRealtime() - viewModel.mLastTimeClick > 300) {
                        categoryItemSelected(item)
                    }
                }

            }
        ).also {
            binding.categoryList.adapter = it
        }

        // product adapter vs listener

        productAdapter = OrderProductAdapter(
            listener = object : BaseItemClickListener<ProductItem> {
                override fun onItemClick(adapterPosition: Int, item: ProductItem) {
                    Log.d("OrderFragment","Product Selected")
                    if (SystemClock.elapsedRealtime() - viewModel.mLastTimeClick > 300) {
                        when(item.uiType){
                            ProductModeViewType.Product -> {
                                navigator.goToWithCustomAnimation(ProductDetailFragment())
                            }
                            ProductModeViewType.PrevButton -> {
                                if (dataVM.pageProductListSl.value!! > 1){
                                    dataVM.pageProductListSl.value = dataVM.pageProductListSl.value!!.minus(1)
                                }
                            }
                            ProductModeViewType.NextButton -> {
                                if ((dataVM.pageProductListSl.value!! * 15) < dataVM.productListSl.value!!.size ){
                                    dataVM.pageProductListSl.value = dataVM.pageProductListSl.value!!.plus(1)
                                }
                            }
                            else -> {}
                        }
                    }
                }

            }
        ).also {
            binding.productList.adapter = it
        }
    }

    override fun initData() {

    }

    override fun initAction() {
        dataVM.pageCategoryList.observe(this, {
            categoryAdapter.submitList(dataVM.getCategoryByPage(it).toMutableList())
            categoryAdapter.notifyDataSetChanged()
        })

        dataVM.categorySelected.observe(this,{
            dataVM.productListSl.value = dataVM.getProductByCategory(it)?.toMutableList()
            dataVM.pageProductListSl.value = 1

        })

        dataVM.pageProductListSl.observe(this,{
            Log.d("OrderFragment","RecycleView Height:" + binding.productList.height)
            if (binding.productList.height > 0) {
                productAdapter.submitList(dataVM.getProductByPage(it).toMutableList())
                productAdapter.notifyDataSetChanged()
            }

        })

        // Wait for ui draw
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (binding.productList.height > 0) {
                    binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    dataVM.initData()
                }
            }
        })
    }

    override fun changeCategoryPageView(view: View?) {
        dataVM.pageCategoryList.value = when (view?.id) {
            R.id.categoryDirDown -> if (dataVM.pageCategoryList.value!! * OrderDataVM.maxItemViewCate > dataVM.categoryList.value?.size!!)
                dataVM.pageCategoryList.value
            else
                dataVM.pageCategoryList.value?.plus(1)
            R.id.categoryDirUp -> if (dataVM.pageCategoryList.value?.minus(1)!! < 1)
                dataVM.pageCategoryList.value
            else
                dataVM.pageCategoryList.value?.minus(1)
            else -> 1
        }
    }

    private fun categoryItemSelected(categoryItem: CategoryItem) {
        dataVM.categorySelected.value = categoryItem
    }
}