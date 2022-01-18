package com.hanheldpos.ui.screens.home.table.customer_input

import android.view.View
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentTableInputBinding
import com.hanheldpos.model.keyboard.KeyBoardType
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.input.KeyBoardVM


class TableInputFragment(
    private val listener : TableInputListener? = null
) : BaseFragment<FragmentTableInputBinding, TableInputVM>(), TableInputUV {

    //ViewModel
    private val keyBoardVM  = KeyBoardVM();

    override fun layoutRes() = R.layout.fragment_table_input

    override fun viewModelClass(): Class<TableInputVM> {
        return TableInputVM::class.java;
    }

    override fun initViewModel(viewModel: TableInputVM) {
        viewModel.run {
            init(this@TableInputFragment);
            binding.viewModel = this;
            binding.keyboardVM = keyBoardVM;
        }

    }

    override fun initView() {
        keyBoardVM.onListener(listener = object : KeyBoardVM.KeyBoardCallBack {
            override fun onComplete() {
                viewModel.onComplete();
            }

            override fun onCancel() {
                viewModel.onCancel();
            }



        });
    }

    override fun initData() {
        keyBoardVM.input.value = "";
        keyBoardVM.keyBoardType.postValue(KeyBoardType.NumberOnly);
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
        if (!keyBoardVM.input.value?.trim().equals(""))
        listener?.onCompleteTable(Integer.valueOf(keyBoardVM.input.value));
    }

}