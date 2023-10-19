package dev.prince.securify.encryption

import android.os.Build
import androidx.annotation.RequiresApi
import dev.prince.securify.local.SharedPrefHelper
import java.security.MessageDigest
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

class EncryptionManager @Inject constructor(
    prefs: SharedPrefHelper
) {

    private val ALGORITHM = "AES"
    private val TRANSFORMATION = "AES/CBC/PKCS5Padding"
//    private val SECRET_KEY = generateRandomString()
    private val SECRET_KEY = "uz_securify_password_key_2023/10"
//    private val IV = deriveIVFromMasterKey(prefs.masterKey)
    private val IV = "=a1Ab2Bc3Cd4De5E".toByteArray(Charsets.UTF_8)

    @RequiresApi(Build.VERSION_CODES.O)
    fun encrypt(textToEncrypt: String): String {
        val secretKeySpec = SecretKeySpec(SECRET_KEY.toByteArray(), ALGORITHM)

        val cipher = Cipher.getInstance(TRANSFORMATION)
        val ivSpec = IvParameterSpec(IV)
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec)

        val encryptedBytes = cipher.doFinal(textToEncrypt.toByteArray(Charsets.UTF_8))
        return Base64.getEncoder().encodeToString(encryptedBytes)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun decrypt(encryptedText: String): String {
        val secretKeySpec = SecretKeySpec(SECRET_KEY.toByteArray(), ALGORITHM)

        val cipher = Cipher.getInstance(TRANSFORMATION)
        val ivSpec = IvParameterSpec(IV)
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec)

        val encryptedBytes = Base64.getDecoder().decode(encryptedText)
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes, Charsets.UTF_8)
    }

    private fun deriveIVFromMasterKey(masterKey: String): ByteArray {
        val salt = "securify"
        val info = "IVGeneration"

        val hmacKey = SecretKeySpec(masterKey.toByteArray(), "HmacSHA256")
        val hmac = Mac.getInstance("HmacSHA256")
        hmac.init(hmacKey)
        val prk = hmac.doFinal(salt.toByteArray())

        val derivedKey = ByteArray(16)
        val label = "$prk$info".toByteArray()
        val hash = MessageDigest.getInstance("SHA-256")
        var currentOffset = 0

        while (currentOffset < 16) {
            hash.update(label)
            label.copyInto(derivedKey, currentOffset, 0, minOf(label.size, 16 - currentOffset))
            currentOffset += label.size
        }
        return derivedKey
    }

    private fun generateRandomString(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..32)
            .map { allowedChars.random() }
            .joinToString("")
    }

}