package com.hanheldpos.ui.screens.home.table.customer_input

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentTableInputBinding
import com.hanheldpos.model.keyboard.KeyBoardType
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.input.KeyBoardVM
import com.hanheldpos.utils.PriceUtils


class TableInputFragment(
    private val listener: TableInputListener? = null
) : BaseFragment<FragmentTableInputBinding, TableInputVM>(), TableInputUV {

    //ViewModel
    private val keyBoardVM = KeyBoardVM(KeyBoardType.NumberOnly);

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
        keyBoardVM.onListener(
            this,
            binding.numberCustomer,
            listener = object : KeyBoardVM.KeyBoardCallBack {
                override fun onComplete() {
                    viewModel.onComplete();
                }

                override fun onCancel() {
                    viewModel.onCancel();
                }
            });

    }

    override fun initData() {

    }

    override fun initAction() {
        binding.numberCustomer.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    viewModel.numberCustomer = 0
                    return
                }
                binding.numberCustomer.removeTextChangedListener(this)
                viewModel.numberCustomer = s.replace(Regex("[,]"), "").toIntOrNull() ?: 1000000000
                binding.numberCustomer.setText(viewModel.numberCustomer.toString())
                binding.numberCustomer.addTextChangedListener(this)
            }

        })
    }

    interface TableInputListener {
        fun onCompleteTable(numberCustomer: Int)
    }

    override fun onCancel() {
        navigator.goOneBack();
    }

    override fun onComplete() {
        navigator.goOneBack();
        if (viewModel.numberCustomer > 0)
            listener?.onCompleteTable(Integer.valueOf(viewModel.numberCustomer))
    }

}