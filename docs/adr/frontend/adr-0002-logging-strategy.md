---
title: "ADR-0002: Use Klog for logging"
status: "Accepted"
date: 2026-05-06
authors: ["Melih Gültekin"]
tags: ["logging"]
---

## Status

**Accepted**

## Context

Kotlin Multiplatform (KMP) projects require a logging solution that works seamlessly across different platforms (Android, iOS, Desktop, WasmJs and server). The ideal solution should provide a developer experience similar to established Android libraries like Timber, while supporting native platform loggers and allowing for extensible, composite logging (e.g., logging to both console and remote crash reporting tools).

Key constraints and requirements include:
- Native platform logging support (`Logcat`, `os_log`, `SLF4J`, `console`).
- Composite logging (planting multiple loggers).
- Low performance overhead.
- Decoupling from specific third-party libraries to avoid vendor lock-in and facilitate testing.

### Decision

We will use **Kermit** as the underlying logging implementation but wrap it in a custom facade (`Klogger`) and a static entry point (`Klog`). 

Rationale:
- **Kermit** provides the best balance of native platform support and active maintenance.
- The **Facade Pattern** (via `Klogger` and `Klog`) ensures that the rest of the codebase is not directly dependent on Kermit, making it easier to swap out or mock for tests.
- A **"Planting" mechanism** (similar to Timber) allows for flexible configuration at the application entry point, supporting different loggers for debug and production environments.

### Consequences

##### Positive

- **POS-001**: Decouples the codebase from the specific third-party library (Kermit) through the `Klogger` interface.
- **POS-002**: Leverages native platform loggers automatically, ensuring logs appear in standard platform tools.
- **POS-003**: Enables composite logging, allowing logs to be dispatched to multiple destinations simultaneously.
- **POS-004**: Minimizes performance impact by using lazy string evaluation (lambdas) for log messages.

##### Negative

- **NEG-001**: Adds a small abstraction layer that developers need to understand.
- **NEG-002**: Inconsistent automatic tag resolution across platforms (specifically challenges on WasmJs).
- **NEG-003**: Requires explicit "planting" of loggers in each platform's entry point.

### Alternatives Considered

##### **ALT-001** kotlin-logging

- **Description**: A popular logging wrapper for Kotlin.
- **Rejection Reason**: Requires complex system property configuration for Android and has issues with default visibility of `DEBUG` logs due to `Log.isLoggable` checks.

##### **ALT-002** klogging

- **Description**: A pure-Kotlin asynchronous logging library.
- **Rejection Reason**: Lacks native Logback support (important for JVM/Backend) and does not natively support composite logging in the same flexible way as Kermit.

##### **ALT-003** Napier

- **Description**: A popular KMP logging library.
- **Rejection Reason**: Appeared less actively maintained and had reported issues with specific logging scenarios like non-fatal error reporting.

### Implementation Notes

- **IMP-001**: Initialize loggers using `Klog.plant()` in platform-specific entry points (e.g., `Application.onCreate` on Android, `MainViewController` on iOS).
- **IMP-002**: Use `Klog.debug { "Message" }` throughout the shared and platform code.
- **IMP-003**: Implement custom `Klogger` instances for third-party integrations like Crashlytics or Sentry.
- **IMP-004**: Pass tags explicitly when automatic class-name resolution is insufficient or on platforms where it is unreliable.

### References

- **REF-001**: [Starting a KMP Project - Episode 4](https://melihgultekin.dev/2025/8/31/starting-a-kmp-project-episode-4.html)
- **REF-002**: [Logs Management in a Kotlin Multiplatform project](https://blog.thesurfcode.com/logs-management-in-kmp)
- **REF-003**: [logcat - a new look at logging with Piwai from Square](https://fragmentedpodcast.com/episodes/253/)
- **REF-004**: [How To Do Logging In Java](https://www.marcobehler.com/guides/java-logging)
- **REF-005**: [Best practices for loggers](https://discuss.kotlinlang.org/t/best-practices-for-loggers/226/15)
