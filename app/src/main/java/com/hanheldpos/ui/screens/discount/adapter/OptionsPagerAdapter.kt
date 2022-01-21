package com.hanheldpos.ui.screens.discount.adapter

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class OptionsPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    private val fragmentList = mutableListOf<Fragment>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(collection: Collection<Fragment>?) {
        fragmentList.clear()
        collection?.let {
            fragmentList.addAll(it)
        }
        notifyDataSetChanged()
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

    override fun getItemId(position: Int): Long {
        return fragmentList[position].hashCode().toLong() // make sure notifyDataSetChanged() works
    }
}