package com.handheld.printer.printer_manager

import android.graphics.Bitmap

abstract class BasePrinterManager {
    protected var width = 0f
    protected var height = 0f
    abstract fun connect()
    abstract fun isConnected(): Boolean
    abstract fun disconnect()
    abstract fun setupPage(width: Float, height: Float)
    abstract fun drawText(data: String?, bold: Boolean = false, size: FontSize = FontSize.Small)
    abstract fun drawBitmap(bitmap: Bitmap?, align: BitmapAlign = BitmapAlign.Left)
    abstract fun drawLine(widthLine: Int)
    abstract fun cutPaper()
    abstract fun feedLines(line: Int)
    abstract fun openCashDrawer()
    abstract fun performPrinterAction(printerAction: () -> Unit)


    enum class FontSize(val charsPerLine: Int) {
        Small(48),
        Medium(48),
        Large(24),
        Wide(24)
    }

    enum class BitmapAlign {
        Right,
        Left,
        Center,
    }
}