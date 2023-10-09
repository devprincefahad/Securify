package dev.prince.securify.ui.passwords

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.prince.securify.database.SecurifyDatabase
import javax.inject.Inject

@HiltViewModel
class PasswordsViewModel @Inject constructor(
    private val db: SecurifyDatabase
) : ViewModel(){

   val accounts = db.accountDao().getAllAccounts()

}