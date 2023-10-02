package dev.prince.securify.local

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.prince.securify.util.ENCRYPTED_SHARED_PREFS_NAME
import javax.inject.Inject

class EncryptedSharedPrefHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private fun getEncryptedSharedPreferences(context: Context): SharedPreferences {
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

        return EncryptedSharedPreferences.create(
            ENCRYPTED_SHARED_PREFS_NAME,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveToSharedPrefs(key: String, value: String) {
        val sharedPreferences = getEncryptedSharedPreferences(context)
        with(sharedPreferences.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun getFromSharedPrefs(key: String): String? {
        val sharedPreferences = getEncryptedSharedPreferences(context)
        return sharedPreferences.getString(key, "")
    }

}