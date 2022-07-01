package com.hanheldpos.ui.screens.menu.settings.general

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentGeneralBinding
import com.hanheldpos.model.menu.settings.ItemSettingOption
import com.hanheldpos.model.menu.settings.BillLanguageOption
import com.hanheldpos.model.menu.settings.GeneralNotificationType
import com.hanheldpos.model.menu.settings.GeneralPushType
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.home.DropDownItem
import com.hanheldpos.ui.screens.menu.settings.SettingsControlVM
import com.hanheldpos.ui.screens.menu.settings.adapter.BillLanguageOptionSpinner
import com.hanheldpos.ui.screens.menu.settings.adapter.SettingOptionType
import com.hanheldpos.ui.screens.menu.settings.adapter.SettingsOptionAdapter


class GeneralFragment : BaseFragment<FragmentGeneralBinding, GeneralVM>(), GeneralUV {

    private lateinit var notificationAdapter: SettingsOptionAdapter
    private lateinit var pushAdapter: SettingsOptionAdapter
    private lateinit var billLanguageOptionSpinner: BillLanguageOptionSpinner
    private val settingsControlVM by activityViewModels<SettingsControlVM>()

    override fun layoutRes(): Int {
        return R.layout.fragment_general
    }

    override fun viewModelClass(): Class<GeneralVM> {
        return GeneralVM::class.java
    }

    override fun initViewModel(viewModel: GeneralVM) {
        viewModel.run {
            init(this@GeneralFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {
        notificationAdapter =
            SettingsOptionAdapter(defaultSelection = settingsControlVM.generalSetting.value?.notificationTime,
                style = SettingOptionType.RADIO,
                object : BaseItemClickListener<ItemSettingOption> {
                    override fun onItemClick(adapterPosition: Int, item: ItemSettingOption) {
                        settingsControlVM.generalSetting.postValue(settingsControlVM.generalSetting.value.apply {
                            this?.notificationTime = item.value as GeneralNotificationType
                        })
                    }
                })
        notificationAdapter.let {
            binding.notificationOption.listOption.apply {
                adapter = it
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
        }

        pushAdapter =
            SettingsOptionAdapter(
                defaultSelection = settingsControlVM.generalSetting.value?.automaticallyPushOrdersTime,
                style = SettingOptionType.RADIO,
                object : BaseItemClickListener<ItemSettingOption> {
                    override fun onItemClick(adapterPosition: Int, item: ItemSettingOption) {
                        settingsControlVM.generalSetting.postValue(settingsControlVM.generalSetting.value.apply {
                            this?.automaticallyPushOrdersTime = item.value as GeneralPushType
                        })
                    }
                })

        pushAdapter.let {
            binding.pushOption.listOption.apply {
                adapter = it
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
        }

        billLanguageOptionSpinner = BillLanguageOptionSpinner(requireContext())
        binding.billLanguageOptions.adapter = billLanguageOptionSpinner
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initData() {
        viewModel.getNotificationOptions(requireContext()).let {
            notificationAdapter.submitList(it)
            notificationAdapter.notifyDataSetChanged()
        }
        viewModel.getPushOptions(requireContext()).let {
            pushAdapter.submitList(it)
            pushAdapter.notifyDataSetChanged()
        }
        BillLanguageOption.values().mapIndexed { index, billLanguageOption ->
            DropDownItem(
                name = billLanguageOption.name,
                realItem = billLanguageOption,
                position = index
            )
        }.let {
            billLanguageOptionSpinner.submitList(it)
            billLanguageOptionSpinner.notifyDataSetChanged()
        }
    }

    override fun initAction() {

    }

}