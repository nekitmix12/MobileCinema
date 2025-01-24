plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "1.9.0"
    kotlin("kapt") version "2.1.0"
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.mobilecinema"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mobilecinema"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    viewBinding{
        enable = true
    }
    buildToolsVersion = "34.0.0"
}

dependencies {
    implementation ("com.squareup.okhttp3:logging-interceptor:3.8.0")
    implementation ("com.google.android.flexbox:flexbox:3.0.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.10")
    implementation (libs.toasty)
    implementation (libs.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.runtime)
    implementation(libs.coil.compose)
    implementation(libs.accompanist.flowlayout)
    implementation (libs.androidx.viewpager2.v100)
    implementation (libs.logging.interceptor.v493)
    implementation(libs.picasso)
    implementation (libs.cardstackview)
    implementation (libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation (libs.converter.gson)
    implementation (libs.retrofit)
    implementation (libs.logging.interceptor)
    implementation (libs.androidx.viewpager2)
    implementation (libs.androidx.recyclerview)
    implementation (libs.androidx.navigation.compose)
    implementation(libs.androidx.fragment.ktx)
    implementation (libs.androidx.navigation.fragment)
    implementation (libs.androidx.navigation.ui)

    implementation (libs.androidx.navigation.dynamic.features.fragment)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    androidTestImplementation (libs.androidx.navigation.testing)
    implementation (libs.mail)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
