package com.handheld.printer.printer_manager

import android.graphics.Bitmap

abstract class BasePrinterManager {
    protected var width = 0f
    protected var height = 0f
    abstract fun connect()
    abstract fun disconnect()
    abstract fun setupPage(width: Float, height: Float)
    abstract fun drawText(data: String?, bold: Boolean = false, size: FontSize = FontSize.Small)
    abstract fun drawBitmap(bitmap: Bitmap?, align: BitmapAlign = BitmapAlign.Left)
    abstract fun drawLine(widthLine: Int)
    abstract fun feedLine(line : Int)
    abstract fun cutPaper()


    enum class FontSize {
        Small,
        Medium,
        Large,
        Wide
    }

    enum class BitmapAlign {
        Right,
        Left,
        Center,
    }
}