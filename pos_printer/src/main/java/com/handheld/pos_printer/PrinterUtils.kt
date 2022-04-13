package com.handheld.pos_printer

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

object PrinterUtils {
    fun bitmapToHexadecimalString(bitmap: Bitmap?): String? {
        bitmap?: return null
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val image = stream.toByteArray()
        return image.joinToString("") { "%02x".format(it) }
    }
}