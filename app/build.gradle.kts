plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services")
    id ("kotlin-kapt") // 2
    id("com.google.dagger.hilt.android") //4
}

android {
    namespace = "com.example.addiction_manage"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.addiction_manage"
        minSdk = 34
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.database.ktx)
    implementation(libs.androidx.room.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation ("androidx.compose.ui:ui:1.0.0")  // 기본 UI 요소
    implementation ("androidx.compose.material:material:1.0.0")  // Material 디자인 요소
    implementation ("androidx.compose.ui:ui-tooling-preview:1.0.0")  // UI 프리뷰 도구
    implementation("androidx.navigation:navigation-compose:2.8.3")  // 네비게이션 컴포넌트
    implementation ("androidx.recyclerview:recyclerview:1.2.1")

    // firebase
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation("com.google.firebase:firebase-analytics")

    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")

    implementation ("com.google.dagger:hilt-android:2.51.1") // 1
    kapt("com.google.dagger:hilt-android-compiler:2.51.1") // 3
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    implementation("com.patrykandpatrick.vico:compose-m2:2.0.0-alpha.22")
    implementation("com.patrykandpatrick.vico:compose-m3:2.0.0-alpha.22")
    implementation("com.patrykandpatrick.vico:core:2.0.0-alpha.22")
    implementation("com.patrykandpatrick.vico:views:2.0.0-alpha.22")

//    implementation("com.patrykandpatrick.vico:compose-m2:2.0.0-alpha.23")
//    implementation("com.patrykandpatrick.vico:compose-m3:2.0.0-alpha.23")
//    implementation("com.patrykandpatrick.vico:core:2.0.0-alpha.23")
//    implementation("com.patrykandpatrick.vico:views:2.0.0-alpha.23")

}

kapt {//5
    correctErrorTypes = true
}