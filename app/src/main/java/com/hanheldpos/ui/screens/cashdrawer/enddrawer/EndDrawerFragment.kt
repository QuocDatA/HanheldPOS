package com.hanheldpos.ui.screens.cashdrawer.enddrawer

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
        return viewModel.run {
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
    }

    override fun initData() {
    }

    override fun initAction() {
    }

    override fun getBack() {
        onFragmentBackPressed()
    }
}