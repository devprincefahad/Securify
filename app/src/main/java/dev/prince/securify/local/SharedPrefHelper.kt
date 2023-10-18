package dev.prince.securify.local

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class SharedPrefHelper @Inject constructor(
    private val pref: SharedPreferences
) {

    val KEY_AUTH = "auth_key"

    var masterKey: String
        get() = getString(KEY_AUTH)
        set(value) { setString(KEY_AUTH, value) }

    private fun getString(key: String): String {
        return pref.getString(key, "") ?: ""
    }

    private fun setString(key: String, value: String) {
        pref.edit {
            putString(KEY_AUTH, value)
        }
    }
}