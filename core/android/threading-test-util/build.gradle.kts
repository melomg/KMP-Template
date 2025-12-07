plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.androidKotlin)
    alias(libs.plugins.kotlinxSerialization)
}

android {
    namespace = "com.melih.kmptemplate.core.android.threading.test.util"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    kotlin {
        jvmToolchain(17)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        defaultConfig {
            minSdk = libs.versions.android.minSdk.get().toInt()
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    dependencies {
        api(projects.core.shared.threading)
        implementation(libs.jetbrains.kotlinx.coroutines.test)
        implementation(libs.jetbrains.kotlin.test.junit5)
        runtimeOnly(libs.junit.jupiter.engine)
    }
}
