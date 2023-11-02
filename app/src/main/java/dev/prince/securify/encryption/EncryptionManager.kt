package dev.prince.securify.encryption

import android.content.Context
import android.util.Base64
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.prince.securify.local.SharedPrefHelper
import dev.prince.securify.signin.GoogleAuthUiClient
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

class EncryptionManager @Inject constructor(
    @ApplicationContext private val context: Context,
    prefs: SharedPrefHelper
) {

    private val loginKey = prefs.masterKey

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }
    private val user = googleAuthUiClient.getSignedInUser()

    fun encrypt(input: String): String {
        return runCatching {
            val key = user?.let { generateKey(it.userId, loginKey) }
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            val ivBytes = ByteArray(16)
            val ivSpec = IvParameterSpec(ivBytes)
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec)
            val encryptedBytes = cipher.doFinal(input.toByteArray(Charsets.UTF_8))
            return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
        }.getOrElse {
            it.printStackTrace()
            ""
        }
    }

    fun decrypt(input: String): String {
        return runCatching {
            val key = user?.let { generateKey(it.userId, loginKey) }
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            val ivBytes = ByteArray(16)
            val ivSpec = IvParameterSpec(ivBytes)
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec)
            val encryptedBytes = Base64.decode(input, Base64.DEFAULT)
            val decryptedBytes = cipher.doFinal(encryptedBytes)
            return String(decryptedBytes, Charsets.UTF_8)
        }.getOrElse {
            it.printStackTrace()
            ""
        }
    }

    private fun generateKey(uid: String, masterKey: String): SecretKeySpec {
        val combinedKey = uid + masterKey
        val md = MessageDigest.getInstance("SHA-256")
        val hashBytes = md.digest(combinedKey.toByteArray(Charsets.UTF_8))
        val keyBytes = hashBytes.copyOf(16) // AES-128 requires a 16-byte key
        return SecretKeySpec(keyBytes, "AES")
    }
}