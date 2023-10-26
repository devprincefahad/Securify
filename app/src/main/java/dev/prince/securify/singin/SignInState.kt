package dev.prince.securify.singin

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)