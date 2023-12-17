package dev.prince.securify.util

import java.security.MessageDigest
import javax.crypto.spec.SecretKeySpec

const val BYTE_SIZE = 16
const val AES_ALGORITHM = "AES"
const val SHA_ALGORITHM = "SHA-256"
const val BLOCK_MODE = "CBC"
const val PADDING = "PKCS5Padding"
const val TRANSFORMATION = "$AES_ALGORITHM/$BLOCK_MODE/$PADDING"

fun generateKey(uid: String, email: String): SecretKeySpec {
    val combinedKey = uid + email
    val md = MessageDigest.getInstance(SHA_ALGORITHM)
    val hashBytes = md.digest(combinedKey.toByteArray(Charsets.UTF_8))
    val keyBytes = hashBytes.copyOf(BYTE_SIZE) // AES-128 requires a 16-byte key
    return SecretKeySpec(keyBytes, AES_ALGORITHM)
}