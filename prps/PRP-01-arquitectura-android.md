# PRP-01 — Arquitectura Android

## Objetivo

Definir la arquitectura de la app: capas, módulos, navegación por rol, persistencia y servicios de sistema necesarios para monitoreo y bloqueo.

## Alcance

### Vista del sistema

```
┌─────────────────────────────────────────────────────────────┐
│                    ControlSelf EDU (App)                      │
│  ┌─────────────┐  ┌──────────────┐  ┌────────────────────┐ │
│  │ UI Compose  │  │ ViewModels   │  │ Domain / UseCases  │ │
│  │ (screens)   │→ │              │→ │                    │ │
│  └─────────────┘  └──────────────┘  └─────────┬──────────┘ │
│                                               │            │
│  ┌──────────────────┐  ┌──────────────────────▼──────────┐ │
│  │ System services  │  │ Data (Room / DataStore / mock)  │ │
│  │ UsageStats,      │  │ Users, Attempts, Stats, Streaks │ │
│  │ Overlay, Admin   │  └─────────────────────────────────┘ │
│  └──────────────────┘                                        │
└─────────────────────────────────────────────────────────────┘
```

### Stack

| Capa | Opción |
|------|--------|
| UI | Jetpack Compose + Material 3 |
| Nav | Navigation Compose |
| Async | Kotlin coroutines + Flow |
| DI | Manual o Koin (decidir en implementación; Hilt si el equipo lo prefiere) |
| Local DB | Room (fase 2+) |
| Preferencias | DataStore |
| Charts | Vico o Compose Canvas simple (PRP-10) |

### Módulos lógicos (paquetes)

| Paquete | Responsabilidad |
|---------|-----------------|
| `ui.theme` | Colores, tipografía, tema |
| `ui.screens.*` | Pantallas por dominio |
| `navigation` | Rutas y NavHost |
| `domain` | Modelos y casos de uso |
| `data` | Repositorios, Room, mocks |
| `system` | UsageStats, overlay, device admin |

### Navegación por rol (post-login)

| Rol | Destino inicial |
|-----|-----------------|
| Estudiante | `student/home` |
| Docente | `teacher/home` |
| Padre | `parent/home` |

Rutas públicas: `welcome`, `login`, `register`, `forgot-password`.

### Permisos Android (mapa)

| Permiso / capacidad | PRP |
|---------------------|-----|
| `PACKAGE_USAGE_STATS` | 05 |
| Overlay / Accessibility (bloqueo) | 06 |
| Device Admin / Device Owner | 13 |
| Internet (si hay API futura) | opcional |

### Principios

- **Un PRP = un dominio** implementable sin arrastrar paneles ajenos.
- **Mock-first:** auth y datos pueden ser locales hasta existir backend.
- **UI sin lógica de sistema:** UsageStats y overlay viven en `system` + use cases.

## Fuera de alcance (por ahora)

- Multi-módulo Gradle (`:core`, `:feature-*`) — monolito `app` en MVP.
- Backend remoto y sync multi-dispositivo.

## Dependencias con otros PRPs

| PRP | Relación |
|-----|----------|
| 00 | Visión |
| 02+ | Pantallas y features sobre esta base |

## Criterios de aceptación

- [x] Diagrama de capas documentado.
- [x] Paquetes y rutas por rol definidos.
- [x] Permisos mapeados a PRPs.
- [x] Scaffold Gradle existente abre en Android Studio.

## Implementación

| Área | Ubicación | Estado |
|------|-----------|--------|
| Capas / paquetes | `domain/`, `data/`, `system/`, `di/` | ✅ |
| DI manual | `AppContainer`, `ControlSelfApplication` | ✅ |
| Rutas por rol | `navigation/Routes.kt` / `Screen.kt` | ✅ |
| Paneles por rol | `TeacherHomeScreen`, `ParentHomeScreen`, `StudentHomeScreen` | ✅ |
| Doc | [docs/arquitectura.md](../docs/arquitectura.md) | ✅ |

## Notas técnicas

- `minSdk 26`, `targetSdk 35`, `applicationId com.controlself.edu`
- Monolito `:app` (sin multi-módulo Gradle)
- Siguiente: [PRP-02](PRP-02-bienvenida-tema.md) (parcial) → [PRP-03](PRP-03-autenticacion-roles.md)
