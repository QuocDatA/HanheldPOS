package com.hanheldpos.ui.screens.home

import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.order.menu.getMenuList
import com.hanheldpos.data.api.pojo.table.getFloorList
import com.hanheldpos.databinding.FragmentHomeBinding
import com.hanheldpos.model.DataHelper
import com.hanheldpos.ui.base.dialog.AppAlertDialog
import com.hanheldpos.ui.base.pager.FragmentPagerAdapter
import com.hanheldpos.ui.screens.cart.CartDataVM
import com.hanheldpos.ui.screens.main.BaseMainFragment
import com.hanheldpos.ui.screens.home.order.OrderFragment
import com.hanheldpos.ui.screens.home.table.TableFragment
import com.hanheldpos.ui.screens.main.adapter.TabSpinnerAdapter
import com.hanheldpos.ui.screens.main.adapter.SubSpinnerAdapter


class HomeFragment : BaseMainFragment<FragmentHomeBinding, HomeVM>(), HomeUV {

    private val fragmentMap: MutableMap<HomePage, Fragment> = mutableMapOf()

    enum class HomePage(val pos: Int, val textId: Int) {
        Table(0, R.string.table),
        Order(1, R.string.order);
    }


    private val screenViewModel by activityViewModels<ScreenViewModel>()
    private val cartDataVM by activityViewModels<CartDataVM>()

    // Adapter
    private lateinit var paperAdapter: FragmentPagerAdapter
    private lateinit var subSpinnerAdapter: SubSpinnerAdapter

    override fun layoutRes() = R.layout.fragment_home;

    override fun viewModelClass(): Class<HomeVM> {
        return HomeVM::class.java;
    }


    override fun initViewModel(viewModel: HomeVM) {
        viewModel.run {
            init(this@HomeFragment)
            binding.viewModel = this;
        }

    }

    override fun initView() {
        // init fragment page
        fragmentMap[HomePage.Table] = TableFragment();
        fragmentMap[HomePage.Order] = OrderFragment();
        paperAdapter = FragmentPagerAdapter(requireActivity().supportFragmentManager, lifecycle);
        binding.homeViewPager.apply {
            adapter = paperAdapter;
            paperAdapter.submitList(fragmentMap.values)
        }
        initSpinner();
    }


    override fun initData() {

    }

    override fun initAction() {
        screenViewModel.screenEvent.observe(this, {
            // Check cart initialized
            if (it.screen == HomePage.Order && cartDataVM.cartModelLD.value == null) {
                showAlert(
                    message =  "Cart has not been initialized!",
                    onClickListener = object : AppAlertDialog.AlertDialogOnClickListener {
                        override fun onPositiveClick() {
                            screenViewModel.showTablePage();
                        }
                    })
            }
            binding.toolbarLayout.spinnerMain.setSelection(it.screen.pos);
        })

        binding.toolbarLayout.spnGroupBy.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    id: Long
                ) {
                    val item = parent?.getItemAtPosition(position) as DropDownItem;
                    when (screenViewModel.screenEvent.value?.screen) {
                        HomePage.Order -> OrderFragment.selectedSort = position;
                        HomePage.Table -> TableFragment.selectedSort = position;
                    }
                    screenViewModel.onChangeDropdown(item);
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }
        binding.toolbarLayout.spinnerMain.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val item = parent?.getItemAtPosition(position) as HomePage;
                    if (screenViewModel.screenEvent.value?.screen != item) {
                        screenViewModel.screenEvent.value = ScreenViewModel.ScreenEvent(item, null)
                    }
                    switchToPage(item);

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        // Init Page
        screenViewModel.showTablePage();

    }

    private fun initSpinner() {
        val tabSpinnerAdapter = TabSpinnerAdapter(fragmentContext)
        tabSpinnerAdapter.submitList(HomePage.values().toMutableList())
        binding.toolbarLayout.spinnerMain.adapter = tabSpinnerAdapter
        subSpinnerAdapter = SubSpinnerAdapter(requireContext());
        binding.toolbarLayout.spnGroupBy.adapter = subSpinnerAdapter;
    }

    private fun switchToPage(page: HomePage?) {
        val listDropdown: MutableList<DropDownItem> = mutableListOf();
        when (page) {
            HomePage.Table -> {
                Log.d("home", "switchPage: page_table");
                binding.homeViewPager.currentItem = 0;
                var i = 1;
                DataHelper.tableResp?.getFloorList()?.map {
                    DropDownItem(name = it?.name.toString(), realItem = it, position = i++)
                }?.let {
                    listDropdown.add(DropDownItem(name = "All", position = 0))
                    listDropdown.addAll(it)
                }
            }
            HomePage.Order -> {
                Log.d("home", "switchPage: page_order")
                binding.homeViewPager.currentItem = 1;
                var i = 0;
                DataHelper.orderMenuResp?.getMenuList()?.map {
                    DropDownItem(name = it?.name.toString(), realItem = it, position = i++)
                }?.let {
                    listDropdown.addAll(it);
                }

            }
        }
        subSpinnerAdapter.submitList(listDropdown);
        binding.toolbarLayout.spnGroupBy.setSelection(
            when (page) {
                HomePage.Order -> OrderFragment.selectedSort;
                HomePage.Table -> TableFragment.selectedSort;
                else -> 0
            }
        )
    }

}