package dev.prince.securify.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.prince.securify.database.AccountDao
import dev.prince.securify.database.AccountEntity
import dev.prince.securify.database.CardDao
import dev.prince.securify.database.CardEntity
import dev.prince.securify.encryption.EncryptionManager
import dev.prince.securify.util.AccountOrCard
import dev.prince.securify.util.oneShotFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dbAccount: AccountDao,
    private val dbCard: CardDao,
    private val encryptionManager: EncryptionManager
) : ViewModel() {

    val accounts = dbAccount.getAllAccounts()
    val cards = dbCard.getAllCards()

    val combinedData: Flow<List<AccountOrCard>> = combine(accounts, cards) { accounts, cards ->
        val combinedList = mutableListOf<AccountOrCard>()

        val itemsWithTimestamp = mutableListOf<Pair<AccountOrCard, Long>>()
        accounts.forEach { item -> itemsWithTimestamp.add(AccountOrCard.AccountItem(item) to item.createdAt) }
        cards.forEach { item -> itemsWithTimestamp.add(AccountOrCard.CardItem(item) to item.createdAt) }

        val sortedItems = itemsWithTimestamp.sortedByDescending { it.second }

        combinedList.addAll(sortedItems.map { it.first })
        combinedList
    }

    val messages = oneShotFlow<String>()

    var showAccountDeleteDialog by mutableStateOf(false)

    var showCardDeleteDialog by mutableStateOf(false)

    var accountToDelete by mutableStateOf(AccountEntity(-1, "", "", "", "", "", "", 0L))
    var cardToDelete by mutableStateOf(CardEntity(-1, "", "", "", "", "", 0L))

    fun onUserAccountDeleteClick(accountEntity: AccountEntity) {
        accountToDelete = accountEntity
        showAccountDeleteDialog = true
    }

    fun deleteAccount() {
        viewModelScope.launch {
            dbAccount.deleteAccount(accountToDelete)
            showAccountDeleteDialog = false
        }
    }

    fun onUserCardDeleteClick(cardEntity: CardEntity) {
        cardToDelete = cardEntity
        showCardDeleteDialog = true
    }

    fun deleteCard() {
        viewModelScope.launch {
            dbCard.deleteCard(cardToDelete)
            showCardDeleteDialog = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun decryptPassword(password: String): String {
        return encryptionManager.decrypt(password)
    }

    fun showCopyMsg() {
        messages.tryEmit("Password copied to clipboard.")
    }

}