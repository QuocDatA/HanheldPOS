package com.hanheldpos.ui.screens.main.home

import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import android.widget.TableLayout
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentHomeBinding
import com.hanheldpos.ui.base.activity.BaseFragmentBindingActivity
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.base.fragment.FragmentNavigator
import com.hanheldpos.ui.base.pager.FragmentPagerAdapter
import com.hanheldpos.ui.screens.main.BaseMainFragment
import com.hanheldpos.ui.screens.main.home.order.OrderFragment
import com.hanheldpos.ui.screens.main.home.table.TableFragment
import com.hanheldpos.ui.screens.main.adapter.TabSpinnerAdapter


class HomeFragment : BaseMainFragment<FragmentHomeBinding, HomeVM>(), HomeUV {

    private val fragmentMap: MutableMap<HomePage, Fragment> = mutableMapOf()


    enum class HomePage(val pos: Int, val textId: Int) {
        Table(0, R.string.table),
        Menu(2, R.string.menu);
    }

    //properties
    private lateinit var paperAdapter: FragmentPagerAdapter

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


        paperAdapter = FragmentPagerAdapter(requireActivity().supportFragmentManager,lifecycle);



        binding.homeViewPager.apply {
            adapter = paperAdapter;
            paperAdapter.submitList(fragmentMap.values)
        }



        initSpinner();
    }


    override fun initData() {


    }

    override fun initAction() {
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
    }

    private fun initSpinner() {
        val tabSpinnerAdapter = TabSpinnerAdapter(fragmentContext)
        tabSpinnerAdapter.submitList(HomePage.values().toMutableList())
        binding.toolbarLayout.spinnerMain.adapter = tabSpinnerAdapter


    }

    private fun switchToPage(page: HomePage?) {
        when (page) {

            HomePage.Table -> {
                Log.d("home", "switchPage: page_table");
                binding.homeViewPager.currentItem = 0;
            }

            HomePage.Menu -> {
                Log.d("home","switchPage: page_order")
                binding.homeViewPager.currentItem = 1;
            }

        }
    }

}