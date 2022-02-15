package com.hanheldpos.ui.screens.menu

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentMenuBinding
import com.hanheldpos.extension.navigateTo
import com.hanheldpos.model.DataHelper
import com.hanheldpos.ui.screens.menu.adapter.ItemOptionNav
import com.hanheldpos.model.menu_nav_opt.LogoutType
import com.hanheldpos.model.menu_nav_opt.NavBarOptionType
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.dialog.AppAlertDialog
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.adapter.OptionNavAdapter
import com.hanheldpos.ui.screens.menu.option.report.ReportFragment
import com.hanheldpos.ui.screens.pincode.PinCodeActivity
import com.hanheldpos.ui.screens.welcome.WelcomeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

    override fun initView() {

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

        binding.avatarEmployee.setOnClickListener {
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
            NavBarOptionType.ORDERS -> {}
            NavBarOptionType.TRANSACTIONS -> {}
            NavBarOptionType.REPORTS -> navigator.goToWithCustomAnimation(ReportFragment());
            NavBarOptionType.CUSTOMER -> {}
            NavBarOptionType.ORDER_HISTORY -> {}
            NavBarOptionType.SETTINGS -> {}
            NavBarOptionType.SUPPORT -> {}
            NavBarOptionType.LOGOUT_USER -> onLogoutOption(
                LogoutType.LOGOUT_DEVICE,
                title = getString(R.string.logout),
                message = getString(R.string.are_you_sure_you_want_to_exit_this_account)
            )
            NavBarOptionType.RESET_SYSTEM -> {

            }
        }
    }

    private fun onLogoutOption(type: LogoutType, title: String?, message: String?) {
        if (!DataHelper.ordersCompletedLocalStorage.isNullOrEmpty()) {
            AppAlertDialog.get().show(
                getString(R.string.notification),
                getString(R.string.please_sync_local_data_before_logging_out_of_this_account)
            )
            return;
        }
        //TODO : syncing local data orders.
        showAlert(
            title = title,
            message = message,
            positiveText = getString(R.string.ok),
            negativeText = getString(R.string.cancel),
            onClickListener = object : AppAlertDialog.AlertDialogOnClickListener {
                override fun onPositiveClick() {
                    CoroutineScope(Dispatchers.IO).launch {
                        DataHelper.clearData();
                        launch {
                            when (type) {
                                LogoutType.LOGOUT_DEVICE -> {
                                    activity?.navigateTo(
                                        WelcomeActivity::class.java,
                                        alsoFinishCurrentActivity = true,
                                        alsoClearActivity = true,
                                    )
                                }
                                LogoutType.RESET -> TODO()
                            }
                        }
                    }


                }
            })
    }

    private fun onLogoutEmployee() {
        showAlert(
            title = getString(R.string.check_out),
            message = getString(R.string.are_you_sure_you_want_to_exit_this_employee),
            positiveText = getString(R.string.ok),
            negativeText = getString(R.string.cancel),
            onClickListener = object : AppAlertDialog.AlertDialogOnClickListener {
                override fun onPositiveClick() {
                    activity?.navigateTo(
                        PinCodeActivity::class.java,
                        alsoFinishCurrentActivity = true,
                        alsoClearActivity = true,
                    )
                }
            })
    }


}