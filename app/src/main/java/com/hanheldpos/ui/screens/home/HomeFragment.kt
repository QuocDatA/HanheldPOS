package com.hanheldpos.ui.screens.home

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
import com.hanheldpos.ui.screens.home.order.OrderFragment
import com.hanheldpos.ui.screens.home.table.TableFragment
import com.hanheldpos.ui.screens.main.adapter.TabSpinnerAdapter


class HomeFragment : BaseFragment<FragmentHomeBinding, HomeVM>(), HomeUV {

    private val fragmentMap: MutableMap<HomePage, Fragment> = mutableMapOf()

    enum class HomePage(val pos: Int, val textId: Int) {
        Table(0, R.string.table),
        Order(2, R.string.order);
    }



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

        page?.let {
            fragmentMap.entries.forEach {
                if (it.key == page) {
                    showOrAddFragment(R.id.fragmentContainer,it.value);
                } else {
                    hideFragment(it.value);
                }
            }
        }

        when (page) {

            HomePage.Order -> {

            }
            HomePage.Table -> {

            }
        }
    }
}