plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.hw04_gymlog_v300"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.hw04_gymlog_v300"

        // NOTE: many samples use minSdk = 24. Keep 34 if that's required for your class/emulator.
        minSdk = 34
        targetSdk = 35

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        // Java because our app code is Java
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    // ✅ lets us use ActivityMainBinding, etc.
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // ✅ Room (Java): runtime + compiler (annotationProcessor)
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    // (Optional later: room-ktx if you add coroutines or Kotlin code)
}
