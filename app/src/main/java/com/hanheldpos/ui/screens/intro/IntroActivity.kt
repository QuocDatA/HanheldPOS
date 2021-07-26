package com.hanheldpos.ui.screens.intro

import com.hanheldpos.R
import com.hanheldpos.databinding.ActivityIntroBinding
import com.hanheldpos.extension.navigateTo
import com.hanheldpos.ui.base.activity.BaseActivity
import com.hanheldpos.ui.screens.main.MainActivity

class IntroActivity : BaseActivity<ActivityIntroBinding, IntroVM>(), IntroUV {

    override fun layoutRes() = R.layout.activity_intro

    override fun viewModelClass() = IntroVM::class.java

    override fun initViewModel(viewModel: IntroVM) {
        viewModel.run {
            init(this@IntroActivity)
            initLifeCycle(this@IntroActivity)
        }
    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun initAction() {

    }

    override fun finishIntro() {
        navigateTo(MainActivity::class.java, true)
    }
}