package dev.prince.securify.util

const val ENCRYPTED_SHARED_PREFS_NAME = "encrypted_shared_prefs"

val accountSuggestions = listOf(
    "Instagram",
    "Facebook",
    "LinkedIn",
    "Snapchat",
    "YouTube",
    "Netflix",
    "Discord",
    "Twitter",
    "Amazon Prime",
    "Spotify",
    "Gmail",
    "Reddit",
    "Quora",
    "Pinterest"
)

fun generatePassword(
    length: Int,
    lowerCase: Boolean,
    upperCase: Boolean,
    digits: Boolean,
    specialCharacters: Boolean
): String {

    val chars = mutableListOf<Char>()
    val symbols = "!@#$%&*+=-~?/_"
    if (lowerCase) chars.addAll('a'..'z')
    if (upperCase) chars.addAll('A'..'Z')
    if (digits) chars.addAll('0'..'9')
    if (specialCharacters) chars.addAll(symbols.toList())

    val password = StringBuilder()
    for (i in 0 until length) {
        password.append(chars.random())
    }

    return password.toString()

}

fun getRandomNumber(): Int {
    val random = kotlin.random.Random
    return random.nextInt(6, 21)
}
