package com.hanheldpos.ui.screens.home.order

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentOrderBinding
import com.hanheldpos.model.home.MenuModel
import com.hanheldpos.model.home.ProductModel
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.home.order.adapter.OrderProductAdapter

class OrderFragment : BaseFragment<FragmentOrderBinding, OrderVM>(), OrderUV {
    override fun layoutRes() = R.layout.fragment_order;

    // Adapter
    private lateinit var productAdapter: OrderProductAdapter;


    override fun viewModelClass(): Class<OrderVM> {
        return OrderVM::class.java;
    }

    override fun initViewModel(viewModel: OrderVM) {
        viewModel.run {
            init(this@OrderFragment);
            binding.viewModel = this;
            initLifecycle(this@OrderFragment);
        }
    }

    override fun initView() {

        // product adapter vs lítener
        productAdapter = OrderProductAdapter(
          listener = object : BaseItemClickListener<ProductModel> {
              override fun onItemClick(adapterPosition: Int, item: ProductModel) {

              }
          }
        );

        binding.orderProductList.apply {
            adapter = productAdapter;
            val itemDrawable = DividerItemDecoration(context,DividerItemDecoration.VERTICAL);
            itemDrawable.setDrawable(ContextCompat.getDrawable(context,R.drawable.divider_vertical)!!);
            addItemDecoration(itemDrawable);
        }
    }

    override fun initData() {

    }

    override fun initAction() {

    }

    override fun menuListObserve() {

    }


    override fun productListObserve() {
        productAdapter.submitList(viewModel.getProductList())
        productAdapter.notifyDataSetChanged();
    }


}