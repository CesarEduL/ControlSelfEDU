# ControlSelf EDU

App Android de control parental educativo: monitorea tiempo en redes/videojuegos, bloquea tras 30 minutos y desbloquea al aprobar una lección (mín. 15/20).

## Stack

- Kotlin 1.9 + Jetpack Compose + Material 3
- Navigation Compose · SplashScreen API
- AGP 8.6.1 · Gradle 8.7 · minSdk 26 · targetSdk 35
- package `com.controlself.edu`

## Documentación (PRPs)

Los requisitos se gestionan como **Product Requirement Prompts** en [`prps/`](prps/README.md).

Orden de implementación: **PRP-00 → 01 → 02 → 03 → … → 13**.

## Abrir en Android Studio

1. File → Open → esta carpeta
2. Espera el sync de Gradle
3. Run en emulador o dispositivo

## Estado actual

- **PRP-01** arquitectura ✅
- **PRP-02** bienvenida + tema ✅
- **PRP-03** autenticación y roles ✅
- **PRP-04** panel estudiante ✅
- Siguiente: **[PRP-05 — Monitoreo de uso](prps/PRP-05-monitoreo-uso.md)**

Demo login: `estudiante` / `docente` / `padre` — clave `123456` (elige el rol correcto).

Detalle de capas: [`docs/arquitectura.md`](docs/arquitectura.md)
