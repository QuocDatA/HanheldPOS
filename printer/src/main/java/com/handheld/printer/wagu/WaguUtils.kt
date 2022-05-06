package com.handheld.printer.wagu

import android.util.Log
import java.lang.StringBuilder

object WaguUtils {
    fun columnListDataBlock(
        width: Int,
        list: MutableList<MutableList<String>>,
        aligns: MutableList<Int>,
        columnSize: MutableList<Int>? = null,
        isWrapWord: Boolean = false,
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
                val textF = if (isWrapWord) wrapWord(items.first(), columnItem) else items.first()
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
                    val textS =if (isWrapWord) wrapWord(it, sizeItem) else it
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
        return content.replace(Regex("[\\n\\r]\$"),"")
    }

    private fun wrapWord(s: String, width: Int): String {
        val content = StringBuilder()
        s.split("\n").forEach { st ->
            val line = mutableListOf<String>()
            val listWord = st.split(' ')
            listWord.forEachIndexed { index, it ->
                if ((if (line.size - 1 > 0) line.size - 1 else 0) + 1 + line.sumOf { word -> word.length } + it.length > width) {
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
            content.append("\n")
        }

        Log.d("Test Wrap", content.toString().trim())
        return content.toString().trim()
    }
}