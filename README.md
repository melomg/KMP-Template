# KMP template ![Work In Progress](https://img.shields.io/badge/WORK%20IN%20PROGRESS-c20404) [![Build Status android-iOS-web(wasm)-desktop](https://github.com/melomg/KMP-Template/actions/workflows/build-all.yml/badge.svg)](https://github.com/melomg/KMP-Template/actions/workflows/build-all.yml) [![Apache V2 License](https://img.shields.io/badge/License-Apache%20V2-blue)](LICENSE) [![Contributor Covenant](https://img.shields.io/badge/Contributor%20Covenant-2.1-4baaaa.svg)](CODE_OF_CONDUCT.md)

## Forked from [Kotlin KMP-App-Template repo](https://github.com/Kotlin/KMP-App-Template)

This is a Kotlin Multiplatform template project targeting Android, iOS, Web, Desktop, Server.

* `/app` is responsible for handling Compose Multiplatform application logic per each supported
  target.
  See why each target has it's own
  folder [here](https://kotlinlang.org/docs/multiplatform/multiplatform-project-agp-9-migration.html).
  The subfolders:
    - `shared` is for application code that’s common for all targets.
    - `androidApp` contains Android applications.
    - `iosMain` contains iOS applications. Even if you’re sharing your UI with Compose
      Multiplatform, you need this
      entry point for your iOS app. This is also where you should add SwiftUI code for your project.
    - `desktopMain` contains desktop applications that works on Windows, MacOS and Linux.
    - `wasmMain` contains web applications that works on Chrome (not tested on other browsers).

* `/server` is for the Ktor server application.

* `/core` is for the code that will be shared between all targets in the project.

Learn more
about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html),[Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform),[Kotlin/Wasm](https://kotl.in/wasm/)…

## Running applications

### Android App

- Change `Build Variants` to a build type that you would like and run `app.androidApp`
  configuration. Credentials for signing config should be inside `local.properties` file. See
  `signingConfigs` section inside `app:androidApp` gradle file.

### Desktop App

- For debug build, run `desktopDebug.run` configuration
- For uber release build, run `desktopUberRelease.run` configuration and locate `
  /app/desktopApp/build/compose/jars/{the-jar} to open the app build.

Or, you can open the desktop application by running the Gradle tasks below:

#### Debug version

```bash
EFFECTIVE_BUILD_TYPE=debug ./gradlew :app:desktopApp:run
```

#### Release version

```bash
./gradlew :app:desktopApp:packageUberJarForCurrentOS
```

### WASM App

- For debug build, run `wasmJsDebug.run` configuration
- For release build, run `wasmJsRelease.run` configuration and locate
  `/app/wasmAppApp/build/dist/wasmJs/productionExecutable/` to open the `index.html` file.

Or, you can open the web application by running the Gradle tasks below:

#### Debug version

```bash
EFFECTIVE_BUILD_TYPE=debug ./gradlew :app:wasmApp:wasmJsBrowserDevelopmentRun
```

#### Release version

```bash
./gradlew :app:wasmApp:wasmJsBrowserDistribution
```

The KMP template tries to help you get started with these points:

- [ ] Core
    - [x] Dependency management: [Renovate](https://docs.renovatebot.com/)
    - [x] DI: [koin](https://insert-koin.io/docs/reference/koin-mp/kmp/)
    - [x] Network: [ktor](https://ktor.io/)
    - [x] Lint & Static code analysis [Detekt](https://medium.com/@mmelihgultekin/starting-a-kmp-project-the-one-with-static-code-analysis-episode-2-0a410146fb68)
    - [x] Build info [Blog](https://medium.com/@mmelihgultekin/starting-a-kmp-project-the-one-with-build-info-episode-3-54e77dcc0849)
    - [x] Logging: [Kermit](https://medium.com/@mmelihgultekin/kermikermit-androidstarting-a-kmp-project-the-one-with-logging-episode-4-a5aeaa9f2aeb)
      - [x] Error reporting
        - [ ] Analytics
        - [ ] Tracing
    - [ ] Benchmarking
    - [ ] Build conventions
    - [ ] Flavours
    - [ ] Mocks
    - [ ] Test fixtures
    - [ ] Preferences
    - [ ] Storage
    - [ ] Feature flags (local & remote)
    - [ ] Deep linking
    - [ ] Push notifications
    - [ ] TimeProvider
    - [ ] Local Formatters
    - [ ] Coroutine Dispatchers
    - [ ] Coroutine Dispatchers Test helper
    - [ ] Unit testing
    - [ ] Test coverage: Jacoco?
    - [ ] Obfuscation & Shrinking
    - [ ] Pipelines
    - [ ] Releasing
    - [ ] Force updates

- [ ] UI
    - [ ] Design system
    - [ ] Gallery App
    - [ ] Navigation
    - [ ] Baseline profiles
    - [ ] Compose compiler metrics
    - [ ] Previews
    - [ ] Network image loading: coil
    - [ ] supportsDynamicTheming
    - [ ] Status bar color changing
    - [ ] App settings with Resource Environment (
      See: [Source 1](https://github.com/JetBrains/compose-multiplatform/pull/5239), [Source 2](https://github.com/JetBrains/compose-multiplatform/blob/master/components/resources/library/src/androidMain/kotlin/org/jetbrains/compose/resources/ResourceEnvironment.android.kt), [Source 3](https://youtrack.jetbrains.com/issue/CMP-4197) )
        - [ ] l10n
        - [ ] i18n
    - [ ] Testing
        - [ ] UI Testing
        - [ ] Compose Screenshot testing

# Building

The **KMP Template** needs a Firebase account set up to run. If you haven't already,
check [here](https://firebase.google.com/docs/android/setup). You'll need to get the
`google-services.json` and put it
in `app/androidApp/` folder for Android. TODO("Add instructions for iOS")

# Architecture

The **KMP Template** is following
the [Android official architecture guidance](https://developer.android.com/topic/architecture) as
closely as possible.

Some inspiring links;

- [Principles & Practice in Repository Layer](https://proandroiddev.com/principles-practice-in-repository-layer-444551b96cf8)
- [Architecture Learning Journey in Now in Android](https://github.com/android/nowinandroid/blob/main/docs/ArchitectureLearningJourney.md)

# Modularization

Over the years, I found below resources helpful for modular design and my aim for **KMP Template**
is to follow them as
closely as possible;

- [Android at scale @Square](https://www.droidcon.com/2019/11/15/android-at-scale-square/)
- [Navigating through multi-module Jetpack Compose applications](https://proandroiddev.com/navigating-through-multi-module-jetpack-compose-applications-6c9a31fa12b6)
- [Modularising Trendyol Android App for Build Efficiency](https://medium.com/trendyol-tech/modularising-trendyol-android-app-for-build-efficiency-94f6b79fc012)
- [Herding Elephants](https://developer.squareup.com/blog/herding-elephants/)
- [Modularization learning journey in Now in Android](https://github.com/android/nowinandroid/blob/main/docs/ModularizationLearningJourney.md)

# Updating gradle wrapper

```bash
./gradlew wrapper --gradle-version latest --distribution-type bin
```

# Validate renovate configuration

```bash
npx --yes --package renovate -- renovate-config-validator --strict
```

# Code of Conduct

This project is managed by the [Code of Conduct](CODE_OF_CONDUCT.md).
By participating, you are expected to uphold this code.

# License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.
