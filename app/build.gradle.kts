import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.remove

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id ("kotlin-kapt")
    id ("com.google.protobuf") version "0.9.1"
}

android {
    namespace = "com.differentshadow.personalfinance"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.differentshadow.personalfinance"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "17"//"1.8"
    }
    buildFeatures {
        compose = true
        dataBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui:1.5.1")
    implementation("androidx.compose.ui:ui-graphics:1.5.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.1")
    implementation("androidx.compose.material3:material3:1.1.1")
    implementation("androidx.room:room-ktx:2.5.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.compose.animation:animation:1.5.1")
    implementation("androidx.compose.foundation:foundation-layout-android:1.5.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.appcompat:appcompat-resources:1.6.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.1")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.5.1")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")


    implementation("androidx.hilt:hilt-work:1.0.0")
    // Kotlin + coroutines
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    // optional - Test helpers
    androidTestImplementation("androidx.work:work-testing:2.8.1")

    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-compiler:2.44")
    implementation("androidx.navigation:navigation-compose:2.7.2")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:2.7.2")
    androidTestImplementation("androidx.navigation:navigation-testing:2.7.2")
    implementation("androidx.room:room-runtime:2.5.2")
    annotationProcessor("androidx.room:room-compiler:2.5.2")
    // To use Kotlin Symbol Processing (KSP)
    kapt("androidx.room:room-compiler:2.5.2")
    // optional - Test helpers
    testImplementation("androidx.room:room-testing:2.5.2")
    // optional - Paging 3 Integration
    implementation("androidx.room:room-paging:2.5.2")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.compose.runtime:runtime-livedata:1.5.1")
    // Saved state module for ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.6.2")
    // optional - ReactiveStreams support for LiveData
    implementation("androidx.lifecycle:lifecycle-reactivestreams-ktx:2.6.2")

    implementation("androidx.datastore:datastore:1.0.0")
    // protobuf
    implementation ("com.google.protobuf:protobuf-kotlin-lite:3.21.7")

    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
}

kapt {
    correctErrorTypes = true
    useBuildCache = true
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.21.7"
    }

    // Generates the java Protobuf-lite code for the Protobufs in this project. See
    // https://github.com/google/protobuf-gradle-plugin#customizing-protobuf-compilation
    // for more information.
    generateProtoTasks {
        all().forEach {
           it.builtins {
               register("java") {
                   option("lite")
               }
               register("kotlin" ) {
                   option("lite")
               }
           }
        }
    }
}