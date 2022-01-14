package com.hanheldpos.ui.screens.cashdrawer.enddrawer

import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentEndDrawerBinding
import com.hanheldpos.ui.base.fragment.BaseFragment

class EndDrawerFragment : BaseFragment<FragmentEndDrawerBinding, EndDrawerVM>(), EndDrawerUV {
    override fun layoutRes() = R.layout.fragment_end_drawer

    override fun viewModelClass(): Class<EndDrawerVM> {
        return EndDrawerVM::class.java
    }

    override fun initViewModel(viewModel: EndDrawerVM) {
        return viewModel.run {
            init(this@EndDrawerFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {
    }

    override fun initData() {
    }

    override fun initAction() {
    }

    override fun getBack() {
        onFragmentBackPressed()
    }
}