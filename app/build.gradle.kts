import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget


plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

compose.resources {
    // Pinned (rather than left to the Compose default, which derives this
    // from the root project + module name) so renaming either one doesn't
    // change the generated resource accessor package.
    packageOfResClass = "app.mrm.metromate.generated.resources"
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    sourceSets {

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            // Use the SQLite that ships with Android instead of bundling native libs
            implementation(libs.sqlite.framework)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            //  implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.lifecycle.viewmodel.compose)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.coroutine)
            implementation(libs.androidx.room.runtime)
            implementation(libs.napier)
            implementation(libs.multiplatform.settings)
            implementation(libs.navigation.compose)

            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.compose.viewmodel.navigation)

        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(kotlin("test-common"))
            implementation(kotlin("test-annotations-common"))
            implementation(libs.kotlinx.coroutines.test)
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(libs.junit)
                implementation(libs.konsist)
            }
        }
    }
}

android {
    namespace = "app.mrm.metromate"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "app.mrm.metromate"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0.0"
        // App only ships English + Bangla strings; drop translations of every library
        androidResources {
            localeFilters += listOf("en", "bn")
        }
    }
    splits {
        abi {
            isEnable = true
            reset()
            include("arm64-v8a")
            isUniversalApk = false
        }
    }
    packaging {
        resources {
            excludes += setOf(
                "/META-INF/{AL2.0,LGPL2.1}",
                "/META-INF/*.version",
                "/META-INF/versions/**",
                "/META-INF/DEPENDENCIES",
                "/META-INF/LICENSE*",
                "/META-INF/NOTICE*",
                "/META-INF/*.kotlin_module",
                "/kotlin/**",
                "DebugProbesKt.bin",
                "kotlin-tooling-metadata.json",
            )
        }
    }
    // A real release keystore (see keystore.jks + KEYSTORE_PASSWORD/KEY_ALIAS/
    // KEY_PASSWORD) is only needed for a signature that's meant to last across
    // updates (e.g. Play Store). Without it, release builds fall back to the
    // debug keystore so the APK is still installable for local/CI testing.
        // Release builds are always signed with the real release keystore,
    // decoded from the KEYSTORE secret in CI (see .github/workflows/build-release.yml).
    // No debug-signing fallback: if these env vars are missing, the build fails.
    signingConfigs {
        create("release") {
            storeFile = file("MetroMate.p12")
            storeType = "PKCS12"
            storePassword = System.getenv("KEYSTORE_PASS")
            keyAlias = System.getenv("KEY")
            keyPassword = System.getenv("KEY_PASS")

            enableV1Signing = true
            enableV2Signing = true
        }
    }
    buildTypes {
        getByName("debug") {
            // Debug builds are usually left unshrunk for faster, more debuggable
            // builds, but shrinking here too so debug APK size reflects reality.
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
        
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }
}

dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    debugImplementation(compose.uiTooling)
}

room {
    schemaDirectory("$projectDir/schemas")
}
