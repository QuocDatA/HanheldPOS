package com.handheld.printer.interfaces

import android.annotation.SuppressLint
import android.device.PrinterManager
import android.device.PrinterManager.PRNSTS_OK
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.handheld.printer.printer_manager.BasePrinterManager

class UrovoPrinterManager : BasePrinterManager() {

    // region classes

    private enum class UrovoPrinterStatus(val value: Int) {
        // Printer status
        PRINSTS_OK(0), //OK
        PRINSTS_OUT_OF_PAPER(-1), //Out of paper
        PRINSTS_OVER_HEAT(-2), //Over heat
        PRINSTS_UNDER_VOLTAGE(-3), //under voltage
        PRINSTS_BUSY(-4), //Device is busy
        PRINSTS_ERR(-256), //Common error
        PRINSTS_ERR_DRIVER(-257),
    }

    private enum class PrintType(val value: Int) {
        PRINT_TEXT(0), //Printed text
        PRINT_BITMAP(1), //print pictures
        PRINT_BARCOD(2), //Print bar code
        PRINT_FORWARD(3), //Forward (paper feed)
        PRINT_LINE(4)
    }

    private inner class CustomThread : Thread() {
        override fun run() {
            //To create a message loop
            Looper.prepare()
            mPrintHandler = @SuppressLint("HandlerLeak")
            object : Handler() {
                //2.Bind handler to looper object of customthread instance
                override fun handleMessage(msg: Message) {   //3.Define how messages are processed
                    when (msg.what) {
                        PrintType.PRINT_TEXT.value,
                        PrintType.PRINT_BITMAP.value,
                        PrintType.PRINT_BARCOD.value -> doPrint(
                            printer,
                            msg.what,
                            msg.obj
                        ) //Print
                        PrintType.PRINT_FORWARD.value -> {
                            printer.paperFeed(20)
                        }
                        else -> {}
                    }
                }
            }
            /*mPrintHandler = Looper.myLooper()?.let { looper ->
                Handler(looper) {

                    true
                }
            }*/
            Looper.loop()
        }
    }

    private data class TextValuePrint(
        val data: String?,
        val bold: Boolean,
        val size: FontSize
    )

    private data class BitmapValuePrint(
        val bitmap: Bitmap?,
        val align: BitmapAlign
    )

    // endregion

    // region variables
    private var mPrintHandler: Handler? = null
    private var thread: CustomThread? = null
    private lateinit var printer: PrinterManager

    // endregion

    init {
        if (!this::printer.isInitialized) {
            printer = PrinterManager()
        }
        if (thread == null) {
            thread = CustomThread()
        }
    }

    override fun connect() {
        printer.open()
        if (thread?.isAlive != true) {
            thread?.start()
        }
    }

    override fun disconnect() {
        printer.close()
        thread?.interrupt()
    }

    override fun setupPage(width: Float, height: Float) {
        this.width = width
        this.height = height
        printer.setupPage(width.toInt(), height.toInt())
    }

    override fun drawText(data: String?, bold: Boolean, size: FontSize) {
        val dataContent = data?.replace(" ", "  ")
        val msg = mPrintHandler!!.obtainMessage(PrintType.PRINT_TEXT.value)
        msg.obj = TextValuePrint(dataContent, bold, size)
        msg.sendToTarget()
    }

    override fun drawBitmap(bitmap: Bitmap?, align: BitmapAlign) {
        val msg = mPrintHandler!!.obtainMessage(PrintType.PRINT_BITMAP.value)
        msg.obj = BitmapValuePrint(bitmap, align)
        msg.sendToTarget()
    }

    override fun drawLine(widthLine: Int) {
        val dataContent = "".padEnd(widthLine * 2, '-')
        val msg = mPrintHandler!!.obtainMessage(PrintType.PRINT_TEXT.value)
        msg.obj = TextValuePrint(dataContent, false, FontSize.Small)
        msg.sendToTarget()
    }

    override fun feedLine(line: Int) {

    }

    override fun cutPaper() {

    }

    private fun doPrint(printerManager: PrinterManager, type: Int, content: Any) {
        if (printerManager.status == PRNSTS_OK) {
            printer.setupPage(width.toInt(), height.toInt())
            when (type) {
                PrintType.PRINT_TEXT.value -> {
                    val value = content as TextValuePrint
                    var height: Int = 0;
                    val fontSize = when (value.size) {
                        FontSize.Small -> 20
                        FontSize.Medium -> 28
                        FontSize.Large -> 35
                        FontSize.Wide -> 35
                    }
                    val fontName = "nunito"
                    val texts = value.data?.split("\n", "\n\r");
                    if (texts != null) {
                        for (text in texts) {
                            height += printerManager.drawText(
                                text,
                                0,
                                height,
                                fontName,
                                fontSize,
                                value.bold,
                                false,
                                0
                            )
                        }
                    }
                }
                PrintType.PRINT_BITMAP.value -> {
                    val value = content as BitmapValuePrint
                    value.bitmap ?: return
                    var x = 0
                    var y = 0
                    x = when (value.align) {
                        BitmapAlign.Right -> (this.width - value.bitmap.width).toInt()
                        BitmapAlign.Left -> 0
                        BitmapAlign.Center -> (this.width / 2 - value.bitmap.width / 2).toInt()
                    }
                    printerManager.drawBitmap(value.bitmap, x, y)
                }
            }
            printerManager.printPage(0);
        }

    }
}