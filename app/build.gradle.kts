plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.gdsc.gdsc_mbu"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.gdsc.gdsc_mbu"
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
//    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
//    implementation("com.google.firebase:firebase-crashlytics")
//    implementation("com.google.firebase:firebase-analytics")
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("io.coil-kt:coil-compose:2.1.0")
    implementation("androidx.navigation:navigation-compose:2.7.5")
    implementation("androidx.compose.ui:ui-tooling-preview:1.0.0")
    implementation("io.coil-kt:coil-compose:1.4.0")
    implementation("com.google.android.gms:play-services-auth:20.2.0")
    implementation("com.google.firebase:firebase-auth:21.0.1")
    implementation("com.google.accompanist:accompanist-swiperefresh:0.23.1")
    implementation("androidx.core:core-splashscreen:1.0.0")
    implementation("androidx.compose.foundation:foundation:1.1.0-alpha02")
    implementation("androidx.compose.ui:ui:1.1.0-alpha02")
    implementation("androidx.compose.material:material:1.1.0-alpha02")
    implementation("com.google.firebase:firebase-firestore-ktx:25.0.0")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.compose.material:material-icons-extended-android:1.6.3")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}