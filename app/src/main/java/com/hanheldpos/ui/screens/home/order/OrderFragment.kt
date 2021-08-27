package com.hanheldpos.ui.screens.home.order

import android.app.AlertDialog
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewTreeObserver
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.order.CategoryItem
import com.hanheldpos.data.api.pojo.order.ProductItem
import com.hanheldpos.databinding.DialogCategoryBinding
import com.hanheldpos.databinding.FragmentOrderBinding
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.home.order.product.ProductModeViewType
import com.hanheldpos.model.product.ProductCompleteModel
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cart.CartFragment
import com.hanheldpos.ui.screens.home.order.adapter.OrderCategoryAdapter
import com.hanheldpos.ui.screens.home.order.adapter.OrderCategoryAdapterHelper
import com.hanheldpos.ui.screens.home.order.adapter.OrderProductAdapter
import com.hanheldpos.ui.screens.home.order.adapter.OrderProductAdapterHelper
import com.hanheldpos.ui.screens.product.ProductDetailFragment
import kotlinx.coroutines.*

class OrderFragment : BaseFragment<FragmentOrderBinding, OrderVM>(), OrderUV {
    override fun layoutRes() = R.layout.fragment_order

    // ViewModel
    private val dataVM by activityViewModels<OrderDataVM>()


    // Adapter
    private lateinit var categoryAdapter: OrderCategoryAdapter;
    private lateinit var categoryAdapHelper: OrderCategoryAdapterHelper;
    private lateinit var productAdapter: OrderProductAdapter;
    private lateinit var productAdapHelper: OrderProductAdapterHelper;

    // Dialog Category
    private lateinit var dialogCategory: AlertDialog;

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

        categoryAdapHelper = OrderCategoryAdapterHelper(callBack = object :
            OrderCategoryAdapterHelper.AdapterCallBack {
            override fun onListSplitCallBack(list: List<CategoryItem>) {
                categoryAdapter.submitList(list);
                categoryAdapter.notifyDataSetChanged();

            }
        });

        categoryAdapter = OrderCategoryAdapter(
            listener = object : BaseItemClickListener<CategoryItem> {
                override fun onItemClick(adapterPosition: Int, item: CategoryItem) {
                    categoryItemSelected(item);
                    GlobalScope.launch(Dispatchers.IO) {
                        delay(500);
                        launch(Dispatchers.Main) {
                            dialogCategory.dismiss();
                        }
                    }
                }

            },
            directionCallBack = object : OrderCategoryAdapter.Callback {
                override fun directionSelectd(value: Int) {
                    when (value) {
                        1 -> categoryAdapHelper.previous();
                        2 -> categoryAdapHelper.next();
                    }
                }

            }
        )

        // Init Dialog Category
        val dialogCateBinding: DialogCategoryBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_category,
            null,
            false
        );
        dialogCateBinding.categoryList.adapter = categoryAdapter;

        val builder = AlertDialog.Builder(context);
        builder.setView(dialogCateBinding.root);

        dialogCategory = builder.create();

        dialogCateBinding.closeBtn.setOnClickListener { dialogCategory.dismiss() }

        // product adapter vs listener

        productAdapHelper = OrderProductAdapterHelper(
            callBack = object : OrderProductAdapterHelper.AdapterCallBack {
                override fun onListSplitCallBack(list: List<ProductItem>) {
                    GlobalScope.launch(Dispatchers.Main) {
                        productAdapter.submitList(list);
                        productAdapter.notifyDataSetChanged();
                    }

                }
            }
        );

        productAdapter = OrderProductAdapter(
            listener = object : BaseItemClickListener<ProductItem> {
                override fun onItemClick(adapterPosition: Int, item: ProductItem) {
                    Log.d("OrderFragment", "Product Selected");
                    if (SystemClock.elapsedRealtime() - viewModel.mLastTimeClick > 100) {
                        when (item.uiType) {
                            ProductModeViewType.Product -> {
                                navigator.goToWithCustomAnimation(ProductDetailFragment.getInstance(
                                    item = ProductCompleteModel(
                                        productItem = item.clone(),
                                    ),
                                    quantityCanChoose = 5,
                                    listener = object : ProductDetailFragment.ProductDetailListener {
                                        override fun onAddCart(productComplete: ProductCompleteModel) {
                                            OrderHelper.cart.add(productComplete);
                                            dataVM.addProductCompleteToCart(productComplete);
                                        }

                                    }
                                ))
                            }
                            ProductModeViewType.PrevButton -> {
                                GlobalScope.launch(Dispatchers.IO) {
                                    productAdapHelper.previous();
                                }

                            }
                            ProductModeViewType.NextButton -> {
                                GlobalScope.launch(Dispatchers.IO) {
                                    productAdapHelper.next();
                                }

                            }
                            else -> {
                            }
                        }
                    }
                }

            }
        ).also {
            binding.productList.adapter = it;
        };

    }

    override fun initData() {

    }

    override fun initAction() {


        dataVM.categoryList.observe(this, {
            categoryAdapHelper.submitList(it);
        })

        dataVM.categorySelected.observe(this, {
            dataVM.getProductByCategory(it)
                ?.let { it1->
                    productAdapHelper.submitList(it1.toMutableList());

                }
        });


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

    private fun categoryItemSelected(categoryItem: CategoryItem) {
        dataVM.categorySelected.value = categoryItem
    }

    override fun showCategoryDialog() {
        dataVM.categoryList.value?.let { categoryAdapHelper.submitList(it) }
        dialogCategory.show();
        dialogCategory.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun showCart() {
        navigator.goToWithCustomAnimation(CartFragment(
            listener = object :CartFragment.CartListener {
                override fun onDeleteCart() {
                    OrderHelper.cart.clear();
                    dataVM.deleteAllProductCart();
                }

            }
        ));
    }

}