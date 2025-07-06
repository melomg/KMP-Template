// Common regex patterns
private val APP_VERSION_REGEX = """<string name="app_version">([^(]+) \((\d+)\)</string>""".toRegex()
private val VERSION_HEADER_REGEX = """## Version (.*) \(([a-f0-9]+)\)""".toRegex()
private val COMMIT_HASH_REGEX = """([a-f0-9]+) -.*""".toRegex()
private val MERGE_PR_REGEX = """([a-f0-9]+) - Merge pull request #(\d+) from.*""".toRegex()

// Helper function to run git commands
private fun runGitCommand(vararg args: String): String {
    val process = ProcessBuilder("git", *args)
        .directory(projectDir)
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .start()

    val output = process.inputStream.bufferedReader().readText().trim()
    process.waitFor()
    return output
}

// Function to normalize content by ensuring there's only one empty line between sections
private fun normalizeContent(content: String): String {
    return content.replace(Regex("\n{3,}"), "\n\n")
}

tasks.register("prepareRelease") {
    group = "releases"
    description = "Bumps versions and prepares release notes"

    dependsOn("updateVersion", ":shared:exportLibraryDefinitions")
}

tasks.register("updateVersion") {
    group = "releases"
    description = "Updates app version code and name across all platforms"

    doLast {
        // Read current state
        val versionCodes = mutableListOf<Int?>()
        val versionNames = mutableListOf<String?>()

        // Android version
        val androidBuildGradle = file("androidApp/build.gradle.kts")
        val androidBuildGradleContent = androidBuildGradle.readText()
        versionCodes.add("""versionCode\s*=\s*(\d+)""".toRegex().find(androidBuildGradleContent)?.groupValues?.get(1)?.toIntOrNull())
        versionNames.add("""versionName\s*=\s*"([^"]+)"""".toRegex().find(androidBuildGradleContent)?.groupValues?.get(1))

        // iOS versions from project file
        val iosProjectFile = file("iosApp/KotlinConf.xcodeproj/project.pbxproj")
        val iosProjectContent = iosProjectFile.readText()
        versionCodes.addAll("""CURRENT_PROJECT_VERSION\s*=\s*(\d+)""".toRegex().findAll(iosProjectContent)
            .map { it.groupValues[1].toIntOrNull() }.toList())
        versionNames.addAll("""MARKETING_VERSION\s*=\s*([^;]+)""".toRegex().findAll(iosProjectContent)
            .map { it.groupValues[1].trim() }.toList())

        // iOS versions from Info.plist
        val iosInfoPlist = file("iosApp/iosApp/Info.plist")
        val iosInfoPlistContent = iosInfoPlist.readText()
        versionCodes.add("""<key>CFBundleVersion</key>\s*<string>(\d+)</string>""".toRegex()
            .find(iosInfoPlistContent)?.groupValues?.get(1)?.toIntOrNull())
        versionNames.add("""<key>CFBundleShortVersionString</key>\s*<string>([^<]+)</string>""".toRegex()
            .find(iosInfoPlistContent)?.groupValues?.get(1))

        // Shared version
        val sharedVersionXml = file("shared/src/commonMain/composeResources/values/version.xml")
        val sharedVersionXmlContent = sharedVersionXml.readText()
        val sharedVersionMatch = APP_VERSION_REGEX.find(sharedVersionXmlContent)
        versionCodes.add(sharedVersionMatch?.groupValues?.get(2)?.toIntOrNull())
        versionNames.add(sharedVersionMatch?.groupValues?.get(1))

        // Validate versions
        if (versionCodes.distinct().size != 1 || versionNames.distinct().size != 1) {
            throw GradleException("Version mismatch.\nVersion codes found: $versionCodes\nVersion names found: $versionNames")
        }

        val currentVersionCode = versionCodes.first() ?: throw GradleException("Invalid version code")
        val currentVersionName = versionNames.first() ?: throw GradleException("Invalid version name")
        println("Current version: $currentVersionName ($currentVersionCode)")

        // Calculate new version
        val newVersionCode = currentVersionCode + 1
        val newVersionName = "${currentVersionName.substringBeforeLast('.')}.${currentVersionName.substringAfterLast('.').toInt() + 1}"
        println("New version: $newVersionName ($newVersionCode)")

        // Update Android version
        androidBuildGradle.writeText(androidBuildGradleContent
            .replace("""versionCode = $currentVersionCode""", """versionCode = $newVersionCode""")
            .replace("""versionName = "$currentVersionName"""", """versionName = "$newVersionName""""))

        // Update iOS project file
        iosProjectFile.writeText(iosProjectContent
            .replace("""CURRENT_PROJECT_VERSION = $currentVersionCode""", """CURRENT_PROJECT_VERSION = $newVersionCode""")
            .replace("""MARKETING_VERSION = $currentVersionName""", """MARKETING_VERSION = $newVersionName"""))

        // Update iOS Info.plist
        iosInfoPlist.writeText(iosInfoPlistContent
            .replace("""<key>CFBundleVersion</key>\s*<string>$currentVersionCode</string>""".toRegex(),
                """<key>CFBundleVersion</key>
	<string>$newVersionCode</string>""")
            .replace("""<key>CFBundleShortVersionString</key>\s*<string>$currentVersionName</string>""".toRegex(),
                """<key>CFBundleShortVersionString</key>
	<string>$newVersionName</string>"""))

        // Update shared version
        sharedVersionXml.writeText(sharedVersionXmlContent
            .replace("""<string name="app_version">$currentVersionName ($currentVersionCode)</string>""",
                """<string name="app_version">$newVersionName ($newVersionCode)</string>"""))

        println("Successfully updated version to $newVersionName ($newVersionCode)")
    }
}

// Second solution

// Function to get git commit hash
fun getGitCommitHash(): String {
    return try {
        val stdout = ByteArrayOutputStream()
        exec {
            commandLine("git", "rev-parse", "--short", "HEAD")
            standardOutput = stdout
        }
        stdout.toString().trim()
    } catch (e: Exception) {
        "unknown"
    }
}

// Task to generate version files for all platforms
tasks.register("generateVersionFiles") {
    doLast {
        val versionCode = 1
        val versionName = "1.0.0"
        val buildTime = System.currentTimeMillis()
        val gitCommit = getGitCommitHash()

        // Desktop version.properties
        val desktopVersionFile = File(project.projectDir, "src/desktopMain/resources/version.properties")
        desktopVersionFile.parentFile.mkdirs()
        desktopVersionFile.writeText("""
            app.version.code=$versionCode
            app.version.name=$versionName
            app.build.time=$buildTime
            app.git.commit=$gitCommit
        """.trimIndent())

        // WASM version.js
        val wasmVersionFile = File(project.projectDir, "src/wasmJsMain/resources/version.js")
        wasmVersionFile.parentFile.mkdirs()
        wasmVersionFile.writeText("""
            window.APP_VERSION = {
                code: $versionCode,
                name: "$versionName",
                buildTime: "$buildTime",
                gitCommit: "$gitCommit"
            };
        """.trimIndent())
    }
}

// Make sure version files are generated before compilation
tasks.named("compileKotlinDesktop") {
    dependsOn("generateVersionFiles")
}

tasks.named("compileKotlinWasmJs") {
    dependsOn("generateVersionFiles")
}


// 5. DESKTOP - Alternative using Gradle generated properties
// You can also generate a properties file during build
// Add this to your build.gradle.kts:


tasks.register("generateVersionProperties") {
    doLast {
        val versionFile = File(project.buildDir, "resources/main/generated-version.properties")
        versionFile.parentFile.mkdirs()
        versionFile.writeText("""
            app.version.code=1
            app.version.name=1.0.0
            app.build.time=${System.currentTimeMillis()}
            app.git.commit=${getGitCommitHash()}
        """.trimIndent())
    }
}

fun getGitCommitHash(): String {
    return try {
        val stdout = ByteArrayOutputStream()
        exec {
            commandLine("git", "rev-parse", "--short", "HEAD")
            standardOutput = stdout
        }
        stdout.toString().trim()
    } catch (e: Exception) {
        "unknown"
    }
}

tasks.named("processResources") {
    dependsOn("generateVersionProperties")
}
