import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag

plugins {
    kotlin("kapt")
    kotlin("plugin.serialization") version libs.versions.kotlin.get()
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.google.hilt)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "tgo1014.gridlauncher"
    // compileSdk = 34
    compileSdkPreview = "VanillaIceCream"

    defaultConfig {
        applicationId = "tgo1014.gridlauncher"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }
    signingConfigs {
        create("release") {
            storeFile = file("dummyKey")
            storePassword = "123456"
            keyAlias = "dummyKey"
            keyPassword = "123456"
        }
    }
    buildTypes {
        debug {
            // isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions.jvmTarget = "17"
    buildFeatures.compose = true
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

composeCompiler {
    featureFlags = setOf(
        ComposeFeatureFlag.IntrinsicRemember,
        ComposeFeatureFlag.OptimizeNonSkippingGroups,
        ComposeFeatureFlag.StrongSkipping,
    )
}

kapt.correctErrorTypes = true
hilt.enableAggregatingTask = true

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.util)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.bundles.hilt)
    kapt(libs.hilt.kapt)
    implementation(libs.reorderable)
    implementation(libs.coil.compose)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.androidx.datastore)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.lazytable)
    implementation(libs.haze)
    implementation(libs.haze.materials)
    implementation(libs.material.kolor)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.app.turbine)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    //androidTestImplementation(platform(libs.compose.bom))
    //androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

}