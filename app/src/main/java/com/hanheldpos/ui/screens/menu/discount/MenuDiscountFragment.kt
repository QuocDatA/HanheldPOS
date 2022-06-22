package com.hanheldpos.ui.screens.menu.discount

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.PosApp
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.databinding.FragmentMenuDiscountBinding
import com.hanheldpos.model.discount.DiscountTriggerType
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.product.buy_x_get_y.BuyXGetYFragment
import com.hanheldpos.ui.screens.cart.CurCartData
import com.hanheldpos.ui.screens.discount.discount_detail.DiscountDetailFragment
import com.hanheldpos.ui.screens.discount.discount_type.adapter.MenuDiscountAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MenuDiscountFragment : BaseFragment<FragmentMenuDiscountBinding, MenuDiscountVM>(),
    MenuDiscountUV {
    override fun layoutRes(): Int = R.layout.fragment_menu_discount

    private lateinit var discountMenuAdapter: MenuDiscountAdapter

    override fun viewModelClass(): Class<MenuDiscountVM> {
        return MenuDiscountVM::class.java
    }

    override fun initViewModel(viewModel: MenuDiscountVM) {
        viewModel.run {
            init(this@MenuDiscountFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {
        discountMenuAdapter =
            MenuDiscountAdapter(listener = object : MenuDiscountAdapter.DiscountItemCallBack {
                override fun onViewDetailClick(item: DiscountResp) {
                    navigator.goTo(
                        DiscountDetailFragment(
                            item,
                            onApplyDiscountAuto = {},
                            onApplyDiscountCode = {
                                if (CurCartData.cartModel != null)
                                    viewModel.onApplyDiscount(item)
                                else showAlert(
                                    PosApp.instance.getString(R.string.notification),
                                    PosApp.instance.getString(R.string.please_choose_table_first)
                                )
                            })
                    )
                }
                override fun onItemClick(item: DiscountResp) {
                    if (CurCartData.cartModel != null) {
                        if (item.isExistsTrigger(DiscountTriggerType.ON_CLICK)) {
                            viewModel.onApplyDiscount(item)
                        }
                    } else showAlert(
                        PosApp.instance.getString(R.string.notification),
                        PosApp.instance.getString(R.string.please_choose_table_first)
                    )
                }
            });
        binding.menuDiscountContainer.apply {
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    LinearLayoutManager.VERTICAL
                ).apply {
                    setDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.divider_vertical
                        )!!
                    )
                }
            )
        };
        binding.menuDiscountContainer.adapter = discountMenuAdapter;
    }

    override fun initData() {
        viewModel.initListDiscountCode()
    }

    override fun initAction() {
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun loadDiscountCode(list: List<DiscountResp>) {
        CoroutineScope(Dispatchers.Main).launch {
            discountMenuAdapter.submitList(list);
            discountMenuAdapter.notifyDataSetChanged();
        }
    }

    override fun openBuyXGetY(discount: DiscountResp) {

    }
}