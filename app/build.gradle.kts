plugins {
    id("com.android.application")
}

android {
    compileSdk = 34
    defaultConfig {
        applicationId = "com.example.bluetoothposprinter"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation("fi.iki.elonen:nanohttpd:2.3.1")
}
