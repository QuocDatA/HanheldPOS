package com.hanheldpos.ui.screens.home.table

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentTableBinding
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel


class TableFragment : BaseFragment<FragmentTableBinding,TableVM>(), TableUV {
    override fun layoutRes() = R.layout.fragment_table;

    override fun viewModelClass(): Class<TableVM> {
        return TableVM::class.java;
    }

    override fun initViewModel(viewModel: TableVM) {
        viewModel.run {
            init(this@TableFragment)
        }
    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun initAction() {

    }

}