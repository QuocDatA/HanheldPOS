package com.hanheldpos.ui.screens.home.order

import android.app.AlertDialog
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewTreeObserver
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.order.menu.MenusItem
import com.hanheldpos.data.api.pojo.order.menu.ProductItem
import com.hanheldpos.data.api.pojo.product.ProductDetailResp
import com.hanheldpos.data.api.pojo.table.FloorItem
import com.hanheldpos.databinding.DialogCategoryBinding
import com.hanheldpos.databinding.FragmentOrderBinding
import com.hanheldpos.model.home.order.ProductModeViewType
import com.hanheldpos.model.home.order.menu.OrderMenuItemModel
import com.hanheldpos.model.product.ProductCompleteModel
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.home.HomeFragment
import com.hanheldpos.ui.screens.home.ScreenViewModel
import com.hanheldpos.ui.screens.home.order.adapter.OrderMenuAdapter
import com.hanheldpos.ui.screens.home.order.adapter.OrderMenuAdapterHelper
import com.hanheldpos.ui.screens.home.order.adapter.OrderProductAdapter
import com.hanheldpos.ui.screens.home.order.adapter.OrderProductAdapterHelper
import com.hanheldpos.ui.screens.product.ProductDetailFragment
import kotlinx.coroutines.*

class OrderFragment : BaseFragment<FragmentOrderBinding, OrderVM>(), OrderUV {
    override fun layoutRes() = R.layout.fragment_order

    // ViewModel
    private val dataVM by activityViewModels<OrderDataVM>()
    private val screenViewModel by activityViewModels<ScreenViewModel>()


    // Adapter
    private lateinit var menuAdapter: OrderMenuAdapter;
    private lateinit var menuAdapHelper: OrderMenuAdapterHelper;
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

        menuAdapHelper = OrderMenuAdapterHelper(callBack = object :
            OrderMenuAdapterHelper.AdapterCallBack {
            override fun onListSplitCallBack(list: List<OrderMenuItemModel>) {
                menuAdapter.submitList(list);
                menuAdapter.notifyDataSetChanged();

            }
        });

        menuAdapter = OrderMenuAdapter(
            listener = object : BaseItemClickListener<OrderMenuItemModel> {
                override fun onItemClick(adapterPosition: Int, item: OrderMenuItemModel) {
                    menuItemSelected(item);
                    dialogCategory.dismiss();

                }

            },
            directionCallBack = object : OrderMenuAdapter.Callback {
                override fun directionSelectd(value: Int) {
                    when (value) {
                        1 -> menuAdapHelper.previous();
                        2 -> menuAdapHelper.next();
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
        dialogCateBinding.categoryList.adapter = menuAdapter;

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
                    if (SystemClock.elapsedRealtime() - viewModel.mLastTimeClick > 1000) {
                        viewModel.mLastTimeClick = SystemClock.elapsedRealtime()
                        when (item.uiType) {
                            ProductModeViewType.Product -> {
                                navigator.goToWithCustomAnimation(
                                    ProductDetailFragment.getInstance(
                                    item = ProductCompleteModel(
                                        productItem = item.clone(),
                                        productDetail = ProductDetailResp()
                                    ),
                                    quantityCanChoose = 5,
                                    listener = object : ProductDetailFragment.ProductDetailListener {
                                        override fun onAddCart(productComplete: ProductCompleteModel) {
                                            if (SystemClock.elapsedRealtime() - viewModel.mLastTimeClick > 1000){
                                                viewModel.mLastTimeClick = SystemClock.elapsedRealtime()
                                                OrderHelper.cart.add(productComplete);
                                                dataVM.addProductCompleteToCart(productComplete);
                                            }
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
        dataVM.initData()
    }

    override fun initAction() {
        screenViewModel.dropDownSelected.observe(this, {
            val screen = screenViewModel.screenEvent.value?.screen;
            if (screen == HomeFragment.HomePage.Order) {
                if (it.realItem is MenusItem) {
                    dataVM.onMenuChange(it.position);
                } else if (it.realItem == null)
                    dataVM.onMenuChange(0)
            }
        })

        dataVM.orderMenuLevel1.observe(this, {
            menuAdapHelper.submitList(it);
        })

        dataVM.orderMenuLevel1Selected.observe(this, {
            dataVM.getProductByMenu(it)
                ?.let { it1->
                    productAdapHelper.submitList(it1.toMutableList());
                }
        });



    }

    private fun menuItemSelected(menuItem: OrderMenuItemModel) {
        dataVM.orderMenuLevel1Selected.value = menuItem
    }

    override fun showCategoryDialog() {
        dataVM.orderMenuLevel1.value?.let { menuAdapHelper.submitList(it) }
        dialogCategory.show();
        dialogCategory.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun showCart() {
        /*navigator.goToWithCustomAnimation(CartFragment(
            listener = object :CartFragment.CartListener {
                override fun onDeleteCart() {
                    OrderHelper.cart.clear();
                    dataVM.deleteAllProductCart();
                }
            }
        ));*/
    }

    companion object {
        var selectedSort : Int = 0;
    }

}