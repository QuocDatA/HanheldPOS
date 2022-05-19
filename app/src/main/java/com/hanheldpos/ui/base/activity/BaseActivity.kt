package com.hanheldpos.ui.base.activity

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.google.android.material.textfield.TextInputEditText
import com.hanheldpos.R
import com.hanheldpos.ui.base.dialog.AppAlertDialog
import com.hanheldpos.ui.base.dialog.AppFunctionDialog
import com.hanheldpos.ui.base.dialog.AppLoadingDialog
import com.hanheldpos.ui.base.viewmodel.BaseViewModel
import com.hanheldpos.ui.base.viewmodel.ViewState
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.coroutines.*

abstract class BaseActivity<T : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity(),
    LifecycleObserver {

    protected lateinit var context: Context

    // Binding
    protected lateinit var binding: T

    @LayoutRes
    protected abstract fun layoutRes(): Int
    protected abstract fun initView()
    protected abstract fun initData()
    protected abstract fun initAction()

    // ViewModel
    protected lateinit var viewModel: VM
    protected abstract fun viewModelClass(): Class<VM>
    protected abstract fun initViewModel(viewModel: VM)

    // Dialog
    private var needShowLoading = false

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this

        // Add LifeCycle observer
        lifecycle.addObserver(this)

        // DataBinding
        binding = DataBindingUtil.setContentView(this, layoutRes())
        binding.lifecycleOwner = this

        // ViewModel
        viewModel = ViewModelProvider(this).get(viewModelClass())
            .also { _viewModel ->
                initViewModel(_viewModel)
                _viewModel.viewState.observe(this, { viewState ->
                    when (viewState) {
                        ViewState.SHOW_LOADING -> showLoading(true)
                        ViewState.HIDE_LOADING -> showLoading(false)
                        ViewState.SHOW_ERROR -> _viewModel.errMessage?.run {
                            showMessage(this)
                        }
                    }
                })
            }

        // Init
        initView()
        initData()
        initAction()
    }

    override fun onStart() {
        super.onStart()

        // Dialog
        AppAlertDialog.get().init(this)
        AppLoadingDialog.get().init(this)
        AppFunctionDialog.get().init(this)
    }

    /**
     * Hide keyboard when click outside EditText
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            try {
                val currFocus = currentFocus
                if (currFocus is TextInputEditText) {
                    val outRect = Rect()
                    currFocus.getGlobalVisibleRect(outRect)
                    if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                            .hideSoftInputFromWindow(currFocus.windowToken, 0)
                    }
                }
            } catch (ignore: Exception) {
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * Show loading dialog
     */
    fun showLoading(show: Boolean) {
        dismissDialog()
        needShowLoading = show
        if (show) {
            CoroutineScope(Dispatchers.IO)
                .launch {
                    withContext(Dispatchers.Main) {
                        if (needShowLoading) {
                            AppLoadingDialog.get().show()
                        }
                    }
                }
        }
    }

    /**
     * Show error dialog
     */
    fun showError() {
        showMessage(getString(R.string.alert_msg_an_error_has_occurred))
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
        title: String? = null,
        message: String?,
        positiveText: String? = null,
        negativeText: String? = null,
        onClickListener: AppAlertDialog.AlertDialogOnClickListener? = null
    ) {

        dismissDialog()
        AppAlertDialog.get().show(
            title,
            message,
            positiveText,
            negativeText,
            onClickListener
        )
    }

    /**
     * Dismiss dialog
     */
    fun dismissDialog() {
        needShowLoading = false
        AppLoadingDialog.get().dismiss()
        AppAlertDialog.get().dismiss()
    }

    /**
     * Request permission
     */
    fun requestPermission(
        explainPermission: String,
        permissions: List<String>,
        callback: RequestPermissionCallback
    ) {
        Dexter.withContext(this)
            .withPermissions(permissions)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        callback.onPermissionGranted()
                    } else {
                        showExplainPermissionDialog(explainPermission, callback)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissionList: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

            })
            .check()
    }

    private fun showExplainPermissionDialog(
        explainPermission: String,
        callback: RequestPermissionCallback
    ) {
        showAlert(
            null,
            explainPermission,
            getString(R.string.alert_btn_go_to_settings),
            getString(R.string.alert_btn_negative),
            object : AppAlertDialog.AlertDialogOnClickListener {
                override fun onPositiveClick() {
                    goToAppSettings()
                }

                override fun onNegativeClick() {
                    callback.onPermissionDenied()
                }
            }
        )
    }

    private fun goToAppSettings() {
        val uri = Uri.fromParts("package", packageName, null)
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            .apply {
                data = uri
            }
        startActivity(intent)
    }

    interface RequestPermissionCallback {
        fun onPermissionGranted()
        fun onPermissionDenied()
    }
}