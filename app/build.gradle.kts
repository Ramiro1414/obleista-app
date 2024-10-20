plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.obleista_app"
    compileSdk = 34

    packagingOptions {
        resources {
            excludes += setOf("META-INF/DEPENDENCIES")
        }
    }

    defaultConfig {
        applicationId = "com.example.obleista_app"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.play.services.location)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler) // Para Java

    implementation("com.konghq:unirest-java:3.11.11")
    implementation("org.json:json:20230227")

    // Retrofit y Gson Converter
    implementation(libs.retrofit)
    implementation(libs.gson.converter)


}