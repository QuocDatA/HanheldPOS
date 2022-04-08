package com.hanheldpos.model.printer.bill

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.textparser.PrinterTextParser
import com.dantsu.escposprinter.textparser.PrinterTextParserImg
import com.dantsu.escposprinter.textparser.PrinterTextParserString
import com.hanheldpos.R
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.order.OrderReq
import com.hanheldpos.model.printer.PrinterHelper
import com.hanheldpos.utils.DateTimeUtils
import com.hanheldpos.utils.StringUtils
import com.hanheldpos.utils.drawableToBitmap
import com.utils.wagu.Block
import com.utils.wagu.Board
import com.utils.wagu.Table
import java.lang.StringBuilder

object BillOrderConverter {
    fun orderToBillString(context: Context, printer: EscPosPrinter, order: OrderReq): String {

        val charPerLine = printer.printerNbrCharactersPerLine - 2
        val printerText = StringBuilder()


        // Bill Status
        Board(charPerLine).let { b ->
            val blockHeaderLeft =
                Block(b, charPerLine / 2, 1, "Onl").setDataAlign(Block.DATA_MIDDLE_LEFT)
                    .allowGrid(false)
            b.setInitialBlock(blockHeaderLeft)
            val blockHeaderRight =
                Block(b, charPerLine / 2, 1, "Delivery").setDataAlign(Block.DATA_MIDDLE_RIGHT)
                    .allowGrid(false)
            blockHeaderLeft.rightBlock = blockHeaderRight
            printerText.append(
                "[L]<b><font size='tall'>" + b.invalidate().build().preview.trim() + "</font></b>\n"
            )
        }

        // Icon
        val drawable = ContextCompat.getDrawable(context as Activity, R.drawable.ic_note)
        val wrappedDrawable: Drawable = DrawableCompat.wrap(drawable!!)
        DrawableCompat.setTint(wrappedDrawable, Color.BLACK)

        printerText.append(
            "[C]<img size='256'>" + PrinterTextParserImg.bitmapToHexadecimalString(
                printer,
                drawableToBitmap(wrappedDrawable)
            ) + "</img>\n"
        )

        // Address
        Board(charPerLine).let { b ->
            val blockAddress = Block(
                b,
                charPerLine - 6,
                6,
                DataHelper.recentDeviceCodeLocalStorage?.first()?.LocationAddress
            ).allowGrid(false).setBlockAlign(Block.BLOCK_CENTRE).setDataAlign(Block.DATA_CENTER)
            b.setInitialBlock(blockAddress)
            b.invalidate().build().preview.split("\n").forEach {
                printerText.append("[L]<font size='normal'>$it</font>\n")
            }

        }

        // Delivery Address
        order.OrderDetail.Billing?.let {
            printerText.append("[L]<b><font size='normal'>${it.FullName}</font></b>\n")
            printerText.append("[L]<font size='normal'>${it.getFullAddressWithLineBreaker()}</font>\n")
            var addressType: String? = ""
            val phoneNumber = it.Phone
            DataHelper.addressTypesLocalStorage?.find { addressTypeResp -> addressTypeResp.AddressTypeId == it.AddressTypeId }
                ?.let { address ->
                    addressType = address.AddressTypeEn
                }
            printerText.append("[L]<b><font size='normal'>$addressType</font></b> | <font size='normal'>$phoneNumber</font>\n")
            it.Note?.let { note ->
                printerText.append("[L]<b><font size='normal'>$note</font></b>\n")
            }
        }

        printerText.append("".padEnd(charPerLine, '-') + "\n")

        // Info Order
        Board(charPerLine + 2).let { b ->
            val orderCode = "Order #: ${order.Order.Code}"
            val platform = DataHelper.recentDeviceCodeLocalStorage?.first()?.Nickname
            val employeeName =
                "Employee : ${DataHelper.deviceCodeLocalStorage?.Employees?.find { it._id == order.Order.EmployeeGuid }?.FullName}"
            val dateCreate = DateTimeUtils.dateToString(
                DateTimeUtils.strToDate(
                    order.Order.CreateDate,
                    DateTimeUtils.Format.FULL_DATE_UTC_TIMEZONE
                ), DateTimeUtils.Format.DD_MM_YYYY_HH_MM
            )
            b.setInitialBlock(
                Table(
                    b,
                    charPerLine +2 ,
                    mutableListOf(orderCode, platform),
                    mutableListOf(mutableListOf(employeeName, dateCreate))
                ).apply {
                    this.gridMode = Table.GRID_NON
                    this.colAlignsList =
                        mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT)
                }.tableToBlocks().allowGrid(false)
            )
            printerText.append(b.invalidate().build().preview)
        }

        printerText.append("".padEnd(charPerLine, '-') + "\n")

        return printerText.toString().apply {
            print(this)
        }
    }


}

