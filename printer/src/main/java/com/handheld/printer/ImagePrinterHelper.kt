package com.handheld.printer

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint

class ImagePrinterHelper(bitmap: Bitmap?) {

    init {
        init(bitmap)
    }

    private var canvas: Canvas? = null

    private var paint: Paint? = null

    private var bm: Bitmap? = null
    var width = 0
    var length = 0.0f

    private var bitbuf: ByteArray? = null

    fun getLength(): Int {
        return length.toInt()
    }

    fun init(bitmap: Bitmap?) {
        if (null != bitmap) {
            initCanvas(bitmap.width)
        }
        if (null == paint) {
            initPaint()
        }
        if (null != bitmap) {
            drawImage(0f, 0f, bitmap)
        }
    }

    fun initCanvas(w: Int) {
        val h = 10 * w
        bm = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565)
        canvas = Canvas(bm!!)
        canvas!!.drawColor(-1)
        width = w
        bitbuf = ByteArray(width / 8)
    }

    fun initPaint() {
        paint = Paint() // 新建一个画笔
        paint!!.isAntiAlias = true //
        paint!!.color = -16777216
        paint!!.style = Paint.Style.STROKE
    }

    /**
     * draw bitmap
     */
    fun drawImage(x: Float, y: Float, btm: Bitmap?) {
        try {
            // Bitmap btm = BitmapFactory.decodeFile(path);
            btm?.let {
                canvas?.drawBitmap(it, x, y, null)
                if (length < y + btm.height) length = y + btm.height
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            btm?.recycle()
        }
    }

    /**
     * 使用光栅位图打印
     *
     * @return 字节
     */
    fun printDraw(): ByteArray? {
        bm ?: return null
        val nbm: Bitmap = Bitmap
            .createBitmap(bm!!, 0, 0, width, getLength())
        val imgbuf = ByteArray(width / 8 * getLength() + 8)
        var s = 0

        // 打印光栅位图的指令
        imgbuf[0] = 29 // 十六进制0x1D
        imgbuf[1] = 118 // 十六进制0x76
        imgbuf[2] = 48 // 30
        imgbuf[3] = 0 // 位图模式 0,1,2,3
        // 表示水平方向位图字节数（xL+xH × 256）
        imgbuf[4] = (width / 8).toByte()
        imgbuf[5] = 0
        // 表示垂直方向位图点数（ yL+ yH × 256）
        imgbuf[6] = (getLength() % 256).toByte() //
        imgbuf[7] = (getLength() / 256).toByte()
        s = 7
        for (i in 0 until getLength()) { // 循环位图的高度
            for (k in 0 until width / 8) { // 循环位图的宽度
                val c0: Int = nbm.getPixel(k * 8 + 0, i) // 返回指定坐标的颜色
                var p0: Int
                p0 = if (c0 == -1) // 判断颜色是不是白色
                    0 // 0,不打印该点
                else {
                    1 // 1,打印该点
                }
                val c1: Int = nbm.getPixel(k * 8 + 1, i)
                var p1: Int
                p1 = if (c1 == -1) 0 else {
                    1
                }
                val c2: Int = nbm.getPixel(k * 8 + 2, i)
                var p2: Int
                p2 = if (c2 == -1) 0 else {
                    1
                }
                val c3: Int = nbm.getPixel(k * 8 + 3, i)
                var p3: Int
                p3 = if (c3 == -1) 0 else {
                    1
                }
                val c4: Int = nbm.getPixel(k * 8 + 4, i)
                var p4: Int
                p4 = if (c4 == -1) 0 else {
                    1
                }
                val c5: Int = nbm.getPixel(k * 8 + 5, i)
                var p5: Int
                p5 = if (c5 == -1) 0 else {
                    1
                }
                val c6: Int = nbm.getPixel(k * 8 + 6, i)
                var p6: Int
                p6 = if (c6 == -1) 0 else {
                    1
                }
                val c7: Int = nbm.getPixel(k * 8 + 7, i)
                var p7: Int
                p7 = if (c7 == -1) 0 else {
                    1
                }
                val value = p0 * 128 + p1 * 64 + p2 * 32 + p3 * 16 + p4 * 8 + p5 * 4 + p6 * 2 + p7
                bitbuf!![k] = value.toByte()
            }
            for (t in 0 until width / 8) {
                s++
                imgbuf[s] = bitbuf!![t]
            }
        }
        if (null != bm) {
            bm!!.recycle()
            bm = null
        }
        return imgbuf
    }
}