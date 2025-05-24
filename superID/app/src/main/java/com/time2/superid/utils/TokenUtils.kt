package com.time2.superid.utils

import android.util.Base64
import java.security.SecureRandom

object TokenUtils {
    /**
     * Gera um token de acesso em Base64 de 256 caracteres
     */
    fun generateAccessToken(): String {
        val random = SecureRandom()
        val bytes = ByteArray(192)
        random.nextBytes(bytes)
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }
}