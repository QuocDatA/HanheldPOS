package com.hanheldpos.ui.screens.menu.settings.general

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentGeneralBinding
import com.hanheldpos.model.menu.ItemRadioSettingOption
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.settings.RadioSettingsOptionAdapter


class GeneralFragment : BaseFragment<FragmentGeneralBinding, GeneralVM>(), GeneralUV {

    private lateinit var notificationAdapter: RadioSettingsOptionAdapter
    private lateinit var pushAdapter: RadioSettingsOptionAdapter

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
            RadioSettingsOptionAdapter(object : BaseItemClickListener<ItemRadioSettingOption> {
                override fun onItemClick(adapterPosition: Int, item: ItemRadioSettingOption) {

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
            RadioSettingsOptionAdapter(object : BaseItemClickListener<ItemRadioSettingOption> {
                override fun onItemClick(adapterPosition: Int, item: ItemRadioSettingOption) {

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
    }

    override fun initAction() {

    }

}