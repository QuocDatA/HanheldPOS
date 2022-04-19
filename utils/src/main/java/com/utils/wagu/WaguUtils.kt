package com.utils.wagu

import android.util.Log
import java.lang.StringBuilder
import kotlin.math.log

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
                val textF = wrapWord(items.first(), columnItem)
                val blockFirst =
                    Block(
                        b,
                        columnItem,
                        textF.split("\n").size,
                        textF
                    ).setDataAlign(if (aligns.isEmpty()) Block.DATA_MIDDLE_LEFT else aligns.first())
                        .allowGrid(false)
                var blockPrevious: Block = blockFirst
                b.setInitialBlock(blockFirst)
                items.removeFirst()
                items.forEachIndexed { index, it ->
                    val i = index + 1
                    val sizeItem =
                        (if (columns.size <= i) width - columns.sumOf { size -> size } else columns[i])
                    val textS = wrapWord(it, sizeItem)
                    val blockTemp =
                        Block(b, sizeItem, textS.split("\n").size, textS).setDataAlign(
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

    private fun wrapWord(s: String, width: Int): String {
        val content = StringBuilder()
        val line = mutableListOf<String>()
        val listWord = s.split(' ')
        listWord.forEachIndexed { index, it ->
            if ((if (line.size - 1 > 0) line.size - 1 else 0) + 1 + line.sumOf { word -> word.length } + it.length >= width) {
                content.append(line.joinToString(" ") + "\n")
                line.clear()
            }
            if (it.length >= width && line.isEmpty()) {
                content.append(
                    it.chunked(width)
                        .joinToString("\n") { t -> t.trim() } + if (index >= listWord.size - 1) "\n" else "")
                return@forEachIndexed
            } else
                line.add(it.trim())
            if (index >= listWord.size - 1) {
                content.append(line.joinToString(" "))
            }
        }
        Log.d("Test Wrap", content.toString())
        return content.toString()
    }
}