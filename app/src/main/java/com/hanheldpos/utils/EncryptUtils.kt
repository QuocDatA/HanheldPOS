package com.hanheldpos.utils

import android.util.Log
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

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