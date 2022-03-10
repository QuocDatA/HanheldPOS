package com.hanheldpos.ui.screens.devicecode

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.device.Device
import com.hanheldpos.data.api.pojo.order.settings.DiningOption
import com.hanheldpos.databinding.FragmentDeviceCodeBinding
import com.hanheldpos.extension.navigateTo
import com.hanheldpos.model.DataHelper
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.devicecode.adapter.RecentDeviceAdapter
import com.hanheldpos.ui.screens.main.MainActivity
import com.hanheldpos.ui.screens.pincode.PinCodeFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeviceCodeFragment : BaseFragment<FragmentDeviceCodeBinding, DeviceCodeVM>(), DeviceCodeUV {

    private lateinit var recentDeviceAdapter: RecentDeviceAdapter

    override fun layoutRes() = R.layout.fragment_device_code

    override fun initView() {
        recentDeviceAdapter = RecentDeviceAdapter(listener = object: RecentDeviceAdapter.RecentAccountClickListener<Device> {
            val subList = DataHelper.recentDeviceCodeLocalStorage?.toMutableList()
            override fun onItemClick(adapterPosition: Int, item: Device, view: View) {
                subList?.remove(item)
                subList?.add(0, item)
                DataHelper.recentDeviceCodeLocalStorage = subList?.toList()
                viewModel.signIn(view, true)
            }
            override fun onDeleteClick(item: Device) {
                subList?.remove(item)
                DataHelper.recentDeviceCodeLocalStorage = subList?.toList()
                if( subList != null ) {
                    if(subList.size == 0) {
                        binding.recentAccount.visibility = View.GONE
                    } else {
                        recentDeviceAdapter.submitList(null)
                        recentDeviceAdapter.submitList(subList.toList())
                    }
                }
            }
        })
        binding.recentAccountRecyclerView.apply {
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
            adapter = recentDeviceAdapter
        }
    }

    override fun initData() {
        if(DataHelper.recentDeviceCodeLocalStorage == null || DataHelper.recentDeviceCodeLocalStorage!!.isEmpty()) {
            binding.recentAccount.visibility = View.GONE
        } else {
            val recentDeviceList: MutableList<Device> =
                (DataHelper.recentDeviceCodeLocalStorage as List<Device>).toMutableList()
            recentDeviceAdapter.submitList(recentDeviceList)
        }
    }

    override fun initAction() {
    }

    override fun viewModelClass(): Class<DeviceCodeVM> {
        return DeviceCodeVM::class.java
    }

    override fun initViewModel(viewModel: DeviceCodeVM) {
        viewModel.run {
            init(this@DeviceCodeFragment)
            binding.viewModel = this
        }
    }

    override fun goBack() {
        requireActivity().finish()
    }

    override fun openPinCode() {
        CoroutineScope(Dispatchers.Main).launch {
            navigator.goTo(PinCodeFragment())
        }

    }

}