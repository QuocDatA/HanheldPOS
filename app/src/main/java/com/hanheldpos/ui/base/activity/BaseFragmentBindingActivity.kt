package com.hanheldpos.ui.base.activity

import androidx.databinding.ViewDataBinding
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.base.fragment.FragmentNavigator
import com.hanheldpos.ui.base.viewmodel.BaseViewModel

abstract class BaseFragmentBindingActivity<T : ViewDataBinding, VM : BaseViewModel> :
    BaseActivity<T, VM>() {

    protected abstract fun createFragmentNavigator(): FragmentNavigator

    private var navigator: FragmentNavigator? = null

    fun getNavigator(): FragmentNavigator {
        if (navigator == null) {
            navigator = createFragmentNavigator()
        }
        return navigator!!
    }

    override fun onBackPressed() {
        navigator?.let {
            if (it.size > 0) {
                if (navigator?.activeFragment is BaseFragment<*, *>) {
                    (navigator?.activeFragment as BaseFragment<*, *>).onFragmentBackPressed()
                }
                return
            }
        }
        super.onBackPressed()
    }
}