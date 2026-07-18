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

- Scaffold Android + tema azul/blanco/verde
- Splash / bienvenida → placeholder de login (**PRP-02** parcial ✅)
- Siguiente a implementar: **[PRP-03 — Autenticación y roles](prps/PRP-03-autenticacion-roles.md)**
