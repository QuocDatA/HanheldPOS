package com.hanheldpos.ui.screens.cashdrawer.enddrawer

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentEndDrawerBinding
import com.hanheldpos.extension.navigateTo
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.screens.cashdrawer.CashDrawerHelper
import com.hanheldpos.ui.screens.pincode.PinCodeActivity
import com.hanheldpos.ui.screens.welcome.WelcomeActivity

class EndDrawerFragment : BaseFragment<FragmentEndDrawerBinding, EndDrawerVM>(), EndDrawerUV {
    override fun layoutRes() = R.layout.fragment_end_drawer

    override fun viewModelClass(): Class<EndDrawerVM> {
        return EndDrawerVM::class.java
    }

    override fun initViewModel(viewModel: EndDrawerVM) {
        viewModel.run {
            init(this@EndDrawerFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {
        binding.btnEndDrawer.setOnClickListener {
            activity?.navigateTo(
                PinCodeActivity::class.java,
                alsoFinishCurrentActivity = true,
                alsoClearActivity = true,
            )
            CashDrawerHelper.isEndDrawer = true
        }
        binding.amountInput.doAfterTextChanged {
            binding.isActive = binding.amountInput.text.toString().toInt() > 1000
        }
    }

    override fun initData() {
    }

    override fun initAction() {
    }

    override fun getBack() {
        onFragmentBackPressed()
    }
}