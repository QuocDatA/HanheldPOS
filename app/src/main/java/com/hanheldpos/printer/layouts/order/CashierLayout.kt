package com.hanheldpos.printer.layouts.order

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.handheld.printer.PrintConstants
import com.handheld.printer.printer_setup.PrintOptions
import com.handheld.printer.printer_setup.printer_manager.BasePrinterManager
import com.handheld.printer.wagu.Block
import com.handheld.printer.wagu.WaguUtils
import com.hanheldpos.PosApp
import com.hanheldpos.R
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.order.OrderFee
import com.hanheldpos.model.order.OrderModel
import com.hanheldpos.model.order.ProductChosen
import com.hanheldpos.model.product.ExtraConverter
import com.hanheldpos.model.product.ProductType
import com.hanheldpos.utils.DrawableHelper
import com.hanheldpos.utils.PriceUtils
import com.hanheldpos.utils.StringUtils


class CashierLayout(
    order: OrderModel,
    printer: BasePrinterManager,
    printOptions: PrintOptions,
    isReprint: Boolean,
) : BaseLayoutOrder(order, printer, printOptions, isReprint) {

    override fun print() {
        // Order Meta Data
        printBillStatus()
        feedLines(printOptions.deviceInfo.linesFeed(1))
        printBrandIcon()
        printAddress()
        feedLines(printOptions.deviceInfo.linesFeed(1))
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
        feedLines(printOptions.deviceInfo.linesFeed(1))

        // Thank you
        printThankYou()

        // End Print

        feedLines(printOptions.deviceInfo.linesFeed(3))
        cutPaper()

        Log.d(PrintConstants.TAG, "Print succeeded.")
    }

    private fun printBrandIcon() {
        val drawable = ContextCompat.getDrawable(PosApp.instance.applicationContext, R.drawable.ic_note)
        val wrappedDrawable: Drawable = DrawableCompat.wrap(drawable!!)
        DrawableCompat.setTint(wrappedDrawable, Color.BLACK)
        printer.drawBitmap(
            DrawableHelper.drawableToBitmap(wrappedDrawable),
            BasePrinterManager.BitmapAlign.Center
        )
    }

    private fun printAddress() {
        WaguUtils.columnListDataBlock(
            charPerLineNormal,
            mutableListOf(
                mutableListOf(
                    "",
                    DataHelper.recentDeviceCodeLocalStorage?.first()?.LocationAddress?.trim() ?: "",
                    ""
                )
            ),
            mutableListOf(Block.DATA_CENTER, Block.DATA_CENTER, Block.DATA_CENTER),
            columnSize = mutableListOf(3, charPerLineNormal - 6, 3),
            isWrapWord = true
        ).let {
            printer.drawText(StringUtils.removeAccent(it))
        }
    }

    private fun printDeliveryAddress() {
        order.OrderDetail.Billing?.let {
            printer.drawText(StringUtils.removeAccent(it.FullName), true)
            it.getFullAddressWithLineBreaker().takeIf { address -> address.isNotEmpty() }
                ?.let { address ->
                    printer.drawText(StringUtils.removeAccent(address))
                }
            var addressType: String? = ""
            val phoneNumber = it.Phone
            DataHelper.addressTypesLocalStorage?.find { addressTypeResp -> addressTypeResp.AddressTypeId == it.AddressTypeId }
                ?.let { address ->
                    addressType = address.AddressTypeEn
                }

            if (!addressType.isNullOrEmpty() || !phoneNumber.isNullOrEmpty())
                printer.drawText(
                    "$addressType${if (addressType.isNullOrEmpty() && phoneNumber.isNullOrEmpty()) "" else " | "}$phoneNumber",
                    true
                )
            it.Note?.let { note ->
                printer.drawText(note, true)
            }
        }
    }

    private fun printOrderDetail() {

        val title = mutableListOf("Qty", "Items", "Amount")
        val contentTitle = WaguUtils.columnListDataBlock(
            charPerLineNormal, mutableListOf(title), columnOrderDetailAlign,
            columnSize()
        )
        printer.drawText(StringUtils.removeAccent(contentTitle), true)
        order.OrderDetail.OrderProducts.forEach { productChosen ->
            printItemDetailForOrder(productChosen)
        }
    }

    private fun printItemDetailForOrder(productChosen: ProductChosen) {
        val quantity = "${productChosen.Quantity}x"
        val amount = PriceUtils.formatStringPrice(productChosen.LineTotal ?: 0.0)
        val proName = productChosen.Name1
        val subtotal = productChosen.Subtotal
        val contentName = WaguUtils.columnListDataBlock(
            charPerLineNormal,
            mutableListOf(
                mutableListOf(
                    quantity,
                    "$proName (${PriceUtils.formatStringPrice(subtotal ?: 0.0)})",
                    amount
                )
            ),
            columnOrderDetailAlign,
            columnSize(),
            isWrapWord = true
        )
        printer.drawText(StringUtils.removeAccent(contentName), true)

        if (productChosen.ProductTypeId == ProductType.BUNDLE.value)
            productChosen.ProductChoosedList?.forEach { pro ->
                StringUtils.removeAccent(
                    WaguUtils.columnListDataBlock(
                        centerColumn,
                        mutableListOf(
                            mutableListOf(
                                "(${pro.Quantity})",
                                pro.Name1 ?: ""
                            )
                        ),
                        columnOrderDetailAlign,
                        columnGroupBundle(),
                    )
                ).toString().trim().let {
                    WaguUtils.columnListDataBlock(
                        charPerLineNormal,
                        mutableListOf(
                            mutableListOf(
                                "",
                                it,
                                if ((pro.LineTotal ?: 0.0) <= 0) "" else "+${
                                    PriceUtils.formatStringPrice(
                                        pro.LineTotal ?: 0.0
                                    )
                                }"
                            )
                        ),
                        columnOrderDetailAlign,
                        columnSize(),
                    ).let { line ->
                        printer.drawText(
                            line
                        )
                    }

                }
                val listInfoGroupExtra = mutableListOf<MutableList<String>>()
                pro.VariantList?.takeIf { it.isNotEmpty() }?.let {
                    listInfoGroupExtra.add(
                        mutableListOf(
                            "*",
                            ExtraConverter.variantStr(it).toString()
                        )
                    )
                }
                pro.ModifierList?.takeIf { it.isNotEmpty() }?.let {
                    listInfoGroupExtra.add(
                        mutableListOf(
                            "*",
                            ExtraConverter.modifierOrderStr(it).toString()
                        )
                    )
                }
                pro.Note?.takeIf { it.isNotEmpty() }?.let {
                    listInfoGroupExtra.add(mutableListOf("+", "Note : ${it.trim()}"))
                }
                val contentExtraPro = StringUtils.removeAccent(
                    WaguUtils.columnListDataBlock(
                        centerColumn - 4,
                        listInfoGroupExtra,
                        columnOrderDetailAlign,
                        columnGroupBundleExtra(),
                        isWrapWord = true
                    )
                ).toString()
                StringUtils.removeAccent(
                    WaguUtils.columnListDataBlock(
                        centerColumn,
                        mutableListOf(
                            mutableListOf(
                                "",
                                contentExtraPro.trim()
                            )
                        ),
                        columnOrderDetailAlign,
                        columnGroupBundle(),
                    )
                ).toString().takeIf { it.isNotEmpty() }?.let {
                    printer.drawText(
                        WaguUtils.columnListDataBlock(
                            charPerLineNormal,
                            mutableListOf(
                                mutableListOf(
                                    "",
                                    it,
                                )
                            ),
                            columnOrderDetailAlign,
                            columnSize(),
                        )
                    )
                }

            }


        val listExtraInfo = mutableListOf<MutableList<String>>()
        productChosen.VariantList?.takeIf { it.isNotEmpty() }?.let {
            listExtraInfo.add(mutableListOf("*", ExtraConverter.variantStr(it).toString()))
        }
        productChosen.ModifierList?.takeIf { it.isNotEmpty() }?.let {
            listExtraInfo.add(
                mutableListOf(
                    "*",
                    ExtraConverter.modifierOrderStr(it).toString()
                )
            )
        }
        productChosen.TaxFeeList?.takeIf { it.isNotEmpty() }?.let {
            listExtraInfo.add(
                mutableListOf(
                    "*",
                    "Tax (${PriceUtils.formatStringPrice(it.sumOf { tax -> tax.TotalPrice })})"
                )
            )
        }
        productChosen.ServiceFeeList?.takeIf { it.isNotEmpty() }?.let {
            listExtraInfo.add(
                mutableListOf(
                    "*",
                    "Service (${PriceUtils.formatStringPrice(it.sumOf { ser -> ser.TotalPrice })})"
                )
            )
        }
        productChosen.SurchargeFeeList?.takeIf { it.isNotEmpty() }?.let {
            listExtraInfo.add(
                mutableListOf(
                    "*",
                    "Surcharge (${PriceUtils.formatStringPrice(it.sumOf { sur -> sur.TotalPrice })})"
                )
            )
        }
        productChosen.DiscountList?.takeIf { it.isNotEmpty() }?.let {
            listExtraInfo.add(
                mutableListOf(
                    "*",
                    "Discount (-${PriceUtils.formatStringPrice(it.sumOf { dis -> dis.DiscountTotalPrice })})"
                )
            )
        }
        productChosen.Note?.takeIf { it.isNotEmpty() }?.let {
            listExtraInfo.add(mutableListOf("+", "Note : ${it.trim()}"))
        }
        val contentExtra = StringUtils.removeAccent(
            WaguUtils.columnListDataBlock(
                centerColumn,
                listExtraInfo,
                columnOrderDetailAlign,
                columnExtraSize(),
                isWrapWord = true
            )
        ).toString()
        contentExtra.takeIf { it.isNotEmpty() }?.let {
            WaguUtils.columnListDataBlock(
                charPerLineNormal,
                mutableListOf(mutableListOf("", contentExtra.trim())),
                columnOrderDetailAlign,
                columnSize(),
            ).let {
                printer.drawText(
                    it
                )
            }

        }
    }


    private fun printSubtotal() {
        order.OrderDetail.Order.Subtotal.let {
            val content = WaguUtils.columnListDataBlock(
                charPerLineNormal,
                mutableListOf(mutableListOf("Subtotal", PriceUtils.formatStringPrice(it))),
                mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT)
            )
            printer.drawText(content)
        }
    }

    private fun printFees() {
        val feesList = mutableListOf<OrderFee>()

        order.OrderDetail.run {
            feesList.addAll(this.ServiceFeeList ?: mutableListOf())
            feesList.addAll(this.ShippingFeeList ?: mutableListOf())
            feesList.addAll(this.SurchargeFeeList ?: mutableListOf())
            feesList.addAll(this.TaxFeeList ?: mutableListOf())
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
            printer.drawText(content)
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

            printer.drawText(content)
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
            printer.drawText(content, true, BasePrinterManager.FontSize.Large)
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
            printer.drawText(content)
        }
    }

    private fun printThankYou() {
        printer.drawText(
            WaguUtils.columnListDataBlock(
                charPerLineLarge,
                mutableListOf(mutableListOf("THANK YOU")),
                mutableListOf(Block.DATA_CENTER)
            ),
            true,
            BasePrinterManager.FontSize.Large
        )
    }

}