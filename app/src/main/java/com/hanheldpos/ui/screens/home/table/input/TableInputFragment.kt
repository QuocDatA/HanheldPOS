package com.hanheldpos.ui.screens.home.table.input

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentTableBinding
import com.hanheldpos.databinding.FragmentTableInputBinding
import com.hanheldpos.model.product.ProductCompleteModel
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.product.ProductDetailFragment


class TableInputFragment(
    private val listener : TableInputListener? = null
) : BaseFragment<FragmentTableInputBinding,TableInputVM>(),TableInputUV {

    //ViewModel
    private val numberPadVM by activityViewModels<NumberPadVM>()

    override fun layoutRes() = R.layout.fragment_table_input

    override fun viewModelClass(): Class<TableInputVM> {
        return TableInputVM::class.java;
    }

    override fun initViewModel(viewModel: TableInputVM) {
        viewModel.run {
            init(this@TableInputFragment);
            binding.viewModel = this;
            binding.numberPadVM = numberPadVM;
            binding.numberPad.viewModel = numberPadVM;
            binding.numberPad.tableInputVM = this;
        }

    }

    override fun initView() {

    }

    override fun initData() {
        numberPadVM.input.value = "";
    }

    override fun initAction() {

    }

    interface TableInputListener {
        fun onCompleteTable(numberCustomer : Int)
    }

    companion object{
        fun getInstance(
            listener: TableInputListener? = null
        ): TableInputFragment {
            return TableInputFragment(
                listener = listener
            ).apply {

            };
        }
    }

    override fun onCancel() {
        navigator.goOneBack();
    }

    override fun onComplete() {
        navigator.goOneBack();
        if (!numberPadVM.input.value?.trim().equals(""))
        listener?.onCompleteTable(Integer.valueOf(numberPadVM.input.value));
    }

}