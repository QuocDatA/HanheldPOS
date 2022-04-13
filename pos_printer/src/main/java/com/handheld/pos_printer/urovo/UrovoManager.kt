package com.handheld.pos_printer.urovo

import android.device.PrinterManager
import android.graphics.Bitmap
import com.handheld.pos_printer.BasePrintManager

class UrovoManager() : BasePrintManager() {

    private lateinit var printer: PrinterManager
    override fun connect() {
        printer.open()
    }

    override fun disconnect() {
        printer.close()
    }


    override fun setupPage(width: Int, height: Int) {
        printer.setupPage(width, height)
    }

    override fun drawText(data: String, bold: Boolean, size: FontSize) {
        var height: Int = 0;
        val fontSize = when (size) {
            FontSize.Small -> 20
            FontSize.Medium -> 28
            FontSize.Large -> 35
        }
        val fontName = "arial"
        val texts = data.split("\n", "\n\r");
        for (text in texts) {
            height += printer.drawText(data, 0, height, fontName, fontSize, bold, false, 0)
        }
    }

    override fun drawBitmap(bitmap: Bitmap, align: BitmapAlign) {

    }

    override fun drawBarcode() {

    }

    init {
        if (!this::printer.isInitialized) {
            printer = PrinterManager()
        }
    }
}