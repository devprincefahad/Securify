package dev.prince.securify.ui.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.prince.securify.database.SecurifyDatabase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val db: SecurifyDatabase
) : ViewModel(){

   val accounts = db.accountDao().getAllAccounts()

}