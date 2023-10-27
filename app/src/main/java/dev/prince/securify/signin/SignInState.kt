package dev.prince.securify.signin

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)