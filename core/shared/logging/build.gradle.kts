plugins {
    alias(libs.plugins.kmptemplate.kotlinMultiplatform.library)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    android {
        namespace = "com.melih.kmptemplate.core.shared.logging"
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.core.shared.model)
            implementation(libs.kermit.logging)
        }
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
