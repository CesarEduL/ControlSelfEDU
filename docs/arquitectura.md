# Arquitectura — ControlSelf EDU

Implementación de [PRP-01](../prps/PRP-01-arquitectura-android.md).

## Capas

```
ui.screens.*  →  (ViewModels futuros)  →  domain (modelos + contratos)
                                              ↓
                              data (mocks / Room / DataStore)
                                              ↑
system (UsageStats, overlay, Device Admin) — solo vía gateways
```

## Paquetes

| Paquete | Contenido actual |
|---------|------------------|
| `ui.theme` / `ui.screens.*` | Compose |
| `navigation` | `Routes`, `ControlSelfNavHost` |
| `domain.model` | `UserRole`, `Session` |
| `domain.repository` | `AuthRepository`, `ScreenTimeRepository`, `LockRepository` |
| `data.*` | Implementaciones in-memory / fake |
| `system.*` | Gateways NoOp (PRP-05/06/13) |
| `di` | `AppContainer` + `LocalAppContainer` |

## DI

**Manual** vía `ControlSelfApplication.container` y `CompositionLocalProvider(LocalAppContainer)`.

## Rutas

| Ruta | Rol |
|------|-----|
| `welcome`, `login`, `register`, `forgot-password` | Públicas |
| `student/home` | Estudiante → PRP-04 |
| `teacher/home` | Docente → PRP-11 |
| `parent/home` | Padre → PRP-12 |

Helper: `Routes.homeFor(UserRole)`.

## Permisos (mapa)

| Capacidad | PRP | Estado en código |
|-----------|-----|------------------|
| Usage Access | 05 | `UsageStatsGateway` NoOp |
| Overlay / bloqueo | 06 | `EntertainmentLockController` NoOp |
| Device Admin | 13 | `DeviceAdminGateway` NoOp |
