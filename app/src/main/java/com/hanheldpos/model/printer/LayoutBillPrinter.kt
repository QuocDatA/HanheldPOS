package com.hanheldpos.model.printer

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.handheld.printer.printer_manager.BasePrinterManager
import com.hanheldpos.R
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.order.OrderReq
import com.hanheldpos.model.payment.PaymentMethodType
import com.hanheldpos.model.product.ExtraConverter
import com.hanheldpos.model.product.ProductType
import com.hanheldpos.utils.*
import com.utils.wagu.Block
import com.utils.wagu.Board
import com.utils.wagu.WaguUtils
import java.lang.StringBuilder

class LayoutBillPrinter(
    private val context: Context,
    private val order: OrderReq,
    private val printer: BasePrinterManager,
    private val printOptions: BillPrinterManager.PrintOptions
) {
    private val charPerLineHeader = printOptions.deviceInfo.charsPerLineHeader()
    private val charPerLineText = printOptions.deviceInfo.charsPerLineText()

    fun print() {
        setUpPage()

        // Order Meta Data
        printBillStatus()
        printBrandIcon()
        printAddress()
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
        printDivider()

        // Total
        printTotal()
        printDivider()

        // Cash - Change
        printCashChange()

        // Thank you
        printThankYou()

        // End Print

        printFeedLine(printOptions.deviceInfo.lineFeed())
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
                            order.OrderSummary.TableName.toString(),
                            order.OrderSummary.DiningOptionName.toString()
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
        Board(charPerLineText).let { b ->
            val textContext = StringBuilder()
            val blockAddress = Block(
                b,
                charPerLineText - 6,
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

    }

    private fun printDeliveryAddress() {
        order.OrderDetail.Billing?.let {
            printer.drawText(StringUtils.removeAccent(it.FullName), true)
            it.getFullAddressWithLineBreaker().takeIf { address-> address.isNotEmpty() }?.let { address->
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

    private fun printOrderDetail() {
        val columnOrderDetailAlign =
            mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT)
        val col1 = 4
        val col3 = 9
        val col2 = charPerLineText - col1 - col3
        val columnSize = mutableListOf(col1, col2, col3)
        val columnExtraSize = mutableListOf(2, col2 - 2)
        val columnGroupBundle = mutableListOf(4, col2 - 4)
        val columnGroupBundleExtra = mutableListOf(2, col2 - 6)

        val title = mutableListOf("Qty", "Items", "Amount")
        val contentTitle = WaguUtils.columnListDataBlock(
            charPerLineText, mutableListOf(title), columnOrderDetailAlign,
            columnSize
        )
        printer.drawText(StringUtils.removeAccent(contentTitle), true)
        order.OrderDetail.OrderProducts.forEach { productChosen ->
            val quantity = "${productChosen.Quantity}x"
            val amount = PriceUtils.formatStringPrice(productChosen.LineTotal ?: 0.0)
            val proName = productChosen.Name1
            val contentName = WaguUtils.columnListDataBlock(
                charPerLineText,
                mutableListOf(mutableListOf(quantity, proName, amount)),
                columnOrderDetailAlign,
                columnSize,
                isWrapWord = true
            )
            printer.drawText(StringUtils.removeAccent(contentName), true)

            if (productChosen.ProductTypeId == ProductType.BUNDLE.value)
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

    private fun printDiscounts() {
        order.OrderDetail.Order.Discount.let {
            val content = WaguUtils.columnListDataBlock(
                charPerLineText,
                mutableListOf(mutableListOf("Discount", PriceUtils.formatStringPrice(-it))),
                mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT)
            )
            printer.drawText(content)
        }
    }

    private fun printTotal() {
        order.OrderDetail.Order.Grandtotal.let {
            val content = WaguUtils.columnListDataBlock(
                charPerLineText,
                mutableListOf(mutableListOf("Total", PriceUtils.formatStringPrice(it))),
                mutableListOf(Block.DATA_MIDDLE_LEFT, Block.DATA_MIDDLE_RIGHT)
            )
            printer.drawText(content, true)
        }
    }

    private fun printCashChange() {
        order.OrderDetail.PaymentList?.filter {
            PaymentMethodType.fromInt(it.PaymentTypeId ?: 0) == PaymentMethodType.CASH
        }?.let { list ->
            val totalPay = list.sumOf { it.OverPay ?: 0.0 }
            val needToPay = list.sumOf { it.Payable ?: 0.0 }
            val content = WaguUtils.columnListDataBlock(
                charPerLineText, mutableListOf(
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

    private fun printFeedLine(line: Int) {
        printer.feedLine(line)
    }

    private fun cutPaper() {
        printer.cutPaper()
    }
}