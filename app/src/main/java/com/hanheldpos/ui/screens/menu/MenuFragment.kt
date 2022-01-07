package com.hanheldpos.ui.screens.menu

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentMenuBinding
import com.hanheldpos.extension.navigateTo
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.menu_nav_opt.ItemMenuOptionNav
import com.hanheldpos.model.menu_nav_opt.LogoutType
import com.hanheldpos.model.menu_nav_opt.NavBarOptionType
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.dialog.AppAlertDialog
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.adapter.MenuAdapter
import com.hanheldpos.ui.screens.pincode.PinCodeActivity
import com.hanheldpos.ui.screens.welcome.WelcomeActivity

class MenuFragment : BaseFragment<FragmentMenuBinding, MenuVM>(), MenuUV {
    override fun layoutRes() = R.layout.fragment_menu

    private lateinit var menuAdapter: MenuAdapter


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
        menuAdapter = MenuAdapter(
            onMenuItemClickListener = object : BaseItemClickListener<ItemMenuOptionNav> {
                override fun onItemClick(adapterPosition: Int, item: ItemMenuOptionNav) {
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

    fun onNavOptionClick(option: ItemMenuOptionNav) {
        when (option.type) {
            NavBarOptionType.ORDERS -> TODO()
            NavBarOptionType.TRANSACTIONS -> TODO()
            NavBarOptionType.REPORTS -> TODO()
            NavBarOptionType.CUSTOMER -> TODO()
            NavBarOptionType.ORDER_HISTORY -> TODO()
            NavBarOptionType.SETTINGS -> TODO()
            NavBarOptionType.SUPPORT -> TODO()
            NavBarOptionType.LOGOUT_USER -> onLogoutOption(
                LogoutType.LOGOUT_DEVICE,
                title = getString(R.string.logout),
                message = getString(R.string.are_you_sure_you_want_to_exit_this_account)
            )
            NavBarOptionType.RESET_SYSTEM -> TODO()
        }
    }

    private fun onLogoutOption(type: LogoutType, title: String?, message: String?) {

        //TODO : syncing local data orders.


        showAlert(
            title = title,
            message = message,
            positiveText = getString(R.string.ok),
            negativeText = getString(R.string.cancel),
            onClickListener = object : AppAlertDialog.AlertDialogOnClickListener {
                override fun onPositiveClick() {
                    DataHelper.clearData();

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