package com.hanheldpos.model.printer

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dantsu.escposprinter.EscPosCharsetEncoding
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.EscPosPrinterCommands
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.dantsu.escposprinter.exceptions.EscPosConnectionException
import com.hanheldpos.binding.setPriceView
import com.hanheldpos.databinding.LayoutBillPrinterBinding
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.order.OrderReq
import com.hanheldpos.model.payment.PaymentMethodType
import com.hanheldpos.model.printer.bill.BillOrderConverter
import com.hanheldpos.ui.screens.printer.bill.ProductBillPrinterAdapter
import com.hanheldpos.utils.DateTimeUtils
import kotlin.math.roundToInt


object PrinterHelper {


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


    /*==============================================================================================
    ======================================BLUETOOTH PART============================================
    ==============================================================================================*/
    private const val PERMISSION_BLUETOOTH = 1
    private const val PERMISSION_BLUETOOTH_ADMIN = 2
    private const val PERMISSION_BLUETOOTH_CONNECT = 3
    private const val PERMISSION_BLUETOOTH_SCAN = 4

    fun printBill(context: Activity, originalBitmap: Bitmap) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context,
                listOf(Manifest.permission.BLUETOOTH).toTypedArray(), PERMISSION_BLUETOOTH
            );
        } else if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_ADMIN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context,
                listOf(Manifest.permission.BLUETOOTH_ADMIN).toTypedArray(),
                PERMISSION_BLUETOOTH_ADMIN
            );
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context,
                listOf(Manifest.permission.BLUETOOTH_CONNECT).toTypedArray(),
                PERMISSION_BLUETOOTH_CONNECT
            );
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context,
                listOf(Manifest.permission.BLUETOOTH_SCAN).toTypedArray(), PERMISSION_BLUETOOTH_SCAN
            );
        } else {
            // Your code HERE
//            val printer = EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 270, 48f, 33)
            val targetWidth =
                480// printer.printerWidthPx -1 // 48mm printing zone with 203dpi => 383px
            val printerCommands =
                EscPosPrinterCommands(BluetoothPrintersConnections.selectFirstPaired())

            val rescaledBitmap = Bitmap.createScaledBitmap(
                originalBitmap,
                targetWidth,
                (originalBitmap.height.toFloat() * targetWidth.toFloat() / originalBitmap.width.toFloat()).roundToInt(),
                false
            )

            try {
//                printerCommands.connect();
//                printerCommands.reset();

                val printText = StringBuilder()
                var y = 0
                while (y < rescaledBitmap.height) {
                    val bitmap: Bitmap = Bitmap.createBitmap(
                        rescaledBitmap,
                        0,
                        y,
                        targetWidth,
                        if (y + 255 >= rescaledBitmap.height) rescaledBitmap.height - y else 255
                    )
                    printerCommands.printImage(EscPosPrinterCommands.bitmapToBytes(bitmap));
                    // printText.append("[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer,bitmap) + "</img>\n")
                    y += 255
                }
                // printer.printFormattedText(printText.toString())
                printerCommands.newLine()
                printerCommands.newLine()
                printerCommands.newLine()
            } catch (e: EscPosConnectionException) {
                e.printStackTrace()
            }
        }
    }

    fun printBill(context: Activity, order: OrderReq) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context,
                listOf(Manifest.permission.BLUETOOTH).toTypedArray(), PERMISSION_BLUETOOTH
            );
        } else if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_ADMIN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context,
                listOf(Manifest.permission.BLUETOOTH_ADMIN).toTypedArray(),
                PERMISSION_BLUETOOTH_ADMIN
            );
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context,
                listOf(Manifest.permission.BLUETOOTH_CONNECT).toTypedArray(),
                PERMISSION_BLUETOOTH_CONNECT
            );
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context,
                listOf(Manifest.permission.BLUETOOTH_SCAN).toTypedArray(), PERMISSION_BLUETOOTH_SCAN
            );
        } else {
            // Your code HERE
            val printer =
                EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 58f, 34,
                    EscPosCharsetEncoding("Windows-1258", 16))

            try {

                printer.printFormattedText(
                    BillOrderConverter.orderToBillString(
                        context,
                        printer,
                        order
                    )
                )

            } catch (e: EscPosConnectionException) {
                e.printStackTrace()
            }
        }
    }


}