package dev.prince.securify.local

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class SharedPrefHelper @Inject constructor(
    private val pref: SharedPreferences
) {

    private val KEY_AUTH = "auth_key"

    var masterKey: String
        get() = getString(KEY_AUTH)
        set(value) { setString(value) }

    private fun getString(key: String): String {
        return pref.getString(key, "") ?: ""
    }

    private fun setString(value: String) {
        pref.edit {
            putString(KEY_AUTH, value)
        }
    }
}