package dev.prince.securify.ui.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.prince.securify.local.EncryptedSharedPrefHelper
import dev.prince.securify.util.AUTH_KEY
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val pref: EncryptedSharedPrefHelper
) : ViewModel(){

    val isUserLoggedIn = !pref.getFromSharedPrefs(AUTH_KEY).isNullOrEmpty()

}