package com.hanheldpos.model.printer.bill

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.textparser.PrinterTextParserImg
import com.hanheldpos.R
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.order.OrderReq
import com.hanheldpos.utils.drawableToBitmap

object BillOrderConverter {
    fun orderToBillString(context: Context, printer: EscPosPrinter, order: OrderReq): String {
        val printText = StringBuilder()
        printText.append("[L]Onl[R]Delivery\n")

        val drawable = ContextCompat.getDrawable(context as Activity, R.drawable.ic_note)
        drawable?.let {
            val wrappedDrawable: Drawable = DrawableCompat.wrap(it)
            DrawableCompat.setTint(wrappedDrawable, Color.WHITE)

            printText.append(
                "[C]<img size ='50'>" + PrinterTextParserImg.bitmapToHexadecimalString(
                    printer,
                    drawableToBitmap(wrappedDrawable)
                )
                        + "</img>\n"
            )
        }

        printText.append("[C]" + DataHelper.recentDeviceCodeLocalStorage?.first()?.LocationAddress)

        return printText.toString()
    }
}

