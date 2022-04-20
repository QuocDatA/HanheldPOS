package com.handheld.pos_printer

import android.graphics.Bitmap

abstract class BasePrintManager {
    protected var width = 0
    protected var height = 0
    abstract fun connect()
    abstract fun disconnect()
    abstract fun setupPage(width : Int, height : Int)
    abstract fun drawText(data: String?, bold: Boolean = false, size : FontSize = FontSize.Small)
    abstract fun drawBitmap(bitmap : Bitmap?, align : BitmapAlign = BitmapAlign.Left)
    abstract fun drawLine(widthLine : Int)



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