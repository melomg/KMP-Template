rootProject.name = "KMP-Template"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("android")
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("android")
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":app:androidApp")
include(":app:desktopApp")
include(":app:iosApp")
include(":app:shared")
include(":app:wasmApp")
include(":core:shared:data")
include(":core:shared:designsystem")
include(":core:shared:domain")
include(":core:shared:l10n")
include(":core:shared:logging")
include(":core:shared:model")
include(":core:shared:navigation")
include(":core:shared:network")
include(":core:shared:testutil")
include(":core:shared:threading")
include(":core:shared:tmdbconfig")
include(":features:movies")
include(":features:settings")
include(":server")
