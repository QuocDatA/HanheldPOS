package com.hanheldpos.model

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Environment
import androidx.core.content.ContextCompat
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import com.downloader.Status
import com.downloader.request.DownloadRequest
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.resource.ResourceResp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.*


object DownloadService {
    private final var INTERNAL_PATH =
        Environment.getDataDirectory().path + "/data/com.hanheldpos/local/"
    private var downloadId: Int = 0
    lateinit var processDialog: ProgressDialog

    private fun initDownloadService(context: Context) {
        val config = PRDownloaderConfig.newBuilder()
            .setDatabaseEnabled(true)
            .setReadTimeout(30000)
            .setConnectTimeout(30000)
            .build()
        PRDownloader.initialize(context, config)
        initDialog(context)
    }

    private fun initDialog(context: Context) {
        processDialog = ProgressDialog(context)
        with(processDialog) {
            setTitle("Downloading...")
            setCancelable(false)
            setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            setButton(
                DialogInterface.BUTTON_NEGATIVE,
                context.getString(R.string.cancel)
            ) { dialog, _ ->
                PRDownloader.cancel(downloadId)
                dialog.dismiss()
            }
            show()
        }
    }

    fun downloadFile(
        context: Context,
        listResources: List<ResourceResp>,
        listener: DownloadFileCallback
    ) {
        var isDownloading = true
        val downloadRequestList: MutableList<DownloadRequest> = mutableListOf()
        var currentDownloadPos = 0
        initDownloadService(context)
        listResources.forEach { item ->
            val downloadRequest = PRDownloader.download(item.Url, INTERNAL_PATH, item.Name)
                .build()
                .setOnStartOrResumeListener {
                    processDialog.setTitle("Downloading \n${item.Name}")
                }
                .setOnPauseListener {
                    isDownloading = false
                    listener.onPause()
                }
                .setOnCancelListener {
                    isDownloading = false
                    listener.onCancel()
                }
                .setOnProgressListener { progress ->
                    val progressPercent: Long = progress.currentBytes * 100 / progress.totalBytes
                    processDialog.progress = progressPercent.toInt()
                    processDialog.setMessage(
                        progress.currentBytes.toString() + "/" +
                                progress.totalBytes.toString()

                    )
                }
            downloadRequestList.add(downloadRequest)
        }
        CoroutineScope(Dispatchers.IO).launch {
            while (isDownloading) {
                downloadId =
                    downloadRequestList[currentDownloadPos].start(object : OnDownloadListener {
                        override fun onDownloadComplete() {

                        }

                        override fun onError(error: com.downloader.Error?) {
                            CoroutineScope(Dispatchers.Main).launch {
                                listener.onFail()
                            }

                        }
                        
                    })
                while (PRDownloader.getStatus(downloadId) in mutableListOf(Status.QUEUED,Status.RUNNING)) { }
                currentDownloadPos++
                if (PRDownloader.getStatus(downloadId) in mutableListOf(Status.CANCELLED,Status.FAILED,Status.PAUSED)) return@launch
                if (isDownloading && currentDownloadPos >= listResources.size) {
                    isDownloading = false
                    processDialog.dismiss()
                    CoroutineScope(Dispatchers.Main).launch {
                        listener.onComplete()
                    }
                    return@launch
                }
            }
        }

    }

    private fun checkForPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun checkFileExist(filePath: String): Boolean {
        val file = File(INTERNAL_PATH + filePath)
        if (file.exists()) {
            return true
        }
        return false
    }

    private fun toMegaByte(bytes: Long): String {
        return String.format(Locale.ENGLISH, "%.2fMB", bytes / (1024.00 * 1024.00))
    }

    interface DownloadFileCallback {
        fun onDownloadStartOrResume()
        fun onPause()
        fun onCancel()
        fun onFail()
        fun onComplete()
    }
}