package com.hanheldpos.ui.screens.cashdrawer.startdrawer

import androidx.core.widget.doAfterTextChanged
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentStartDrawerBinding
import com.hanheldpos.model.keyboard.KeyBoardType
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.devicecode.DeviceCodeFragment
import com.hanheldpos.ui.screens.home.HomeFragment
import com.hanheldpos.ui.screens.input.KeyBoardVM
import com.hanheldpos.ui.screens.main.MainActivity
import com.hanheldpos.utils.PriceHelper

class StartDrawerFragment : BaseFragment<FragmentStartDrawerBinding, StartDrawerVM>(), StartDrawerUV {


    override fun layoutRes(): Int = R.layout.fragment_start_drawer;

    private val keyBoardVM = KeyBoardVM();

    override fun initViewModel(viewModel: StartDrawerVM) {
        viewModel.run {
            init(this@StartDrawerFragment);
            binding.viewModel = this;
        }
        binding.keyboardVM = keyBoardVM;

    }

    override fun initView() {
        binding.startingCash.let { input ->
            var isEditing = false
            input.doAfterTextChanged {
                if (isEditing) return@doAfterTextChanged;
                if (it.toString().isEmpty()) input.setText("0");
                else {
                    isEditing = true;
                    input.setText(PriceHelper.formatStringPrice(it.toString()));

                }
                input.setSelection(input.length());
                isEditing = false;
            }
        }


    }

    override fun initData() {
        keyBoardVM.input.observe(this) {
            viewModel.amountString.value = it;
        };
        keyBoardVM.keyBoardType.postValue(KeyBoardType.NumberOnly)
    }

    override fun initAction() {
        keyBoardVM.listener = object : KeyBoardVM.KeyBoardCallBack {
            override fun onComplete() {
                viewModel.startDrawer(requireContext())
            }

            override fun onCancel() {

            }

        }
        binding.btnStartDrawer.setOnClickListener {
            viewModel.startDrawer(requireContext());
        }
    }

    override fun viewModelClass(): Class<StartDrawerVM> {
        return StartDrawerVM::class.java;
    }

    override fun onFragmentBackPressed() {
        requireActivity().finish()
    }

    override fun goHome() {
        navigator.goTo(HomeFragment())
    }
}