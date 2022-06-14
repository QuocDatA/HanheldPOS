package com.hanheldpos.printer.interfaces

import com.dantsu.escposprinter.connection.tcp.TcpConnection
import com.hanheldpos.printer.printer_setup.printer_manager.BasePrinterUniversalManager

class LanPrinterManager(
    ipAddress: String,
    port: Int,
    timeout: Int?,
    printerDPI: Int,
    printerPaperWidth: Float,
    charsPerLine: Int,
) : BasePrinterUniversalManager(
    deviceConnection =
    if (timeout != null)
        TcpConnection(ipAddress, port, timeout)
    else
        TcpConnection(ipAddress, port),
    printerDPI,
    printerPaperWidth,
    charsPerLine,
)