plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.callblocker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.callblocker"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    // No external libraries are required for this MVP.
}
