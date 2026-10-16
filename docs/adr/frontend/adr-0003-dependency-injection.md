---
title: "ADR-0003: Use Koin for dependency injection"
status: "Accepted"
date: 2026-10-16
authors: ["Melih Gültekin"]
tags: ["DI", "dependency-injection"]
---

## Status

**Accepted**

## Context

This Kotlin Multiplatform template project needs a way to assemble shared services, platform dependencies, and feature ViewModels across its client targets. As a starting point for new projects, the dependency injection (DI) setup should be familiar to developers, require little configuration, and let features register their own dependencies without repeatedly changing a central graph. The choice should not force application and domain APIs to depend on a particular DI framework.

### Decision

Use **Koin** as the default DI framework for the frontend template. Its widespread use in Kotlin Multiplatform projects and concise module DSL make it a practical, approachable default rather than a requirement for downstream projects. Register dependencies in feature and core modules, and assemble them at the application entry point. Keep domain contracts and business logic independent of Koin where practical so adopters can replace the DI wiring with another framework or manual construction.

Overall, Koin is the most popular DI choice among other frameworks, and suits as a default choice for this template project. And it is very easy to replace it with other frameworks.

### Consequences

##### Positive

- **POS-001**: A familiar KMP DI option lets template adopters start wiring dependencies with a small module DSL.
- **POS-002**: Feature modules can declare their own bindings and ViewModels without adding a factory interface for every feature to a central graph.
- **POS-003**: Modules can be assembled or replaced for tests and platform-specific configuration.
- **POS-004**: Keeping framework usage near composition boundaries makes a later migration feasible without rewriting domain contracts.

##### Negative

- **NEG-001**: Not all dependency errors are necessarily caught at compile time; missing or extra assisted parameters and dependency cycles can fail at runtime depending on configuration and validation coverage.
- **NEG-002**: Koin-specific module declarations, ViewModel integration, and tests still have to be rewritten when switching frameworks.
- **NEG-003**: Runtime container mutability requires care to avoid accidental binding overrides or inconsistent startup configuration.

### Alternatives Considered

##### **ALT-001** [Kodein](https://klibs.io/project/kosi-libs/Kodein)

- **Description**: A Kotlin Multiplatform DI container with a binding DSL.
- **Rejection Reason**: It also provides runtime DI, but its binding DSL offers no clear advantage over Koin's familiar, concise setup for a starter template.

##### **ALT-002** [Metro](https://klibs.io/project/ZacSweers/metro)

- **Description**: A compile-time DI framework for Kotlin Multiplatform with explicit dependency graphs.
- **Rejection Reason**: Its explicit graph and factory wiring can offer stronger compile-time guarantees, but adds setup and coordination that are less suitable for this template's simple default. Projects prioritizing strict graph safety may reasonably choose it instead.

##### **ALT-003** [Kotlin Inject](https://klibs.io/project/evant/kotlin-inject)

- **Description**: A compile-time DI approach using annotations and generated components for multiplatform code.
- **Rejection Reason**: Generated component wiring increases the initial configuration needed for a starter project; it remains an option for adopters who prefer explicit compile-time wiring.

##### **ALT-004** Manual dependency construction

- **Description**: Construct dependencies directly at each application entry point without a DI framework.
- **Rejection Reason**: This avoids a library dependency but makes shared and platform-specific graph assembly and test substitutions more repetitive as features are added.

### Implementation Notes

- **IMP-001**: Define registrations near their owning core or feature modules and assemble them at each application's composition root.
- **IMP-002**: Prefer constructor dependencies over fetching from the global container in business logic; keep domain contracts independent of Koin.
- **IMP-003**: Use Koin's ViewModel integration at UI boundaries and provide isolated bindings for tests and platform-specific needs.
- **IMP-004**: If changing frameworks, replace registrations, startup wiring, ViewModel retrieval, and test configuration while preserving framework-independent contracts; verify graph resolution with tests and application builds.

### References

- **REF-001**: [Koin trade-offs in the KotlinConf App discussion](https://github.com/JetBrains/kotlinconf-app/pull/699#issuecomment-4300223920)
- **REF-002**: [Koin team's response on modularity and compile safety](https://github.com/JetBrains/kotlinconf-app/pull/699#issuecomment-4305559120)
- **REF-003**: [Metro: The KMP DI Framework You Never Knew You Needed](https://dev.to/mastrgamr/metro-the-kmp-di-framework-you-never-knew-you-needed-64f)
- **REF-004**: [Kodein Docs](https://kosi-libs.org/kodein/7.31/index.html)
- **REF-005**: [Metro Multiplatform Docs](https://zacsweers.github.io/metro/latest/multiplatform/)
- **REF-006**: [Kotlin Inject Multiplatform Docs](https://github.com/evant/kotlin-inject/blob/main/docs/multiplatform.md)
