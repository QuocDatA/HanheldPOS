package com.hanheldpos.ui.screens.menu

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.BuildConfig
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.data.DataVersion
import com.hanheldpos.data.api.pojo.discount.DiscountCoupon
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.databinding.FragmentMenuBinding
import com.hanheldpos.extension.setOnClickDebounce
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.DatabaseHelper
import com.hanheldpos.model.OrderHelper
import com.hanheldpos.model.SyncDataService
import com.hanheldpos.model.menu.LogoutType
import com.hanheldpos.model.menu.NavBarOptionType
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.dialog.AppAlertDialog
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.home.ScreenViewModel
import com.hanheldpos.ui.screens.home.order.OrderFragment
import com.hanheldpos.ui.screens.home.table.TableFragment
import com.hanheldpos.ui.screens.menu.adapter.ItemOptionNav
import com.hanheldpos.ui.screens.menu.adapter.OptionNavAdapter
import com.hanheldpos.ui.screens.menu.customers.CustomerMenuFragment
import com.hanheldpos.ui.screens.menu.discount.MenuDiscountFragment
import com.hanheldpos.ui.screens.menu.order_history.OrderHistoryFragment
import com.hanheldpos.ui.screens.menu.orders.OrdersMenuFragment
import com.hanheldpos.ui.screens.menu.report.ReportFragment
import com.hanheldpos.ui.screens.menu.settings.SettingsFragment
import com.hanheldpos.ui.screens.pincode.PinCodeFragment
import com.hanheldpos.ui.screens.welcome.WelcomeFragment
import com.hanheldpos.utils.NetworkUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MenuFragment(private val listener: MenuCallBack) :
    BaseFragment<FragmentMenuBinding, MenuVM>(), MenuUV {
    override fun layoutRes() = R.layout.fragment_menu

    private lateinit var menuAdapter: OptionNavAdapter

    // Fetch all data for storage local
    private val syncDataService by lazy { SyncDataService() }
    private val screenViewModel by activityViewModels<ScreenViewModel>()

    override fun viewModelClass(): Class<MenuVM> {
        return MenuVM::class.java
    }

    override fun initViewModel(viewModel: MenuVM) {
        viewModel.run {
            init(this@MenuFragment)
            binding.viewModel = this
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        binding.versionName.text = "Version ${BuildConfig.VERSION_NAME}"
        //region setup payment suggestion pay in cash recycler view
        menuAdapter = OptionNavAdapter(
            onMenuItemClickListener = object : BaseItemClickListener<ItemOptionNav> {
                override fun onItemClick(adapterPosition: Int, item: ItemOptionNav) {
                    onNavOptionClick(item)
                }
            },
        )
        binding.menuItemContainer.apply {
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
        binding.menuItemContainer.adapter = menuAdapter
        //endregion

        binding.avatarEmployee.setOnClickDebounce {
            onLogoutEmployee()
        }
    }

    override fun initData() {

        //region init payment suggestion data
        menuAdapter.submitList(viewModel.initMenuItemList(requireContext()))
        //endregion
    }

    override fun initAction() {
    }

    override fun getBack() {
        onFragmentBackPressed()
    }

    fun onNavOptionClick(option: ItemOptionNav) {
        when (option.type as NavBarOptionType) {
            NavBarOptionType.DISCOUNT -> {
                navigator.goToWithCustomAnimation(MenuDiscountFragment(listener = object :
                    MenuDiscountFragment.MenuDiscountCallBack {
                    override fun updateDiscountCouponCode(discountCouponList: List<DiscountCoupon>?) {
                        onFragmentBackPressed()
                        listener.updateDiscountCouponCode(discountCouponList)
                    }

                    override fun openDiscountBuyXGetY(discount: DiscountResp) {
                        onFragmentBackPressed()
                        listener.openBuyXGetY(discount)
                    }

                }))
            }
            NavBarOptionType.ORDERS -> {
                navigator.goToWithCustomAnimation(OrdersMenuFragment())
            }
            NavBarOptionType.TRANSACTIONS -> {}
            NavBarOptionType.REPORTS -> navigator.goToWithCustomAnimation(ReportFragment())
            NavBarOptionType.CUSTOMER -> navigator.goToWithCustomAnimation(CustomerMenuFragment())
            NavBarOptionType.ORDER_HISTORY -> navigator.goToWithCustomAnimation(OrderHistoryFragment())
            NavBarOptionType.SETTINGS -> navigator.goToWithCustomAnimation(SettingsFragment())
            NavBarOptionType.SUPPORT -> {}
            NavBarOptionType.LOGOUT_DEVICE -> onLogoutOption(
                LogoutType.LOGOUT_DEVICE,
                title = getString(R.string.logout),
                message = getString(R.string.are_you_sure_you_want_to_exit_this_account)
            )
            NavBarOptionType.RESET_SYSTEM -> {

            }
            NavBarOptionType.UPDATE_DATA -> {
                onUpdateData()
            }
        }
    }

    private fun onLogoutOption(typeLogout: LogoutType, title: String?, message: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            val ordersCompletedFlow = DatabaseHelper.ordersCompleted.getAll()
            ordersCompletedFlow.let { ordersCompleted ->
                // Check to valid number increase
                var isCallingApi = false
                val orderEnhances = ordersCompleted.filter { OrderHelper.isValidOrderLogout(it) }
                if (orderEnhances.isNotEmpty()) {
                    isCallingApi = true
                    showLoading(true)
                    viewModel.onLogoutDevice(requireContext()) {
                        showLoading(false)
                        isCallingApi = false
                    }
                }
                while (true) {
                    if (!isCallingApi) break
                    delay(200)
                }
                val listOrder = ordersCompleted.filter { OrderHelper.isValidOrderPush(it) }
                if (listOrder.isNotEmpty()) {
                    launch(Dispatchers.Main) {
                        showAlert(
                            getString(R.string.notification),
                            getString(R.string.please_sync_local_data_before_logging_out_of_this_account)
                        )
                    }
                    return@let
                }
                launch(Dispatchers.Main) {

                    showAlert(
                        title = title,
                        message = message,
                        positiveText = getString(R.string.yes),
                        negativeText = getString(R.string.no),
                        onClickListener = object : AppAlertDialog.AlertDialogOnClickListener {
                            override fun onPositiveClick() {
                                DataHelper.clearData()
                                when (typeLogout) {
                                    LogoutType.LOGOUT_DEVICE -> {
                                        navigator.clearHistory()
                                        navigator.rootFragment = WelcomeFragment()
                                        NetworkUtils.enableNetworkCheck()
                                    }
                                    LogoutType.RESET -> TODO()
                                }
                            }
                        })
                }
            }
        }

    }

    private fun onLogoutEmployee() {
        showAlert(
            title = getString(R.string.check_out),
            message = getString(R.string.are_you_sure_you_want_to_exit_this_employee),
            positiveText = getString(R.string.ok),
            negativeText = getString(R.string.cancel),
            onClickListener = object : AppAlertDialog.AlertDialogOnClickListener {
                override fun onPositiveClick() {
                    navigator.clearHistory()
                    navigator.rootFragment = PinCodeFragment()
                    NetworkUtils.enableNetworkCheck()
                }
            })
    }

    private fun onUpdateData() {
        context?.let {
            showLoading(true)
            syncDataService.checkNewUpdateVersion(it,
                object : SyncDataService.SyncDataServiceListener {
                    override fun onLoadedResources(data: Any?) {
                        showLoading(false)
                        val dataVersion = data as DataVersion?
                        showAlert(
                            getString(R.string.new_data_available),
                            getString(R.string.new_data_update_description),
                            negativeText = getString(
                                R.string.later
                            ),
                            positiveText = getString(R.string.update),
                            onClickListener = object :
                                AppAlertDialog.AlertDialogOnClickListener {
                                override fun onPositiveClick() {
                                    showLoading(true)
                                    DataHelper.dataVersionLocalStorage = dataVersion
                                    syncDataService.fetchMenuDiscountData(it,
                                        object : SyncDataService.SyncDataServiceListener {
                                            override fun onLoadedResources(data: Any?) {

                                                DataHelper.isNeedToUpdateNewData.postValue(
                                                    false
                                                )
                                                OrderFragment.selectedSort = 0
                                                TableFragment.selectedSort = 0
                                                menuAdapter.submitList(
                                                    viewModel.initMenuItemList(
                                                        requireContext()
                                                    )
                                                )
                                                menuAdapter.notifyDataSetChanged()
                                                screenViewModel.showTablePage()
                                                showSuccessfully(
                                                    getString(R.string.done),
                                                    getString(R.string.new_data_have_been_updated_successfully)
                                                )
                                                onFragmentBackPressed()
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

                    override fun onError(message: String?) {
                        showLoading(false)
                        showMessage(message)
                    }

                })
        }
    }

    interface MenuCallBack {
        fun updateDiscountCouponCode(discountCouponList: List<DiscountCoupon>?)
        fun openBuyXGetY(discount: DiscountResp)
    }

}