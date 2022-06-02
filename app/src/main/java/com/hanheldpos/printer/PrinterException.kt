package com.hanheldpos.printer

import java.lang.Exception

class PrinterException(
    override val message: String
) : Exception(message) {

}