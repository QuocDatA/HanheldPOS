package com.hanheldpos.ui.base.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.hanheldpos.ui.base.activity.BaseActivity
import com.hanheldpos.ui.base.activity.BaseFragmentBindingActivity
import com.hanheldpos.ui.base.dialog.AppAlertDialog
import com.hanheldpos.ui.base.viewmodel.BaseViewModel
import com.hanheldpos.ui.base.viewmodel.ViewState

abstract class BaseFragment<T : ViewDataBinding, VM : BaseViewModel> : Fragment() {

    protected lateinit var fragmentContext: Context

    // Binding
    protected lateinit var binding: T

    @LayoutRes
    protected abstract fun layoutRes(): Int

    // ViewModel
    protected lateinit var viewModel: VM
    protected abstract fun viewModelClass(): Class<VM>
    protected abstract fun initViewModel(viewModel: VM)

    // Data & Actions
    protected abstract fun initView()
    protected abstract fun initData()
    protected abstract fun initAction()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(fragmentContext),
            layoutRes(),
            container,
            false
        )
        binding.apply {
            root.setOnTouchListener { v: View, _: MotionEvent? ->
                v.isClickable = true
                v.isFocusable = true
                false
            }
            lifecycleOwner = this@BaseFragment
        }

        // ViewModel
        viewModel = ViewModelProvider(this).get(viewModelClass())
            .apply {
                initViewModel(this)
            }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initData()
        initAction()

        viewModel.viewState.observe(viewLifecycleOwner, { viewState ->
            viewState?.run {
                when (viewState) {
                    ViewState.SHOW_LOADING -> showLoading(true)
                    ViewState.HIDE_LOADING -> showLoading(false)
                    ViewState.SHOW_ERROR -> viewModel.errMessage?.run {
                        showMessage(this)
                    }
                }
            }
        })
    }

    val navigator: FragmentNavigator
        get() = (fragmentContext as BaseFragmentBindingActivity<*, *>?)!!.getNavigator()

    open fun onFragmentBackPressed() {
        navigator.goOneBack()
    }

    /**
     * Show loading dialog
     */
    fun showLoading(show: Boolean) {
        (activity as? BaseActivity<*, *>)?.showLoading(show)
    }

    /**
     * Show error dialog
     */
    private fun showError() {
        (activity as? BaseActivity<*, *>)?.showError()
    }

    /**
     * Show error dialog
     */
    fun showMessage(message: String?) {
        showAlert(message = message)
    }

    /**
     * Show alert dialog with full options
     * ------------------------------------------
     * | Title                                  |
     * | Message                                |
     * ------NEGATIVE BUTTON---POSITIVE BUTTON---
     */
    fun showAlert(
        message: String?,
        positiveText: String? = null,
        negativeText: String? = null,
        onClickListener: AppAlertDialog.AlertDialogOnClickListener? = null
    ) {
        (activity as? BaseActivity<*, *>)?.showAlert(
            message,
            positiveText,
            negativeText,
            onClickListener
        )
    }

    /**
     * Request permission
     */
    fun requestPermission(
        explainPermission: String,
        permissions: List<String>,
        callback: BaseActivity.RequestPermissionCallback
    ) {
        (activity as? BaseActivity<*, *>)?.requestPermission(
            explainPermission,
            permissions,
            callback
        )
    }

    /**
     * Check if fragment is already in ContainerView then show fragment
     * else add new one to
     *
     * @param containerViewId
     * @param fragment
     */
    protected open fun showOrAddFragment(
        @IdRes containerViewId: Int,
        fragment: Fragment?
    ) {
        showOrAddFragment(null, containerViewId, fragment)
    }

    /**
     * Check if fragment is already in ContainerView then show fragment
     * else add new one to
     *
     * @param containerViewId
     * @param fragment
     */
    protected open fun showOrAddFragment(
        fragmentManager: FragmentManager?,
        @IdRes containerViewId: Int,
        fragment: Fragment?
    ) {
        var fragmentManager = fragmentManager
        if (fragment == null) return
        if (fragmentManager == null) fragmentManager = childFragmentManager
        val fragmentTag = getFragmentTag(fragment)
        val fragmentByTag = fragmentManager.findFragmentByTag(fragmentTag)
        if (fragmentByTag != null) {
            //if the fragment exists, show it.
            fragmentManager.beginTransaction().show(fragmentByTag).commit()
        } else {
            //if the fragment does not exist, add it to fragment manager.
            fragmentManager.beginTransaction().add(containerViewId, fragment, fragmentTag).commit()
        }
    }

    protected open fun hideFragment(
        fragment: Fragment?
    ) {
        if (fragment == null) return
        val fragmentManager = childFragmentManager
        val fragmentTag = getFragmentTag(fragment)
        if (fragmentManager.findFragmentByTag(fragmentTag) != null) {
            //if the other fragment is visible, hide it.
            fragmentManager.beginTransaction().hide(fragment).commit()
        }
    }

    private fun getFragmentTag(fragment: Fragment): String {
        return fragment.javaClass.simpleName
    }


}