package com.handheld.pos_printer.bluetooth

import android.graphics.Bitmap
import com.dantsu.escposprinter.EscPosCharsetEncoding
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.EscPosPrinterCommands
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.dantsu.escposprinter.textparser.PrinterTextParserImg
import com.handheld.pos_printer.BasePrintManager
import com.handheld.pos_printer.PrintPic

class BluetoothManager : BasePrintManager() {
    private lateinit var printer: EscPosPrinter
    override fun connect() {
    }

    override fun disconnect() {
        printer.disconnectPrinter()
    }

    override fun setupPage(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    override fun drawText(data: String?, bold: Boolean, size: FontSize) {
        var content = data?.replace(Regex("[\n\r]$"), "")
        if (bold) {
            content = "<b>$content</b>"
        }
        content = when (size) {
            FontSize.Small -> "<font size='normal'>$content</font>"
            FontSize.Medium ->"<font size='tall'>$content</font>"
            FontSize.Large -> "<font size='big'>$content</font>"
        }
        printer.printFormattedText(
            content + "\n",
            1
        )
    }

    override fun drawBitmap(bitmap: Bitmap?, align: BitmapAlign) {
        bitmap ?: return
        var y = 0

        while (y < bitmap.height) {
            val bitmapScale: Bitmap = Bitmap.createBitmap(
                bitmap,
                0,
                y,
                bitmap.width,
                if (y + 200 >= bitmap.height) bitmap.height - y else 200
            )
            val printPic = PrintPic()
            printPic.init(bitmapScale)
            var imagePrint = "<img>${
                PrinterTextParserImg.bytesToHexadecimalString(
                    printPic.printDraw()
                )
            }</img>\n"
            imagePrint = when (align) {
                BitmapAlign.Right -> "[R]$imagePrint"
                BitmapAlign.Left -> "[L]$imagePrint"
                BitmapAlign.Center -> "[C]$imagePrint"
            }
            printer.printFormattedText(imagePrint, 1);
            y += 200
        }


    }

    override fun drawLine(widthLine: Int) {
        val dataContent = "".padEnd(widthLine, '-')
        printer.printFormattedText(dataContent + "\n", 1)
    }


    init {
        if (!this::printer.isInitialized)
            printer = EscPosPrinter(
                BluetoothPrintersConnections.selectFirstPaired(), 203, 48f, 34,
                EscPosCharsetEncoding("windows-1258", 16)
            )
    }
}