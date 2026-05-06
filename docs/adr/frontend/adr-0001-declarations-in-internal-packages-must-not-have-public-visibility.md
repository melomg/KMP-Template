---
title: "ADR-0001: Declarations in internal packages must not have public visibility"
status: "Accepted"
date: 2026-05-06
authors: ["Melih Gultekin"]
tags: ["architecture", "visibility", "modifiers"]
---

## Status

**Accepted**

## Context

Visibility modifiers are easily forgotten to be implemented.

This leads to several issues:
- Accidental leakage of implementation details into the public API.
- Breaking changes for consumers when "internal" code is refactored.
- Increased cognitive load for developers who must distinguish between truly public API and "public but intended to be internal" code.

## Decision

All declarations (classes, interfaces, objects, functions, and properties) located within any package that contains `.internal.` in its path MUST be explicitly marked with the `internal` visibility modifier (or `private` where applicable). Public or default visibility is strictly prohibited in these packages.

## Consequences

### Positive

- **POS-001**: Stronger encapsulation by ensuring implementation details are strictly module-private.
- **POS-002**: Reduced public API surface area, making the library easier to maintain and evolve. It also makes it easy to “Program to an interface, not an implementation.” — Gang of Four
- **POS-003**: Improved binary compatibility as changes to `internal` code do not affect the public ABI.
- **POS-004**: Automated enforcement reduces the burden on manual code reviews.

### Negative

- **NEG-001**: Developers must manually add the `internal` modifier to every new declaration in these packages.
- **NEG-002**: Slight increase in initial development time for adding visibility modifiers.

## Alternatives Considered

### **ALT-001** Manual Code Review

- **Description**: Rely on developers and reviewers to catch public declarations in internal packages during PR reviews.
- **Rejection Reason**: Human error makes this inconsistent. It is easy to miss a missing modifier, especially in large PRs.

### **ALT-002** Public-Implementation Modules

- **Description**: By applying dependency inversion to a module, we can split a module into two (api|impl). This shortens the height of graph for Gradle to re-compile in case of ABI changes.
- **Rejection Reason**: In this project, there is only one core layer that contains one data module and one domain module for fast development since the project is being developed to keep in mind that not many people will work on this setup. This already keeps the height of the graph short. There is also slight cost of creating api|impl modules and becomes slightly not performant when developing this project with a small number of people. If the number of people who work on the project grows, or graph height becomes an issue, api folders can easily be migrated into the api modules.

## Implementation Notes

- **IMP-001**: This decision is enforced via a fitness function so that CI would capture violations automatically.

## References

- **REF-001**: [Android at Scale @Square](https://speakerdeck.com/vrallev/android-at-scale-at-square)
- **REF-001**: [Modularization learning journey](https://github.com/android/nowinandroid/blob/main/docs/ModularizationLearningJourney.md)
- **REF-002**: [Modularising Trendyol Android App for Build Efficiency](https://medium.com/trendyol-tech/modularising-trendyol-android-app-for-build-efficiency-94f6b79fc012)
