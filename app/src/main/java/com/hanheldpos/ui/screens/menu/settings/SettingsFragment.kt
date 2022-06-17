package com.hanheldpos.ui.screens.menu.settings

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentSettingsBinding
import com.hanheldpos.model.menu.settings.SettingsOptionType
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.adapter.ItemOptionNav
import com.hanheldpos.ui.screens.menu.adapter.OptionNavAdapter
import com.hanheldpos.ui.screens.menu.settings.general.GeneralFragment
import com.hanheldpos.ui.screens.menu.settings.hardware.HardwareFragment


class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsVM>(), SettingsUV {

    private lateinit var menuOptionAdapter: OptionNavAdapter

    override fun layoutRes(): Int {
        return R.layout.fragment_settings
    }

    override fun viewModelClass(): Class<SettingsVM> {
        return SettingsVM::class.java
    }

    override fun initViewModel(viewModel: SettingsVM) {

        viewModel.run {
            init(this@SettingsFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {
        menuOptionAdapter = OptionNavAdapter(object : BaseItemClickListener<ItemOptionNav> {
            override fun onItemClick(adapterPosition: Int, item: ItemOptionNav) {
                onNavOptionClick(item)
            }
        })

        binding.menuItemContainer.apply {
            adapter = menuOptionAdapter
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

    @SuppressLint("NotifyDataSetChanged")
    override fun initData() {
        menuOptionAdapter.submitList(viewModel.initSettingsItemList(requireContext()))
        menuOptionAdapter.notifyDataSetChanged()
    }

    override fun initAction() {

    }

    fun onNavOptionClick(option: ItemOptionNav) {
        when (option.type as SettingsOptionType) {
            SettingsOptionType.GENERAL -> navigator.goToWithAnimationEnterFromRight(
                GeneralFragment()
            );
            SettingsOptionType.HARDWARE -> navigator.goToWithAnimationEnterFromRight(
                HardwareFragment()
            );
        }
    }
}