# Porting Notes

The port starts from a Cleanroom 1.12.2 template instead of modifying the original source tree directly.

Initial constraints:

- Keep `../eidolon-1.20x` as the reference source.
- Keep `../CleanroomModTemplate-mixin` as the clean template reference.
- Move one system at a time and keep the project buildable between steps.
- Prefer simple Forge 1.12 registry events before introducing compatibility layers.

## Run Client Verification

Detailed `runClient` crash notes are recorded in `../Eidolon_1.12_Port_Preparation.md`.
The actual `runClient` verification is intentionally left to the user from IDEA/Gradle after refreshing the project.
