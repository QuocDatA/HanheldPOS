package com.hanheldpos.ui.screens.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.hanheldpos.ui.base.fragment.BaseFragment
import com.hanheldpos.ui.base.viewmodel.BaseViewModel

abstract class BaseMainFragment<T: ViewDataBinding, VM: BaseViewModel>  : BaseFragment<T,VM>() {



}