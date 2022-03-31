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
import java.io.*
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


object DownloadService {
    private final var INTERNAL_PATH =
        Environment.getDataDirectory().path + "/data/com.hanheldpos/local/"
    private var downloadId: Int = 0
    private var listFileToExtract: MutableList<String> = mutableListOf()
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
            if (item.Name.contains(".zip"))
                listFileToExtract.add(item.Name)
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
                        CoroutineScope(Dispatchers.IO).launch {
                            listFileToExtract.forEach { file ->
                                unpackZip(INTERNAL_PATH, file)
                            }
                        }
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
        return file.exists()
    }

    private fun toMegaByte(bytes: Long): String {
        return String.format(Locale.ENGLISH, "%.2fMB", bytes / (1024.00 * 1024.00))
    }


    private fun unpackZip(path: String, zipName: String): Boolean {
        val inputStream: InputStream
        val zipInputStream: ZipInputStream
        try {
            var filePath: String
            inputStream = FileInputStream(path + zipName)
            zipInputStream = ZipInputStream(BufferedInputStream(inputStream))
            var zipEntry: ZipEntry?
            val buffer = ByteArray(1024)
            var count: Int
            while (zipInputStream.nextEntry.also { zipEntry = it } != null) {
                filePath = (zipEntry!!.name).replace("\\", "/")

                val fileName = filePath.split("/").last()

                val fileDirs = filePath.substring(0,filePath.length - fileName.length)

                File(path + fileDirs).run {
                    if(!exists())
                        mkdirs()
                }

                val fileOutputStream = FileOutputStream(path + filePath)
                while (zipInputStream.read(buffer).also { count = it } != -1) {
                    fileOutputStream.write(buffer, 0, count)
                }
                fileOutputStream.close()
                zipInputStream.closeEntry()
            }
            zipInputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
        return true
    }


    interface DownloadFileCallback {
        fun onDownloadStartOrResume()
        fun onPause()
        fun onCancel()
        fun onFail()
        fun onComplete()
    }
}