package com.hanheldpos.ui.screens.printer

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import com.hanheldpos.binding.setPriceView
import com.hanheldpos.databinding.LayoutBillPrinterBinding
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.order.OrderReq
import com.hanheldpos.model.payment.PaymentMethodType
import com.hanheldpos.ui.screens.printer.bill.ProductBillPrinterAdapter
import com.hanheldpos.utils.DateTimeUtils

object PrinterViewModel {


    fun getPrintOrderBill(context: Context, order: OrderReq): Bitmap {
        val inflater = LayoutInflater.from(context);
        val view = LayoutBillPrinterBinding.inflate(inflater)

        // Setup view bill
        setupViewOrderBill(view, order)

        //Fetch the dimensions of the viewport
        val displayMetrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            context.display?.getRealMetrics(displayMetrics)
            displayMetrics.densityDpi
        } else {
            (context as Activity).windowManager.defaultDisplay.getMetrics(
                displayMetrics
            )
        }
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