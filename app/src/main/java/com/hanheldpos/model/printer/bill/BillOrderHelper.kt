package com.hanheldpos.model.printer.bill

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.handheld.pos_printer.BasePrintManager
import com.handheld.pos_printer.bluetooth.BluetoothManager
import com.handheld.pos_printer.urovo.UrovoManager
import com.hanheldpos.R
import com.hanheldpos.binding.setPriceView
import com.hanheldpos.databinding.LayoutBillPrinterBinding
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.order.OrderReq
import com.hanheldpos.model.payment.PaymentMethodType
import com.hanheldpos.model.printer.PrinterHelper
import com.hanheldpos.ui.screens.printer.bill.ProductBillPrinterAdapter
import com.hanheldpos.utils.DateTimeUtils
import com.hanheldpos.utils.StringUtils
import com.hanheldpos.utils.drawableToBitmap
import com.utils.wagu.Block
import com.utils.wagu.Board
import com.utils.wagu.Table
import com.utils.wagu.WaguUtils
import java.lang.StringBuilder
import kotlin.math.log

object BillOrderHelper {

    /*==============================================================================================
    ======================================BLUETOOTH PART============================================
    ==============================================================================================*/
    //region Bluetooth
    private const val PERMISSION_BLUETOOTH = 1
    private const val PERMISSION_BLUETOOTH_ADMIN = 2
    private const val PERMISSION_BLUETOOTH_CONNECT = 3
    private const val PERMISSION_BLUETOOTH_SCAN = 4
    fun printBillWithBluetooth(context: Context, printerSize: Int, order: OrderReq) {

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                listOf(Manifest.permission.BLUETOOTH).toTypedArray(),
                PERMISSION_BLUETOOTH
            );
        } else if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_ADMIN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                listOf(Manifest.permission.BLUETOOTH_ADMIN).toTypedArray(),
                PERMISSION_BLUETOOTH_ADMIN
            );
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                listOf(Manifest.permission.BLUETOOTH_CONNECT).toTypedArray(),
                PERMISSION_BLUETOOTH_CONNECT
            );
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                listOf(Manifest.permission.BLUETOOTH_SCAN).toTypedArray(),
                PERMISSION_BLUETOOTH_SCAN
            );
        } else {
            try {
                val printer: BluetoothManager = BluetoothManager()
                printer.connect()
                //printBill(context, printer, printerSize, order)
                printImageBill(context, printer, printerSize, order)
            } catch (e: Exception) {
                Log.d("Error print", e.message.toString())
            }

        }


    }
    //endregion

    /*==============================================================================================
    ======================================UROVO PART============================================
    ==============================================================================================*/
    //region Urovo
    fun printBillWithUrovo(context: Context, printerSize: Int, order: OrderReq) {
        Thread {
            try {
                val printer: UrovoManager = UrovoManager()
                printer.connect()
                printBill(context, printer, printerSize, order)
            } catch (e: Exception) {
                Log.d("Error print", e.message.toString())
            }
        }.start()
    }
    //endregion

    private fun printBill(
        context: Context,
        printer: BasePrintManager,
        printerSize: Int,
        order: OrderReq
    ) {
        val paddingExtend = if (printer is UrovoManager) 4 else 0
        val charPerLineHeader = printerSize - 2 - paddingExtend
        val charPerLinerText = printerSize - 2 + paddingExtend * 2

        printer.setupPage(384, -1)
        // Bill Status
        Board(charPerLineHeader).let { b ->
            val blockHeaderLeft =
                Block(
                    b,
                    (charPerLineHeader) / 2,
                    1,
                    "Onl"
                ).setDataAlign(Block.DATA_MIDDLE_LEFT)
                    .allowGrid(false)
            b.setInitialBlock(blockHeaderLeft)
            val blockHeaderRight =
                Block(
                    b,
                    (charPerLineHeader) / 2,
                    1,
                    "Delivery"
                ).setDataAlign(Block.DATA_MIDDLE_RIGHT)
                    .allowGrid(false)
            blockHeaderLeft.rightBlock = blockHeaderRight
            printer.drawText(
                StringUtils.removeAccent(b.invalidate().build().preview.trim()).toString(),
                false,
                BasePrintManager.FontSize.Medium
            )
        }

        // Icon
        val drawable = ContextCompat.getDrawable(context as Activity, R.drawable.ic_note)
        val wrappedDrawable: Drawable = DrawableCompat.wrap(drawable!!)
        DrawableCompat.setTint(wrappedDrawable, Color.BLACK)
        printer.drawBitmap(
            drawableToBitmap(wrappedDrawable),
            BasePrintManager.BitmapAlign.Center
        )

        // Address
        Board(charPerLinerText - paddingExtend).let { b ->
            val textContext = StringBuilder()
            val blockAddress = Block(
                b,
                charPerLinerText - paddingExtend - 6,
                6,
                DataHelper.recentDeviceCodeLocalStorage?.first()?.LocationAddress?.trim()
            ).allowGrid(false).setBlockAlign(Block.BLOCK_CENTRE)
                .setDataAlign(Block.DATA_CENTER)
            b.setInitialBlock(blockAddress)
            b.invalidate().build().preview.split("\n", "\r\n").forEach {
                if (it.trim().isNotEmpty())
                    textContext.append(it + "\n")
            }
            printer.drawText(StringUtils.removeAccent(textContext.toString()))
        }

        // Delivery Address
        order.OrderDetail.Billing?.let {
            printer.drawText(StringUtils.removeAccent(it.FullName), true)
            printer.drawText(StringUtils.removeAccent(it.getFullAddressWithLineBreaker()))
            var addressType: String? = ""
            val phoneNumber = it.Phone
            DataHelper.addressTypesLocalStorage?.find { addressTypeResp -> addressTypeResp.AddressTypeId == it.AddressTypeId }
                ?.let { address ->
                    addressType = address.AddressTypeEn
                }
            printer.drawText("$addressType | $phoneNumber", true)
            it.Note?.let { note ->
                printer.drawText(note, true)
            }
        }

        printer.drawLine(charPerLinerText)

        // Info Order
        Board(charPerLinerText).let { b ->
            val orderCode = mutableListOf("Order #:", order.Order.Code.toString())
            val employee =
                mutableListOf(
                    "Employee :",
                    DataHelper.deviceCodeLocalStorage?.Employees?.find { it._id == order.Order.EmployeeGuid }?.FullName.toString()
                )
            val dateCreate = mutableListOf(
                "Create Date", DateTimeUtils.dateToString(
                    DateTimeUtils.strToDate(
                        order.Order.CreateDate,
                        DateTimeUtils.Format.FULL_DATE_UTC_TIMEZONE
                    ), DateTimeUtils.Format.DD_MM_YYYY_HH_MM
                )
            )
            val content = WaguUtils.columnListDataBlock(
                charPerLineHeader, mutableListOf(orderCode, employee, dateCreate),
                mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT)
            )
            // Order Code
            /*val blockHeaderLeft =
                Block(
                    b,
                    (charPerLineHeader) / 2,
                    1,
                    orderCode.first()
                ).setDataAlign(Block.DATA_MIDDLE_LEFT)
                    .allowGrid(false)
            b.setInitialBlock(blockHeaderLeft)
            val blockHeaderRight =
                Block(
                    b,
                    (charPerLineHeader ) / 2,
                    1,
                    orderCode.last()
                ).setDataAlign(Block.DATA_MIDDLE_RIGHT)
                    .allowGrid(false)
            blockHeaderLeft.rightBlock = blockHeaderRight
            // Employee
            val blockHeaderLeft2 =
                Block(
                    b,
                    (charPerLineHeader) / 2,
                    1,
                    employee.first()
                ).setDataAlign(Block.DATA_MIDDLE_LEFT)
                    .allowGrid(false)
            blockHeaderLeft.belowBlock = blockHeaderLeft2
            val blockHeaderRight2 =
                Block(
                    b,
                    (charPerLineHeader ) / 2,
                    1,
                    employee.last()
                ).setDataAlign(Block.DATA_MIDDLE_RIGHT)
                    .allowGrid(false)
            blockHeaderLeft2.rightBlock = blockHeaderRight2*/

            printer.drawText(content)
        }

        printer.drawLine(charPerLinerText)

        // Finish Print
        Thread.sleep(1500)
        printer.disconnect()
    }

    private fun printImageBill(
        context: Context,
        printer: BasePrintManager,
        printerSize: Int,
        order: OrderReq
    ) {
        printer.drawBitmap(getPrintOrderBill(context, order))
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
        displayMetrics.widthPixels = 384
        displayMetrics.heightPixels = 0

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
        order.OrderDetail.Billing?.let {
            view.customerBill.customer = it
            DataHelper.addressTypesLocalStorage?.find { addressTypeResp -> addressTypeResp.AddressTypeId == it.AddressTypeId }
                ?.let { address ->
                    view.customerBill.placeCustomer = address.AddressTypeEn
                }
        }

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

