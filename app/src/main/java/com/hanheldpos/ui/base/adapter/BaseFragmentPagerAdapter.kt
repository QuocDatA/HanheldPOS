package com.hanheldpos.ui.base.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.*

class BaseFragmentPagerAdapter(fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_SET_USER_VISIBLE_HINT) {
    private val fragmentList: MutableList<Fragment>? = ArrayList()
    private val fragmentTitleList: MutableList<String>? = ArrayList()

    /**
     * Add fragment to list
     *
     * @param fragment
     */
    fun addFragment(fragment: Fragment) {
        fragmentList!!.add(fragment)
    }

    /**
     * Add fragment & title to list
     */
    fun addFragment(fragment: Fragment, title: String) {
        fragmentList!!.add(fragment)
        fragmentTitleList!!.add(title)
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList!![position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitleList?.get(position)
    }

    override fun getCount(): Int {
        return fragmentList?.size ?: 0
    }
}