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

    fun initDownloadService(context: Context) {
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
        processDialog.setTitle("Downloading...")
        processDialog.setCancelable(false)
        processDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        processDialog.setButton(
            DialogInterface.BUTTON_NEGATIVE,
            "Cancel",
            DialogInterface.OnClickListener { dialog, _ ->
                dialog.dismiss()
                PRDownloader.cancel(downloadId)
            })
        processDialog.show()
    }

    fun downloadFile(listResources: List<ResourceResp>, listener: DownloadFileCallback) {
        val downloadRequestList: MutableList<DownloadRequest> = mutableListOf()
        var currentDownloadPos = 0
        listResources.forEach { item ->
            val downloadRequest = PRDownloader.download(item.Url, INTERNAL_PATH, item.Name)
                .build()
                .setOnStartOrResumeListener {
                    processDialog.setTitle("Downloaing ${item.Name}")
                }
                .setOnPauseListener {

                }
                .setOnCancelListener {

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
            var isDownloading = true
            var downloadId = 0
            while (isDownloading) {
                downloadId =
                    downloadRequestList[currentDownloadPos].start(object : OnDownloadListener {
                        override fun onDownloadComplete() {
                            currentDownloadPos++
                        }

                        override fun onError(error: com.downloader.Error?) {
                            CoroutineScope(Dispatchers.Main).launch{
                                listener.onFail()
                            }

                        }
                    })
                if (currentDownloadPos == 0) {
                    while (currentDownloadPos == 0) {
                    }
                } else {
                    while (PRDownloader.getStatus(downloadId) == Status.RUNNING) {
                    }
                    if (currentDownloadPos == listResources.size - 1) {
                        isDownloading = false
                        processDialog.dismiss()
                        CoroutineScope(Dispatchers.Main).launch{
                            listener.onComplete()
                        }

                    }
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