package com.hanheldpos.ui.screens.home

import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.order.settings.DiningOption
import com.hanheldpos.databinding.FragmentHomeBinding
import com.hanheldpos.model.DataHelper
import com.hanheldpos.ui.base.dialog.AppAlertDialog
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.base.pager.FragmentPagerAdapter
import com.hanheldpos.ui.screens.cart.CartDataVM
import com.hanheldpos.ui.screens.cashdrawer.CashDrawerHelper
import com.hanheldpos.ui.screens.home.adapter.DiningOptionSpinnerAdapter
import com.hanheldpos.ui.screens.home.order.OrderFragment
import com.hanheldpos.ui.screens.home.table.TableFragment
import com.hanheldpos.ui.screens.main.adapter.SubSpinnerAdapter
import com.hanheldpos.ui.screens.main.adapter.TabSpinnerAdapter
import com.hanheldpos.ui.screens.menu.MenuFragment
import com.hanheldpos.utils.NetworkUtils


class HomeFragment : BaseFragment<FragmentHomeBinding, HomeVM>(), HomeUV {

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
    private lateinit var diningOptionSpinnerAdapter: DiningOptionSpinnerAdapter

    override fun layoutRes() = R.layout.fragment_home;

    override fun viewModelClass(): Class<HomeVM> {
        return HomeVM::class.java;
    }


    override fun initViewModel(viewModel: HomeVM) {
        viewModel.run {
            init(this@HomeFragment)
            binding.viewModel = this;
            binding.cartDataVM = cartDataVM
        }
        cartDataVM.initObserveData(requireActivity())
    }

    override fun initView() {
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (CashDrawerHelper.isStartDrawer)
                    CashDrawerHelper.showDrawerNotification(this@HomeFragment.requireActivity());
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this);
            }
        })
        // init fragment page
        fragmentMap[HomePage.Table] = TableFragment();
        fragmentMap[HomePage.Order] = OrderFragment();
        paperAdapter = FragmentPagerAdapter(requireActivity().supportFragmentManager, lifecycle);
        binding.homeViewPager.apply {
            adapter = paperAdapter;
            paperAdapter.submitList(fragmentMap.values)
        }

        binding.toolbarLayout.spnDiningOptionBox.setOnClickListener {

            val diningOptions: MutableList<DiningOption> =
                (DataHelper.orderSettingLocalStorage?.ListDiningOptions as List<DiningOption>).toMutableList();

            diningOptions.forEachIndexed { index, diningOption ->
                if (cartDataVM.diningOptionLD.value?.Id == diningOption.Id) {
                    if (diningOptions.size - 1 <= index) {
                        cartDataVM.diningOptionChange(diningOptions[0])
                    } else {
                        cartDataVM.diningOptionChange(diningOptions[index + 1])
                    }
                    return@setOnClickListener
                }
            }
        }
        initSpinner();
    }

    override fun initData() {

    }

    override fun initAction() {
        NetworkUtils.cancelNetworkCheck()

        cartDataVM.diningOptionLD.observe(this) { diningOption ->
            if (diningOption?.SubDiningOption.isNullOrEmpty()) {
                diningOptionSpinnerAdapter.submitList(mutableListOf())
            } else {
                diningOption?.SubDiningOption?.mapIndexed { index, subDiningOptionItem ->
                    DropDownItem(
                        name = subDiningOptionItem.NickName!!,
                        realItem = subDiningOptionItem,
                        position = index
                    )
                }?.let { listSubDiningOption ->
                    diningOptionSpinnerAdapter.submitList(listSubDiningOption)
                }
            }
        }
        screenViewModel.screenEvent.observe(this) {
            // Check cart initialized
            when (it.screen) {
                HomePage.Order -> {
                    if (cartDataVM.cartModelLD.value == null) {
                        showAlert(
                            message = "Cart has not been initialized!",
                            onClickListener = object : AppAlertDialog.AlertDialogOnClickListener {
                                override fun onPositiveClick() {
                                    screenViewModel.showTablePage()
                                }
                            })
                    } else {
                        binding.toolbarLayout.spnDiningOptionBox.visibility = View.VISIBLE
                    }
                }
                HomePage.Table -> {
                    binding.toolbarLayout.spnDiningOptionBox.visibility = View.INVISIBLE
                }
            }
            binding.toolbarLayout.spinnerMain.setSelection(it.screen.pos);
        }

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
                        else -> {}
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

        //init SubDiningOption
        diningOptionSpinnerAdapter = DiningOptionSpinnerAdapter(fragmentContext)
    }

    private fun switchToPage(page: HomePage?) {
        val listDropdown: MutableList<DropDownItem> = mutableListOf();
        when (page) {
            HomePage.Table -> {
                Log.d("home", "switchPage: page_table");
                binding.homeViewPager.currentItem = 0;
                var i = 1;
                DataHelper.floorLocalStorage?.Floor?.map {
                    DropDownItem(name = it.Name, realItem = it, position = i++)
                }?.let {
                    listDropdown.add(DropDownItem(name = "All", position = 0))
                    listDropdown.addAll(it)
                }
            }
            HomePage.Order -> {
                Log.d("home", "switchPage: page_order")
                binding.homeViewPager.currentItem = 1;
                var i = 0;
                DataHelper.menuLocalStorage?.MenuList?.map {
                    DropDownItem(name = it.Name, realItem = it, position = i++)
                }?.let {
                    listDropdown.addAll(it)
                }

            }
            else -> {}
        }
        subSpinnerAdapter.submitList(listDropdown);
        binding.toolbarLayout.spnGroupBy.setSelection(
            when (page) {
                HomePage.Order -> OrderFragment.selectedSort
                HomePage.Table -> TableFragment.selectedSort
                else -> 0
            }
        )
    }

    override fun openSelectMenu() {
        navigator.goToWithAnimationEnterFromLeft(MenuFragment());
    }

    override fun onFragmentBackPressed() {
        requireActivity().finish()
    }
}