package com.hanheldpos.printer

import com.hanheldpos.printer.printer_devices.Printer
import java.lang.Exception

class PrinterException(
    val printer: Printer? = null,
    override val message: String
) : Exception(message) {

}