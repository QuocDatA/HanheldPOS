package com.hanheldpos.model

import android.os.Handler
import android.os.Looper
import com.hanheldpos.databinding.DialogProcessDownloadResourceBinding
import kotlinx.coroutines.*
import java.io.*
import java.util.zip.ZipFile
/**
 * UnzipUtils class extracts files and sub-directories of a standard zip file to
 * a destination directory.
 *
 */
object UnzipUtils {
    /**
     * @param zipFilePath
     * @param destDirectory
     * @throws IOException
     */
    @Throws(IOException::class)
    fun unzip(zipFilePath: File, destDirectory: String, binding: DialogProcessDownloadResourceBinding, listener: UnZipCallback) {
        var counter = 0
        var progress = 0f
        File(destDirectory).run {
            if (!exists()) {
                mkdirs()
            }
        }

        ZipFile(zipFilePath).use { zip ->
            val zipFileSize = zip.size()

            val mainHandler = Handler(Looper.getMainLooper())
            val runnable = Runnable {
                listener.onStart("", zipFileSize)
            }
            mainHandler.post(runnable)

            zip.entries().asSequence().forEach { entry ->

                zip.getInputStream(entry).use { input ->

                    var filePath = destDirectory + File.separator + entry.name

                    filePath = filePath.replace("\\", "/")

                    val fileName = filePath.split("/").last()

                    val fileDirs = filePath.substring(0, filePath.length - fileName.length)

                    File(fileDirs).run {
                        if (!exists())
                            mkdirs()
                    }

                    if (!entry.isDirectory) {
                        // if the entry is a file, extracts it
                        extractFile(input, filePath)

                        val progressRunnable = Runnable {
                            listener.onProgress(counter, progress, zipFileSize)
                        }
                        mainHandler.post(progressRunnable)

                        progress = (counter.toFloat() / zipFileSize.toFloat()) * 100
                        counter++
                    } else {
                        // if the entry is a directory, make the directory
                        val dir = File(filePath)
                        dir.mkdir()
                    }

                }
            }
            CoroutineScope(Dispatchers.IO).launch {
                delay(50)
                withContext(Dispatchers.Main) {
                    listener.onFinish()
                }
            }
        }
    }

    /**
     * Extracts a zip entry (file entry)
     * @param inputStream
     * @param destFilePath
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun extractFile(inputStream: InputStream, destFilePath: String) {
        val bos = BufferedOutputStream(FileOutputStream(destFilePath))
        val bytesIn = ByteArray(BUFFER_SIZE)
        var read: Int
        while (inputStream.read(bytesIn).also { read = it } != -1) {
            bos.write(bytesIn, 0, read)
        }
        bos.close()
    }

    /**
     * Size of the buffer to read/write data
     */
    private const val BUFFER_SIZE = 4096


    interface UnZipCallback {
        fun onStart(zipFileName: String, zipFileSize: Int)
        fun onProgress(counter: Int, progress: Float, zipFileSize: Int)
        fun onFinish()
    }
}