package com.hanheldpos.ui.screens.home.order

import androidx.recyclerview.widget.DividerItemDecoration
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentOrderBinding
import com.hanheldpos.model.home.MenuModel
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.home.order.adapter.OrderMenuAdapter

class OrderFragment : BaseFragment<FragmentOrderBinding, OrderVM>(), OrderUV {
    override fun layoutRes() = R.layout.fragment_order;

    // Adapter
    private lateinit var menuAdapter: OrderMenuAdapter;


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


        //menu adapter vs listener
        menuAdapter = OrderMenuAdapter(
            listener = object : BaseItemClickListener<MenuModel> {
                override fun onItemClick(adapterPosition: Int, item: MenuModel) {
                    viewModel.menuSelected.value = item;
                    viewModel.menuItemSelected(adapterPosition, item);
                }
            }
        );

        binding.orderMenu.apply {
            adapter = menuAdapter;
            addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.HORIZONTAL))
        }

        viewModel.menuSelected.observe(this, {

            });

    }

    override fun initData() {

    }

    override fun initAction() {

    }

    override fun menuListObserve() {
        menuAdapter.submitList(viewModel.getListOrderMenu());
        menuAdapter.notifyDataSetChanged();
    }


}