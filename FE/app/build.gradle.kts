plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
    id("kotlin-parcelize") // add
}

android {
    namespace = "com.example.mealtoyou"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mealtoyou"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.work:work-runtime-ktx:2.7.1")
// Health Connect 라이브러리
    implementation("androidx.health.connect:connect-client:1.0.0-alpha06")

// Core library
    implementation("androidx.core:core-ktx:1.7.0")

// Jetpack Compose UI
    implementation("androidx.compose.ui:ui:1.3.0")

// Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")

// Lifecycle components
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

// AppCompat library
    implementation("androidx.appcompat:appcompat:1.6.1")

// Material3 for Jetpack Compose
    implementation("androidx.compose.material3:material3:1.0.0-alpha13")

// ConstraintLayout
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

// Retrofit for network operations
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation (libs.androidx.navigation.compose)
    implementation (libs.material3)
    implementation(libs.compose)
    implementation(libs.compose.m2)
    implementation(libs.compose.m3)
    implementation(libs.core)
    implementation(libs.views)
    implementation(libs.androidx.compose.material)
    implementation(libs.firebase.messaging)
    implementation(libs.androidx.browser)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation (libs.okhttp)
    implementation (libs.squareup.logging.interceptor)
    implementation (libs.gson)
//    implementation("io.coil-kt:coil-compose:1.4.0")
    implementation("io.coil-kt:coil-compose:2.6.0")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
    // The view calendar library
    implementation("com.kizitonwose.calendar:view:2.5.1")
    // The compose calendar library
    implementation("com.kizitonwose.calendar:compose:2.5.1")

    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-messaging:24.0.0")
    implementation("com.firebaseui:firebase-ui-auth:8.0.2")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.android.gms:play-services-auth:20.7.0")


}