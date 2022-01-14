package com.hanheldpos.ui.screens.menu.option.report.drawer.end_drawer

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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