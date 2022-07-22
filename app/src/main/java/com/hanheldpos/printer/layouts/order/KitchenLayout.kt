package com.hanheldpos.printer.layouts.order

import com.hanheldpos.model.cart.DinningOptionType
import com.hanheldpos.model.order.OrderModel
import com.hanheldpos.model.order.ProductChosen
import com.hanheldpos.model.product.ExtraConverter
import com.hanheldpos.model.product.ProductType
import com.hanheldpos.printer.printer_devices.Printer
import com.hanheldpos.printer.printer_setup.printer_manager.BasePrinterManager
import com.hanheldpos.printer.wagu.Block
import com.hanheldpos.printer.wagu.WaguUtils
import com.hanheldpos.printer.wagu.WrapType
import com.hanheldpos.utils.StringUtils


open class KitchenLayout(
    order: OrderModel,
    private val printer: Printer,
    isReprint: Boolean,
) : BaseLayoutOrder(
    order, printer, isReprint
) {


    final override val leftColumn = printConfig.deviceInfo.leftColumnWidth() / 2
    final override val rightColumn = printConfig.deviceInfo.rightColumnWidth() / 2
    override fun columnExtraSize() = mutableListOf(centerColumn - 2)

    override val centerColumn =
        printConfig.deviceInfo.charsPerLineLarge() - leftColumn - rightColumn


    override fun print() {
        printBillStatus()
        feedLines(printConfig.deviceInfo.linesFeed(1))

        printOrderInfo()
        printDivider()

        printOrderDetail()
        printDivider()

        printNote(drawBottomLine = false)
        feedLines(printConfig.deviceInfo.linesFeed(1))

        printTableNumber()
        feedLines(printConfig.deviceInfo.linesFeed(3))

        cutPaper()
    }

    private fun printOrderDetail() {

        order.OrderDetail.OrderProducts.forEach { productChosen ->
            if (productChosen.printable(printer.printingSpecification.printerTypeId)) {
                printProduct(productChosen, level = 0)
            }
        }
    }

    private fun printProduct(
        productChosen: ProductChosen,
        level: Int = 0,
        parentQuantity: Int? = 1
    ) {
        val quantity = "${productChosen.Quantity * (parentQuantity ?: 1)} "
        val diningOption = "(${productChosen.DiningOption?.Acronymn})"

        val isBundleOrBuyXGetY =
            productChosen.ProductTypeId == ProductType.BUNDLE.value || productChosen.ProductTypeId == ProductType.BUYX_GETY_DISC.value


        if (!productChosen.printable(printer.printingSpecification.printerTypeId)) {
            return
        }

        val productString =

            productChosen.Name1 +
                    (if (isBundleOrBuyXGetY) ":" else "") +
                    (if (productChosen.VariantList?.isNotEmpty() == true)
                        ExtraConverter.variantStr(
                            productChosen.VariantList,
                            separator = " "
                        )
                    else "") +
                    (if (productChosen.ModifierList?.isNotEmpty() == true) ExtraConverter.modifierOrderStr(
                        productChosen.ModifierList,
                        separator = " "
                    ).toString() else "") +
                    (if (productChosen.Note.isNullOrBlank()) ""
                    else
                        "(${productChosen.Note})")


        device.drawText(
            StringUtils.removeAccent(
                WaguUtils.columnListDataBlock(
                    charPerLineLarge,
                    mutableListOf(
                        mutableListOf(
                            "",
                            WaguUtils.columnListDataBlock(
                                charPerLineLarge - 5 - level * 2,
                                mutableListOf(
                                    mutableListOf(
                                        (if (level % 2 == 1) "-" else "") +
                                                (if (isBundleOrBuyXGetY) "" else quantity) +
                                                productString
                                    )
                                ),
                                aligns = mutableListOf(Block.DATA_MIDDLE_LEFT),
                                columnSize = mutableListOf(
                                    charPerLineLarge - 5 - level * 2
                                ),
                                wrapType = WrapType.SOFT_WRAP
                            ),
                            if (level == 0)
                                if (productChosen.DiningOption?.Id == DinningOptionType.TaiBan.value)
                                    ""
                                else
                                    diningOption
                            else "",
                        ),
                    ),
                    mutableListOf(
                        Block.DATA_MIDDLE_LEFT,
                        Block.DATA_MIDDLE_LEFT,
                        Block.DATA_MIDDLE_RIGHT,
                    ),
                    columnSize = mutableListOf(level * 2, charPerLineLarge - 5 - level * 2, 5),

                    )
            ),
            bold = true,
            size = BasePrinterManager.FontSize.Large
        )

        if (isBundleOrBuyXGetY) {
            productChosen.ProductChoosedList?.forEach {
                printProduct(it, level + 1, parentQuantity = productChosen.Quantity)
            }
        }
    }

    private fun printTableNumber() {
        device.drawText(
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