package com.hanheldpos.model

import android.os.Environment
import android.util.Base64
import android.util.Log
import com.google.gson.Gson
import com.hanheldpos.prefs.PrefKey
import com.utils.helper.AppPreferences
import java.io.*
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object JsonHelper {
    private final var INTERNAL_PATH =
        Environment.getDataDirectory().path + "/data/com.hanheldpos/local/"

    fun encryptAndSaveFile(fileName: String, data: Any) {
        var jsonData = Gson().toJson(data)
        try {
            //get filePath
            val filePath = INTERNAL_PATH + fileName
            //get secret key
            val secretKey = getSecretKey()
            //encrypt file
            val encodedData = encrypt(secretKey, jsonData.toString().toByteArray())
            //save file
            saveFile(encodedData, filePath)

        } catch (e: Exception) {
            Log.d("Error", e.message.toString())
        }
    }

    fun decryptEncryptedFile(fileName: String) {
        val filePath = INTERNAL_PATH + fileName
        val fileData = readFile(filePath)
        val decodedKey = Base64.decode(AppPreferences.get().getString(PrefKey.SECRET_KEY), Base64.NO_WRAP)
        val secretKey = SecretKeySpec(decodedKey, 0, decodedKey.size, "AES")
        Log.d("Decrypt ",decrypt(secretKey, fileData).toString(Charsets.UTF_8))
    }


    //region manage key
    fun getSecretKey(): SecretKey {
        //generate secure random
        val secretKey = generateSecretKey()
        saveSecretKey(secretKey!!)
        return secretKey
    }

    fun generateSecretKey(): SecretKey? {
        val secureRandom = SecureRandom()
        val keyGenerator = KeyGenerator.getInstance("AES")
        //generate a key with secure random
        keyGenerator?.init(128, secureRandom)
        return keyGenerator?.generateKey()
    }

    fun saveSecretKey(secretKey: SecretKey): String {
        val encodedKey = Base64.encodeToString(secretKey.encoded, Base64.NO_WRAP)
        AppPreferences.get().storeValue(PrefKey.SECRET_KEY,encodedKey)
        return encodedKey
    }
    //end region

    //encrypt data
    fun encrypt(yourKey: SecretKey, fileData: ByteArray): ByteArray {
        val data = yourKey.getEncoded()
        val skeySpec = SecretKeySpec(data, 0, data.size, "AES")
        val cipher = Cipher.getInstance("AES", "BC")
        cipher.init(
            Cipher.ENCRYPT_MODE,
            skeySpec,
            IvParameterSpec(ByteArray(cipher.getBlockSize()))
        )
        return cipher.doFinal(fileData)
    }

    //decrypt data
    fun decrypt(yourKey: SecretKey, fileData: ByteArray): ByteArray {
        val decrypted: ByteArray
        val cipher = Cipher.getInstance("AES", "BC")
        cipher.init(Cipher.DECRYPT_MODE, yourKey, IvParameterSpec(ByteArray(cipher.blockSize)))
        decrypted = cipher.doFinal(fileData)
        return decrypted
    }


    //region manage file
    fun saveFile(fileData: ByteArray, path: String) {
        val file = File(path)
        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.createNewFile()
        }
        val bos = BufferedOutputStream(FileOutputStream(file, false))
        bos.write(fileData)
        bos.flush()
        bos.close()
    }

    fun readFile(filePath: String): ByteArray {
        val file = File(filePath)
        val fileContents = file.readBytes()
        val inputBuffer = BufferedInputStream(
            FileInputStream(file)
        )

        inputBuffer.read(fileContents)
        inputBuffer.close()

        return fileContents
    }
    //end region
}