package com.time2.superid.utils

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AESEncryption {
    companion object {
        private const val KEY_SIZE = 256
        private const val ALGORITHM = "AES"
        private const val TRANSFORMATION = "AES/CBC/PKCS7Padding"


        // chave estática
        private val KEY = "D6F3Efeq3v5PQdsTMuSl472F3AvJqMqS"

        /**
         * Criptografa uma string
         */
        fun encrypt(plainText: String): String {
            try {

                val secretKey = SecretKeySpec(KEY.toByteArray(), ALGORITHM)


                val iv = ByteArray(16)
                SecureRandom().nextBytes(iv)
                val ivParameterSpec = IvParameterSpec(iv)


                val cipher = Cipher.getInstance(TRANSFORMATION)
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec)


                val encrypted = cipher.doFinal(plainText.toByteArray())


                val combined = ByteArray(iv.size + encrypted.size)
                System.arraycopy(iv, 0, combined, 0, iv.size)
                System.arraycopy(encrypted, 0, combined, iv.size, encrypted.size)


                return Base64.encodeToString(combined, Base64.DEFAULT)
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }

        /**
         * Descriptografa uma string
         */
        fun decrypt(encryptedText: String): String {
            try {

                val combined = Base64.decode(encryptedText, Base64.DEFAULT)


                val iv = ByteArray(16)
                System.arraycopy(combined, 0, iv, 0, iv.size)
                val ivParameterSpec = IvParameterSpec(iv)


                val encryptedBytes = ByteArray(combined.size - iv.size)
                System.arraycopy(combined, iv.size, encryptedBytes, 0, encryptedBytes.size)


                val secretKey = SecretKeySpec(KEY.toByteArray(), ALGORITHM)
                val cipher = Cipher.getInstance(TRANSFORMATION)
                cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec)


                val decrypted = cipher.doFinal(encryptedBytes)
                return String(decrypted)
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }
    }
}