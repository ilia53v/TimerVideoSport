import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    kotlin("plugin.compose") version "2.0.21"
}

kotlin {
    androidTarget {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_1_8)
                }
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation("org.jetbrains.compose.runtime:runtime:1.6.11")
            implementation("org.jetbrains.compose.foundation:foundation:1.6.11")  // <- обязательно
            implementation("org.jetbrains.compose.material3:material3:1.6.11")
            implementation("org.jetbrains.compose.animation:animation:1.6.11")

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        androidMain.dependencies {
            implementation("androidx.core:core-ktx:1.13.1")
            implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")

            // Compose Android Integration
            implementation("androidx.activity:activity-compose:1.9.2")
            implementation("androidx.compose.ui:ui-tooling-preview-android:1.6.8")

            // CameraX
            implementation("androidx.camera:camera-core:1.4.0")
            implementation("androidx.camera:camera-camera2:1.4.0")
            implementation("androidx.camera:camera-lifecycle:1.4.0")
            implementation("androidx.camera:camera-video:1.4.0")
            implementation("androidx.camera:camera-view:1.4.0")

            // Media
            implementation("androidx.media:media:1.7.0")
        }
    }
}

android {
    namespace = "com.sport.timervideosport"
    compileSdk = 35
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
