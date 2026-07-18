# PRP-05 — Monitoreo de uso

## Objetivo

Registrar el tiempo que el estudiante dedica a redes sociales y videojuegos, y exponer un contador diario con límite de **30 minutos**.

## Alcance

### Fuente de datos

- `UsageStatsManager` + permiso especial **Usage Access** (`PACKAGE_USAGE_STATS`).
- Pantalla de onboarding que explique por qué se pide el permiso y abra ajustes del sistema.

### Catálogo de apps monitoreadas

Lista configurable (MVP: lista fija embebida), por ejemplo:

- Redes: Instagram, TikTok, Facebook, WhatsApp (opcional), X, YouTube
- Juegos: paquetes comunes o categoría “game” vía `ApplicationInfo.FLAG` / categoría

Docente/padre podrán ampliar la lista en fases posteriores.

### Contador diario

| Regla | Spec |
|-------|------|
| Ventana | Día civil local (00:00–23:59) |
| Límite | 30 minutos |
| Qué suma | Foreground time de apps del catálogo |
| Persistencia | Room/DataStore: minutos del día, última sync |

**Decisión a fijar en implementación:** ¿30 min *continuos* o *acumulados del día*? El brief dice “30 minutos de uso continuo”; el dashboard dice “de 30 disponibles” (acumulado). **Recomendación MVP:** acumulado diario de 30 min (más alineado al copy del panel); documentar la alternativa continua como mejora.

### Servicio / worker

- Sync periódico (WorkManager o foreground service ligero) mientras la app tiene permiso.
- Exponer `ScreenTimeRepository.observeTodayMinutes(): Flow<Int>`.

## Fuera de alcance (por ahora)

- Bloqueo en sí (PRP-06).
- Monitoreo de Chrome/webs sueltas.
- Root / VPN filtering.

## Dependencias con otros PRPs

| PRP | Relación |
|-----|----------|
| 01 | Capa `system` |
| 04 | Tarjeta de tiempo |
| 06 | Dispara bloqueo al llegar a 30 |
| 10, 12 | Estadísticas y panel padre |

## Criterios de aceptación

- [ ] Onboarding de permiso Usage Access.
- [ ] Contador refleja uso de al menos 1–2 apps de prueba.
- [ ] Reseteo correcto al cambiar de día.
- [ ] Dashboard estudiante consume el Flow de minutos.

## Notas técnicas

- API 26+; probar en dispositivo real (emulador limita UsageStats).
- No confundir con `QUERY_ALL_PACKAGES` innecesariamente.
