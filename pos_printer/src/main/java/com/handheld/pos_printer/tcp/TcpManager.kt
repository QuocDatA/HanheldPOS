package com.handheld.pos_printer.tcp

import android.graphics.Bitmap
import com.dantsu.escposprinter.EscPosCharsetEncoding
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.dantsu.escposprinter.connection.tcp.TcpConnection
import com.dantsu.escposprinter.textparser.PrinterTextParserImg
import com.handheld.pos_printer.BasePrintManager
import com.handheld.pos_printer.PrintPic
import java.lang.StringBuilder

class TcpManager : BasePrintManager() {
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
        val content = StringBuilder()
        while (y < bitmap.height) {
            val bitmapScale: Bitmap = Bitmap.createBitmap(
                bitmap,
                0,
                y,
                bitmap.width,
                if (y + 250 >= bitmap.height) bitmap.height - y else 250
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
            content.append(imagePrint)

            y += 250
        }
        printer.printFormattedText(content.toString(), 1);

    }

    override fun drawLine(widthLine: Int) {
        val dataContent = "".padEnd(widthLine, '-')
        printer.printFormattedText(dataContent + "\n", 1)
    }


    init {
        if (!this::printer.isInitialized)
            printer = EscPosPrinter(
                TcpConnection("192.168.0.87",9100), 203, 80f, 70,
                EscPosCharsetEncoding("windows-1258", 16)
            )
    }
}