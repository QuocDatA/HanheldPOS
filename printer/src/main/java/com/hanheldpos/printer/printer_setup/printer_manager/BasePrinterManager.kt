package com.hanheldpos.printer.printer_setup.printer_manager

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


    enum class FontSize() {
        Small() {
            override fun urovo() = 20
        },
//        Medium() {
//            override fun urovo() = 28
//        },
        Large() {
            override fun urovo() = 28
        }
//,
//        Wide() {
//            override fun urovo() = 35
//        }
        ;

        abstract fun urovo(): Int;
    }

    enum class BitmapAlign {
        Right,
        Left,
        Center,
    }
}