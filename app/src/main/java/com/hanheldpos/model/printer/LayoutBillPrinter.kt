package com.hanheldpos.model.printer

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.handheld.printer.printer_manager.BasePrinterManager
import com.hanheldpos.R
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.order.OrderFee
import com.hanheldpos.model.order.OrderModel
import com.hanheldpos.model.order.ProductChosen
import com.hanheldpos.model.product.ExtraConverter
import com.hanheldpos.model.product.ProductType
import com.hanheldpos.utils.*
import com.handheld.printer.wagu.Block
import com.handheld.printer.wagu.WaguUtils

class LayoutBillPrinter(
    private val context: Context,
    private val order: OrderModel,
    private val printer: BasePrinterManager,
    private val printOptions: BillPrinterManager.PrintOptions
) {
    private val charPerLineHeader = printOptions.deviceInfo.charsPerLineLarge()
    private val charPerLineText = printOptions.deviceInfo.charsPerLineNormal()

    fun print() {
        setUpPage()

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

        // Cash - Change
        printPayments()
        feedLines(printOptions.deviceInfo.linesFeed(1))

        // Thank you
        printThankYou()

        // End Print

        feedLines(printOptions.deviceInfo.linesFeed(3))
        cutPaper()
    }


    private fun setUpPage() {
        printer.setupPage(printOptions.deviceInfo.paperWidth(), -1f)
    }


    // region print sections

    private fun printDivider() {
        printer.drawLine(charPerLineText)
    }

    private fun printBillStatus() {
        printer.drawText(
            StringUtils.removeAccent(
                WaguUtils.columnListDataBlock(
                    charPerLineHeader,
                    mutableListOf(
                        mutableListOf(
                            order.OrderDetail.TableList.firstOrNull()?.TableName.toString(),
                            order.OrderDetail.DiningOption.Title.toString()
                        )
                    ),
                    mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT)
                )
            ).toString(),
            false,
            BasePrinterManager.FontSize.Medium
        )
    }

    private fun printBrandIcon() {
        val drawable = ContextCompat.getDrawable(context as Activity, R.drawable.ic_note)
        val wrappedDrawable: Drawable = DrawableCompat.wrap(drawable!!)
        DrawableCompat.setTint(wrappedDrawable, Color.BLACK)
        printer.drawBitmap(
            DrawableHelper.drawableToBitmap(wrappedDrawable),
            BasePrinterManager.BitmapAlign.Center
        )
    }

    private fun printAddress() {
        WaguUtils.columnListDataBlock(
            charPerLineText,
            mutableListOf(
                mutableListOf(
                    "",
                    DataHelper.recentDeviceCodeLocalStorage?.first()?.LocationAddress?.trim() ?: "",
                    ""
                )
            ),
            mutableListOf(Block.DATA_CENTER, Block.DATA_CENTER, Block.DATA_CENTER),
            columnSize = mutableListOf(3, charPerLineText - 6, 3),
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

    private fun printOrderInfo() {
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
            charPerLineText, mutableListOf(orderCode, employee, dateCreate),
            mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT), isWrapWord = true
        )
        printer.drawText(content)
    }


    private val columnOrderDetailAlign = mutableListOf(
        Block.DATA_MIDDLE_LEFT,
        Block.DATA_MIDDLE_LEFT,
        Block.DATA_MIDDLE_RIGHT
    )

    private val leftColumn = printOptions.deviceInfo.leftColumnWidth()
    private val rightColumn = printOptions.deviceInfo.rightColumnWidth()
    private val centerColumn = printOptions.deviceInfo.centerColumnWidth()

    private val columnSize = mutableListOf(leftColumn, centerColumn, rightColumn)
    private val columnExtraSize = mutableListOf(2, centerColumn - 2)
    private val columnGroupBundle = mutableListOf(4, centerColumn - 4)
    private val columnGroupBundleExtra = mutableListOf(2, centerColumn - 6)
    private fun printOrderDetail() {

        val title = mutableListOf("Qty", "Items", "Amount")
        val contentTitle = WaguUtils.columnListDataBlock(
            charPerLineText, mutableListOf(title), columnOrderDetailAlign,
            columnSize
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
            charPerLineText,
            mutableListOf(
                mutableListOf(
                    quantity,
                    "$proName (${PriceUtils.formatStringPrice(subtotal ?: 0.0)})",
                    amount
                )
            ),
            columnOrderDetailAlign,
            columnSize,
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
                        ) ,
                        columnOrderDetailAlign,
                        columnGroupBundle,
                    )
                ).toString().trim().let {
                    WaguUtils.columnListDataBlock(
                        charPerLineText,
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
                        columnGroupBundleExtra,
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
                        columnGroupBundle,
                    )
                ).toString().takeIf { it.isNotEmpty() }?.let {
                    printer.drawText(
                        WaguUtils.columnListDataBlock(
                            charPerLineText,
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
                columnExtraSize,
                isWrapWord = true
            )
        ).toString()
        contentExtra.takeIf { it.isNotEmpty() }?.let {
            WaguUtils.columnListDataBlock(
                charPerLineText,
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

    private fun printNote() {
        order.OrderDetail.Order.Note?.let {
            printer.drawText(StringUtils.removeAccent("Note : $it"))
            printer.drawLine(charPerLineText)
        }
    }

    private fun printSubtotal() {
        order.OrderDetail.Order.Subtotal.let {
            val content = WaguUtils.columnListDataBlock(
                charPerLineText,
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
                charPerLineText,
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
        if (discounts.isNullOrEmpty()) return

        discounts.forEach { discount ->
            val content = WaguUtils.columnListDataBlock(
                charPerLineText,
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

    private fun feedLines(line: Int) {
        printer.feedLines(line)
    }

    private fun printTotal() {
        order.OrderDetail.Order.Grandtotal.let {
            val content = WaguUtils.columnListDataBlock(
                charPerLineHeader,
                mutableListOf(mutableListOf("Total", PriceUtils.formatStringPrice(it))),
                mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT)
            )
            printer.drawText(content, true, BasePrinterManager.FontSize.Medium)
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
                charPerLineText, lines,
                mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT)
            )
            printer.drawText(content)
        }
    }

    private fun printThankYou() {
        printer.drawText(
            WaguUtils.columnListDataBlock(
                charPerLineHeader,
                mutableListOf(mutableListOf("THANK YOU")),
                mutableListOf(Block.DATA_CENTER)
            ),
            true,
            BasePrinterManager.FontSize.Medium
        )
    }

    private fun cutPaper() {
        printer.cutPaper()
    }
}