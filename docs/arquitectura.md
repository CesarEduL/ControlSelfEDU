# Arquitectura — ControlSelf EDU

Estructura de la app Android: UI Compose, dominio, datos y acceso al sistema.

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
| `domain.model` | `UserRole`, `Session`, `ChildAccount` |
| `domain.repository` | `AuthRepository`, `ScreenTimeRepository`, `LockRepository` |
| `data.*` | Implementaciones in-memory / fake |
| `system.*` | UsageStats, Device Admin, lock gateways |
| `di` | `AppContainer` + `LocalAppContainer` |

## DI

**Manual** vía `ControlSelfApplication.container` y `CompositionLocalProvider(LocalAppContainer)`.

## Rutas

| Ruta | Rol |
|------|-----|
| `welcome`, `login`, `register`, `forgot-password` | Públicas |
| `student/home` | Estudiante |
| `teacher/home` | Docente |
| `parent/home` | Padre/madre |

Helper: `Routes.homeFor(UserRole)`.

## Identidad (jerarquía)

| Rol | Registro en `register` | Origen de la cuenta |
|-----|------------------------|---------------------|
| Padre / Docente | Sí | Auto-registro |
| Estudiante | No | Creada por el padre (`createChildAccount` / panel padre); 1 padre → N hijos |

Detalle de producto: [PRP-03](../prps/PRP-03-autenticacion-roles.md), [PRP-12](../prps/PRP-12-panel-padre.md).

## Permisos (mapa)

| Capacidad | Implementación |
|-----------|----------------|
| Usage Access | `AndroidUsageStatsGateway` |
| Overlay / bloqueo UI | `LockScreen` + `LockRepository` |
| Device Admin | `AndroidDeviceAdminGateway` + receiver |
