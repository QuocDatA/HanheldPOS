package com.utils.helper

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import java.util.*

object FileHelper {

    /**
     * Get mime type from file
     */
    fun getMimeType(context: Context, uri: Uri?): String? {
        var mimeType: String? = null
        try {
            mimeType = if (uri!!.scheme == ContentResolver.SCHEME_CONTENT) {
                val cr = context.contentResolver
                cr.getType(uri)
            } else {
                val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase(Locale.ROOT)
                )
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        return mimeType
    }
}