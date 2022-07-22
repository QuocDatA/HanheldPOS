package com.hanheldpos.printer

import com.hanheldpos.printer.printer_devices.Printer

class PrinterException(
    val printer: Printer? = null,
    override val message: String
) : Exception(message)