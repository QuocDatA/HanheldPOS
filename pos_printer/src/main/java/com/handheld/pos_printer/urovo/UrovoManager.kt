package com.handheld.pos_printer.urovo

import android.device.PrinterManager
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.handheld.pos_printer.BasePrintManager

class UrovoManager() : BasePrintManager() {

    private var mPrintHandler: Handler? = null
    private var thread: CustomThread? = null

    // Printer status
    private val PRNSTS_OK = 0 //OK
    private val PRNSTS_OUT_OF_PAPER = -1 //Out of paper
    private val PRNSTS_OVER_HEAT = -2 //Over heat
    private val PRNSTS_UNDER_VOLTAGE = -3 //under voltage
    private val PRNSTS_BUSY = -4 //Device is busy
    private val PRNSTS_ERR = -256 //Common error
    private val PRNSTS_ERR_DRIVER = -257

    private lateinit var printer: PrinterManager
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


    override fun setupPage(width: Int, height: Int) {
        this.width = width
        this.height = height
        printer.setupPage(width, height)
    }

    override fun drawText(data: String?, bold: Boolean, size: FontSize) {
        val dataContent = data?.replace(" ", "  ")
        val msg = mPrintHandler!!.obtainMessage(PRINT_TEXT)
        msg.obj = TextValuePrint(dataContent, bold, size)
        msg.sendToTarget()

    }

    override fun drawBitmap(bitmap: Bitmap?, align: BitmapAlign) {
        val msg = mPrintHandler!!.obtainMessage(PRINT_BITMAP)
        msg.obj = BitmapValuePrint(bitmap, align)
        msg.sendToTarget()
    }

    override fun drawLine(widthLine : Int) {
        val dataContent = "".padEnd(widthLine * 2,'-')
        val msg = mPrintHandler!!.obtainMessage(PRINT_TEXT)
        msg.obj = TextValuePrint(dataContent, false, FontSize.Small)
        msg.sendToTarget()
    }

    init {
        if (!this::printer.isInitialized) {
            printer = PrinterManager()
        }
        if (thread == null) {
            thread = CustomThread()
        }
    }

    private val PRINT_TEXT = 0 //Printed text
    private val PRINT_BITMAP = 1 //print pictures
    private val PRINT_BARCOD = 2 //Print bar code
    private val PRINT_FORWARD = 3 //Forward (paper feed)
    private val PRINT_LINE = 4

    private data class TextValuePrint(
        val data: String?,
        val bold: Boolean,
        val size: FontSize
    )

    private data class BitmapValuePrint(
        val bitmap: Bitmap?,
        val align: BitmapAlign
    )

    private inner class CustomThread : Thread() {
        override fun run() {
            //To create a message loop
            Looper.prepare()
            mPrintHandler = object : Handler() {
                //2.Bind handler to looper object of customthread instance
                override fun handleMessage(msg: Message) {   //3.Define how messages are processed
                    when (msg.what) {
                        PRINT_TEXT, PRINT_BITMAP, PRINT_BARCOD -> doPrint(
                            printer,
                            msg.what,
                            msg.obj
                        ) //Print
                        PRINT_FORWARD -> {
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

    private fun doPrint(printerManager: PrinterManager, type: Int, content: Any) {
        if (printerManager.status == PRNSTS_OK) {
            printer.setupPage(width, height)
            when (type) {
                PRINT_TEXT -> {
                    val value = content as TextValuePrint
                    var height: Int = 0;
                    val fontSize = when (value.size) {
                        FontSize.Small -> 20
                        FontSize.Medium -> 28
                        FontSize.Large -> 35
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
                PRINT_BITMAP -> {
                    val value = content as BitmapValuePrint
                    value.bitmap ?: return
                    var x = 0
                    var y = 0
                    x = when (value.align) {
                        BitmapAlign.Right -> this.width - value.bitmap.width
                        BitmapAlign.Left -> 0
                        BitmapAlign.Center -> this.width / 2 - value.bitmap.width / 2
                    }
                    printerManager.drawBitmap(value.bitmap, x, y)
                }
            }
            printerManager.printPage(0);
        }

    }
}