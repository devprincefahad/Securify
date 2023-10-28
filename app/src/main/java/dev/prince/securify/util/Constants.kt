package dev.prince.securify.util

import dev.prince.securify.R

const val ENCRYPTED_SHARED_PREFS_NAME = "encrypted_shared_prefs"

val accountSuggestions = listOf(
    "Amazon Prime",
    "Behance",
    "Discord",
    "Dribbble",
    "Facebook",
    "Github",
    "Gmail",
    "Instagram",
    "LinkedIn",
    "Medium",
    "Messenger",
    "Netflix",
    "Pinterest",
    "Quora",
    "Reddit",
    "Snapchat",
    "Spotify",
    "Stackoverflow",
    "Tumblr",
    "Twitter",
    "Whatsapp",
    "Wordpress",
    "YouTube"
)

val suggestionsWithImages = listOf(
    "Amazon Prime" to R.drawable.icon_amazon_prime_video,
    "Behance" to R.drawable.icon_behance,
    "Dribbble" to R.drawable.icon_dribbble,
    "Discord" to R.drawable.icon_discord,
    "Facebook" to R.drawable.icon_facebook,
    "Github" to R.drawable.icon_github,
    "Gmail" to R.drawable.icon_gmail,
    "Instagram" to R.drawable.icon_instagram,
    "LinkedIn" to R.drawable.icon_linkedin,
    "Medium" to R.drawable.icon_medium,
    "Messenger" to R.drawable.icon_messenger,
    "Netflix" to R.drawable.icon_netflix,
    "Pinterest" to R.drawable.icon_pinterest,
    "Quora" to R.drawable.icon_quora,
    "Reddit" to R.drawable.icon_reddit,
    "Snapchat" to R.drawable.icon_snapchat,
    "Spotify" to R.drawable.icon_spotify,
    "Stackoverflow" to R.drawable.icon_stackoverflow,
    "Tumblr" to R.drawable.icon_tumblr,
    "Twitter" to R.drawable.icon_twitterx,
    "Whatsapp" to R.drawable.icon_whatsapp,
    "Wordpress" to R.drawable.icon_wordpress,
    "YouTube" to R.drawable.icon_youtube
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
