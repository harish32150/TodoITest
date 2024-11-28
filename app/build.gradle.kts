import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
//    alias(libs.plugins.ksp)
    kotlin("kapt")// version "2.0.21"
    alias(libs.plugins.dagger.hilt)
}

android {
    namespace = "com.harish.todoitest"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.harish.todoitest"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildFeatures.buildConfig = true

        project.rootDir.resolve("local.properties").let { localPropertiesFile ->
            if (localPropertiesFile.exists()) {
                Properties()
                    .apply { localPropertiesFile.inputStream().use { load(it) } }
                    .getProperty("base_url")
                    .also {
                        buildConfigField("String", "BASE_URL", "\"$it\"")
                    }
            }
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.worker)

    /* navigation */
    implementation(libs.androidx.compose.navigation)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.accompanist.navigation.material)

    /* room */
    implementation(libs.room.runtime)
    kapt(libs.room.compiler)
    implementation(libs.room.ktx)

    /* hilt */
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kapt(libs.androidx.hilt.compiler)
    implementation(libs.hilt.work)


    /* network */
    implementation(libs.retrofit2)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.retrofit2.converter.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.moshi.adapters)
}

kapt {
    correctErrorTypes = true
}

hilt {
    enableAggregatingTask = false
}