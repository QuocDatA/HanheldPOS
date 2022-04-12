package com.hanheldpos.model.printer.bill

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.handheld.pos_printer.PrinterUtils
import com.hanheldpos.R
import com.hanheldpos.binding.setPriceView
import com.hanheldpos.databinding.LayoutBillPrinterBinding
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.order.OrderReq
import com.hanheldpos.model.payment.PaymentMethodType
import com.hanheldpos.ui.screens.printer.bill.ProductBillPrinterAdapter
import com.hanheldpos.utils.DateTimeUtils
import com.hanheldpos.utils.drawableToBitmap
import com.utils.wagu.Block
import com.utils.wagu.Board
import com.utils.wagu.Table
import java.lang.StringBuilder

object BillOrderHelper {
    fun orderToBillString(context: Context, printerSize: Int, order: OrderReq): String {

        val charPerLine = printerSize - 2
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
            "[C]<img size='256'>" + PrinterUtils.bitmapToHexadecimalString(
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


    fun getPrintOrderBill(context: Context, order: OrderReq): Bitmap {
        val inflater = LayoutInflater.from(context);
        val view = LayoutBillPrinterBinding.inflate(inflater)

        // Setup view bill
        setupViewOrderBill(view, order)

        //Fetch the dimensions of the viewport
        val displayMetrics = DisplayMetrics()
        displayMetrics.densityDpi = 203
        displayMetrics.density = 48f
        displayMetrics.widthPixels = 383
        displayMetrics.heightPixels = 800

        (view.root).measure(
            View.MeasureSpec.makeMeasureSpec(
                displayMetrics.widthPixels, View.MeasureSpec.EXACTLY
            ),
            View.MeasureSpec.makeMeasureSpec(
                displayMetrics.heightPixels, View.MeasureSpec.EXACTLY
            )
        )

        //Create a bitmap with the measured width and height. Attach the bitmap to a canvas object and draw the view inside the canvas
        view.root.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
        view.root.requestLayout()


        val bitmap =
            Bitmap.createBitmap(
                view.root.width,
                view.rootScrollView.getChildAt(0).height,
                Bitmap.Config.ARGB_8888
            )
        val canvas = Canvas(bitmap)
        view.root.draw(canvas)
        return bitmap
    }

    @SuppressLint("SetTextI18n")
    private fun setupViewOrderBill(view: LayoutBillPrinterBinding, order: OrderReq) {
        view.order = order
        view.addressBill.text = DataHelper.recentDeviceCodeLocalStorage?.first()?.LocationAddress

        view.codeOrder.text = "Order #: ${order.Order.Code}"
        view.platformDevice.text = DataHelper.recentDeviceCodeLocalStorage?.first()?.Nickname
        view.nameEmployee.text =
            "Employee : ${DataHelper.deviceCodeLocalStorage?.Employees?.find { it._id == order.Order.EmployeeGuid }?.FullName}"
        view.dateCreateOrder.text = DateTimeUtils.dateToString(
            DateTimeUtils.strToDate(
                order.Order.CreateDate,
                DateTimeUtils.Format.FULL_DATE_UTC_TIMEZONE
            ), DateTimeUtils.Format.DD_MM_YYYY_HH_MM
        )

        order.OrderDetail.PaymentList?.filter {
            PaymentMethodType.fromInt(it.PaymentTypeId ?: 0) == PaymentMethodType.CASH
        }?.let { list ->
            val totalPay = list.sumOf { it.OverPay ?: 0.0 }
            val needToPay = list.sumOf { it.Payable ?: 0.0 }
            setPriceView(view.cashAmount, totalPay)
            setPriceView(view.changeAmount, totalPay - needToPay)
        }

        val adapter = ProductBillPrinterAdapter()
        adapter.submitList(order.OrderDetail.OrderProducts)
        view.listProductBill.adapter = adapter

        view.executePendingBindings()
    }

}

