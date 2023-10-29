package dev.prince.securify.util

import dev.prince.securify.database.AccountEntity
import dev.prince.securify.database.CardEntity

sealed class AccountOrCard {
    data class AccountItem(val account: AccountEntity) : AccountOrCard()
    data class CardItem(val card: CardEntity) : AccountOrCard()
}