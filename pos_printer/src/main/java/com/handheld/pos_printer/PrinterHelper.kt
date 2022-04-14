package com.handheld.pos_printer

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dantsu.escposprinter.EscPosCharsetEncoding
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.EscPosPrinterCommands
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.dantsu.escposprinter.connection.usb.UsbPrintersConnections
import com.dantsu.escposprinter.exceptions.EscPosConnectionException
import kotlin.math.roundToInt


object PrinterHelper {

    /*==============================================================================================
    ======================================UROVO PART============================================
    ==============================================================================================*/
    //region Urovo
    fun printBillUrovo(context: Activity, content: String) {

    }
    //endregion

    /*==============================================================================================
    ======================================BLUETOOTH PART============================================
    ==============================================================================================*/
    //region Bluetooth
    private const val PERMISSION_BLUETOOTH = 1
    private const val PERMISSION_BLUETOOTH_ADMIN = 2
    private const val PERMISSION_BLUETOOTH_CONNECT = 3
    private const val PERMISSION_BLUETOOTH_SCAN = 4

    fun printBillBluetooth(context: Activity, originalBitmap: Bitmap) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context,
                listOf(Manifest.permission.BLUETOOTH).toTypedArray(), PERMISSION_BLUETOOTH
            );
        } else if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_ADMIN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context,
                listOf(Manifest.permission.BLUETOOTH_ADMIN).toTypedArray(),
                PERMISSION_BLUETOOTH_ADMIN
            );
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context,
                listOf(Manifest.permission.BLUETOOTH_CONNECT).toTypedArray(),
                PERMISSION_BLUETOOTH_CONNECT
            );
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context,
                listOf(Manifest.permission.BLUETOOTH_SCAN).toTypedArray(), PERMISSION_BLUETOOTH_SCAN
            );
        } else {
            // Your code HERE
//            val printer = EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 270, 48f, 33)
            val targetWidth =
                480// printer.printerWidthPx -1 // 48mm printing zone with 203dpi => 383px
            val printerCommands =
                EscPosPrinterCommands(BluetoothPrintersConnections.selectFirstPaired())

            val rescaledBitmap = Bitmap.createScaledBitmap(
                originalBitmap,
                targetWidth,
                (originalBitmap.height.toFloat() * targetWidth.toFloat() / originalBitmap.width.toFloat()).roundToInt(),
                false
            )

            try {
                val printText = StringBuilder()
                var y = 0
                while (y < rescaledBitmap.height) {
                    val bitmap: Bitmap = Bitmap.createBitmap(
                        rescaledBitmap,
                        0,
                        y,
                        targetWidth,
                        if (y + 255 >= rescaledBitmap.height) rescaledBitmap.height - y else 255
                    )
                    printerCommands.printImage(EscPosPrinterCommands.bitmapToBytes(bitmap));
                    // printText.append("[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer,bitmap) + "</img>\n")
                    y += 255
                }
                // printer.printFormattedText(printText.toString())
                printerCommands.newLine()
                printerCommands.newLine()
                printerCommands.newLine()
            } catch (e: EscPosConnectionException) {
                e.printStackTrace()
            }
        }
    }

    fun printBillBluetooth(context: Activity, content : String) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context,
                listOf(Manifest.permission.BLUETOOTH).toTypedArray(), PERMISSION_BLUETOOTH
            );
        } else if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_ADMIN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context,
                listOf(Manifest.permission.BLUETOOTH_ADMIN).toTypedArray(),
                PERMISSION_BLUETOOTH_ADMIN
            );
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context,
                listOf(Manifest.permission.BLUETOOTH_CONNECT).toTypedArray(),
                PERMISSION_BLUETOOTH_CONNECT
            );
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context,
                listOf(Manifest.permission.BLUETOOTH_SCAN).toTypedArray(), PERMISSION_BLUETOOTH_SCAN
            );
        } else {
            // Your code HERE
            val printer =
                EscPosPrinter(
                    BluetoothPrintersConnections.selectFirstPaired(), 203, 48f, 34,
                    EscPosCharsetEncoding("windows-1258", 16)
                )

            try {
                content.split("\n").forEach {
                    printer.printFormattedText(
                        it,
                        1
                    )
                }
                printer.printFormattedText("")
                printer.disconnectPrinter()

            } catch (e: EscPosConnectionException) {
                e.printStackTrace()
            }
        }
    }
    //endregion



}