import com.github.jk1.license.render.InventoryMarkdownReportRenderer
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget


plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.jaredsburrowsLicense)
}

compose.resources {
    // Pinned so renaming the module folder doesn't change the generated
    // resource accessor package (mrtbuddy.composeapp.generated.resources.*).
    packageOfResClass = "mrtbuddy.composeapp.generated.resources"
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
        versionCode = 26
        versionName = "0.0.26"
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
    val hasReleaseKeystore =
        file("keystore.jks").let { it.exists() && it.length() > 0 } &&
            !System.getenv("KEYSTORE_PASSWORD").isNullOrBlank()

    signingConfigs {
        create("release") {
            storeFile = file("keystore.jks")
            storePassword = System.getenv("KEYSTORE_PASSWORD")
            keyAlias = System.getenv("KEY_ALIAS")
            keyPassword = System.getenv("KEY_PASSWORD")

            // Optional, specify signing versions used
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
            signingConfig =
                if (hasReleaseKeystore) {
                    signingConfigs.getByName("release")
                } else {
                    signingConfigs.getByName("debug")
                }
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

licenseReport {
    unionParentPomLicenses = false
    renderers =
        arrayOf(
            InventoryMarkdownReportRenderer(
                "open-source-licenses.md",
                "Open Source Libraries",
            ),
        )
}

tasks.register("processLicenseReport") {
    dependsOn("generateLicenseReport")

    doLast {
        val reportPath = file("build/reports/dependency-license/open-source-licenses.md")
        val processedPath = file("build/reports/dependency-license/processed-open-source-licenses.md")

        if (!reportPath.exists()) {
            throw GradleException("License report not found at $reportPath")
        }

        val content = reportPath.readText()
        val processedContent =
            content.replace(
                Regex("_\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} [A-Z]+_"),
                "",
            )
        processedPath.writeText(processedContent)
    }
}

tasks.register("copyLicenseReportToAssets") {
    dependsOn("processLicenseReport")

    doLast {
        val processedPath = file("build/reports/dependency-license/processed-open-source-licenses.md")
        val commonAssetsPath = file("src/commonMain/composeResources/files")

        if (!processedPath.exists()) {
            throw GradleException("Processed license report not found at $processedPath")
        }

        copy {
            from(processedPath)
            into(commonAssetsPath)
            rename { "open-source-licenses.md" }
        }
    }
}

tasks.named("preBuild").configure {
    dependsOn("copyLicenseReportToAssets")
}
