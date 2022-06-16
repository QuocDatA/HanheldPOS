package com.hanheldpos.printer.layouts.order

import com.hanheldpos.printer.printer_setup.PrintOptions
import com.hanheldpos.printer.printer_setup.printer_manager.BasePrinterManager
import com.hanheldpos.printer.wagu.Block
import com.hanheldpos.printer.wagu.WaguUtils
import com.hanheldpos.printer.wagu.WrapType
import com.hanheldpos.model.order.OrderModel
import com.hanheldpos.model.order.ProductChosen
import com.hanheldpos.model.product.ExtraConverter
import com.hanheldpos.utils.StringUtils


open class KitchenLayout(
    order: OrderModel,
    printer: BasePrinterManager,
    printOptions: PrintOptions,
    isReprint: Boolean,
) : BaseLayoutOrder(
    order, printer, printOptions, isReprint
) {


    final override val leftColumn = printOptions.deviceInfo.leftColumnWidth() / 2
    final override val rightColumn = printOptions.deviceInfo.rightColumnWidth() / 2
    override fun columnExtraSize() = mutableListOf(centerColumn - 2)

    override val centerColumn =
        printOptions.deviceInfo.charsPerLineLarge() - leftColumn - rightColumn


    override fun print() {
        printBillStatus()
        feedLines(printOptions.deviceInfo.linesFeed(1))

        printOrderInfo()
        printDivider()

        printOrderDetail()
        printDivider()

        printNote(drawBottomLine = false)
        feedLines(printOptions.deviceInfo.linesFeed(1))

        printTableNumber()
        feedLines(printOptions.deviceInfo.linesFeed(3))

        cutPaper()
    }

    private fun printOrderDetail() {

        order.OrderDetail.OrderProducts.forEach { productChosen ->
            printer.drawText(
                StringUtils.removeAccent(
                    getProductDetailItemString(productChosen)
                ),
                bold = true,
                size = BasePrinterManager.FontSize.Large
            )
        }
    }

    private fun getProductDetailItemString(productChosen: ProductChosen): String {

        val quantity = "${productChosen.Quantity}x"
        val diningOption = "(${productChosen.DiningOption?.Acronymn})"
        val proName = productChosen.Name1

        val productDetail = mutableListOf<MutableList<String>>()
        productDetail.add(mutableListOf(proName ?: ""))

        productChosen.VariantList?.takeIf { it.isNotEmpty() }?.let {
            productDetail.add(
                mutableListOf(
                    "(${ExtraConverter.variantOrderStr(it, separator = " ").toString()})"
                )
            )
        }

        productChosen.ModifierList?.takeIf { it.isNotEmpty() }?.let {
            productDetail.add(
                mutableListOf(
                    "(${ExtraConverter.modifierOrderStr(it, separator = " ").toString()})"
                )
            )
        }

        productChosen.Note?.takeIf { it.isNotEmpty() }?.let {
            productDetail.add(mutableListOf("(${it.trim()})"))
        }

        var result = ""

        StringUtils.removeAccent(
            WaguUtils.columnListDataBlock(
                centerColumn,
                productDetail,
                columnOrderDetailAlign,
                columnExtraSize(),
                wrapType = WrapType.SOFT_WRAP
            )
        ).toString().takeIf { it.isNotEmpty() }?.let {
            result = WaguUtils.columnListDataBlock(
                charPerLineLarge,
                mutableListOf(
                    mutableListOf(
                        quantity,
                        it.trim(),
                        diningOption
                    )
                ),
                columnOrderDetailAlign,
                columnSize(),
            )
        }

        return result
    }

    private fun printTableNumber() {
        printer.drawText(
            StringUtils.removeAccent(
                WaguUtils.columnListDataBlock(
                    charPerLineLarge,
                    list = mutableListOf(
                        mutableListOf(
                            order.OrderDetail.TableList.firstOrNull()?.TableName.toString()
                        )
                    ),
                    mutableListOf(
                        Block.DATA_MIDDLE_RIGHT
                    ),
                )
            ).toString(),
            false,
            BasePrinterManager.FontSize.Large
        )
    }
}