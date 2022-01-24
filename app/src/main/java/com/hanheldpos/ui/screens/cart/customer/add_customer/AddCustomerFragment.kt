package com.hanheldpos.ui.screens.cart.customer.add_customer

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


class AddCustomerFragment(
    private val listener: CustomerEvent
) : BaseFragment<FragmentAddCustomerBinding, AddCustomerVM>(), AddCustomerUV {
    //Adapter
    private lateinit var adapterCustomer: CustomerAdapter;
    private lateinit var adapterCustomerHelper: CustomerAdapterHelper;

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
        adapterCustomer = CustomerAdapter(listener = object : BaseItemClickListener<CustomerResp?> {
            override fun onItemClick(adapterPosition: Int, item: CustomerResp?) {
                // Dealing with select customer
                listener.onSelectedCustomer(item!!);
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
            });
        }

        binding.searchInput.doAfterTextChanged {
            adapterCustomerHelper.search(it.toString());
        }

        adapterCustomerHelper = CustomerAdapterHelper(listener = object :
            CustomerAdapterHelper.SearchCallBack {
            override fun onSearch(keyword: String, pageNo: Int?) {

                if (pageNo == 1) {
                    binding.customerContainer.smoothScrollToPosition(0);
                    adapterCustomer.apply {
                        submitList(listOf());
                        notifyDataSetChanged()
                    }
                    if (keyword.trim().isBlank()) return;
                }
                viewModel.searchCustomer(keyword, pageNo);
            }

            override fun onLoadingNextPage(customers: List<CustomerResp?>) {
                adapterCustomer.apply {
                    submitList(customers);
                    notifyItemRangeInserted(customers.size - 1, 1)
                    if (customers.isNotEmpty())
                        binding.customerContainer.smoothScrollToPosition(customers.size - 1);
                }
            }

            override fun onLoadedNextPage(startIndex: Int, customers: List<CustomerResp?>) {
                adapterCustomer.apply {
                    submitList(customers)
                    notifyDataSetChanged();
                }

            }

        })
    }

    override fun initData() {

    }

    override fun initAction() {

    }

    override fun getBack() {
        navigator.goOneBack();
    }

    override fun loadCustomer(list: List<CustomerResp>, isSuccess: Boolean) {
        adapterCustomerHelper.loaded(list.toMutableList(), isSuccess);
    }

    override fun onAddNewCustomer() {
        navigator.goToWithCustomAnimation(AddNewCustomerFragment())
    }

    interface CustomerEvent {
        fun onSelectedCustomer(item: CustomerResp): Unit {
            /* default implementation */
        }
    }
}