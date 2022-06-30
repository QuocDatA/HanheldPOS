package com.hanheldpos.ui.screens.menu.settings.hardware.hardware_detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.setting.hardware.HardwareConnection
import com.hanheldpos.data.api.pojo.setting.hardware.HardwarePrinter
import com.hanheldpos.databinding.FragmentHardwareBinding
import com.hanheldpos.databinding.FragmentHardwareDetailBinding
import com.hanheldpos.model.DataHelper
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.settings.hardware.hardware_detail.adapter.HardwareConnectionAdapter

class HardwareDetailFragment(
    private val printer: HardwarePrinter,
    private val listHardwareConnection: List<HardwareConnection>,
) : BaseFragment<FragmentHardwareDetailBinding, HardwareDetailVM>(),
    HardwareDetailUV {
    override fun layoutRes(): Int = R.layout.fragment_hardware_detail

    private lateinit var hardwareConnectionAdapter: HardwareConnectionAdapter

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
            BaseItemClickListener<HardwareConnection> {
            override fun onItemClick(adapterPosition: Int, item: HardwareConnection) {
                DataHelper.hardwareSettingLocalStorage?.printerList?.forEach {
                    if (it.id == printer.id)
                        it.connectionList?.get(adapterPosition)?.isChecked = item.isChecked
                }
            }
        })
        binding.listConnection.adapter = hardwareConnectionAdapter
    }

    override fun initData() {
        binding.subTitle.text = "(${printer.name})"
        hardwareConnectionAdapter.submitList(listHardwareConnection)
    }

    override fun initAction() {

    }
}