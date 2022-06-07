package com.hanheldpos.ui.screens.menu.customers

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.databinding.FragmentCustomerMenuBinding
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cart.customer.add_customer.adapter.CustomerAdapterHelper
import com.hanheldpos.ui.screens.menu.customers.adapter.CustomerMenuGroupAdapter


class CustomerMenuFragment : BaseFragment<FragmentCustomerMenuBinding,CustomerMenuVM>() , CustomerMenuUV {
    private lateinit var customerAdapterHelper: CustomerAdapterHelper
    private lateinit var customerGroupAdapter : CustomerMenuGroupAdapter
    override fun layoutRes(): Int {
        return R.layout.fragment_customer_menu
    }

    override fun viewModelClass(): Class<CustomerMenuVM> {
        return CustomerMenuVM::class.java
    }

    override fun initViewModel(viewModel: CustomerMenuVM) {
        viewModel.run {
            init(this@CustomerMenuFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {
        customerGroupAdapter = CustomerMenuGroupAdapter(listener = object : BaseItemClickListener<CustomerResp> {
            override fun onItemClick(adapterPosition: Int, item: CustomerResp) {

            }
        })
        binding.customerGroups.run {
            adapter = customerGroupAdapter
            setHasFixedSize(true)
            addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        /// User had scrolled to last
                        customerAdapterHelper.nextPage()
                    }
                }
            })
        }
        customerAdapterHelper = CustomerAdapterHelper(listener = object :
            CustomerAdapterHelper.SearchCallBack {
            @SuppressLint("NotifyDataSetChanged")
            override fun onSearch(keyword: String, pageNo: Int?, keyRequest: Int) {
                if (pageNo == 1) {
                    binding.customerGroups.smoothScrollToPosition(0)
                    if (keyword.trim().isBlank()) return
                }
                viewModel.fetchDataCustomer(keyword, pageNo,keyRequest)
            }

            override fun onLoadingNextPage(customers: List<CustomerResp>) {
                customerGroupAdapter.apply {
                    submitList(viewModel.groupingCustomers(customers))
                    if (customers.isNotEmpty())
                        binding.customerGroups.smoothScrollToPosition(customers.size - 1)
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onLoadedNextPage(startIndex: Int, customers: List<CustomerResp>) {
                customerGroupAdapter.apply {
                    submitList(viewModel.groupingCustomers(customers))
                    notifyDataSetChanged()
                }

            }

        })
    }

    override fun initData() {

    }

    override fun initAction() {
        viewModel.customerSearchString.observe(this) {
            customerAdapterHelper.search(it)
        }
    }

    override fun onLoadedCustomers(list: List<CustomerResp>?, isSuccess: Boolean, keyRequest: Int) {
        list?.let { customerAdapterHelper.loaded(it,isSuccess,keyRequest) }
    }

}