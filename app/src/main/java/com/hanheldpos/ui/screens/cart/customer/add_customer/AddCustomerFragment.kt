package com.hanheldpos.ui.screens.cart.customer.add_customer

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.databinding.FragmentAddCustomerBinding
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cart.customer.add_customer.adapter.CustomerAdapter
import com.hanheldpos.ui.screens.cart.customer.add_customer.adapter.CustomerAdapterHelper
import com.hanheldpos.ui.screens.scanner.ScanQrCodeFragment
import kotlin.random.Random


class AddCustomerFragment(
    private val listener: CustomerEvent
) : BaseFragment<FragmentAddCustomerBinding, AddCustomerVM>(), AddCustomerUV {
    //Adapter
    private lateinit var adapterCustomer: CustomerAdapter
    private lateinit var adapterCustomerHelper: CustomerAdapterHelper

    override fun layoutRes(): Int = R.layout.fragment_add_customer

    override fun viewModelClass(): Class<AddCustomerVM> {
        return AddCustomerVM::class.java
    }

    override fun initViewModel(viewModel: AddCustomerVM) {
        viewModel.run {
            init(this@AddCustomerFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {
        adapterCustomer = CustomerAdapter(listener = object : BaseItemClickListener<CustomerResp?> {
            override fun onItemClick(adapterPosition: Int, item: CustomerResp?) {
                // Dealing with select customer
                listener.onSelectedCustomer(item!!)
                onFragmentBackPressed()
            }
        })
        binding.customerContainer.apply {
            adapter = adapterCustomer
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
            setHasFixedSize(true)
            addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        /// User had scrolled to last
                        adapterCustomerHelper.nextPage()
                    }
                }
            })
        }

        binding.searchInput.doAfterTextChanged {
            adapterCustomerHelper.search(it.toString())
        }

        adapterCustomerHelper = CustomerAdapterHelper(listener = object :
            CustomerAdapterHelper.SearchCallBack {
            @SuppressLint("NotifyDataSetChanged")
            override fun onSearch(keyword: String, pageNo: Int?, keyRequest: Int) {
                if (pageNo == 1) {
                    binding.customerContainer.smoothScrollToPosition(0)
                    adapterCustomer.apply {
                        submitList(mutableListOf())
                        notifyDataSetChanged()
                    }
                    if (keyword.trim().isBlank()) return
                }
                viewModel.searchCustomer(keyword, pageNo, keyRequest)
            }

            override fun onLoadingNextPage(customers: List<CustomerResp>) {
                adapterCustomer.apply {
                    submitList(customers)
                    if (customers.isNotEmpty())
                        binding.customerContainer.smoothScrollToPosition(customers.size - 1)
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onLoadedNextPage(startIndex: Int, customers: List<CustomerResp>) {
                adapterCustomer.apply {
                    submitList(customers)
                    notifyDataSetChanged()
                }

            }

        })
    }

    override fun initData() {

    }

    override fun initAction() {

    }

    override fun onLoadedCustomerView(
        list: List<CustomerResp>,
        isSuccess: Boolean,
        keyRequest: Int
    ) {
        adapterCustomerHelper.loaded(list.toMutableList(), isSuccess, keyRequest)
    }

    override fun onLoadedCustomerScan(
        list: List<CustomerResp>,
        isSuccess: Boolean,
        keyRequest: Int
    ) {
        showLoading(false)
        // Dealing with select customer
        if (list.isEmpty()) {
            showMessage(getString(R.string.customer_does_not_exist))
        } else
            list.firstOrNull()?.let { item ->
                onFragmentBackPressed()
                listener.onSelectedCustomer(item,true)
            }

    }

    override fun onAddNewCustomer() {
        navigator.goToWithCustomAnimation(AddNewCustomerFragment())
    }

    override fun onScanQrCode() {
        navigator.goToWithCustomAnimation(ScanQrCodeFragment(onSuccess = {
            showLoading(true)
            viewModel.searchCustomer(it, keyRequest = Random.nextInt(100000, 999999), isScan = true)
        }))
    }

    interface CustomerEvent {
        fun onSelectedCustomer(item: CustomerResp,openDetail : Boolean? = false)
    }
}