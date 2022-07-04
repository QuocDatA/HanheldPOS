package com.hanheldpos.ui.screens.scanner

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Log
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import com.hanheldpos.R
import com.hanheldpos.databinding.FragmentScanQrCodeBinding
import com.hanheldpos.ui.base.activity.BaseActivity
import com.hanheldpos.ui.base.fragment.BaseFragment
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class ScanQrCodeFragment(private val onSuccess : (data : String?) -> Unit) : BaseFragment<FragmentScanQrCodeBinding, ScanQrCodeVM>(), ScanQrCodeUV {
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var analyzer: ImageAnalyzer
    override fun layoutRes(): Int {
        return R.layout.fragment_scan_qr_code
    }

    override fun viewModelClass(): Class<ScanQrCodeVM> {
        return ScanQrCodeVM::class.java
    }

    override fun initViewModel(viewModel: ScanQrCodeVM) {
        viewModel.run {
            init(this@ScanQrCodeFragment)
            binding.viewModel = this
        }
    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun initAction() {
        checkCameraPermission()
    }

    private fun checkCameraPermission() {

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission(
                "",
                arrayOf(Manifest.permission.CAMERA).toList(),
                callback = object : BaseActivity.RequestPermissionCallback {
                    override fun onPermissionGranted() {
                        initializeCamera()
                    }

                    override fun onPermissionDenied() {
                        onFragmentBackPressed()
                    }
                })
        } else {
            initializeCamera()
        }
    }

    private fun initializeCamera() {
        var isAlreadySuccess = false
        analyzer = ImageAnalyzer(object : ImageAnalyzer.ImageAnalyzerCallback{
            override fun onSuccess(value: String?) {
                if (isAlreadySuccess) return
                isAlreadySuccess = true
                Log.d("Read Barcode", value.toString())
                onFragmentBackPressed()
                onSuccess.invoke(value)
            }
        })
        cameraExecutor = Executors.newSingleThreadExecutor()
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    private fun bindPreview(cameraProvider: ProcessCameraProvider?) {

        val preview: Preview = Preview.Builder()
            .build()
        val cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
        preview.setSurfaceProvider(binding.previewView.surfaceProvider)
        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(1280, 720))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
        imageAnalysis.setAnalyzer(cameraExecutor, analyzer)
        cameraProvider?.bindToLifecycle(
            this as LifecycleOwner,
            cameraSelector, imageAnalysis, preview
        )

    }

}