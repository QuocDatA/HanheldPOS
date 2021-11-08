package com.hanheldpos.ui.screens.cart.customer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.databinding.FragmentAddCustomerBinding
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cart.customer.adapter.CustomerAdapter


class AddCustomerFragment(
    private val listener: CustomerEvent
) : BaseFragment<FragmentAddCustomerBinding, AddCustomerVM>(), AddCustomerUV {
    //Adapter
    private lateinit var adapterCustomer: CustomerAdapter;

    override fun layoutRes(): Int = R.layout.fragment_add_customer;

    override fun viewModelClass(): Class<AddCustomerVM> {
        return AddCustomerVM::class.java;
    }

    override fun initViewModel(viewModel: AddCustomerVM) {
        viewModel.run {
            init(this@AddCustomerFragment);
            binding.viewModel = this
        }
    }

    override fun initView() {
        adapterCustomer = CustomerAdapter(listener = object : BaseItemClickListener<CustomerResp> {
            override fun onItemClick(adapterPosition: Int, item: CustomerResp) {
                // Dealing with select customer
                listener.onSelectedCustomer(item);
                getBack();
            }
        })
        binding.customerContainer.apply {
            adapter = adapterCustomer;
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
        }

        binding.searchInput.doAfterTextChanged {
            binding.customerContainer.smoothScrollToPosition(0);
            viewModel.searchCustomer(it.toString());
        }
    }

    override fun initData() {

    }

    override fun initAction() {

    }

    override fun getBack() {
        navigator.goOneBack();
    }

    override fun loadCustomer(list: List<CustomerResp>) {
        adapterCustomer.submitList(list);
    }

    interface CustomerEvent {
        fun onSelectedCustomer(item: CustomerResp): Unit {
            /* default implementation */
        }
    }
}