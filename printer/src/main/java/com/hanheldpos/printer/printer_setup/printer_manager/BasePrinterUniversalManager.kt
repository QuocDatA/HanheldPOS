package com.hanheldpos.printer.printer_setup.printer_manager

import android.graphics.Bitmap
import android.util.Log
import com.dantsu.escposprinter.EscPosCharsetEncoding
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.DeviceConnection
import com.dantsu.escposprinter.textparser.PrinterTextParserImg
import com.hanheldpos.printer.printer_setup.ImagePrinterHelper
import com.hanheldpos.printer.PrintConstants

/**
 * This is the base print manager for universal method
 * Which they are usable for all device not limited to the type
 * Hence this base does not use the device's SDK
 * */
abstract class BasePrinterUniversalManager(
    private val deviceConnection: DeviceConnection?,
    printerDPI: Int,
    printerPaperWidth: Float,
    charsPerLine: Int,
) : BasePrinterManager() {
    private val printer: EscPosPrinter = EscPosPrinter(
        deviceConnection,
        printerDPI,
        printerPaperWidth,
        charsPerLine,
        EscPosCharsetEncoding("windows-1258", 16)
    )

    override fun cutPaper() {
        printer.printFormattedTextAndCut("")
    }

    override fun feedLines(line: Int) {
        printer.printFormattedText("", line)
    }

    override fun isConnected(): Boolean {
        return deviceConnection?.isConnected == true
    }

    override fun connect() {
        if (!isConnected())
            deviceConnection?.connect()
    }

    override fun disconnect() {
        if (isConnected())
            deviceConnection?.disconnect()
    }

    override fun drawText(data: String?, bold: Boolean, size: FontSize) {
        var content = data?.replace(Regex("[\n\r]$"), "")
        if (bold) {
            content = "<b>$content</b>"
        }
        content = when (size) {
            FontSize.Small -> "<font size='normal'>$content</font>"
//            FontSize.Medium -> "<font size='big'>$content</font>"
            FontSize.Large -> "<font size='big'>$content</font>"
//            FontSize.Wide -> "<font size='wide'>$content</font>"
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
            val printPic = ImagePrinterHelper(bitmapScale)
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
        printer.printFormattedText(content.toString(), 1)
    }

    override fun drawLine(widthLine: Int) {
        val dataContent = "".padEnd(widthLine, '-')
        printer.printFormattedText(dataContent + "\n", 1)
    }

    override fun setupPage(width: Float, height: Float) {

    }

    override fun openCashDrawer() {
        deviceConnection?.write(byteArrayOf(27, 112, 0, 60, -1))
        deviceConnection?.send(100)
        Log.d(PrintConstants.TAG, "Drawer opened")
    }

    override fun performPrinterAction(printerAction: () -> Unit) {
        try {
            disconnect()
            connect()
            printerAction.invoke()
        } catch (e: Exception) {
            Log.d(PrintConstants.TAG, e.toString())
        } finally {
            disconnect()
        }

    }
}