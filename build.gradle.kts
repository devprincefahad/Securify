buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.12" apply false
    id ("com.google.dagger.hilt.android") version "2.48" apply false
    // Add the dependency for the Crashlytics Gradle plugin
    id("com.google.firebase.crashlytics") version "2.9.9" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
}
true // Needed to make the Suppress annotation work for the plugins block