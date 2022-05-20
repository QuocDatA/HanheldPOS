package com.hanheldpos.ui.screens.cashdrawer.startdrawer

import android.text.Editable
import android.text.TextWatcher
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentStartDrawerBinding
import com.hanheldpos.extension.setOnClickDebounce
import com.hanheldpos.model.keyboard.KeyBoardType
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.home.HomeFragment
import com.hanheldpos.ui.screens.input.KeyBoardVM
import com.hanheldpos.utils.PriceUtils

class StartDrawerFragment : BaseFragment<FragmentStartDrawerBinding, StartDrawerVM>(),
    StartDrawerUV {


    override fun layoutRes(): Int = R.layout.fragment_start_drawer;

    private val keyBoardVM = KeyBoardVM(KeyBoardType.NumberOnly,15);

    override fun initViewModel(viewModel: StartDrawerVM) {
        viewModel.run {
            init(this@StartDrawerFragment);
            binding.viewModel = this;
        }
        binding.keyboardVM = keyBoardVM;

    }

    override fun initView() {
        binding.startingCash.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    viewModel.amount = 0.0
                    return
                }
                binding.startingCash.removeTextChangedListener(this)
                viewModel.amount =  s.replace(Regex("[,]"), "").toDouble()
                binding.startingCash.setText(PriceUtils.formatStringPrice(viewModel.amount))
                binding.startingCash.addTextChangedListener(this)
            }

        })


    }

    override fun initData() {
    }

    override fun initAction() {
        keyBoardVM.onListener(
            this,
            binding.startingCash,
            initInput = PriceUtils.formatStringPrice(viewModel.amount),
            listener = object : KeyBoardVM.KeyBoardCallBack {
                override fun onComplete() {
                    viewModel.startDrawer(requireContext())
                }

                override fun onCancel() {
                    onFragmentBackPressed()
                }

            })
        binding.btnStartDrawer.setOnClickDebounce {
            viewModel.startDrawer(requireContext());
        }
    }

    override fun viewModelClass(): Class<StartDrawerVM> {
        return StartDrawerVM::class.java;
    }

    override fun goHome() {
        navigator.goTo(HomeFragment())
    }
}