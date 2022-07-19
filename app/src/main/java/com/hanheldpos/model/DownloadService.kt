package com.hanheldpos.model

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import com.downloader.Status
import com.downloader.request.DownloadRequest
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.resource.ResourceResp
import com.hanheldpos.databinding.DialogProcessDownloadResourceBinding
import com.hanheldpos.extension.showWithoutSystemUI
import kotlinx.coroutines.*
import java.io.*
import java.lang.Runnable
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream


object DownloadService {
    private var INTERNAL_PATH = ""

    private var downloadId: Int = 0
    private var listFileToExtract: MutableList<String> = mutableListOf()
    private lateinit var processDialog: Dialog

    @SuppressLint("StaticFieldLeak")
    lateinit var binding: DialogProcessDownloadResourceBinding
    private var currentByte: Long = 0L

    private fun initDownloadService(context: Context) {

        val path = File(
            "/${context.filesDir.absolutePath}/",
        )

        INTERNAL_PATH = path.path

        val config = PRDownloaderConfig.newBuilder()
            .setDatabaseEnabled(true)
            .setReadTimeout(30000)
            .setConnectTimeout(30000)
            .build()
        PRDownloader.initialize(context, config)
        initDialog(context)
    }

    private fun initDialog(context: Context) {
        processDialog = Dialog(context, R.style.WideDialog)
        processDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        processDialog.setCancelable(false)
        processDialog.setContentView(R.layout.dialog_process_download_resource)
        binding = DialogProcessDownloadResourceBinding.inflate(LayoutInflater.from(context))
        binding.isLoading = true
        processDialog.setContentView(binding.root)
    }

    @SuppressLint("SetTextI18n")
    fun downloadFile(
        context: Context,
        listResources: List<ResourceResp>,
        listener: DownloadFileCallback
    ) {
        var isDownloading = true
        val downloadRequestList: MutableList<DownloadRequest> = mutableListOf()
        var currentDownloadPos = 0
        var isGettingSpeed = false
        initDownloadService(context)
        val listNeedDownload = listResources.filter { !checkFileExist(it.Name) }
        if (listNeedDownload.isEmpty()) {
            listener.onComplete()
            return
        }
        processDialog.showWithoutSystemUI()
        listNeedDownload.forEach { item ->
            if (item.Name.contains(".zip"))
                listFileToExtract.add(item.Name)
            val downloadRequest = PRDownloader.download(item.Url, INTERNAL_PATH, item.Name)
                .build()
                .setOnStartOrResumeListener {
                    binding.downloadTitle.text = "Downloading"
                    binding.itemName.text = item.Name
                    currentByte = 0L
                }
                .setOnPauseListener {
                    binding.isLoading = false
                    isDownloading = false
                    listener.onPause()
                }
                .setOnCancelListener {
                    binding.isLoading = false
                    isDownloading = false
                    listener.onCancel()
                }
                .setOnProgressListener { progress ->
                    binding.isLoading = false
                    val progressPercent: Long = progress.currentBytes * 100 / progress.totalBytes
                    if (progressPercent.toInt() == 0) currentByte = 0L
                    binding.processBar.progress = progressPercent.toInt()
                    binding.tvProgressCount.text = "$progressPercent%"
                    val tempByte: Long = progress.currentBytes
                    binding.tvDownloadSpeed.text =
                        toMegaByte(tempByte.minus(currentByte)) + "/s | "
                    binding.tvStoreCount.text =
                        toMegaByte(progress.currentBytes) + " / " + toMegaByte(progress.totalBytes)
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(300)
                        currentByte = progress.currentBytes
                        isGettingSpeed = true
                    }
                }
            downloadRequestList.add(downloadRequest)
        }
        CoroutineScope(Dispatchers.IO).launch {
            while (isDownloading) {
                delay(700)
                if (!isGettingSpeed) {
                    currentByte = 0L
                    val mainHandler = Handler(Looper.getMainLooper())
                    val runnable = Runnable {
                        binding.tvDownloadSpeed.text = toMegaByte(currentByte) + "/s | "
                    }
                    mainHandler.post(runnable)
                } else isGettingSpeed = false
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            while (isDownloading) {
                downloadId =
                    downloadRequestList[currentDownloadPos].start(object : OnDownloadListener {
                        override fun onDownloadComplete() {
                        }

                        override fun onError(error: com.downloader.Error?) {
                            Log.d("Download Resources", error.toString())

                        }

                    })
                while (PRDownloader.getStatus(downloadId) in mutableListOf(
                        Status.QUEUED,
                        Status.RUNNING
                    )
                ) {
                }
                currentDownloadPos++
                if (PRDownloader.getStatus(downloadId) in mutableListOf(
                        Status.CANCELLED,
                        Status.PAUSED
                    )
                ) {
                    return@launch
                }
                if (PRDownloader.getStatus(downloadId) in mutableListOf(
                        Status.FAILED,
                        Status.UNKNOWN
                    )
                ) Log.d("Download Resources", "Failed")
                if (isDownloading && currentDownloadPos >= listResources.size) {
                    isDownloading = false
                    CoroutineScope(Dispatchers.Main).launch {
//                        listFileToExtract.forEach { file ->
//                            unpackZip(INTERNAL_PATH, file, object : UnZipCallback {
//                                override fun onStart() {
//                                    binding.isLoading = true
//                                }
//
//                                override fun onFinish() {
//                                    CoroutineScope(Dispatchers.Main).launch {
//                                        processDialog.dismiss()
//                                        listener.onComplete()
//                                    }
//                                }
//                            })
//
//                        }
                        listFileToExtract.forEach { file ->
                            unZip(
                                "$INTERNAL_PATH/$file",
                                INTERNAL_PATH,
                                file,
                                object : UnZipCallback {
                                    override fun onStart(zipFileName: String, zipFileSize: Int) {
                                        binding.itemName.text = zipFileName
                                        binding.processBar.max = zipFileSize
                                        binding.processBar.progress = 0
                                        binding.tvProgressCount.text = "0%"
                                        binding.downloadTitle.text = "Extracting..."
                                        binding.tvDownloadSpeed.text = "0/${zipFileSize}"
                                        binding.tvStoreCount.text = " Files"
                                    }

                                    override fun onProgress(
                                        counter: Int,
                                        progress: Float,
                                        zipFileSize: Int
                                    ) {
//                                        val mainHandler = Handler(Looper.getMainLooper())
//                                        val runnable = Runnable {
                                            binding.processBar.progress = counter
                                            binding.tvProgressCount.text = "${progress.toInt()}%"
                                            binding.tvDownloadSpeed.text = "$counter/$zipFileSize"
//                                        }
//                                        mainHandler.post(runnable)
                                    }

                                    override fun onFinish() {
                                        processDialog.dismiss()
                                        listener.onComplete()
                                    }
                                }
                            )
                        }
                    }
                    return@launch
                }
            }
        }

    }

    private fun checkFileExist(filePath: String): Boolean {
        val file = File("$INTERNAL_PATH/$filePath")
        return file.exists()
    }

    private fun toMegaByte(bytes: Long): String {
        return String.format(Locale.ENGLISH, "%.2fMB", bytes / (1024.00 * 1024.00))
    }

    private fun unZip(filePath: String, desPath: String, fileExtractName: String, listener: UnZipCallback) {
        val file = File(filePath)
        UnzipUtils.unzip(file, desPath, binding, object : UnzipUtils.UnZipCallback{
            override fun onStart(zipFileName: String, zipFileSize: Int) {
                listener.onStart(fileExtractName, zipFileSize)
            }

            override fun onProgress(counter: Int, progress: Float, zipFileSize: Int) {
                listener.onProgress(counter, progress, zipFileSize)
            }

            override fun onFinish() {
                listener.onFinish()
            }
        })
    }

    private fun unpackZip(
        path: String,
        zipName: String,
        //listener: UnZipCallback
    ): Boolean {
        var inputStream: InputStream
        var zipInputStream: ZipInputStream
        var counter = 0
        try {
            //listener.onStart()
            var filePath: String

            var zipEntry: ZipEntry?
            val buffer = ByteArray(1024)
            var count: Int

            val zipFile = ZipFile("$path/$zipName")
            val zipFileSize = zipFile.size()

            binding.itemName.text = zipName
            binding.processBar.max = zipFileSize
            binding.processBar.progress = 0
            binding.tvProgressCount.text = "0%"
            binding.downloadTitle.text = "Extracting..."
            binding.tvDownloadSpeed.text = "$counter/$zipFileSize"
            binding.tvStoreCount.text = " Files"

            var progressText = 0f
            binding.isLoading = false

            CoroutineScope(Dispatchers.IO).launch {
                inputStream = FileInputStream("$path/$zipName")
                zipInputStream = ZipInputStream(BufferedInputStream(inputStream))
                while (zipInputStream.nextEntry.also { zipEntry = it } != null) {

                    filePath = (zipEntry!!.name).replace("\\", "/")

                    val fileName = filePath.split("/").last()

                    val fileDirs = filePath.substring(0, filePath.length - fileName.length)

                    File(path + fileDirs).run {
                        if (!exists())
                            mkdirs()
                    }

                    val fileOutputStream = FileOutputStream(path + filePath)
                    while (zipInputStream.read(buffer).also { count = it } != -1) {
                        fileOutputStream.write(buffer, 0, count)
                    }

                    val mainHandler = Handler(Looper.getMainLooper())
                    val runnable = Runnable {
                        binding.processBar.progress = counter
                        binding.tvProgressCount.text = "${progressText.toInt()}%"
                        binding.tvDownloadSpeed.text = "$counter/$zipFileSize"
                    }
                    mainHandler.post(runnable)

                    counter++
                    progressText = (counter.toFloat() / zipFileSize.toFloat()) * 100
                    fileOutputStream.close()
                    zipInputStream.closeEntry()
                }
                zipInputStream.close()
                //listener.onFinish()
            }

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
        fun onFail(errorMessage: String? = null)
        fun onComplete()
    }

    interface UnZipCallback {
        fun onStart(zipFileName: String, zipFileSize: Int)
        fun onProgress(counter: Int, progress: Float, zipFileSize: Int)
        fun onFinish()
    }
}