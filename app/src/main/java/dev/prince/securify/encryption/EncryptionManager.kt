package dev.prince.securify.encryption

import android.os.Build
import androidx.annotation.RequiresApi
import java.security.Key
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import java.util.Base64

class EncryptionManager {

    private val secretKey: Key

    init {
        // Generate a random secret key for AES encryption
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(256) // Use 256 bits for the key
        secretKey = keyGenerator.generateKey()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun encryptPassword(password: String): String {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encryptedBytes = cipher.doFinal(password.toByteArray())
        return Base64.getEncoder().encodeToString(encryptedBytes)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun decryptPassword(encryptedPassword: String): String {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        val encryptedBytes = Base64.getDecoder().decode(encryptedPassword)
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes)
    }
}