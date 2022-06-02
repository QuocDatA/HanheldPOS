package com.handheld.printer.wagu

import java.lang.StringBuilder
import kotlin.math.floor

enum class WrapType {
    ELLIPSIS,
    NONE,
    SOFT_WRAP
}

object WaguUtils {
    fun columnListDataBlock(
        width: Int,
        list: MutableList<MutableList<String>>,
        aligns: MutableList<Int>,
        columnSize: MutableList<Int>? = null,
        wrapType: WrapType = WrapType.NONE,
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
            val columns =processColumnSize(width,columnSize,listSize)

            Board(width).let { b ->
                val columnItem = columns.first()
                val textF =  getTextFromWrapType(items.first(), columnItem, wrapType)
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
                    val sizeItem = columns[i]
                    val textS =  getTextFromWrapType(it, sizeItem, wrapType)
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
        return content.replace(Regex("[\\n\\r]\$"), "")
    }

    private fun getTextFromWrapType(src: String, columnSize: Int, wrapType: WrapType): String {
        return when (wrapType) {
            WrapType.NONE -> src
            WrapType.SOFT_WRAP -> wrapWord(src, columnSize)
            WrapType.ELLIPSIS -> {
                if (src.length > columnSize) {
                    val t = src.removeRange(columnSize - 3, src.length - 1)
                    "$t.."
                } else src
            }
        }
    }

    private fun processColumnSize(width : Int,columnSize: MutableList<Int>?, size : Int) : MutableList<Int> {
        return if (columnSize?.isEmpty() != false) {
            val sizeFloor = floor(width.toDouble() / size).toInt()
            if (width % size != 0) {
                val remain = width % size
                val s = mutableListOf<Int>()
                s.addAll(IntArray(remain) { sizeFloor + 1 }.toMutableList())
                s.addAll(remain / 2, IntArray(size - remain) { sizeFloor }.toList())
                s
            } else
                IntArray(size) { sizeFloor }.toMutableList()
        } else if (columnSize.size >= size) {
            columnSize
        } else {
            columnSize.addAll(processColumnSize(width - columnSize.sumOf { size -> size },null,size - columnSize.size))
            columnSize
        }

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
        return content.toString().trim()
    }
}