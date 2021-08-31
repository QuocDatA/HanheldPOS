package com.hanheldpos.ui.screens.home

import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentHomeBinding
import com.hanheldpos.ui.base.pager.FragmentPagerAdapter
import com.hanheldpos.ui.screens.home.order.OrderDataVM
import com.hanheldpos.ui.screens.main.BaseMainFragment
import com.hanheldpos.ui.screens.home.order.OrderFragment
import com.hanheldpos.ui.screens.home.table.TableFragment
import com.hanheldpos.ui.screens.main.adapter.TabSpinnerAdapter
import com.hanheldpos.ui.screens.home.order.PriceItem
import com.hanheldpos.ui.screens.home.table.TableDataVM
import com.hanheldpos.ui.screens.main.adapter.SubSpinnerAdapter


class HomeFragment : BaseMainFragment<FragmentHomeBinding, HomeVM>(), HomeUV {

    private val fragmentMap: MutableMap<HomePage, Fragment> = mutableMapOf()


    enum class HomePage(val pos: Int, val textId: Int) {
        Table(0, R.string.table),
        Menu(1, R.string.menu);
    }

    private val tableDataViewModel by activityViewModels<TableDataVM>()
    private val orderDataViewModel by activityViewModels<OrderDataVM>()
    private val screenViewModel by activityViewModels<ScreenViewModel>()

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
        fragmentMap[HomePage.Menu] = OrderFragment();



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

        screenViewModel.screenEvent.observe(this,{
            binding.toolbarLayout.spinnerMain.setSelection(it.screen.pos);
        })

        binding.toolbarLayout.spinnerMain.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val item = parent?.getItemAtPosition(position) as HomePage;
                    switchToPage(item);
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}

            }
        binding.toolbarLayout.spinnerMain.setSelection(0);
    }

    private fun initSpinner() {
        val tabSpinnerAdapter = TabSpinnerAdapter(fragmentContext)
        tabSpinnerAdapter.submitList(HomePage.values().toMutableList())

        binding.toolbarLayout.spinnerMain.adapter = tabSpinnerAdapter

        subSpinnerAdapter = SubSpinnerAdapter(requireContext());
        binding.toolbarLayout.spnGroupBy.adapter = subSpinnerAdapter;
    }

    private fun switchToPage(page: HomePage?) {
        when (page) {
            HomePage.Table -> {
                Log.d("home", "switchPage: page_table");
                binding.homeViewPager.currentItem = 0;
                subSpinnerAdapter.submitList(mutableListOf(PriceItem(name = "Group By")))

            }

            HomePage.Menu -> {
                Log.d("home", "switchPage: page_order")
                binding.homeViewPager.currentItem = 1;
                subSpinnerAdapter.submitList(mutableListOf(PriceItem(name = "Price List")));
            }

        }
    }

}