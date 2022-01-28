package com.hanheldpos.ui.screens.menu.option.report.sale.reports.adapter

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hanheldpos.ui.base.adapter.BaseFragmentPagerAdapter

class ReportOptionPageAdapter(fragmentManager: FragmentManager,
                              lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    private val fragmentList = mutableListOf<Fragment>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(collection: Collection<Fragment>?) {
        fragmentList.clear()
        collection?.let {
            fragmentList.addAll(it)
        }
        notifyDataSetChanged();
    }

    /**
     * Add fragment to list
     */
    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}