package com.hanheldpos.ui.screens.menu.settings.hardware

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.setting.hardware.HardwarePrinter
import com.hanheldpos.databinding.FragmentHardwareBinding
import com.hanheldpos.model.menu.settings.ItemSettingOption
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.adapter.GridSpacingItemDecoration
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.settings.SettingsControlVM
import com.hanheldpos.ui.screens.menu.settings.adapter.SettingOptionType
import com.hanheldpos.ui.screens.menu.settings.adapter.SettingsOptionAdapter
import com.hanheldpos.ui.screens.menu.settings.hardware.hardware_detail.HardwareDetailFragment

class HardwareFragment : BaseFragment<FragmentHardwareBinding, HardwareVM>(), HardwareUV {
    private lateinit var printerStatusAdapter: SettingsOptionAdapter
    private lateinit var deviceStatusAdapter: SettingsOptionAdapter
    private lateinit var selectPrinterAdapter: SettingsOptionAdapter
    private val settingsControlVM by activityViewModels<SettingsControlVM>()

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
                            (item.value).connectionList ?: listOf()
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
        viewModel.getPrinterStatusOptions(requireContext()).let {
            printerStatusAdapter.submitList(it)
            printerStatusAdapter.notifyDataSetChanged()
        }
        viewModel.getDeviceStatusOptions(requireContext()).let {
            deviceStatusAdapter.submitList(it)
            deviceStatusAdapter.notifyDataSetChanged()
        }
        viewModel.getPrinterDeviceOptions(requireContext()).let {
            selectPrinterAdapter.submitList(it)
            selectPrinterAdapter.notifyDataSetChanged()
        }
    }

    override fun initAction() {

    }

}