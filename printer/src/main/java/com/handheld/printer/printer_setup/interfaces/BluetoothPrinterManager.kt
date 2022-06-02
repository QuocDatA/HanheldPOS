package com.handheld.printer.printer_setup.interfaces

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.handheld.printer.printer_setup.printer_manager.BasePrinterUniversalManager

class BluetoothPrinterManager(
    context: Context,
    printerDPI: Int,
    printerPaperWidth: Float,
    charsPerLine: Int,
) : BasePrinterUniversalManager(
    deviceConnection = BluetoothPrintersConnections.selectFirstPaired(),
    printerDPI,
    printerPaperWidth,
    charsPerLine,
) {
    private val PERMISSION_BLUETOOTH = 1
    private val PERMISSION_BLUETOOTH_ADMIN = 2
    private val PERMISSION_BLUETOOTH_CONNECT = 3
    private val PERMISSION_BLUETOOTH_SCAN = 4

    init {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                listOf(Manifest.permission.BLUETOOTH).toTypedArray(),
                PERMISSION_BLUETOOTH
            )
        } else if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_ADMIN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                listOf(Manifest.permission.BLUETOOTH_ADMIN).toTypedArray(),
                PERMISSION_BLUETOOTH_ADMIN
            )
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                listOf(Manifest.permission.BLUETOOTH_CONNECT).toTypedArray(),
                PERMISSION_BLUETOOTH_CONNECT
            )
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                listOf(Manifest.permission.BLUETOOTH_SCAN).toTypedArray(),
                PERMISSION_BLUETOOTH_SCAN
            )
        }
    }
}