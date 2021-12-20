package com.hanheldpos.ui.screens.home.table.input

import androidx.fragment.app.activityViewModels
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentTableInputBinding
import com.hanheldpos.ui.base.fragment.BaseFragment


class TableInputFragment(
    private val listener : TableInputListener? = null
) : BaseFragment<FragmentTableInputBinding,TableInputVM>(),TableInputUV {

    //ViewModel
    private val numberPadVM  = NumberPadVM();

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
        }

    }

    override fun initView() {
        numberPadVM.onListener(listener = object : NumberPadVM.NumberPadCallBack{
            override fun onComplete() {
                viewModel.onComplete();
            }

            override fun onCancel() {
                viewModel.onCancel();
            }
        });
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