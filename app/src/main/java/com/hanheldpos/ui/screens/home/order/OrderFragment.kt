package com.hanheldpos.ui.screens.home.order

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.os.SystemClock
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.binding.loadImageFromUrlToCircular
import com.hanheldpos.data.api.pojo.order.menu.MenusItem
import com.hanheldpos.databinding.DialogCategoryBinding
import com.hanheldpos.databinding.FragmentOrderBinding
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
                                        type = OrderItemType.Product
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
                                override fun onCartAdded(item: OrderItemModel,actionType: ItemActionType) {
                                    showCartAnimation(item);
                                }
                            }
                            navigator.goToWithCustomAnimation(
                                ComboFragment.getInstance(
                                    item = OrderItemModel(
                                        productOrderItem = item,
                                        type = OrderItemType.Combo
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
        binding.rootPopup.visibility = View.VISIBLE
        val animSlideIn = ObjectAnimator.ofFloat(
            binding.rootPopup,
            "translationY",
            100f,
            0f
        ).apply {
            duration = 400
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    cartDataVM.addToCart(item);
                }
            })
        }

        val animSlideStand = ObjectAnimator.ofFloat(
            binding.rootPopup,
            "translationY",
            0f,
            0f
        ).apply {
            duration = 1000
        }

        val animSlideOut = ObjectAnimator.ofFloat(
            binding.rootPopup,
            "translationY",
            0f,
            100f
        ).apply {
            duration = 400
        }

        AnimatorSet().apply {
            play(animSlideIn).before(animSlideStand)
            play(animSlideStand).before(animSlideOut)
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    binding.rootPopup.visibility = View.GONE
                }
            })
        }.start()
        showPopupWindowWithoutBinging(
            binding.imgCart,
            item.productOrderItem?.img
        )
    }

    private fun showPopupWindowWithoutBinging(anchor: View, imgUrl: String?) {
        PopupWindow(anchor.context).apply {
            isOutsideTouchable = true
            val inflater = LayoutInflater.from(anchor.context)
            contentView = inflater.inflate(R.layout.popup_cart_added, null).apply {
                measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                )
            }
            setBackgroundDrawable(null)

            val imgView: ImageView = contentView.findViewById(R.id.imgProductAdded)
            loadImageFromUrlToCircular(imgView, imgUrl)
//            animationStyle = R.style.popup_window_animation

        }.also { popupWindow ->
            // Absolute location of the anchor view
//            popupWindow.animationStyle = R.style.popup_window_animation
            val location = IntArray(2).apply {
                anchor.getLocationOnScreen(this)
            }

            val rootView: FrameLayout = popupWindow.contentView.findViewById(R.id.rootPopup)

            val size = Size(
                popupWindow.contentView.measuredWidth,
                popupWindow.contentView.measuredHeight
            )
            popupWindow.showAtLocation(
                anchor,
                0,
                location[0] - size.width + anchor.width + (size.width - anchor.width) / 2,
                location[1] - size.height/2
            )

            val animTranslateIn =
                ObjectAnimator.ofFloat(rootView, "translationY", 100f, 0f).setDuration(400)
            val animScaleXUp =
                ObjectAnimator.ofFloat(rootView, "scaleX", 0.0f, 1.0f).setDuration(400)
            val animScaleYUp =
                ObjectAnimator.ofFloat(rootView, "scaleY", 0.0f, 1.0f).setDuration(400)
            val animPivotX = ObjectAnimator.ofFloat(rootView, "pivotX", 50f).setDuration(400)
            val animPivotY = ObjectAnimator.ofFloat(rootView, "pivotY", 100f).setDuration(400)
            val animScaleXDown =
                ObjectAnimator.ofFloat(rootView, "scaleX", 1.0f, 0.0f).setDuration(400)
            val animScaleYDown =
                ObjectAnimator.ofFloat(rootView, "scaleY", 1.0f, 0.0f).setDuration(400)
            val animStand =
                ObjectAnimator.ofFloat(rootView, "translationY", 0f, 0f).setDuration(700)
            val animTranslateOut =
                ObjectAnimator.ofFloat(rootView, "translationY", 0f, 100f).apply {
                    duration = 400
                    addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            popupWindow.dismiss()
                        }
                    })
                }

            AnimatorSet().apply {
                playTogether(animTranslateIn, animScaleXUp, animScaleYUp)
                play(animTranslateIn).before(animStand)
                play(animStand).before(animTranslateOut)
                playTogether(
                    animTranslateOut,
                    animScaleXDown,
                    animScaleYDown,
                    animPivotX,
                    animPivotY
                )
            }.start()

//            CoroutineScope(Dispatchers.IO).launch {
//                delay(3000)
//                withContext(Dispatchers.Main) {
//
//                    popupWindow.dismiss()
//                }
//            }
//            Handler().postDelayed({
//                popupWindow.dismiss()
//            },1000)
        }
    }

    private fun menuItemSelected(menuItem: OrderMenuItemModel) {
        dataVM.selectedMenu.value = menuItem
    }

    override fun showCategoryDialog() {
        dataVM.menus.value?.let { menuAdapHelper.submitList(it) }
        dialogCategory.show();
        dialogCategory.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun showCart() {
        navigator.goToWithCustomAnimation(CartFragment.getIntance());
    }

    companion object {
        var selectedSort: Int = 0;
    }

}