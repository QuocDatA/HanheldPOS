package com.hanheldpos.ui.screens.home.order

import android.annotation.SuppressLint
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.order.menu.Menu
import com.hanheldpos.databinding.FragmentOrderBinding
import com.hanheldpos.model.cart.*
import com.hanheldpos.model.combo.ItemActionType
import com.hanheldpos.model.home.order.ProductModeViewType
import com.hanheldpos.model.home.order.menu.ProductMenuItem
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cart.CartFragment
import com.hanheldpos.ui.screens.cart.CartDataVM
import com.hanheldpos.ui.screens.combo.ComboFragment
import com.hanheldpos.ui.screens.home.HomeFragment
import com.hanheldpos.ui.screens.home.ScreenViewModel
import com.hanheldpos.ui.screens.home.order.adapter.OrderProductAdapter
import com.hanheldpos.ui.screens.home.order.adapter.OrderProductAdapterHelper
import com.hanheldpos.ui.screens.home.order.menu.CategoryMenuFragment
import com.hanheldpos.ui.screens.product.ProductDetailFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderFragment : BaseFragment<FragmentOrderBinding, OrderVM>(), OrderUV {
    override fun layoutRes() = R.layout.fragment_order

    // ViewModel
    private val dataVM by activityViewModels<OrderDataVM>()
    private val screenViewModel by activityViewModels<ScreenViewModel>()
    private val cartDataVM by activityViewModels<CartDataVM>()
    //Adapter
    private lateinit var productAdapter: OrderProductAdapter;
    private lateinit var productAdapterHelper: OrderProductAdapterHelper;

    override fun viewModelClass(): Class<OrderVM> {
        return OrderVM::class.java
    }

    override fun initViewModel(viewModel: OrderVM) {
        viewModel.run {
            init(this@OrderFragment)
            binding.viewModel = this
            binding.cartDataVM = cartDataVM
        }
        binding.dataVM = this.dataVM
    }

    override fun initView() {

        // product adapter vs listener
        productAdapterHelper = OrderProductAdapterHelper(
            callBack = object : OrderProductAdapterHelper.AdapterCallBack {
                @SuppressLint("NotifyDataSetChanged")
                override fun onListSplitCallBack(list: List<ProductMenuItem>) {
                    CoroutineScope(Dispatchers.Main).launch {
                        productAdapter.submitList(list)
                        productAdapter.notifyDataSetChanged()
                    }

                }
            }
        );

        productAdapter = OrderProductAdapter(
            listener = object : BaseItemClickListener<ProductMenuItem> {
                override fun onItemClick(adapterPosition: Int, item: ProductMenuItem) {
                    Log.d("OrderFragment", "Product Selected");
                    onProductMenuSelected(item)
                }
            }
        ).also {
            binding.productList.adapter = it;
        }

    }

    override fun initData() {
        dataVM.initData()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initAction() {

        screenViewModel.dropDownSelected.observe(this) {
            val screen = screenViewModel.screenEvent.value?.screen;
            if (screen == HomeFragment.HomePage.Order) {
                if (it.realItem is Menu) {
                    dataVM.onMenuChange(it.position);
                } else if (it.realItem == null)
                    dataVM.onMenuChange(0)
            }
        }

        dataVM.selectedMenu.observe(this) { orderMenuItemModel ->
            val list = dataVM.getProductByMenu(orderMenuItemModel);
            if (list == null) productAdapterHelper.submitList(mutableListOf());
            else {
                val rs: MutableList<ProductMenuItem> = mutableListOf();
                list.forEach {
                    rs.add(it)
                }
                productAdapterHelper.submitList(rs.toMutableList());
            }
        }

        cartDataVM.cartModelLD.observe(this) {
            productAdapter.notifyDataSetChanged()
        }


    }

    fun onProductMenuSelected(item: ProductMenuItem) {
        when (item.uiType) {
            ProductModeViewType.Product -> {
                if (SystemClock.elapsedRealtime() - viewModel.mLastTimeClick <= 500) return
                viewModel.mLastTimeClick = SystemClock.elapsedRealtime()
                val onCartAdded = object : OrderMenuListener {
                    override fun onCartAdded(item: BaseProductInCart, action: ItemActionType) {
                        showCartAnimation(item)
                    }
                }
                item.proOriginal?.let {
                    if (!it.isBundle()) {
                        val product = ProductDetailFragment(
                            regular = Regular(
                                it,
                                cartDataVM.diningOptionLD.value!!,
                                1,
                                it.skuDefault,
                                it.variantDefault,
                                null
                            ),
                            quantityCanChoose = 1000,
                            action = ItemActionType.Add,
                            listener = onCartAdded
                        )
                        navigator.goTo(product)
                    } else {
                        val combo = ComboFragment(
                            combo = Combo(
                                it,
                                it.groupComboList.map { pro ->
                                    GroupBundle(
                                        pro,
                                        mutableListOf()
                                    )
                                },
                                cartDataVM.diningOptionLD.value!!,
                                1,
                                it.skuDefault,
                                it.Variants,
                                null
                            ),
                            action = ItemActionType.Add,
                            quantityCanChoose = 1000,
                            listener = onCartAdded
                        )
                        navigator.goTo(combo)
                    }


                }
            }
            ProductModeViewType.PrevButtonEnable -> {
                CoroutineScope(Dispatchers.IO).launch {
                    productAdapterHelper.previous()
                }
            }
            ProductModeViewType.NextButtonEnable -> {
                CoroutineScope(Dispatchers.IO).launch {
                    productAdapterHelper.next()
                }
            }
            else -> {}
        }
    }

    fun showCartAnimation(item: BaseProductInCart) {
        binding.txtProduct.text = String.format(
            getString(R.string.added),
            item.name
        )
        CartPresenter.showCartAnimation(item, binding.rootPopup, binding.imgCart) {
            cartDataVM.addItemToCart(item);
        }
    }

    override fun showCategoryDialog(isGoBackTable: Boolean) {
        dataVM.menus.value?.let {
            navigator.goTo(CategoryMenuFragment(listener = object :
                CategoryMenuFragment.CategoryMenuCallBack {
                override fun onMenuClose() {
                    screenViewModel.showTablePage()
                }
            }, listMenuCategory = it, isBackToTable = isGoBackTable))
        }
    }

    override fun showCart() {
        navigator.goToWithCustomAnimation(CartFragment(listener = object :
            CartFragment.CartCallBack {
            override fun onCartDelete() {
                showCategoryDialog(true)
                dataVM.onMenuChange(0)
            }

            override fun onBillSuccess() {

            }

            override fun onOrderSuccess() {
                screenViewModel.showTablePage()
                dataVM.onMenuChange(0)
            }
        }))
    }

    interface OrderMenuListener {
        fun onCartAdded(item: BaseProductInCart, action: ItemActionType)
    }

    companion object {
        // Position previous dropdown item
        var selectedSort: Int = 0
    }

}