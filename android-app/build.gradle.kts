plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("plugin.serialization")
}

android {
    namespace = "com.example.m3musicapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.m3musicapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Jetpack Compose
    implementation("androidx.compose.ui:ui:1.6.0")
    implementation("androidx.compose.material3:material3:1.1.1")
    implementation("androidx.compose.foundation:foundation:1.6.0")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    implementation("androidx.navigation:navigation-compose:2.7.4")
    implementation("androidx.compose.material:material-icons-extended:1.6.0")

    // Networking - 只用 OkHttp + Kotlinx Serialization
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")

    // YouTube Player
    implementation("com.pierfrancescosoffritti:android-youtube-player:11.0.1")

    // Image loading
    implementation("io.coil-kt:coil-compose:2.4.0")

    // 其他
    implementation("androidx.core:core:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
}
