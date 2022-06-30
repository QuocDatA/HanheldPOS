package com.hanheldpos.ui.screens.menu.report

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentReportBinding
import com.hanheldpos.model.menu.report.ReportOptionType
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.adapter.ItemOptionNav
import com.hanheldpos.ui.screens.menu.adapter.OptionNavAdapter
import com.hanheldpos.ui.screens.menu.report.current_drawer.CurrentDrawerFragment
import com.hanheldpos.ui.screens.menu.report.sale.SaleReportsMenuFragment

class ReportFragment : BaseFragment<FragmentReportBinding, ReportVM>(),
    ReportUV {

    private lateinit var menuAdapter: OptionNavAdapter

    override fun layoutRes() = R.layout.fragment_report

    override fun viewModelClass(): Class<ReportVM> {
        return ReportVM::class.java
    }

    override fun initViewModel(viewModel: ReportVM) {
        viewModel.run {
            init(this@ReportFragment);
            binding.viewModel = this;
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
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initData() {
        menuAdapter.submitList(viewModel.initReportItemList(requireContext()));
        menuAdapter.notifyDataSetChanged();
    }

    override fun initAction() {
    }

    override fun getBack() {
        onFragmentBackPressed()
    }

    fun onNavOptionClick(option: ItemOptionNav) {
        when (option.type as ReportOptionType) {
            ReportOptionType.CURRENT_DRAWER -> navigator.goToWithAnimationEnterFromRight(CurrentDrawerFragment());
            ReportOptionType.SALES_REPORT -> navigator.goToWithAnimationEnterFromRight(
                SaleReportsMenuFragment()
            );
        }
    }
}