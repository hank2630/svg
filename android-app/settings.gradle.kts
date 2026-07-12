pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    
    plugins {
        id("com.android.application") version "8.1.0"
        id("com.android.library") version "8.1.0"
        kotlin("android") version "1.9.21"
        kotlin("jvm") version "1.9.21"
        kotlin("plugin.serialization") version "1.9.21"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "M3MusicApp"
