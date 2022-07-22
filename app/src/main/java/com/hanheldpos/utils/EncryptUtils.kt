package com.hanheldpos.utils

import java.security.MessageDigest

class EncryptUtils {
    companion object {
        fun getHash(obj: Any?) : String {
            if (obj == null) {
                return ""
            }
            return obj.toString().toMD5()
        }

        private fun String.toMD5(): String {
            val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
            return bytes.toHex()
        }

        private fun ByteArray.toHex(): String {
            return joinToString("") { "%02x".format(it) }
        }
    }
}