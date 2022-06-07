package com.hanheldpos.ui.screens.menu

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.BuildConfig
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentMenuBinding
import com.hanheldpos.extension.setOnClickDebounce
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.DatabaseHelper
import com.hanheldpos.model.OrderHelper
import com.hanheldpos.model.menu.LogoutType
import com.hanheldpos.model.menu.NavBarOptionType
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.dialog.AppAlertDialog
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.adapter.ItemOptionNav
import com.hanheldpos.ui.screens.menu.adapter.OptionNavAdapter
import com.hanheldpos.ui.screens.menu.customers.CustomerMenuFragment
import com.hanheldpos.ui.screens.menu.customers.CustomerMenuVM
import com.hanheldpos.ui.screens.menu.order_history.OrderHistoryFragment
import com.hanheldpos.ui.screens.menu.orders.OrdersMenuFragment
import com.hanheldpos.ui.screens.menu.report.ReportFragment
import com.hanheldpos.ui.screens.menu.settings.SettingsFragment
import com.hanheldpos.ui.screens.pincode.PinCodeFragment
import com.hanheldpos.ui.screens.welcome.WelcomeFragment
import com.hanheldpos.utils.NetworkUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class MenuFragment : BaseFragment<FragmentMenuBinding, MenuVM>(), MenuUV {
    override fun layoutRes() = R.layout.fragment_menu

    private lateinit var menuAdapter: OptionNavAdapter


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
                    onNavOptionClick(item);
                }
            },
        );
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
        };
        binding.menuItemContainer.adapter = menuAdapter
        //endregion

        binding.avatarEmployee.setOnClickDebounce {
            onLogoutEmployee()
        }
    }

    override fun initData() {

        //region init payment suggestion data
        menuAdapter.submitList(viewModel.initMenuItemList(requireContext()));
        //endregion
    }

    override fun initAction() {
    }

    override fun getBack() {
        onFragmentBackPressed()
    }

    fun onNavOptionClick(option: ItemOptionNav) {
        when (option.type as NavBarOptionType) {
            NavBarOptionType.ORDERS -> {
                navigator.goToWithCustomAnimation(OrdersMenuFragment());
            }
            NavBarOptionType.TRANSACTIONS -> {}
            NavBarOptionType.REPORTS -> navigator.goToWithCustomAnimation(ReportFragment());
            NavBarOptionType.CUSTOMER -> navigator.goToWithCustomAnimation(CustomerMenuFragment());
            NavBarOptionType.ORDER_HISTORY -> navigator.goToWithCustomAnimation(OrderHistoryFragment())
            NavBarOptionType.SETTINGS -> navigator.goToWithCustomAnimation(SettingsFragment());
            NavBarOptionType.SUPPORT -> {}
            NavBarOptionType.LOGOUT_DEVICE -> onLogoutOption(
                LogoutType.LOGOUT_DEVICE,
                title = getString(R.string.logout),
                message = getString(R.string.are_you_sure_you_want_to_exit_this_account)
            )
            NavBarOptionType.RESET_SYSTEM -> {

            }
        }
    }

    private fun onLogoutOption(typeLogout: LogoutType, title: String?, message: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            val ordersCompletedFlow = DatabaseHelper.ordersCompleted.getAll()
            ordersCompletedFlow.take(1).collectLatest { ordersCompleted ->
                val listOrder = ordersCompleted.filter { OrderHelper.isValidOrderPush(it) }
                if (!listOrder.isNullOrEmpty()) {
                    launch(Dispatchers.Main) {
                        AppAlertDialog.get().show(
                            getString(R.string.notification),
                            getString(R.string.please_sync_local_data_before_logging_out_of_this_account)
                        )
                    }
                    return@collectLatest
                }
                launch(Dispatchers.Main) {
                    //TODO : syncing local data orders.
                    showAlert(
                        title = title,
                        message = message,
                        positiveText = getString(R.string.ok),
                        negativeText = getString(R.string.cancel),
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


}