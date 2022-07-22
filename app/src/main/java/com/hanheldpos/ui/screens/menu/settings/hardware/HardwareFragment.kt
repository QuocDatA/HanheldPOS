package com.hanheldpos.ui.screens.menu.settings.hardware

import android.annotation.SuppressLint
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.setting.hardware.HardwarePrinter
import com.hanheldpos.databinding.FragmentHardwareBinding
import com.hanheldpos.model.menu.settings.HardwarePrinterDeviceType
import com.hanheldpos.model.menu.settings.ItemSettingOption
import com.hanheldpos.printer.BillPrinterManager
import com.hanheldpos.printer.PrinterException
import com.hanheldpos.printer.printer_devices.Printer
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.adapter.GridSpacingItemDecoration
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.settings.SettingsControlVM
import com.hanheldpos.ui.screens.menu.settings.adapter.SettingOptionType
import com.hanheldpos.ui.screens.menu.settings.adapter.SettingsOptionAdapter
import com.hanheldpos.ui.screens.menu.settings.hardware.hardware_detail.HardwareDetailFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HardwareFragment : BaseFragment<FragmentHardwareBinding, HardwareVM>(), HardwareUV {
    private lateinit var printerStatusAdapter: SettingsOptionAdapter
    private lateinit var deviceStatusAdapter: SettingsOptionAdapter
    private lateinit var selectPrinterAdapter: SettingsOptionAdapter
    private val settingsControlVM by activityViewModels<SettingsControlVM>()
    private val MILISECONDS_RECALL_CONECTION: Long = 20000
    override fun layoutRes(): Int {
        return R.layout.fragment_hardware
    }

    override fun viewModelClass(): Class<HardwareVM> {
        return HardwareVM::class.java
    }

    override fun initViewModel(viewModel: HardwareVM) {
        viewModel.run {
            init(this@HardwareFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {
        printerStatusAdapter = SettingsOptionAdapter(
            null, style = SettingOptionType.BOX,
            object : BaseItemClickListener<ItemSettingOption> {
                override fun onItemClick(adapterPosition: Int, item: ItemSettingOption) {
                    navigator.goTo(
                        HardwareDetailFragment(
                            item.value as HardwarePrinter,
                            (item.value).connectionList?.map { it.copy() } ?: listOf(),
                            onSaveSucceeded = {
                                val updatedIndex = viewModel.updateDeviceStatus(
                                    printerId = item.value.id ?: "",
                                    status = HardwarePrinterDeviceType.CONNECTING
                                )
                                if (updatedIndex >= 0) {
                                    printerStatusAdapter.notifyItemChanged(updatedIndex)
                                }
                                lifecycleScope.launch(Dispatchers.IO) {
                                    checkConnectionStatus()
                                }
                            }
                        )
                    )
                }
            })
        binding.printerStatus.listOption.apply {
            addItemDecoration(
                GridSpacingItemDecoration(
                    3,
                    30,
                    false
                )
            )
            adapter = printerStatusAdapter
        }

        deviceStatusAdapter = SettingsOptionAdapter(null, style = SettingOptionType.BOX,
            object : BaseItemClickListener<ItemSettingOption> {
                override fun onItemClick(adapterPosition: Int, item: ItemSettingOption) {

                }
            })
        binding.orderDeviceStatus.listOption.apply {
            addItemDecoration(
                GridSpacingItemDecoration(
                    3,
                    30,
                    false
                )
            )
            adapter = deviceStatusAdapter
        }

        selectPrinterAdapter = SettingsOptionAdapter(null, style = SettingOptionType.STATUS,
            object : BaseItemClickListener<ItemSettingOption> {
                override fun onItemClick(adapterPosition: Int, item: ItemSettingOption) {

                }
            })
        binding.printerConnectionStatus.apply {
            addItemDecoration(
                GridSpacingItemDecoration(
                    1,
                    15,
                    false
                )
            )
            adapter = selectPrinterAdapter
        }


    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initData() {
        viewModel.initData()
        viewModel.getPrinterStatusOptions().let {
            printerStatusAdapter.submitList(it)
            printerStatusAdapter.notifyDataSetChanged()
        }
        viewModel.getDeviceStatusOptions().let {
            deviceStatusAdapter.submitList(it)
            deviceStatusAdapter.notifyDataSetChanged()
        }
        viewModel.getPrinterDeviceOptions(requireContext()).let {
            selectPrinterAdapter.submitList(it)
            selectPrinterAdapter.notifyDataSetChanged()
        }
    }

    override fun initAction() {
        lifecycleScope.launch(Dispatchers.IO) {
            while (true) {
                checkConnectionStatus()
                delay(MILISECONDS_RECALL_CONECTION)
            }
        }
    }

    private fun checkConnectionStatus() {

        val onConnectionSuccess: (printer: Printer) -> Unit = {
            lifecycleScope.launch(Dispatchers.Main) {

                val updatedIndex = viewModel.updateDeviceStatus(
                    printerId = it.printingSpecification.id ?: "",
                    status = HardwarePrinterDeviceType.CONNECTED
                )

                if (updatedIndex >= 0) {
                    printerStatusAdapter.notifyItemChanged(updatedIndex)
                }
            }
        }

        val onConnectionFailed: (exception: PrinterException) -> Unit = {

            lifecycleScope.launch(Dispatchers.Main) {

                val updatedIndex = viewModel.updateDeviceStatus(
                    printerId = it.printer?.printingSpecification?.id ?: "",
                    status = HardwarePrinterDeviceType.NO_CONNECTION
                )

                if (updatedIndex >= 0) {
                    printerStatusAdapter.notifyItemChanged(updatedIndex)
                }
            }
        }

        BillPrinterManager
            .init(
                context = fragmentContext,
                onConnectionSuccess = onConnectionSuccess,
                onConnectionFailed = onConnectionFailed,
            )
    }

}