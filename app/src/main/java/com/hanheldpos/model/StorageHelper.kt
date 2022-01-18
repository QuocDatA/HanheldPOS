package com.hanheldpos.model

import android.os.Environment
import android.os.Parcelable
import android.util.Base64
import android.util.Log
import com.google.gson.Gson
import com.hanheldpos.prefs.PrefKey
import com.utils.helper.AppPreferences
import java.io.*
import java.lang.reflect.Type
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object StorageHelper {
    private final var INTERNAL_PATH =
        Environment.getDataDirectory().path + "/data/com.hanheldpos/local/"

    public fun setDataToEncryptedFile(key: String, data: Any?) {
        encryptAndSaveFile(key, data)
    }

    public fun <T : Parcelable> getDataFromEncryptedFile(key: String, classOff: Class<T>?): T? {
        val data = decryptEncryptedFile(key)
        return Gson().fromJson(data, classOff)
    }

    public fun <T > getDataFromEncryptedFile(key: String, typeOff: Type): T? {
        val data = decryptEncryptedFile(key)
        return Gson().fromJson(data, typeOff)
    }

     fun encryptAndSaveFile(fileName: String, data: Any?) {
        var jsonData = Gson().toJson(data)
        try {
            //get filePath
            val filePath = INTERNAL_PATH + fileName

            //get secret key
            val secretKey = AppPreferences.get().getString(PrefKey.SECRET_KEY)

            //secretKey already exist -> reuse
            if (!secretKey.isEmpty()) {
                //encrypt file and then save
                saveFile(
                    encrypt(
                        //decrypt existed secretKey
                        decryptSecretKey(secretKey),
                        jsonData.toString().toByteArray()),
                    filePath
                )
            }

            //create secretKey to use
            else {
                //encrypt file and then save
                saveFile(
                    encrypt(
                        //generate a secretKey
                        getSecretKey(),
                        jsonData.toString().toByteArray()),
                    filePath
                )
            }

        } catch (e: Exception) {
            Log.d("Error", e.message.toString())
        }
    }

    fun decryptEncryptedFile(fileName: String): String? {
        val filePath = INTERNAL_PATH + fileName
        val fileData = readFile(filePath)
        if (fileData != null) {
            val decodedKey =
                Base64.decode(AppPreferences.get().getString(PrefKey.SECRET_KEY), Base64.NO_WRAP)
            val secretKey = SecretKeySpec(decodedKey, 0, decodedKey.size, "AES")
            return decrypt(secretKey, fileData)!!.toString(Charsets.UTF_8)
        }
        return null
    }


    //region manage key
    private fun getSecretKey(): SecretKey {
        //generate secure random
        val secretKey = generateSecretKey()
        saveSecretKey(secretKey!!)
        return secretKey
    }

    private fun generateSecretKey(): SecretKey? {
        val secureRandom = SecureRandom()
        val keyGenerator = KeyGenerator.getInstance("AES")
        //generate a key with secure random
        keyGenerator?.init(128, secureRandom)
        return keyGenerator?.generateKey()
    }

    private fun saveSecretKey(secretKey: SecretKey): String {
        val encodedKey = Base64.encodeToString(secretKey.encoded, Base64.NO_WRAP)
        AppPreferences.get().storeValue(PrefKey.SECRET_KEY, encodedKey)
        return encodedKey
    }

    private fun decryptSecretKey(key: String): SecretKey {
        val decodedKey = Base64.decode(key, Base64.NO_WRAP)
        val secretKey = SecretKeySpec(decodedKey, 0, decodedKey.size, "AES")
        return secretKey
    }
    //end region

    //encrypt data
    private fun encrypt(yourKey: SecretKey, fileData: ByteArray): ByteArray {
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
    private fun decrypt(yourKey: SecretKey, fileData: ByteArray?): ByteArray? {
        if (fileData != null) {
            val decrypted: ByteArray
            val cipher = Cipher.getInstance("AES", "BC")
            cipher.init(Cipher.DECRYPT_MODE, yourKey, IvParameterSpec(ByteArray(cipher.blockSize)))
            decrypted = cipher.doFinal(fileData)
            return decrypted
        }
        return null
    }


    //region manage file
    private fun saveFile(fileData: ByteArray, path: String) {
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

    private fun readFile(filePath: String): ByteArray? {
        val file = File(filePath)
        if (file.exists()) {
            val fileContents = file.readBytes()
            val inputBuffer = BufferedInputStream(
                FileInputStream(file)
            )

            inputBuffer.read(fileContents)
            inputBuffer.close()
            return fileContents
        }
        return null
    }
    //end region
}