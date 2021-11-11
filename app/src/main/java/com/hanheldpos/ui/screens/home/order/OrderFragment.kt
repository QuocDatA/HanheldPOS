package com.hanheldpos.ui.screens.home.order

import android.app.AlertDialog
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.order.menu.MenusItem
import com.hanheldpos.databinding.DialogCategoryBinding
import com.hanheldpos.databinding.FragmentOrderBinding
import com.hanheldpos.model.cart.CartPresenter
import com.hanheldpos.model.cart.order.OrderItemModel
import com.hanheldpos.model.cart.order.OrderItemType
import com.hanheldpos.model.home.order.ProductModeViewType
import com.hanheldpos.model.home.order.combo.ItemActionType
import com.hanheldpos.model.home.order.menu.OrderMenuItemModel
import com.hanheldpos.model.product.ProductOrderItem
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cart.CartDataVM
import com.hanheldpos.ui.screens.cart.CartFragment
import com.hanheldpos.ui.screens.home.HomeFragment
import com.hanheldpos.ui.screens.home.ScreenViewModel
import com.hanheldpos.ui.screens.home.order.adapter.OrderMenuAdapter
import com.hanheldpos.ui.screens.home.order.adapter.OrderMenuAdapterHelper
import com.hanheldpos.ui.screens.home.order.adapter.OrderProductAdapter
import com.hanheldpos.ui.screens.home.order.adapter.OrderProductAdapterHelper
import com.hanheldpos.ui.screens.home.order.combo.ComboFragment
import com.hanheldpos.ui.screens.product.ProductDetailFragment
import kotlinx.coroutines.*

class OrderFragment : BaseFragment<FragmentOrderBinding, OrderVM>(), OrderUV {
    override fun layoutRes() = R.layout.fragment_order

    // ViewModel
    private val dataVM by activityViewModels<OrderDataVM>()
    private val screenViewModel by activityViewModels<ScreenViewModel>()
    private val cartDataVM by activityViewModels<CartDataVM>()

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
        binding.cartDataVM = this.cartDataVM
    }

    override fun initView() {

        // category adapter vs listener

        menuAdapHelper = OrderMenuAdapterHelper(callBack = object :
            OrderMenuAdapterHelper.AdapterCallBack {
            override fun onListSplitCallBack(list: List<OrderMenuItemModel?>) {
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
        dataVM.menus.value?.let { menuAdapHelper.submitList(it) }

        dialogCateBinding.closeBtn.setOnClickListener { dialogCategory.dismiss() }

        // product adapter vs listener

        productAdapHelper = OrderProductAdapterHelper(
            callBack = object : OrderProductAdapterHelper.AdapterCallBack {
                override fun onListSplitCallBack(list: List<ProductOrderItem>) {
                    GlobalScope.launch(Dispatchers.Main) {
                        productAdapter.submitList(list);
                        productAdapter.notifyDataSetChanged();
                    }

                }
            }
        );

        productAdapter = OrderProductAdapter(
            listener = object : BaseItemClickListener<ProductOrderItem> {
                override fun onItemClick(adapterPosition: Int, item: ProductOrderItem) {
                    Log.d("OrderFragment", "Product Selected");
                    if (SystemClock.elapsedRealtime() - viewModel.mLastTimeClick <= 500) return;
                    viewModel.mLastTimeClick = SystemClock.elapsedRealtime();
                    when (item.uiType) {
                        ProductModeViewType.Product -> {
                            val onCartAdded = object : ProductDetailFragment.ProductDetailListener {
                                override fun onCartAdded(
                                    item: OrderItemModel,
                                    action: ItemActionType
                                ) {
                                    showCartAnimation(item);
                                }
                            }
                            navigator.goToWithCustomAnimation(
                                ProductDetailFragment.getInstance(
                                    item = OrderItemModel(
                                        productOrderItem = item,
                                        type = OrderItemType.Product,
                                    ),
                                    quantityCanChoose = 100,
                                    action = ItemActionType.Add,
                                    listener = onCartAdded
                                )
                            )
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
                        ProductModeViewType.Combo -> {
                            val onCartAdded = object : ComboFragment.ComboListener {
                                override fun onCartAdded(
                                    item: OrderItemModel,
                                    actionType: ItemActionType
                                ) {
                                    showCartAnimation(item);
                                }
                            }
                            navigator.goToWithCustomAnimation(
                                ComboFragment.getInstance(
                                    item = OrderItemModel(
                                        productOrderItem = item,
                                        type = OrderItemType.Combo,
                                    ),
                                    action = ItemActionType.Add,
                                    quantityCanChoose = 100,
                                    listener = onCartAdded
                                )
                            );
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

        dataVM.menus.observe(this, {
            menuAdapHelper.submitList(it);
        })

        dataVM.selectedMenu.observe(this, { orderMenuItemModel ->
            dataVM.getProductByMenu(orderMenuItemModel)
                ?.let { it1 ->
                    val rs: MutableList<ProductOrderItem> = mutableListOf();
                    it1.forEach {
                        it?.let { it2 -> rs.add(it2) }
                    }
                    productAdapHelper.submitList(rs.toMutableList());
                }
        });




    }

    fun showCartAnimation(item: OrderItemModel) {

        binding.txtProduct.text = String.format(
            getString(R.string.added),
            item.productOrderItem?.text
        )
        CartPresenter.showCartAnimation(item, binding.rootPopup, binding.imgCart) {
            cartDataVM.addItemToCart(item);
        };
    }


    private fun menuItemSelected(menuItem: OrderMenuItemModel) {
        dataVM.selectedMenu.value = menuItem
    }

    override fun showCategoryDialog() {
        //dataVM.menus.value?.let { menuAdapHelper.submitList(it) }
        dialogCategory.show();
        dialogCategory.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun showCart() {
        navigator.goToWithCustomAnimation(CartFragment.getInstance());
    }

    companion object {
        // Position previous dropdown item
        var selectedSort: Int = 0;
    }

}