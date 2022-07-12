package com.hanheldpos.ui.screens.home

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.data.DataVersion
import com.hanheldpos.data.api.pojo.discount.DiscountCoupon
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.api.pojo.order.settings.DiningOption
import com.hanheldpos.databinding.FragmentHomeBinding
import com.hanheldpos.extension.avoidDropdownFocus
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.SyncDataService
import com.hanheldpos.model.order.OrderModel
import com.hanheldpos.ui.base.dialog.AppAlertDialog
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.base.pager.FragmentPagerAdapter
import com.hanheldpos.ui.screens.cart.CartDataVM
import com.hanheldpos.ui.screens.cart.CartFragment
import com.hanheldpos.ui.screens.cart.CurCartData
import com.hanheldpos.ui.screens.cashdrawer.CashDrawerHelper
import com.hanheldpos.ui.screens.home.adapter.DiningOptionSpinnerAdapter
import com.hanheldpos.ui.screens.home.order.OrderFragment
import com.hanheldpos.ui.screens.home.table.TableFragment
import com.hanheldpos.ui.screens.main.adapter.SubSpinnerAdapter
import com.hanheldpos.ui.screens.main.adapter.TabSpinnerAdapter
import com.hanheldpos.ui.screens.menu.MenuFragment
import com.hanheldpos.ui.screens.menu.report.sale.SaleReportCommonVM
import com.hanheldpos.ui.screens.menu.settings.SettingsControlVM
import com.hanheldpos.utils.NetworkUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeFragment : BaseFragment<FragmentHomeBinding, HomeVM>(), HomeUV {

    private val fragmentMap: MutableMap<HomePage, Fragment> = mutableMapOf()

    enum class HomePage(val pos: Int, val textId: Int) {
        Table(0, R.string.table),
        Order(1, R.string.order);
    }

    private val screenViewModel by activityViewModels<ScreenViewModel>()
    private val settingsControlVM by activityViewModels<SettingsControlVM>()
    private val saleReportCommonVM by activityViewModels<SaleReportCommonVM>()
    private val cartDataVM by activityViewModels<CartDataVM>()
    private val syncDataService by lazy { SyncDataService() }

    // Adapter
    private lateinit var paperAdapter: FragmentPagerAdapter
    private lateinit var subSpinnerAdapter: SubSpinnerAdapter
    private lateinit var diningOptionSpinnerAdapter: DiningOptionSpinnerAdapter

    // Value
    private var isWaitingForNotification: Boolean = false
    override fun layoutRes() = R.layout.fragment_home

    override fun viewModelClass(): Class<HomeVM> {
        return HomeVM::class.java
    }


    override fun initViewModel(viewModel: HomeVM) {
        viewModel.run {
            init(this@HomeFragment)
            binding.viewModel = this
            binding.cartDataVM = cartDataVM
        }
        cartDataVM.initObserveData(requireActivity())
    }

    override fun initView() {
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (CashDrawerHelper.isStartDrawer)
                    CashDrawerHelper.showDrawerNotification(this@HomeFragment.requireActivity())
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
        // init fragment page
        fragmentMap[HomePage.Table] = TableFragment()
        fragmentMap[HomePage.Order] = OrderFragment()
        paperAdapter = FragmentPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
        binding.homeViewPager.apply {
            adapter = paperAdapter
            paperAdapter.submitList(fragmentMap.values)
        }

        binding.toolbarLayout.spnDiningOptionBox.apply {
        }.setOnClickListener {

            val diningOptions: MutableList<DiningOption> =
                (DataHelper.orderSettingLocalStorage?.ListDiningOptions as List<DiningOption>).toMutableList()

            diningOptions.forEachIndexed { index, diningOption ->
                if (cartDataVM.diningOptionLD.value?.Id == diningOption.Id) {
                    if (diningOptions.size - 1 <= index) {
                        cartDataVM.diningOptionChange(diningOptions[0])
                    } else {
                        cartDataVM.diningOptionChange(diningOptions[index + 1])
                    }
                    return@setOnClickListener
                }
            }
        }
        initSpinner()
    }

    override fun initData() {
        settingsControlVM.init(this, listener = object : SettingsControlVM.SettingListener {
            override fun onNotification() {
                CoroutineScope(Dispatchers.IO).launch {
                    if (isWaitingForNotification) return@launch
                    isWaitingForNotification = true
                    while (true) {
                        if (navigator.activeFragment != null && navigator.activeFragment!!::class.java == HomeFragment::class.java && isAdded)
                            break
                        delay(2000)
                    }
                    isWaitingForNotification = false
                    delay(1000)
                    if (DataHelper.isNeedToUpdateNewData.value != true) return@launch
                    launch(Dispatchers.Main) {
                        showAlert(
                            title = getString(R.string.new_data_available),
                            message = getString(R.string.new_data_update_description),
                            negativeText = getString(R.string.later),
                            positiveText = getString(R.string.update),
                            onClickListener = object :
                                AppAlertDialog.AlertDialogOnClickListener {
                                override fun onPositiveClick() {
                                    showLoading(true)
                                    syncDataService.checkNewUpdateVersion(requireContext(),
                                        object : SyncDataService.SyncDataServiceListener {
                                            override fun onLoadedResources(data: Any?) {
                                                val dataVersion = data as DataVersion?
                                                syncDataService.fetchMenuDiscountData(requireContext(),
                                                    object :
                                                        SyncDataService.SyncDataServiceListener {
                                                        override fun onLoadedResources(data: Any?) {
                                                            DataHelper.dataVersionLocalStorage =
                                                                dataVersion
                                                            DataHelper.isNeedToUpdateNewData.postValue(
                                                                false
                                                            )
                                                            OrderFragment.selectedSort = 0
                                                            TableFragment.selectedSort = 0
                                                            screenViewModel.showTablePage()
                                                            showSuccessfully(
                                                                getString(R.string.done),
                                                                getString(R.string.new_data_have_been_updated_successfully)
                                                            )
                                                        }

                                                        override fun onError(message: String?) {
                                                            showLoading(false)
                                                            showMessage(message)
                                                        }
                                                    })
                                            }

                                            override fun onError(message: String?) {
                                                showLoading(false)
                                                showMessage(message)
                                            }
                                        })


                                }
                            }
                        )
                    }

                }

            }

            override fun onPushOrder() {
                Log.d("Push Order", "Time to push")
                saleReportCommonVM.onSyncOrders(requireView(), {}, {})
            }
        })

        DataHelper.isNeedToUpdateNewData.observe(this) {
            settingsControlVM.generalSetting.notifyValueChange()
        }
    }

    override fun initAction() {
        NetworkUtils.cancelNetworkCheck()

        cartDataVM.cartModelLD.observe(this) {
            if (it == null) {
                screenViewModel.showTablePage()
            }
        }

        cartDataVM.diningOptionLD.observe(this) { diningOption ->
            if (diningOption?.SubDiningOption.isNullOrEmpty()) {
                diningOptionSpinnerAdapter.submitList(mutableListOf())
            } else {
                diningOption?.SubDiningOption?.mapIndexed { index, subDiningOptionItem ->
                    DropDownItem(
                        name = subDiningOptionItem.NickName!!,
                        realItem = subDiningOptionItem,
                        position = index
                    )
                }?.let { listSubDiningOption ->
                    diningOptionSpinnerAdapter.submitList(listSubDiningOption)
                }
            }
        }
        screenViewModel.screenEvent.observe(this) {
            // Check cart initialized
            when (it.screen) {
                HomePage.Order -> {
                    if (cartDataVM.cartModelLD.value == null) {
                        showAlert(
                            message = getString(R.string.cart_has_not_been_initialized),
                            onClickListener = object : AppAlertDialog.AlertDialogOnClickListener {
                                override fun onPositiveClick() {
                                    screenViewModel.showTablePage()
                                }
                            })
                    } else {
                        binding.toolbarLayout.spnDiningOptionBox.visibility = View.VISIBLE
                    }
                }
                HomePage.Table -> {
                    binding.toolbarLayout.spnDiningOptionBox.visibility = View.INVISIBLE
                }
            }
            binding.toolbarLayout.spinnerMain.setSelection(it.screen.pos)
        }

        binding.toolbarLayout.spnGroupBy.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    id: Long
                ) {
                    val item = parent?.getItemAtPosition(position) as DropDownItem
                    when (screenViewModel.screenEvent.value?.screen) {
                        HomePage.Order -> OrderFragment.selectedSort = position
                        HomePage.Table -> TableFragment.selectedSort = position
                        else -> {}
                    }
                    screenViewModel.onChangeDropdown(item)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }
        binding.toolbarLayout.spinnerMain.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val item = parent?.getItemAtPosition(position) as HomePage
                    if (screenViewModel.screenEvent.value?.screen != item) {
                        screenViewModel.screenEvent.postValue(
                            ScreenViewModel.ScreenEvent(
                                item,
                                null
                            )
                        )
                    }
                    switchToPage(item)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        // Init Page
        screenViewModel.showTablePage()

    }

    private fun initSpinner() {
        val tabSpinnerAdapter = TabSpinnerAdapter(fragmentContext)
        tabSpinnerAdapter.submitList(HomePage.values().toMutableList())
        binding.toolbarLayout.spinnerMain.apply {
            avoidDropdownFocus()
            adapter = tabSpinnerAdapter
        }
        subSpinnerAdapter = SubSpinnerAdapter(requireContext())
        binding.toolbarLayout.spnGroupBy.apply {
            avoidDropdownFocus()
            adapter = subSpinnerAdapter
        }

        //init SubDiningOption
        diningOptionSpinnerAdapter = DiningOptionSpinnerAdapter(fragmentContext)
    }

    private fun switchToPage(page: HomePage?) {
        val listDropdown: MutableList<DropDownItem> = mutableListOf()
        when (page) {
            HomePage.Table -> {
                Log.d("home", "switchPage: page_table")
                binding.homeViewPager.currentItem = 0
                var i = 1
                DataHelper.floorLocalStorage?.Floor?.map {
                    DropDownItem(name = it.Name, realItem = it, position = i++)
                }?.let {
                    listDropdown.add(DropDownItem(name = "All", position = 0))
                    listDropdown.addAll(it)
                }
            }
            HomePage.Order -> {
                Log.d("home", "switchPage: page_order")
                binding.homeViewPager.currentItem = 1
                var i = 0
                DataHelper.menuLocalStorage?.MenuList?.map {
                    DropDownItem(name = it.Name, realItem = it, position = i++)
                }?.let {
                    listDropdown.addAll(it)
                }

            }
            else -> {}
        }
        subSpinnerAdapter.submitList(listDropdown)
        binding.toolbarLayout.spnGroupBy.setSelection(
            when (page) {
                HomePage.Order -> OrderFragment.selectedSort
                HomePage.Table -> TableFragment.selectedSort
                else -> 0
            }
        )
    }

    override fun openSelectMenu() {
        navigator.goToWithAnimationEnterFromLeft(MenuFragment(listener = object :
            MenuFragment.MenuCallBack {
            override fun updateDiscountCouponCode(discountCouponList: List<DiscountCoupon>?) {
                openCartFragment(null)
            }

            override fun openBuyXGetY(discount: DiscountResp) {
                openCartFragment(discount)
            }
        }))
    }

    fun openCartFragment(discount: DiscountResp? = null) {
        navigator.goTo(CartFragment(listener = object : CartFragment.CartCallBack {
            override fun onCartDelete() {

            }

            override fun onBillSuccess() {

            }

            override fun onOrderSuccess() {

            }

        }, discount = discount))
    }

    override fun onFragmentBackPressed() {
        requireActivity().finish()
    }
}