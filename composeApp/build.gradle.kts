import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.INT
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import java.util.Properties

val appProperties = ApplicationProperties(project)

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.buildkonfig)
    id("com.google.gms.google-services")
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm("desktop")

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName = "composeApp"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.cio)

            implementation(project.dependencies.platform(libs.firebase.bom))
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonMain.dependencies {
            implementation(projects.shared.model)
            implementation(projects.shared.network)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.jetbrains.lifecycle.viewmodel)
            implementation(libs.jetbrains.lifecycle.runtimeCompose)
            implementation(libs.jetbrains.navigation.compose)
            implementation(libs.jetbrains.material.icons.core)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
            implementation(libs.koin.core)
            implementation(libs.koin.compose.viewmodel)
        }
        commonTest.dependencies {
            implementation(libs.jetbrains.kotlin.test)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.jetbrains.kotlinx.coroutines.swing)
            implementation(libs.ktor.client.cio)
        }
        wasmJsMain.dependencies {
            implementation(libs.ktor.client.js)
        }
    }
}

android {
    namespace = "com.melih.kmptemplate"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = appProperties.appIdBase
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = appProperties.versionCode
        versionName = appProperties.versionNameBase
        resValue("string", "app_name", appProperties.appName)
    }

    signingConfigs {
        val localProperties = gradleLocalProperties(rootDir, providers)
        create("prod") {
            storeFile = file("../tools/release.jks")
            storePassword = localProperties.getProperty("storePassword")
            keyAlias = localProperties.getProperty("keyAlias")
            keyPassword = localProperties.getProperty("keyPassword")
        }
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        debug {
            applicationIdSuffix = appProperties.appIdDebugSuffix
            versionNameSuffix = appProperties.versionNameDebugSuffix
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
        }
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))

            signingConfig = signingConfigs.getByName("prod")
        }
        create("staging") {
            initWith(getByName("release"))
            applicationIdSuffix = appProperties.appIdStagingSuffix
            versionNameSuffix = appProperties.versionNameStagingSuffix

            signingConfig = signingConfigs.getByName("debug")
        }
    }

    bundle {
        language {
            // Include all languages in app bundles
            enableSplit = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    lint {
        warningsAsErrors = true
        abortOnError = appProperties.isCI
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.melih.kmptemplate.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = appProperties.appIdBase
            packageVersion = appProperties.versionNameBase
        }
    }
}

buildkonfig {
    packageName = "com.melih.kmptemplate"

    defaultConfigs {
        buildConfigField(STRING, "APP_NAME", appProperties.appName)
        buildConfigField(STRING, "EFFECTIVE_BUILD_TYPE", appProperties.effectiveBuildType)
        buildConfigField(INT, "VERSION_CODE", appProperties.versionCode.toString())
        buildConfigField(STRING, "VERSION_NAME", appProperties.versionName)
        buildConfigField(
            BOOLEAN, "IS_DEBUGGABLE", appProperties.isDebuggable.toString()
        )
    }
}

class ApplicationProperties(project: Project) {

    private val properties = Properties()
    private val propertiesFile: File = project.rootProject.file("application.properties")

    init {
        if (propertiesFile.exists()) {
            propertiesFile.inputStream().use { input ->
                properties.load(input)
            }
        }
    }

    val isCI: Boolean
        get() = System.getenv().containsKey("CI")

    val appId: String
        get() = when (AppBuildType.byKey(effectiveBuildType)) {
            AppBuildType.DEBUG -> appIdBase + appIdDebugSuffix
            AppBuildType.STAGING -> appIdBase + appIdStagingSuffix
            AppBuildType.RELEASE -> appIdBase
        }

    val appIdBase: String
        get() = properties.getProperty("appIdBase")

    val appIdDebugSuffix: String
        get() = properties.getProperty("appIdDebugSuffix")

    val appIdStagingSuffix: String
        get() = properties.getProperty("appIdStagingSuffix")

    val appName: String
        get() = when (AppBuildType.byKey(effectiveBuildType)) {
            AppBuildType.DEBUG -> appNameBase + properties.getProperty("appNameDebugSuffix")
            AppBuildType.STAGING -> appNameBase + properties.getProperty("appNameStagingSuffix")
            AppBuildType.RELEASE -> appNameBase
        }

    private val appNameBase: String
        get() = properties.getProperty("appNameBase")

    private val localProperties: Properties
        get() = gradleLocalProperties(rootDir, providers)

    private val localPropertiesFile: File
        get() = project.rootProject.file("local.properties")

    var effectiveBuildType: String
        get() = localProperties.getProperty("effectiveBuildType")
        set(value) {
            localProperties.setProperty("effectiveBuildType", value)
            localPropertiesFile.outputStream().use { output ->
                logger.lifecycle("Updating local.properties with effectiveBuildType=$value")
                properties.store(output, null)
            }
        }

    val versionCode: Int
        get() = properties.getProperty("versionCode").toInt()

    val versionName: String
        get() = when (AppBuildType.byKey(effectiveBuildType)) {
            AppBuildType.DEBUG -> versionNameBase + versionNameDebugSuffix
            AppBuildType.STAGING -> versionNameBase + versionNameStagingSuffix
            AppBuildType.RELEASE -> versionNameBase
        }

    val versionNameBase: String
        get() = properties.getProperty("versionNameBase")

    val versionNameDebugSuffix: String
        get() = properties.getProperty("versionNameDebugSuffix")

    val versionNameStagingSuffix: String
        get() = properties.getProperty("versionNameStagingSuffix")

    val isDebuggable: Boolean
        get() = when (AppBuildType.byKey(effectiveBuildType)) {
            AppBuildType.DEBUG -> true
            AppBuildType.STAGING -> false
            AppBuildType.RELEASE -> false
        }

    enum class AppBuildType(val key: String) {
        DEBUG("debug"),
        STAGING("staging"),
        RELEASE("release");

        companion object {
            @OptIn(ExperimentalStdlibApi::class)
            fun byKey(key: String): AppBuildType = requireNotNull(values().find { it.key == key }) {
                "BuildType with key $key does not exist"
            }
        }
    }
}

abstract class UpdateEffectiveBuildTypeTask : DefaultTask() {

    @get:Input
    abstract val effectiveBuildType: Property<String>

    @get:OutputFile
    val outputFile: RegularFileProperty = project.objects
        .fileProperty()
        .convention(
            project
                .rootProject
                .layout
                .projectDirectory
                .file("local.properties")
        )

    @TaskAction
    fun writeProperty() {
        val props = Properties()
        val file = outputFile.get().asFile

        if (file.exists()) {
            file.inputStream().use { props.load(it) }
        }

        val input = effectiveBuildType.get()

        props.setProperty("effectiveBuildType", input)
        file.outputStream().use { props.store(it, null) }
    }
}

tasks.register<UpdateEffectiveBuildTypeTask>("updateEffectiveBuildType") {
    project.findProperty("effectiveBuildType")?.toString()?.let {
        effectiveBuildType.set(it)
    }
}

tasks.register("runAndroidDebug", Exec::class) {
    dependsOn("installDebug")

    doFirst {
        providers.exec {
            commandLine("adb", "shell", "am", "force-stop", appProperties.appId)
        }
    }

    commandLine(
        "adb", "shell", "am", "start", "-n",
        "${appProperties.appId}/com.melih.kmptemplate.MainActivity"
    )
}

tasks.register("runAndroidStaging", Exec::class) {
    dependsOn("installStaging")

    doFirst {
        providers.exec {
            commandLine("adb", "shell", "am", "force-stop", appProperties.appId)
        }
    }

    commandLine(
        "adb", "shell", "am", "start", "-n",
        "${appProperties.appId}/com.melih.kmptemplate.MainActivity"
    )
}

tasks.register("runAndroidRelease", Exec::class) {
    dependsOn("installRelease")

    doFirst {
        providers.exec {
            commandLine("adb", "shell", "am", "force-stop", appProperties.appId)
        }
    }

    commandLine(
        "adb", "shell", "am", "start", "-n",
        "${appProperties.appId}/com.melih.kmptemplate.MainActivity"
    )
}
