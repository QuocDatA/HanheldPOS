package com.hanheldpos.ui.screens.cart.customer

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.view.View
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentAddNewCustomerBinding
import com.hanheldpos.ui.base.fragment.BaseFragment
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS

class AddNewCustomerFragment : BaseFragment<FragmentAddNewCustomerBinding, AddNewCustomerVM>(),
    AddNewCustomerUV {
    override fun layoutRes(): Int = R.layout.fragment_add_new_customer

    override fun viewModelClass(): Class<AddNewCustomerVM> {
        return AddNewCustomerVM::class.java;
    }

    override fun initViewModel(viewModel: AddNewCustomerVM) {
        viewModel.run {
            init(this@AddNewCustomerFragment);
            binding.viewModel = this
        }
        binding.birthDayTextInputLayout.setEndIconOnClickListener(View.OnClickListener {
            onDateTimePick()
        })
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

    @SuppressLint("SetTextI18n")
    override fun onDateTimePick() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this.requireActivity(),R.style.CustomDatePickerDialogTheme, DatePickerDialog.OnDateSetListener { view, yearOfPick, monthOfPick, dayOfPick ->

            // Display Selected date in textbox
            val monthPicked = monthOfPick+1
            binding.birthDayInput.setText("$dayOfPick/$monthPicked/$yearOfPick")

        }, year, month, day)

        datePickerDialog.show()
    }
}