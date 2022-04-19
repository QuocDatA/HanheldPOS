package com.utils.wagu

import java.lang.StringBuilder

object WaguUtils {
    fun columnListDataBlock(
        width: Int,
        list: MutableList<MutableList<String>>,
        aligns: MutableList<Int>,
        columnSize: MutableList<Int>? = null
    ): String {
        aligns.forEach { dataAlign ->
            if (dataAlign == Block.DATA_TOP_LEFT || dataAlign == Block.DATA_TOP_MIDDLE || dataAlign == Block.DATA_TOP_RIGHT || dataAlign == Block.DATA_MIDDLE_LEFT || dataAlign == Block.DATA_CENTER || dataAlign == Block.DATA_MIDDLE_RIGHT || dataAlign == Block.DATA_BOTTOM_LEFT || dataAlign == Block.DATA_BOTTOM_MIDDLE || dataAlign == Block.DATA_BOTTOM_RIGHT) {

            } else {
                throw RuntimeException("Invalid data align mode. $dataAlign given.")
            }
        }
        val content = StringBuilder()
        list.forEach lit@{ items ->
            if (items.isEmpty()) return@lit
            val listSize = items.size
            val columns =
                if (columnSize?.isNullOrEmpty() != false) mutableListOf(width / listSize) else columnSize
            Board(width).let { b ->
                val columnItem = columns.first()
                val blockFirst =
                    Block(
                        b,
                        columnItem,
                        items.first().chunked(columnItem).size,
                        items.first()
                    ).setDataAlign(if (aligns.isEmpty()) Block.DATA_MIDDLE_LEFT else aligns.first())
                        .allowGrid(false)
                var blockPrevious: Block = blockFirst
                b.setInitialBlock(blockFirst)
                items.removeFirst()
                items.forEachIndexed { index, it ->
                    val i = index + 1
                    val sizeItem =
                        (if (columns.size <= i) width - columns.sumOf { size -> size } else columns[i] )
                    val blockTemp =
                        Block(b, sizeItem,it.chunked(sizeItem).size , it).setDataAlign(
                            if (aligns.isEmpty() || aligns.size <= i) Block.DATA_MIDDLE_LEFT else aligns[i]
                        ).allowGrid(false)
                    blockPrevious.rightBlock = blockTemp
                    blockPrevious = blockTemp
                }
                content.append(b.invalidate().build().preview)
            }
        }
        return content.toString()
    }
}