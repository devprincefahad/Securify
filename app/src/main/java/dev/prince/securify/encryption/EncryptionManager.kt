package dev.prince.securify.encryption

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import java.security.KeyStore
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.inject.Inject

class EncryptionManager @Inject constructor() {

    private val ALIAS = "secure_key_alias"
    private val IV = "=a1Ab2Bc3Cd4De5E".toByteArray(Charsets.UTF_8)
    private val KEY_STORE_TYPE = "AndroidKeyStore"

    init {
        createKeyIfNeeded()
    }

    private fun createKeyIfNeeded() {
        val keyStore = KeyStore.getInstance(KEY_STORE_TYPE)
        keyStore.load(null)

        if (!keyStore.containsAlias(ALIAS)) {
            val keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES, KEY_STORE_TYPE
            )

            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .setRandomizedEncryptionRequired(false)
                .build()

            keyGenerator.init(keyGenParameterSpec)
            keyGenerator.generateKey()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun encrypt(textToEncrypt: String): String {
        return runCatching {
            val cipher = Cipher.getInstance(
                "${KeyProperties.KEY_ALGORITHM_AES}/" +
                        "${KeyProperties.BLOCK_MODE_CBC}/${KeyProperties.ENCRYPTION_PADDING_PKCS7}"
            )

            val keyStore = KeyStore.getInstance(KEY_STORE_TYPE)
            keyStore.load(null)
            val secretKey = keyStore.getKey(ALIAS, null) as SecretKey
            val ivSpec = IvParameterSpec(IV)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)

            val encryptedBytes = cipher.doFinal(textToEncrypt.toByteArray(Charsets.UTF_8))
            return Base64.getEncoder().encodeToString(encryptedBytes)
        }.getOrElse {
            it.printStackTrace()
            ""
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun decrypt(encryptedText: String): String {
        return runCatching {
            val cipher =
                Cipher.getInstance("${KeyProperties.KEY_ALGORITHM_AES}/${KeyProperties.BLOCK_MODE_CBC}/${KeyProperties.ENCRYPTION_PADDING_PKCS7}")
            val keyStore = KeyStore.getInstance(KEY_STORE_TYPE)
            keyStore.load(null)
            val secretKey = keyStore.getKey(ALIAS, null) as SecretKey

            val ivSpec = IvParameterSpec(IV)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)

            val encryptedBytes = Base64.getDecoder().decode(encryptedText)
            val decryptedBytes = cipher.doFinal(encryptedBytes)
            return String(decryptedBytes, Charsets.UTF_8)
        }.getOrElse {
            it.printStackTrace()
            ""
        }
    }
}