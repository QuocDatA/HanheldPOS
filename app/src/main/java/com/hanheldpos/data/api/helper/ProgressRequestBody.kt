package com.hanheldpos.data.api.helper

import android.os.Handler
import android.os.Looper
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream

class ProgressRequestBody(
    file: File,
    private val contentType: String,
    listener: UploadCallback
) : RequestBody() {

    private val mFile: File = file
    private val mListener: UploadCallback = listener

    override fun contentType(): MediaType {
        return contentType.toMediaType()
    }

    override fun contentLength(): Long {
        return mFile.length()
    }

    override fun writeTo(sink: BufferedSink) {
        val fileLength: Long = mFile.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        try {
            FileInputStream(mFile).use { inputStream ->
                var uploaded: Long = 0
                var read: Int
                val handler = Handler(Looper.getMainLooper())
                while (inputStream.read(buffer).also { read = it } != -1) {
                    // update progress on UI thread
                    handler.post(ProgressUpdater(uploaded, fileLength))
                    uploaded += read.toLong()
                    sink.write(buffer, 0, read)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    interface UploadCallback {
        fun onProgressUpdate(percentage: Int)
    }

    private inner class ProgressUpdater(
        private val mUploaded: Long,
        private val mTotal: Long
    ) : Runnable {
        override fun run() {
            mListener.onProgressUpdate((100 * mUploaded / mTotal).toInt())
        }
    }

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 2048
    }

}
