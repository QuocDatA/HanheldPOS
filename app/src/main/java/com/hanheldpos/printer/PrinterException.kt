package com.hanheldpos.model.printer

import java.lang.Exception

class PrinterException(
    override val message: String
) : Exception(message) {

}