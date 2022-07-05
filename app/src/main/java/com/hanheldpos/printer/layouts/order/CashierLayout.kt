package com.hanheldpos.printer.layouts.order

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.hanheldpos.printer.PrintConstants
import com.hanheldpos.printer.printer_setup.printer_manager.BasePrinterManager
import com.hanheldpos.printer.wagu.Block
import com.hanheldpos.printer.wagu.WaguUtils
import com.hanheldpos.printer.wagu.WrapType
import com.hanheldpos.PosApp
import com.hanheldpos.R
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.order.OrderFee
import com.hanheldpos.model.order.OrderModel
import com.hanheldpos.model.order.ProductChosen
import com.hanheldpos.model.product.ExtraConverter
import com.hanheldpos.model.product.ProductType
import com.hanheldpos.printer.printer_devices.Printer
import com.hanheldpos.utils.DrawableHelper
import com.hanheldpos.utils.PriceUtils
import com.hanheldpos.utils.StringUtils


class CashierLayout(
    order: OrderModel,
    private val printer: Printer,
    isReprint: Boolean,
) : BaseLayoutOrder(order, printer, isReprint) {

    override fun print() {
        // Order Meta Data
        printBillStatus()
        feedLines(printConfig.deviceInfo.linesFeed(1))
        printBrandIcon()
        printAddress()
        feedLines(printConfig.deviceInfo.linesFeed(1))
        printDeliveryAddress()
        printDivider()

        // Order Info
        printOrderInfo()
        printDivider()

        // Order Detail
        printOrderDetail()
        printDivider()

        // Note
        printNote()

        // Subtotal
        printSubtotal()
        printDivider()


        // Discount
        printDiscounts()

        // Fee
        printFees()

        // Total
        printTotal()
        printDivider()

        // Payments - Change
        printPayments()
        feedLines(printConfig.deviceInfo.linesFeed(1))

        // Thank you
        printThankYou()

        // End Print

        feedLines(printConfig.deviceInfo.linesFeed(3))
        cutPaper()

        Log.d(PrintConstants.TAG, "Print succeeded.")
    }

    private fun printBrandIcon() {
        val drawable =
            ContextCompat.getDrawable(PosApp.instance.applicationContext, R.drawable.ic_note)
        val wrappedDrawable: Drawable = DrawableCompat.wrap(drawable!!)
        DrawableCompat.setTint(wrappedDrawable, Color.BLACK)
        device.drawBitmap(
            DrawableHelper.drawableToBitmap(wrappedDrawable),
            BasePrinterManager.BitmapAlign.Center
        )
    }

    private fun printAddress() {
        val deviceInfo = DataHelper.recentDeviceCodeLocalStorage?.first()
        WaguUtils.columnListDataBlock(
            charPerLineNormal,
            mutableListOf(
                mutableListOf(
                    "",
                    deviceInfo?.LocationAddress?.trim() ?: "",

                    ""
                ),
                mutableListOf("", "Tel: ${deviceInfo?.Phone?.trim()}", "")
            ),
            mutableListOf(Block.DATA_CENTER, Block.DATA_CENTER, Block.DATA_CENTER),
            columnSize = mutableListOf(3, charPerLineNormal - 6, 3),
            wrapType = WrapType.SOFT_WRAP
        ).let {
            device.drawText(StringUtils.removeAccent(it))
        }
    }

    private fun printDeliveryAddress() {
        order.OrderDetail.Billing?.let {
            device.drawText(StringUtils.removeAccent(it.FullName), true)
            it.getFullAddressWithLineBreaker().takeIf { address -> address.isNotEmpty() }
                ?.let { address ->
                    device.drawText(StringUtils.removeAccent(address))
                }
            var addressType: String? = ""
            val phoneNumber = it.Phone
            DataHelper.addressTypesLocalStorage?.find { addressTypeResp -> addressTypeResp.AddressTypeId == it.AddressTypeId }
                ?.let { address ->
                    addressType = address.AddressTypeEn
                }

            if (!addressType.isNullOrEmpty() || !phoneNumber.isNullOrEmpty())
                device.drawText(
                    "$addressType${if (addressType.isNullOrEmpty() && phoneNumber.isNullOrEmpty()) "" else " | "}$phoneNumber",
                    true
                )
            it.Note?.let { note ->
                device.drawText(note, true)
            }
        }
    }

    private fun printOrderDetail() {

        val title = mutableListOf("Qty", "Items", "Amount")
        val contentTitle = WaguUtils.columnListDataBlock(
            charPerLineNormal, mutableListOf(title), columnOrderDetailAlign,
            columnSize()
        )
        device.drawText(StringUtils.removeAccent(contentTitle), true)
        order.OrderDetail.OrderProducts.forEach { productChosen ->
            printProduct(productChosen)
        }
    }

    private fun printProduct(productChosen: ProductChosen, level: Int = 0) {
        // Process detail product
        val quantity = when (true) {
            (level == 0) -> "${productChosen.Quantity}x"
            else -> "(${productChosen.Quantity})"
        }
        val amount = if ((productChosen.LineTotal
                ?: 0.0) > 0.0
        ) ((if (level > 0) "+" else "") + PriceUtils.formatStringPrice(
            productChosen.LineTotal ?: 0.0
        )) else ""

        val proName = productChosen.Name1
        val subtotal = productChosen.Subtotal
        val isBundleOrBuyXGetY =
            productChosen.ProductTypeId == ProductType.BUNDLE.value || productChosen.ProductTypeId == ProductType.BUYX_GETY_DISC.value
        // Print Header
        val titleColumnSize = charPerLineNormal - rightColumn - level * leftColumn
        val contentHeader = StringUtils.removeAccent(
            WaguUtils.columnListDataBlock(
                titleColumnSize,
                mutableListOf(
                    mutableListOf(
                        quantity,
                        "$proName " + if ((subtotal ?: 0.0) > 0) ("(${
                            PriceUtils.formatStringPrice(
                                subtotal ?: 0.0
                            )
                        })") else "",
                    )
                ),
                columnOrderDetailAlign,
                mutableListOf(leftColumn, titleColumnSize - leftColumn),
                wrapType = WrapType.SOFT_WRAP
            )
        )

        device.drawText(
            WaguUtils.columnListDataBlock(
                charPerLineNormal,
                mutableListOf(
                    mutableListOf(
                        "",
                        contentHeader.toString(),
                        amount
                    )
                ),
                columnOrderDetailAlign,
                mutableListOf(level * leftColumn, titleColumnSize, rightColumn)
            ), true
        )




        if (isBundleOrBuyXGetY) {
            productChosen.ProductChoosedList?.forEach {
                printProduct(it, level + 1)
            }
        }

        // Print Extra
        val extraColumnSize =
            charPerLineNormal - rightColumn - level * leftColumn - 2 // 2 is space from * to text
        val listInfoGroupExtra = mutableListOf<MutableList<String>>()
        productChosen.VariantList?.takeIf { it.isNotEmpty() }?.let {
            listInfoGroupExtra.add(
                mutableListOf(
                    "*",
                    ExtraConverter.variantOrderStr(it).toString()
                )
            )
        }
        productChosen.ModifierList?.takeIf { it.isNotEmpty() }?.let {
            listInfoGroupExtra.add(
                mutableListOf(
                    "*",
                    ExtraConverter.modifierOrderStr(it).toString()
                )
            )
        }

        productChosen.TaxFeeList?.takeIf { it.isNotEmpty() }?.let {
            listInfoGroupExtra.add(
                mutableListOf(
                    "*",
                    "Tax (${PriceUtils.formatStringPrice(it.sumOf { tax -> tax.TotalPrice })})"
                )
            )
        }
        productChosen.ServiceFeeList?.takeIf { it.isNotEmpty() }?.let {
            listInfoGroupExtra.add(
                mutableListOf(
                    "*",
                    "Service (${PriceUtils.formatStringPrice(it.sumOf { ser -> ser.TotalPrice })})"
                )
            )
        }
        productChosen.SurchargeFeeList?.takeIf { it.isNotEmpty() }?.let {
            listInfoGroupExtra.add(
                mutableListOf(
                    "*",
                    "Surcharge (${PriceUtils.formatStringPrice(it.sumOf { sur -> sur.TotalPrice })})"
                )
            )
        }
        productChosen.DiscountList?.takeIf { it.isNotEmpty() }?.let {
            listInfoGroupExtra.add(
                mutableListOf(
                    "*",
                    "Discount (-${PriceUtils.formatStringPrice(it.sumOf { dis -> dis.DiscountTotalPrice })})"
                )
            )
        }

        productChosen.Note?.takeIf { it.isNotEmpty() }?.let {
            listInfoGroupExtra.add(mutableListOf("+", "Note : ${it.trim()}"))
        }

        val contentExtra = StringUtils.removeAccent(
            WaguUtils.columnListDataBlock(
                extraColumnSize,
                listInfoGroupExtra,
                columnOrderDetailAlign,
                mutableListOf(2, extraColumnSize - 2),
                wrapType = WrapType.SOFT_WRAP
            )
        )
        if (contentExtra?.isNotEmpty() == true)
            device.drawText(
                WaguUtils.columnListDataBlock(
                    charPerLineNormal,
                    mutableListOf(
                        mutableListOf(
                            "",
                            contentExtra,
                            ""
                        )
                    ),
                    columnOrderDetailAlign,
                    mutableListOf(level * leftColumn + 2, extraColumnSize, rightColumn)
                ), true
            )
    }


    private fun printSubtotal() {
        order.OrderDetail.Order.Subtotal.let {
            val content = WaguUtils.columnListDataBlock(
                charPerLineNormal,
                mutableListOf(mutableListOf("Subtotal", PriceUtils.formatStringPrice(it))),
                mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT)
            )
            device.drawText(content)
        }
    }

    private fun printFees() {
        val feesList = mutableListOf<OrderFee>()

        order.OrderDetail.run {
            feesList.addAll(this.ServiceFeeList)
            feesList.addAll(this.ShippingFeeList ?: mutableListOf())
            feesList.addAll(this.SurchargeFeeList)
            feesList.addAll(this.TaxFeeList)
        }

        if (feesList.isEmpty()) return

        feesList.forEach { fee ->
            val content = WaguUtils.columnListDataBlock(
                charPerLineNormal,
                mutableListOf(
                    mutableListOf(
                        fee.FeeName.toString(),
                        PriceUtils.formatStringPrice(fee.FeeValue ?: 0.0)
                    ),
                ),
                mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT)
            )
            View.GONE
            device.drawText(content)
        }
        printDivider()
    }

    private fun printDiscounts() {
        val discounts = order.OrderDetail.DiscountList
        if (discounts.isEmpty()) return

        discounts.forEach { discount ->
            val content = WaguUtils.columnListDataBlock(
                charPerLineNormal,
                mutableListOf(
                    mutableListOf(
                        discount.DiscountName,
                        PriceUtils.formatStringPrice(-(discount.DiscountTotalPrice ?: 0.0))
                    ),
                ),
                mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT)
            )

            device.drawText(content)
        }
        printDivider()
    }

    private fun printTotal() {
        order.OrderDetail.Order.Grandtotal.let {
            val content = WaguUtils.columnListDataBlock(
                charPerLineLarge,
                mutableListOf(mutableListOf("Total", PriceUtils.formatStringPrice(it))),
                mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT)
            )
            device.drawText(content, true, BasePrinterManager.FontSize.Large)
        }
    }

    private fun printPayments() {
        order.OrderDetail.PaymentList?.let { list ->
            val totalPay = list.sumOf { it.OverPay ?: 0.0 }
            val needToPay = list.sumOf { it.Payable ?: 0.0 }
            val lines = list.map { paymentOrder ->
                mutableListOf(
                    paymentOrder.Title.toString(),
                    PriceUtils.formatStringPrice(paymentOrder.OverPay ?: 0.0)
                )
            }.toMutableList()
            lines.add(
                mutableListOf(
                    "Change",
                    PriceUtils.formatStringPrice(totalPay - needToPay)
                )
            )
            val content = WaguUtils.columnListDataBlock(
                charPerLineNormal, lines,
                mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT)
            )
            device.drawText(content)
        }
    }

    private fun printThankYou() {
        DataHelper.receiptCashierLocalStorage?.let {
            device.drawText(
                WaguUtils.columnListDataBlock(
                    charPerLineNormal,
                    mutableListOf(mutableListOf("", it.AdditionalText_ReturnPolicy.trim(), "")),
                    mutableListOf(Block.DATA_CENTER, Block.DATA_CENTER, Block.DATA_CENTER),
                    columnSize = mutableListOf(1, charPerLineNormal - 2, 1),
                    wrapType = WrapType.SOFT_WRAP

                ),
                true,
                BasePrinterManager.FontSize.Small
            )
            feedLines(3)
            device.drawText(
                WaguUtils.columnListDataBlock(
                    charPerLineLarge,
                    mutableListOf(mutableListOf(it.AdditionalText_CustomText)),
                    mutableListOf(Block.DATA_CENTER)
                ),
                true,
                BasePrinterManager.FontSize.Large
            )
        }


    }

}