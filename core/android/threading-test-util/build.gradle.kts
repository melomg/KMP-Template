import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    jvmToolchain(17)

    dependencies {
        api(projects.core.shared.threading)
        implementation(libs.jetbrains.kotlinx.coroutines.test)
        implementation(libs.jetbrains.kotlin.test.junit5)
        runtimeOnly(libs.junit.jupiter.engine)
    }

    androidLibrary {
        namespace = "com.melih.kmptemplate.core.android.threading.test.util"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions.jvmTarget = JvmTarget.JVM_17
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
