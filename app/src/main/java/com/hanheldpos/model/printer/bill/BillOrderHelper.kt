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
import android.media.AudioMetadata
import android.os.StrictMode
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.handheld.pos_printer.BasePrintManager
import com.handheld.pos_printer.bluetooth.BluetoothManager
import com.handheld.pos_printer.tcp.TcpManager
import com.handheld.pos_printer.urovo.UrovoManager
import com.hanheldpos.R
import com.hanheldpos.binding.setPriceView
import com.hanheldpos.databinding.LayoutBillPrinterBinding
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.order.OrderReq
import com.hanheldpos.model.order.ProductChosen
import com.hanheldpos.model.payment.PaymentMethodType
import com.hanheldpos.model.printer.GroupBundlePrinter
import com.hanheldpos.model.product.ExtraConverter
import com.hanheldpos.model.product.ProductType
import com.hanheldpos.ui.screens.printer.bill.ProductBillPrinterAdapter
import com.hanheldpos.utils.DateTimeUtils
import com.hanheldpos.utils.PriceUtils
import com.hanheldpos.utils.StringUtils
import com.hanheldpos.utils.drawableToBitmap
import com.utils.wagu.Block
import com.utils.wagu.Board
import com.utils.wagu.WaguUtils
import java.lang.StringBuilder
import java.util.*

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
            Thread {
                try {
                    val printer: BluetoothManager = BluetoothManager()
                    printer.connect()
                    try {
                        printBill(context, printer, printerSize, order)
//                        printImageBill(context, printer, printerSize, order)
                    } catch (e: Exception) {
                        Log.d("Error print", e.message.toString())
                    }

                    // Finish Print
                    Thread.sleep(1500)
                    printer.disconnect()
                } catch (e: Exception) {
                    Log.d("Error print", e.message.toString())
                }
            }.start()

        }
    }
    //endregion

    /*==============================================================================================
    ======================================UROVO PART============================================
    ==============================================================================================*/
    //region Urovo
    fun printBillWithUrovo(context: Context, printerSize: Int, order: OrderReq) {

        try {
            val printer: UrovoManager = UrovoManager()
            printer.connect()
            try {
                printBill(context, printer, printerSize, order)
            } catch (e: Exception) {
                Log.d("Error print", e.message.toString())
            }
            // Finish Print
            Thread.sleep(1500)
            printer.disconnect()
        } catch (e: Exception) {
            Log.d("Error print", e.message.toString())
        }

    }
    //endregion

    /*==============================================================================================
   ======================================TCP PART============================================
   ==============================================================================================*/
    //region Tcp
    fun printBillWithTcp(context: Context, printerSize: Int, order: OrderReq) {

        try {
            val policy: StrictMode.ThreadPolicy =
                StrictMode
                    .ThreadPolicy
                    .Builder()
                    .permitAll()
                    .build()

            StrictMode.setThreadPolicy(policy)
            val printer: TcpManager = TcpManager()
            printer.connect()
            try {
                printBill(context, printer, printerSize, order)
            } catch (e: Exception) {
                Log.d("Error print", e.message.toString())
            }

            // Finish Print
            Thread.sleep(1500)
            printer.disconnect()
        } catch (e: Exception) {
            Log.d("Error print", e.message.toString())
        }


    }
    //endregion

    private fun printBill(
        context: Context,
        printer: BasePrintManager,
        charTextPerLine: Int,
        order: OrderReq
    ) {
        val charPerLineHeader = charTextPerLine
        val charPerLinerText = charTextPerLine

        printer.setupPage(384, -1)
        // Bill Status
        printer.drawText(
            StringUtils.removeAccent(
                WaguUtils.columnListDataBlock(
                    charPerLineHeader,
                    mutableListOf(
                        mutableListOf(
                            order.OrderSummary.TableName.toString(),
                            order.OrderSummary.DiningOptionName.toString()
                        )
                    ),
                    mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT)
                )
            ).toString(),
            false,
            BasePrintManager.FontSize.Medium
        )


        // Icon
        val drawable = ContextCompat.getDrawable(context as Activity, R.drawable.ic_note)
        val wrappedDrawable: Drawable = DrawableCompat.wrap(drawable!!)
        DrawableCompat.setTint(wrappedDrawable, Color.BLACK)
        printer.drawBitmap(
            drawableToBitmap(wrappedDrawable),
            BasePrintManager.BitmapAlign.Center
        )

        // Address
        Board(charPerLinerText).let { b ->
            val textContext = StringBuilder()
            val blockAddress = Block(
                b,
                charPerLinerText - 6,
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
        val orderCode = mutableListOf("Order #:", order.Order.Code.toString())
        val employee =
            mutableListOf(
                "Employee :",
                DataHelper.deviceCodeLocalStorage?.Employees?.find { it._id == order.Order.EmployeeGuid }?.FullName.toString()
            )
        val dateCreate = mutableListOf(
            "Create Date :", DateTimeUtils.dateToString(
                DateTimeUtils.strToDate(
                    order.Order.CreateDate,
                    DateTimeUtils.Format.FULL_DATE_UTC_TIMEZONE
                ), DateTimeUtils.Format.DD_MM_YYYY_HH_MM
            )
        )
        val content = WaguUtils.columnListDataBlock(
            charPerLineHeader, mutableListOf(orderCode, employee, dateCreate),
            mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT), isWrapWord = true
        )
        printer.drawText(content)

        printer.drawLine(charPerLinerText)

        // Order detail
        val columnOrderDetailAlign =
            mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT)
        val col1 = 4
        val col3 = 9
        val col2 = charPerLinerText - col1 - col3
        val columnSize = mutableListOf(col1, col2, col3)
        val columnExtraSize = mutableListOf(2, col2 - 2)
        val columnGroupBundle = mutableListOf(4, col2 - 4)
        val columnGroupBundleExtra = mutableListOf(2, col2 - 6)

        val title = mutableListOf("Qty", "Items", "Amount")
        val contentTitle = WaguUtils.columnListDataBlock(
            charPerLineHeader, mutableListOf(title), columnOrderDetailAlign,
            columnSize
        )
        printer.drawText(StringUtils.removeAccent(contentTitle), true)
        order.OrderDetail.OrderProducts.forEach { productChosen ->
            val quantity = "${productChosen.Quantity}x"
            val amount = PriceUtils.formatStringPrice(productChosen.LineTotal ?: 0.0)
            val proName = productChosen.Name1
            val contentName = WaguUtils.columnListDataBlock(
                charPerLineHeader,
                mutableListOf(mutableListOf(quantity, proName, amount)),
                columnOrderDetailAlign,
                columnSize,
                isWrapWord = true
            )
            printer.drawText(StringUtils.removeAccent(contentName), true)

            if (productChosen.ProductTypeId == ProductType.BUNDLE.value) {
                /*val contentBase = WaguUtils.columnListDataBlock(
                    charPerLineHeader, mutableListOf(
                        mutableListOf(
                            "",
                            "Base Price",
                            PriceUtils.formatStringPrice(productChosen.Price ?: 0.0)
                        )
                    ), columnOrderDetailAlign,
                    columnSize
                )
                printer.drawText(StringUtils.removeAccent(contentBase))*/


                productChosen.ProductChoosedList?.forEach { pro ->
                    StringUtils.removeAccent(
                        WaguUtils.columnListDataBlock(
                            col2,
                            mutableListOf(
                                mutableListOf(
                                    "(${pro.Quantity})",
                                    pro.Name1
                                )
                            ),
                            columnOrderDetailAlign,
                            columnGroupBundle,
                        )
                    ).toString().trim().let {
                        WaguUtils.columnListDataBlock(
                            charPerLinerText,
                            mutableListOf(
                                mutableListOf(
                                    "",
                                    it,
                                    if (pro.LineTotal ?: 0.0 <= 0) "" else "+${
                                        PriceUtils.formatStringPrice(
                                            pro.LineTotal ?: 0.0
                                        )
                                    }"
                                )
                            ),
                            columnOrderDetailAlign,
                            columnSize,
                        ).let { line->
                            printer.drawText(
                                line
                            )
                        }

                    }
                    val listInfoGroupExtra = mutableListOf<MutableList<String>>()
                    pro.VariantList?.takeIf { it.isNotEmpty() }?.let {
                        listInfoGroupExtra.add(
                            mutableListOf(
                                "•",
                                ExtraConverter.variantStr(it).toString()
                            )
                        )
                    }
                    pro.ModifierList?.takeIf { it.isNotEmpty() }?.let {
                        listInfoGroupExtra.add(
                            mutableListOf(
                                "•",
                                ExtraConverter.modifierOrderStr(it).toString()
                            )
                        )
                    }
                    pro.Note?.let {
                        listInfoGroupExtra.add(mutableListOf("+", "Note : ${it.trim()}"))
                    }
                    val contentExtraPro = StringUtils.removeAccent(
                        WaguUtils.columnListDataBlock(
                            col2 - 4,
                            listInfoGroupExtra,
                            columnOrderDetailAlign,
                            columnGroupBundleExtra,
                            isWrapWord = true
                        )
                    ).toString()
                    StringUtils.removeAccent(
                        WaguUtils.columnListDataBlock(
                            col2,
                            mutableListOf(
                                mutableListOf(
                                    "",
                                    contentExtraPro.trim()
                                )
                            ),
                            columnOrderDetailAlign,
                            columnGroupBundle,
                        )
                    ).toString().let {
                        printer.drawText(
                            WaguUtils.columnListDataBlock(
                                charPerLinerText,
                                mutableListOf(
                                    mutableListOf(
                                        "",
                                        it,
                                    )
                                ),
                                columnOrderDetailAlign,
                                columnSize,
                            )
                        )
                    }

                }
            }

            val listExtraInfo = mutableListOf<MutableList<String>>()
            productChosen.VariantList?.takeIf { it.isNotEmpty() }?.let {
                listExtraInfo.add(mutableListOf("•", ExtraConverter.variantStr(it).toString()))
            }
            productChosen.ModifierList?.takeIf { it.isNotEmpty() }?.let {
                listExtraInfo.add(
                    mutableListOf(
                        "•",
                        ExtraConverter.modifierOrderStr(it).toString()
                    )
                )
            }
            productChosen.TaxFeeList?.takeIf { it.isNotEmpty() }?.let {
                listExtraInfo.add(
                    mutableListOf(
                        "•",
                        "Tax (${PriceUtils.formatStringPrice(it.sumOf { tax -> tax.TotalPrice })})"
                    )
                )
            }
            productChosen.ServiceFeeList?.takeIf { it.isNotEmpty() }?.let {
                listExtraInfo.add(
                    mutableListOf(
                        "•",
                        "Service (${PriceUtils.formatStringPrice(it.sumOf { ser -> ser.TotalPrice })})"
                    )
                )
            }
            productChosen.SurchargeFeeList?.takeIf { it.isNotEmpty() }?.let {
                listExtraInfo.add(
                    mutableListOf(
                        "•",
                        "Surcharge (${PriceUtils.formatStringPrice(it.sumOf { sur -> sur.TotalPrice })})"
                    )
                )
            }
            productChosen.DiscountList?.takeIf { it.isNotEmpty() }?.let {
                listExtraInfo.add(
                    mutableListOf(
                        "•",
                        "Discount (-${PriceUtils.formatStringPrice(it.sumOf { dis -> dis.DiscountTotalPrice })})"
                    )
                )
            }
            productChosen.Note?.let {
                listExtraInfo.add(mutableListOf("+", "Note : ${it.trim()}"))
            }
            val contentExtra = StringUtils.removeAccent(
                WaguUtils.columnListDataBlock(
                    col2,
                    listExtraInfo,
                    columnOrderDetailAlign,
                    columnExtraSize,
                    isWrapWord = true
                )
            ).toString()
            contentExtra.takeIf { it.isNotBlank() }?.let {
                WaguUtils.columnListDataBlock(
                    charPerLinerText,
                    mutableListOf(mutableListOf("", contentExtra.trim())),
                    columnOrderDetailAlign,
                    columnSize,
                ).let {
                    printer.drawText(
                        it
                    )
                }

            }

        }


        printer.drawLine(charPerLinerText)

        // Note
        order.OrderDetail.Order.Note?.let {
            printer.drawText(StringUtils.removeAccent("Note : $it"))
            printer.drawLine(charPerLinerText)
        }

        // Subtotal
        order.OrderDetail.Order.Subtotal.let {
            val content = WaguUtils.columnListDataBlock(
                charPerLineHeader,
                mutableListOf(mutableListOf("Subtotal", PriceUtils.formatStringPrice(it))),
                mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT)
            )
            printer.drawText(content)
        }
        printer.drawLine(charPerLinerText)

        // Discount
        order.OrderDetail.Order.Discount.let {
            val content = WaguUtils.columnListDataBlock(
                charPerLineHeader,
                mutableListOf(mutableListOf("Discount", PriceUtils.formatStringPrice(-it))),
                mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT)
            )
            printer.drawText(content)
        }
        printer.drawLine(charPerLinerText)

        // Total
        order.OrderDetail.Order.Grandtotal.let {
            val content = WaguUtils.columnListDataBlock(
                charPerLineHeader,
                mutableListOf(mutableListOf("Total", PriceUtils.formatStringPrice(it))),
                mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT)
            )
            printer.drawText(content, true)
        }
        printer.drawLine(charPerLinerText)

        // Cash - Change
        order.OrderDetail.PaymentList?.filter {
            PaymentMethodType.fromInt(it.PaymentTypeId ?: 0) == PaymentMethodType.CASH
        }?.let { list ->
            val totalPay = list.sumOf { it.OverPay ?: 0.0 }
            val needToPay = list.sumOf { it.Payable ?: 0.0 }
            val content = WaguUtils.columnListDataBlock(
                charPerLineHeader, mutableListOf(
                    mutableListOf(
                        "Cash",
                        PriceUtils.formatStringPrice(totalPay)
                    ), mutableListOf(
                        "Change",
                        PriceUtils.formatStringPrice(totalPay - needToPay)
                    )
                ),
                mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT)
            )
            printer.drawText(content)
        }


        printer.drawText(
            WaguUtils.columnListDataBlock(
                charPerLineHeader,
                mutableListOf(mutableListOf("THANK YOU")),
                mutableListOf(Block.DATA_CENTER)
            ),
            true,
            BasePrintManager.FontSize.Medium
        )

        // End Print

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

        view.codeOrder.text = "${order.Order.Code}"
        view.location.text = DataHelper.locationGuid()
        view.nameEmployee.text =
            "${DataHelper.deviceCodeLocalStorage?.Employees?.find { it._id == order.Order.EmployeeGuid }?.FullName}"
        view.createDate.text = DateTimeUtils.dateToString(
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

