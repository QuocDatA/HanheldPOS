package com.hanheldpos.ui.screens.menu.customers.customer_detail

import android.annotation.SuppressLint
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.databinding.FragmentCustomerMenuDetailBinding
import com.hanheldpos.extension.addItemDecorationWithoutLastDivider
import com.hanheldpos.extension.setOnClickDebounce
import com.hanheldpos.model.order.OrderSummaryPrimary
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.customers.customer_detail.adapter.CustomerActivityAdapter
import com.hanheldpos.ui.screens.menu.customers.customer_detail.adapter.CustomerActivityAdapterHelper
import com.hanheldpos.ui.screens.menu.order_detail.OrderDetailViewFragment

class CustomerMenuDetailFragment(private val customer: CustomerResp) :
    BaseFragment<FragmentCustomerMenuDetailBinding, CustomerDetailVM>(), CustomerDetailUV {
    private lateinit var activityAdapter: CustomerActivityAdapter
    private lateinit var activityAdapterHelper: CustomerActivityAdapterHelper
    override fun layoutRes(): Int {
        return R.layout.fragment_customer_menu_detail
    }

    override fun viewModelClass(): Class<CustomerDetailVM> {
        return CustomerDetailVM::class.java
    }

    override fun initViewModel(viewModel: CustomerDetailVM) {
        viewModel.run {
            init(this@CustomerMenuDetailFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {
        activityAdapter = CustomerActivityAdapter(object : BaseItemClickListener<OrderSummaryPrimary>{
            override fun onItemClick(adapterPosition: Int, item: OrderSummaryPrimary) {
                item._id?.let {
                    navigator.goToWithCustomAnimation(OrderDetailViewFragment(it))
                }

            }
        })
        binding.activities.run {
            adapter = activityAdapter
            addItemDecorationWithoutLastDivider()
        }
        activityAdapterHelper = CustomerActivityAdapterHelper(object : CustomerActivityAdapterHelper.ActivityCallBack {
            @SuppressLint("NotifyDataSetChanged")
            override fun onGetActivities(customerId: String?, pageNo: Int?, keyRequest: Int) {
                if (pageNo == 1) {
                    binding.scrollView.smoothScrollTo(0,binding.scrollView.getChildAt(0).height)
                    activityAdapter.apply {
                        submitList(mutableListOf())
                        notifyDataSetChanged()
                    }
                }
                viewModel.fetchActivitiesOfCustomer(viewModel.customer.value?._id, pageNo,keyRequest)
            }

            override fun onLoadingNextPage(activities: List<OrderSummaryPrimary>) {
                activityAdapter.apply {
                    submitList(activities)
                    if (activities.isNotEmpty())
                        binding.scrollView.smoothScrollTo(0,binding.scrollView.getChildAt(0).height)
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onLoadedNextPage(startIndex: Int, activities: List<OrderSummaryPrimary>) {
                activityAdapter.apply {
                    submitList(activities)
                    notifyDataSetChanged()
                }
            }
        })
    }

    override fun initData() {
        viewModel.customer.postValue(customer)

    }

    override fun initAction() {
        viewModel.customer.observe(this) {
            activityAdapterHelper.fetch(it?._id)
        }

        binding.btnLoadMore.setOnClickDebounce {
            activityAdapterHelper.nextPage()
        }
    }


    override fun onLoadedActivities(
        activities: List<OrderSummaryPrimary>?,
        isSuccess: Boolean,
        keyRequest: Int
    ) {
        activityAdapterHelper.loaded(activities ?: mutableListOf(),isSuccess,keyRequest)
    }

}