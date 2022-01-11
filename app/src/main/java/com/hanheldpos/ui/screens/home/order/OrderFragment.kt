package com.hanheldpos.ui.screens.home.order

import android.annotation.SuppressLint
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.order.menu.MenusItem
import com.hanheldpos.databinding.FragmentOrderBinding
import com.hanheldpos.model.cart.CartPresenter
import com.hanheldpos.model.cart.Combo
import com.hanheldpos.model.cart.GroupBundle
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.home.order.ProductModeViewType
import com.hanheldpos.model.combo.ItemActionType
import com.hanheldpos.model.home.order.menu.ProductMenuItem
import com.hanheldpos.model.product.BaseProductInCart
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cart.CartDataVM
import com.hanheldpos.ui.screens.cart.CartFragment
import com.hanheldpos.ui.screens.combo.ComboFragment
import com.hanheldpos.ui.screens.home.HomeFragment
import com.hanheldpos.ui.screens.home.ScreenViewModel
import com.hanheldpos.ui.screens.home.order.adapter.OrderProductAdapter
import com.hanheldpos.ui.screens.home.order.adapter.OrderProductAdapterHelper
import com.hanheldpos.ui.screens.home.order.menu.CategoryMenuFragment
import com.hanheldpos.ui.screens.product.ProductDetailFragment
import com.hanheldpos.ui.screens.product.temporary_style.TemporaryStyleFragment
import com.hanheldpos.ui.screens.product_new.ProductDetailNewFragment
import kotlinx.coroutines.*

class OrderFragment : BaseFragment<FragmentOrderBinding, OrderVM>(), OrderUV {
    override fun layoutRes() = R.layout.fragment_order

    // ViewModel
    private val dataVM by activityViewModels<OrderDataVM>()
    private val cartDataVM by activityViewModels<CartDataVM>()
    private val screenViewModel by activityViewModels<ScreenViewModel>()

    //Adapter
    private lateinit var productAdapter: OrderProductAdapter;
    private lateinit var productAdapHelper: OrderProductAdapterHelper;

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

        // product adapter vs listener
        productAdapHelper = OrderProductAdapterHelper(
            callBack = object : OrderProductAdapterHelper.AdapterCallBack {
                @SuppressLint("NotifyDataSetChanged")
                override fun onListSplitCallBack(list: List<ProductMenuItem>) {
                    GlobalScope.launch(Dispatchers.Main) {
                        productAdapter.submitList(list);
                        productAdapter.notifyDataSetChanged();
                    }

                }
            }
        );

        productAdapter = OrderProductAdapter(
            listener = object : BaseItemClickListener<ProductMenuItem> {
                override fun onItemClick(adapterPosition: Int, item: ProductMenuItem) {
                    Log.d("OrderFragment", "Product Selected");
                    onProductMenuSelected(item);
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

        dataVM.selectedMenu.observe(this, { orderMenuItemModel ->

            val list = dataVM.getProductByMenu(orderMenuItemModel);
            if (list == null) productAdapHelper.submitList(mutableListOf());
            else {
                list.let { it1 ->
                    val rs: MutableList<ProductMenuItem> = mutableListOf();
                    it1.forEach {
                        it.let { it2 -> rs.add(it2) }
                    }
                    productAdapHelper.submitList(rs.toMutableList());
                }
                cartDataVM.updatePriceList(orderMenuItemModel?.id!!);
            }

        });
    }

    fun onProductMenuSelected(item: ProductMenuItem) {
        when (item.uiType) {
            ProductModeViewType.Product -> {
                if (SystemClock.elapsedRealtime() - viewModel.mLastTimeClick <= 500) return;
                viewModel.mLastTimeClick = SystemClock.elapsedRealtime();
                val onCartAdded = object : OrderMenuListener {
                    override fun onCartAdded(item: BaseProductInCart, action: ItemActionType) {
                        showCartAnimation(item);
                    }
                }
                item.proOriginal!!.let {
                    /*navigator.goToWithCustomAnimation(TemporaryStyleFragment());*/
                    if (!it.isBundle())
                        navigator.goToWithCustomAnimation(
                            ProductDetailNewFragment(
                                regular = Regular(
                                    it,
                                    cartDataVM.diningOptionLD.value!!,
                                    1,
                                    it.skuDefault,
                                    it.variantDefault,
                                    it.price,
                                    null
                                ),
                                quantityCanChoose = 100,
                                action = ItemActionType.Add,
                                listener = onCartAdded
                            )
                        )
                    else navigator.goToWithCustomAnimation(
                        ComboFragment(
                            item = Combo(
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
                                it.variants,
                                it.price,
                                null
                            ),
                            action = ItemActionType.Add,
                            quantityCanChoose = 100,
                            listener = onCartAdded
                        )
                    );
                }
            }
            ProductModeViewType.PrevButtonEnable -> {
                GlobalScope.launch(Dispatchers.IO) {
                    productAdapHelper.previous();
                }
            }
            ProductModeViewType.NextButtonEnable -> {
                GlobalScope.launch(Dispatchers.IO) {
                    productAdapHelper.next();
                }
            }
        }
    }

    fun showCartAnimation(item: BaseProductInCart) {

        binding.txtProduct.text = String.format(
            getString(R.string.added),
            item.name
        )
        CartPresenter.showCartAnimation(item, binding.rootPopup, binding.imgCart) {
            cartDataVM.addItemToCart(item);
        };
    }

    override fun showCategoryDialog(isGoBackTable : Boolean) {
        dataVM.menus.value?.let {
            navigator.goTo(CategoryMenuFragment(it))
        }
    }

    override fun showCart() {
        navigator.goToWithCustomAnimation(CartFragment.getInstance(listener = object : CartFragment.CartCallBack {
            override fun onCartDelete() {
                dataVM.onMenuChange(0);
                showCategoryDialog();
            }
        }));
    }

    interface OrderMenuListener {
        fun onCartAdded(item: BaseProductInCart, action: ItemActionType)
    }

    companion object {
        // Position previous dropdown item
        var selectedSort: Int = 0;
    }

}