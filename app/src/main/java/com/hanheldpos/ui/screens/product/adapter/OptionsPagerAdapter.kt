package com.hanheldpos.ui.screens.product.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hanheldpos.ui.screens.product.options.modifier.ModifierFragment
import com.hanheldpos.ui.screens.product.options.variant.VariantFragment

class OptionsPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2;
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                VariantFragment();
            }
            1 -> {
                ModifierFragment();
            }
            else -> {
                Fragment();
            }
        }
    }
}