package com.hanheldpos.ui.screens.pincode

class PinCodeRecyclerElement {
    var text: String? = null
    var resource: Int? = null
    var underline: Boolean? = null

    constructor()

    constructor(text: String?, resource: Int?, underline:Boolean? = true) {
        this.text = text
        this.resource = resource
        this.underline = underline
    }
}