package com.handheld.pos_printer

import android.graphics.Bitmap

abstract class BasePrintManager {
    abstract fun connect()
    abstract fun disconnect()
    abstract fun setupPage(width : Int, height : Int)
    abstract fun drawText(data: String, bold: Boolean = false, size : FontSize = FontSize.Small)
    abstract fun drawBitmap(bitmap : Bitmap, align : BitmapAlign)

    enum class FontSize {
        Small,
        Medium,
        Large,
    }

    enum class BitmapAlign {
        Right,
        Left,
        Center,
    }
}