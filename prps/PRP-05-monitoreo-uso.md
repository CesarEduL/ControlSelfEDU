# PRP-05 — Monitoreo de uso

## Objetivo

Registrar el tiempo acumulado del día en redes/videojuegos (límite **30 min**) vía `UsageStatsManager`, con permiso Usage Access y sync periódico.

## Alcance

### Decisión de producto (fijada)

| Tema | Decisión MVP |
|------|--------------|
| Límite | **30 min acumulados del día** (00:00–23:59 local) |
| Continuo | Mejora futura (no MVP) |
| Sin permiso | Contador 0 + CTA onboarding; no inventar minutos |

### Fuente de datos

- `UsageStatsManager` + AppOps `GET_USAGE_STATS`.
- Onboarding in-app → `Settings.ACTION_USAGE_ACCESS_SETTINGS`.

### Catálogo (MVP fijo)

- Redes: Instagram, TikTok, Facebook, Messenger, WhatsApp, X, YouTube, Snapchat, Reddit.
- Juegos: paquetes con `CATEGORY_GAME` que tengan uso hoy.

### Persistencia y sync

- DataStore: `dayKey` + `minutesCached`.
- Refresh: al abrir panel estudiante, al volver a foreground, y **WorkManager** cada ~15 min.
- Al cambiar el día civil → reset a 0 (o nueva query del día).
- Si minutos ≥ 30 → `LockRepository.setLocked(true)` (UI de bloqueo = PRP-06).

### API

- `ScreenTimeRepository.observeTodayMinutes()`
- `observeUsagePermissionGranted()`
- `refresh()` / abrir ajustes de permiso

## Fuera de alcance (por ahora)

- Pantalla fullscreen de bloqueo (PRP-06).
- Chrome/webs, VPN, root.
- Catálogo editable por padre/docente.

## Dependencias con otros PRPs

| PRP | Relación |
|-----|----------|
| 01 | `system.usage` |
| 04 | Tarjeta de tiempo |
| 06 | Consume `LockRepository` cuando ≥ 30 |
| 10, 12 | Estadísticas |

## Criterios de aceptación

- [x] Onboarding / CTA de Usage Access.
- [x] Con permiso, contador suma apps del catálogo (dispositivo real).
- [x] Reset al cambiar de día.
- [x] Dashboard consume el Flow; sync periódico.

## Implementación

| Área | Ubicación |
|------|-----------|
| Catálogo | `system/usage/EntertainmentAppCatalog.kt` |
| Gateway | `AndroidUsageStatsGateway.kt` |
| Repo | `data/screentime/UsageScreenTimeRepository.kt` |
| Worker | `system/usage/ScreenTimeSyncWorker.kt` |
| UI | banner en `StudentHomeScreen` |

## Notas técnicas

- Emulador: UsageStats suele ser pobre; probar en físico.
- `PACKAGE_USAGE_STATS` es permiso especial (no runtime dialog estándar).
- Siguiente: [PRP-06](PRP-06-bloqueo-automatico.md).
