package com.hanheldpos.ui.screens.menu

import android.graphics.BitmapFactory
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.database.DatabaseMapper
import com.hanheldpos.databinding.FragmentMenuBinding
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.DatabaseHelper
import com.hanheldpos.model.OrderHelper
import com.hanheldpos.model.menu_nav_opt.LogoutType
import com.hanheldpos.model.menu_nav_opt.NavBarOptionType
import com.hanheldpos.model.printer.PrinterHelper
import com.hanheldpos.model.printer.bill.BillOrderHelper
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.dialog.AppAlertDialog
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.adapter.ItemOptionNav
import com.hanheldpos.ui.screens.menu.adapter.OptionNavAdapter
import com.hanheldpos.ui.screens.menu.option.report.ReportFragment
import com.hanheldpos.ui.screens.pincode.PinCodeFragment
import com.hanheldpos.ui.screens.welcome.WelcomeFragment
import com.hanheldpos.utils.NetworkUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import java.io.File

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
            NavBarOptionType.ORDERS -> {
//                val filePath = File(requireActivity().getExternalFilesDir(null), "bitmap.jpeg")
//                val billImage = BitmapFactory.decodeFile(filePath.absolutePath)
//                PrinterHelper.printBillBluetooth(requireActivity(),billImage)
                CoroutineScope(Dispatchers.IO).launch {
                    DatabaseHelper.ordersCompleted.getAll().take(1).collectLatest {
                        it.lastOrNull()?.let { completedEntity ->
                            launch(Dispatchers.Main) {

                                BillOrderHelper.printBillWithBluetooth(requireActivity(),34,
                                    DatabaseMapper.mappingOrderReqFromEntity(completedEntity))

                            }
                        }

                    }
                }

            }
            NavBarOptionType.TRANSACTIONS -> {}
            NavBarOptionType.REPORTS -> navigator.goToWithCustomAnimation(ReportFragment());
            NavBarOptionType.CUSTOMER -> {}
            NavBarOptionType.ORDER_HISTORY -> {}
            NavBarOptionType.SETTINGS -> {}
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