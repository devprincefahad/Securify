package dev.prince.securify.util

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class KeyUtils {

    companion object {

        private const val ENCRYPTED_SHARED_PREFS_NAME = "encrypted_shared_prefs"
        private const val INTRO_SCREEN_KEY = "intro_screen_key"
        private const val SETUP_SCREEN_KEY = "setup_screen_key"
        private const val UNLOCK_SCREEN_KEY = "unlock_screen_key"

/*val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
        val sharedPreferences = EncryptedSharedPreferences.create(
            "shared_preferences_filename",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        var isVisitedIntroScreen = false
        sharedPreferences.edit()
        editor.putBoolean(INTRO_SCREEN_KEY, isVisitedIntroScreen)
        editor.apply()
        */

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

        fun hasVisitedIntroScreen(context: Context): Boolean {
            val sharedPreferences = getEncryptedSharedPreferences(context)
            return sharedPreferences.getBoolean(INTRO_SCREEN_KEY, false)
        }

        fun setHasVisitedIntroScreen(context: Context) {
            val sharedPreferences = getEncryptedSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.putBoolean(INTRO_SCREEN_KEY, true)
            editor.apply()
        }

        /*
               fun hasSetupPassword(context: Context): Boolean {
                   val sharedPreferences = getEncryptedSharedPreferences(context)
                   return sharedPreferences.contains(SETUP_SCREEN_KEY)
               }

               fun setSetupPassword(context: Context, password: String) {
                   val sharedPreferences = getEncryptedSharedPreferences(context)
                   val editor = sharedPreferences.edit()
                   editor.putString(SETUP_SCREEN_KEY, password)
                   editor.apply()
               }

               fun getSetupPassword(context: Context): String? {
                   val sharedPreferences = getEncryptedSharedPreferences(context)
                   return sharedPreferences.getString(SETUP_SCREEN_KEY, null)
               }

               fun hasUnlockScreenPassword(context: Context): Boolean {
                   val sharedPreferences = getEncryptedSharedPreferences(context)
                   return sharedPreferences.contains(UNLOCK_SCREEN_KEY)
               }

               fun setUnlockScreenPassword(context: Context, password: String) {
                   val sharedPreferences = getEncryptedSharedPreferences(context)
                   val editor = sharedPreferences.edit()
                   editor.putString(UNLOCK_SCREEN_KEY, password)
                   editor.apply()
               }

               fun getUnlockScreenPassword(context: Context): String? {
                   val sharedPreferences = getEncryptedSharedPreferences(context)
                   return sharedPreferences.getString(UNLOCK_SCREEN_KEY, null)
               }*/
    }


}