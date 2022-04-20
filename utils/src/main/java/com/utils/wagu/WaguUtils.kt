package com.utils.wagu

import java.lang.StringBuilder

object WaguUtils {
    fun columnListDataBlock(width : Int,list : MutableList<MutableList<String>>, aligns : MutableList<Int>) : String{
        aligns.forEach { dataAlign->
            if (dataAlign == Block.DATA_TOP_LEFT || dataAlign == Block.DATA_TOP_MIDDLE || dataAlign == Block.DATA_TOP_RIGHT || dataAlign == Block.DATA_MIDDLE_LEFT || dataAlign == Block.DATA_CENTER || dataAlign == Block.DATA_MIDDLE_RIGHT || dataAlign == Block.DATA_BOTTOM_LEFT || dataAlign == Block.DATA_BOTTOM_MIDDLE || dataAlign == Block.DATA_BOTTOM_RIGHT) {

            } else {
                throw RuntimeException("Invalid data align mode. $dataAlign given.")
            }
        }
        val content = StringBuilder()
        list.forEach lit@ { items->
            if (items.isEmpty()) return@lit
            Board(width).let { b ->
                val blockFirst =
                    Block(
                        b,
                        width / items.size,
                        1,
                        items.first()
                    ).setDataAlign(if (aligns.isEmpty())  Block.DATA_MIDDLE_LEFT else  aligns.first())
                        .allowGrid(false)
                var blockPrevious : Block = blockFirst
                b.setInitialBlock(blockFirst)
                items.removeFirst()
                items.forEach {
                    val blockTemp = Block(b,width/items.size,1,it).setDataAlign(if (aligns.isEmpty())  Block.DATA_MIDDLE_LEFT else  aligns.first())
                    blockPrevious.rightBlock = blockTemp
                    blockPrevious = blockTemp
                }
                content.append(b.invalidate().build().preview)
            }
        }
        return content.toString()
    }
}