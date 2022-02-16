package com.hanheldpos.ui.screens.root

import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentRootBinding
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.main.MainVM

class RootFragment : BaseFragment<FragmentRootBinding, RootVM>(),
    RootUV {

    override fun layoutRes() = R.layout.fragment_root

    override fun viewModelClass(): Class<RootVM> {
        return RootVM::class.java
    }

    override fun initViewModel(viewModel: RootVM) {
        viewModel.run {
            init(this@RootFragment)
            binding.vm = this
        }
    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun initAction() {

    }

}