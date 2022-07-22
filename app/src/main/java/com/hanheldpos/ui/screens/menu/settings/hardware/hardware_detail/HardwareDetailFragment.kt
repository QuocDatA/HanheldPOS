package com.hanheldpos.ui.screens.menu.settings.hardware.hardware_detail

import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.setting.hardware.HardwareConnection
import com.hanheldpos.data.api.pojo.setting.hardware.HardwarePrinter
import com.hanheldpos.databinding.FragmentHardwareDetailBinding
import com.hanheldpos.model.DataHelper
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.settings.hardware.hardware_detail.adapter.HardwareConnectionAdapter

class HardwareDetailFragment(
    private val printer: HardwarePrinter,
    private val listHardwareConnection: List<HardwareConnection>,
    private val onSaveSucceeded : () -> Unit,
) : BaseFragment<FragmentHardwareDetailBinding, HardwareDetailVM>(),
    HardwareDetailUV {
    override fun layoutRes(): Int = R.layout.fragment_hardware_detail

    private lateinit var hardwareConnectionAdapter: HardwareConnectionAdapter
    private lateinit var changedHardwareConnection: HardwareConnection

    override fun viewModelClass(): Class<HardwareDetailVM> {
        return HardwareDetailVM::class.java
    }

    override fun initViewModel(viewModel: HardwareDetailVM) {
        viewModel.run {
            init(this@HardwareDetailFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {
        hardwareConnectionAdapter = HardwareConnectionAdapter(listener = object :
            HardwareConnectionAdapter.HardwareConnectionCallBack {
            override fun onItemClick(position: Int, item: HardwareConnection) {
                listHardwareConnection.forEach { if (it.id != item.id) it.isChecked = false }
                item.isChecked = true
                changedHardwareConnection = item
            }

            override fun onSaveEnable() {
                binding.isSave = true
            }

            override fun onSaveDisable() {
                binding.isSave = false
            }
        })
        binding.listConnection.adapter = hardwareConnectionAdapter
        binding.btnSave.setOnClickListener {
            printer.apply {
                connectionList = listHardwareConnection
            }
            val changedPrinterList =
                DataHelper.hardwareSettingLocalStorage?.printerList as MutableList?
            changedPrinterList?.set(changedPrinterList.indexOf(printer), printer)
            DataHelper.hardwareSettingLocalStorage = DataHelper.hardwareSettingLocalStorage
            onSaveSucceeded.invoke()
            onFragmentBackPressed()
        }
    }

    override fun initData() {
        binding.subTitle.text = "(${printer.name})"
        hardwareConnectionAdapter.submitList(listHardwareConnection.toMutableList())
    }

    override fun initAction() {

    }
}