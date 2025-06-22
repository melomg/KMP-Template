# KMP template [![Build Status Android](https://github.com/melomg/KMP-Template/actions/workflows/build-android.yml/badge.svg)](https://github.com/melomg/KMP-Template/actions/workflows/build-android.yml) [![Build Status iOS](https://github.com/melomg/KMP-Template/actions/workflows/build-ios.yml/badge.svg)](https://github.com/melomg/KMP-Template/actions/workflows/build-ios.yml) [![Apache V2 License](https://img.shields.io/badge/License-Apache%20V2-blue)](LICENSE) [![Contributor Covenant](https://img.shields.io/badge/Contributor%20Covenant-2.1-4baaaa.svg)](CODE_OF_CONDUCT.md)

## Forked from [Kotlin KMP-App-Template repo](https://github.com/Kotlin/KMP-App-Template)

This is a Kotlin Multiplatform template project targeting Android, iOS, Web, Desktop, Server.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
    - `commonMain` is for code that’s common for all targets.
    - `iosMain` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.
    - `desktopMain` contains desktop applications that works on Windows, MacOS and Linux.
    - `wasmMain` contains web applications that works on Chrome (not tested on other browsers).

* `/server` is for the Ktor server application.

* `/shared` is for the code that will be shared between all targets in the project.

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html),[Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform),[Kotlin/Wasm](https://kotl.in/wasm/)…

You can open the web application by running the Gradle task below:

```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

The KMP template tries to help you get started with these points:

- [ ] Core
    - [x] Dependency management: Renovate
    - [ ] Logging: [Napier](https://github.com/AAkira/Napier)?
        - [ ] Error reporting
        - [ ] Analytics
        - [ ] Tracing
    - [ ] Network: [ktor](https://ktor.io/)
    - [ ] Benchmarking
    - [ ] Build conventions
    - [ ] Flavours
    - [ ] Mocks
    - [ ] Test fixtures
    - [ ] Build info
    - [ ] Preferences
    - [ ] Storage
    - [ ] DI: koin?
    - [ ] Feature flags (local & remote)
    - [ ] Deep linking
    - [ ] Push notifications
    - [ ] TimeProvider
    - [ ] Local Formatters
    - [ ] Coroutine Dispatchers
    - [ ] Coroutine Dispatchers Test helper
    - [ ] Lint
    - [ ] Static code analysis
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
    - [ ] App settings with Resource Environment (See: [Source 1](https://github.com/JetBrains/compose-multiplatform/pull/5239), [Source 2](https://github.com/JetBrains/compose-multiplatform/blob/master/components/resources/library/src/androidMain/kotlin/org/jetbrains/compose/resources/ResourceEnvironment.android.kt), [Source 3](https://youtrack.jetbrains.com/issue/CMP-4197) )
        - [ ] l10n
        - [ ] i18n
    - [ ] Testing
        - [ ] UI Testing
        - [ ] Compose Screenshot testing

# Architecture

The **KMP Template** is following the [Android official architecture guidance](https://developer.android.com/topic/architecture) as closely as possible.

Some inspiring links;

- [Principles & Practice in Repository Layer](https://proandroiddev.com/principles-practice-in-repository-layer-444551b96cf8)
- [Architecture Learning Journey in Now in Android](https://github.com/android/nowinandroid/blob/main/docs/ArchitectureLearningJourney.md)

# Modularization

Over the years, I found below resources helpful for modular design and my aim for **KMP Template** is to follow them as closely as possible;

- [Android at scale @Square](https://www.droidcon.com/2019/11/15/android-at-scale-square/)
- [Navigating through multi-module Jetpack Compose applications](https://proandroiddev.com/navigating-through-multi-module-jetpack-compose-applications-6c9a31fa12b6)
- [Modularising Trendyol Android App for Build Efficiency](https://medium.com/trendyol-tech/modularising-trendyol-android-app-for-build-efficiency-94f6b79fc012)
- [Herding Elephants](https://developer.squareup.com/blog/herding-elephants/)
- [Modularization learning journey in Now in Android](https://github.com/android/nowinandroid/blob/main/docs/ModularizationLearningJourney.md)

# Code of Conduct

This project is managed by the [Code of Conduct](CODE_OF_CONDUCT.md).
By participating, you are expected to uphold this code.

# License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.
