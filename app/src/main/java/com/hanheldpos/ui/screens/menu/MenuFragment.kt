package com.hanheldpos.ui.screens.menu

import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentMenuBinding
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.menu.adapter.MenuAdapter
import com.hanheldpos.ui.screens.product.adapter.GridSpacingItemDecoration

class MenuFragment : BaseFragment<FragmentMenuBinding, MenuVM>(), MenuUV {
    override fun layoutRes() = R.layout.fragment_menu

    private lateinit var menuAdapter: MenuAdapter

    private val menuVM by activityViewModels<MenuVM>();

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
        menuAdapter = MenuAdapter(
            onMenuItemClickListener = object : BaseItemClickListener<FakeMenuItem> {
                override fun onItemClick(adapterPosition: Int, item: FakeMenuItem) {
                }
            },
        );
        binding.menuItemContainer.adapter = menuAdapter
        //endregion
    }

    override fun initData() {

        //region init payment suggestion data
        val menuItemList: MutableList<FakeMenuItem> =
            (menuVM.initMenuItemList() as List<FakeMenuItem>).toMutableList();

        menuAdapter.submitList(menuItemList)

        binding.menuItemContainer.apply {
            addItemDecoration(
                GridSpacingItemDecoration(spanCount = 1,includeEdge = false, spacing = 2)
            )
        };
        binding.menuItemContainer.adapter = menuAdapter;
        //endregion

    }

    override fun initAction() {
    }

    override fun getBack() {
        onFragmentBackPressed()
    }

    data class FakeMenuItem(val name: String)

}